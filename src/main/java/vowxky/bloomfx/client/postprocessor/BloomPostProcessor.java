package vowxky.bloomfx.client.postprocessor;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL30;
import team.lodestar.lodestone.LodestoneLib;
import vowxky.bloomfx.client.PostProcessorModified;

public class BloomPostProcessor extends PostProcessorModified {
    private Framebuffer bloomTarget;
    private final RenderPhase.Target bloomOutput;

    public BloomPostProcessor() {
        this.bloomOutput = new RenderPhase.Target("bloomTarget",
                () -> {
                    if (this.bloomTarget != null) this.bloomTarget.beginWrite(false);
                },
                () -> MinecraftClient.getInstance().getFramebuffer().beginWrite(false)
        );
        this.setActive(false);
    }
    @Override
    public Identifier getPostChainLocation() {
        return LodestoneLib.lodestonePath("bloom");
    }

    @Override
    public void init() {
        super.init();
        if (this.postChain != null) {
            this.bloomTarget = this.postChain.getSecondaryTarget("bloomColor");
            this.bloomTarget.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    @Override
    public void beforeProcess(MatrixStack matrixStack) {

    }

    @Override
    public void afterProcess() {
        this.bloomTarget.clear(MinecraftClient.IS_SYSTEM_MAC);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (this.bloomTarget == null) return;
        this.bloomTarget.resize(width, height, MinecraftClient.IS_SYSTEM_MAC);
    }

    public RenderPhase.Target getBloomOutput() {
        return bloomOutput;
    }

    public Framebuffer getBloomTarget() {
        return bloomTarget;
    }

    public void copyDepthFromMain() {
        this.copyDepthFrom(MinecraftClient.getInstance().getFramebuffer());
    }

    public void copyDepthFrom(Framebuffer src) {
        if (this.bloomTarget == null) return;
        this.bloomTarget.copyDepthFrom(src);
        GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, src.fbo);
    }
}