package vowxky.api;

import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;
import vowxky.bloomfx.client.postprocessor.BloomPostProcessor;
import vowxky.bloomfx.client.postprocessor.PhysicallyBasedBloomPostProcessor;

public class BloomFxAPI {
    private static final BloomPostProcessor BLOOM = new BloomPostProcessor();
    private static final PhysicallyBasedBloomPostProcessor PHYSICAL_BLOOM = new PhysicallyBasedBloomPostProcessor();

    public static void init() {
        PostProcessHandler.addInstance(BLOOM);
    }

    public static BloomPostProcessor getBloom() {
        return BLOOM;
    }

    public static PhysicallyBasedBloomPostProcessor getPhysicalBloom() {
        return PHYSICAL_BLOOM;
    }
}
