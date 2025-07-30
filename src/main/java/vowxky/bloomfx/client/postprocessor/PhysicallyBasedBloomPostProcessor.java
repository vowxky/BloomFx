package vowxky.bloomfx.client.postprocessor;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import team.lodestar.lodestone.LodestoneLib;
import vowxky.bloomfx.client.PostProcessorModified;

public class PhysicallyBasedBloomPostProcessor extends PostProcessorModified {
    private Framebuffer bloomTarget;
    private final RenderPhase.Target bloomOutput;

    private Framebuffer BLURX2, BLURY2, BLURX4, BLURY4, BLURX8, BLURY8;

    public PhysicallyBasedBloomPostProcessor() {
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
        return LodestoneLib.lodestonePath("pb_bloom");
    }

    @Override
    public void init() {
        super.init();
        if (this.postChain != null) {
            this.bloomTarget = this.postChain.getSecondaryTarget("bloomColor");

            this.BLURX2 = this.postChain.getSecondaryTarget("blurX2");
            this.BLURY2 = this.postChain.getSecondaryTarget("blurY2");
            this.BLURX4 = this.postChain.getSecondaryTarget("blurX4");
            this.BLURY4 = this.postChain.getSecondaryTarget("blurY4");
            this.BLURX8 = this.postChain.getSecondaryTarget("blurX8");
            this.BLURY8 = this.postChain.getSecondaryTarget("blurY8");

            var window = MinecraftClient.getInstance().getWindow();
            this.resize(window.getFramebufferWidth(), window.getFramebufferHeight());
        }
    }

    @Override
    public void beforeProcess(MatrixStack viewModelMatrix) {
    }

    @Override
    public void afterProcess() {
        if (this.bloomTarget == null) return;
        this.bloomTarget.clear(MinecraftClient.IS_SYSTEM_MAC);
    }

    @Override
    public void resize(int width, int height) {
        if (this.postChain == null) return;

        this.postChain.width = width;
        this.postChain.height = this.postChain.mainTarget.textureHeight;
        this.postChain.setupProjectionMatrix();

        for (PostEffectPass postPass : this.postChain.passes) {
            postPass.setProjectionMatrix(this.postChain.projectionMatrix);
        }

        this.bloomTarget.resize(width, height, MinecraftClient.IS_SYSTEM_MAC);

        resize(this.BLURX2, width, height, 2);
        resize(this.BLURY2, width, height, 2);
        resize(this.BLURX4, width, height, 4);
        resize(this.BLURY4, width, height, 4);
        resize(this.BLURX8, width, height, 8);
        resize(this.BLURY8, width, height, 8);
    }

    private void resize(Framebuffer target, int width, int height, int divisor) {
        if (target == null) return;
        target.resize(width / divisor, height / divisor, MinecraftClient.IS_SYSTEM_MAC);
        target.setTexFilter(GL11.GL_LINEAR);
    }

    public RenderPhase.Target getBloomOutput() {
        return bloomOutput;
    }

    public Framebuffer getBloomTarget() {
        return bloomTarget;
    }

    public void copyDepthFromMain() {
        copyDepthFrom(MinecraftClient.getInstance().getFramebuffer());
    }

    public void copyDepthFrom(Framebuffer src) {
        if (this.bloomTarget == null || src == null) return;
        this.bloomTarget.copyDepthFrom(src);
        GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, src.fbo);
    }
}