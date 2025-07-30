package vowxky.bloomfx.client;

import team.lodestar.lodestone.systems.postprocess.PostProcessor;


public abstract class PostProcessorModified extends PostProcessor {
    private boolean forceDisabled = false;

    public void setForceDisabled() {
        this.forceDisabled = true;

        if (this.isActive()) {
            this.setActive(false);
        }
    }

    public void enable() {
        setActiveForced(true);
    }

    public void setActiveForced(boolean active) {
        if (!active || !forceDisabled) {
            this.setActive(active);
        }
    }
}