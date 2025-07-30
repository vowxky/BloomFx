package vowxky.bloomfx;

import net.fabricmc.api.ModInitializer;
import vowxky.api.BloomFxAPI;

public class Bloomfx implements ModInitializer {
    @Override
    public void onInitialize() {
        BloomFxAPI.init();
    }
}
