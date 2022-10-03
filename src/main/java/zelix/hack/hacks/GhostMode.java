package zelix.hack.hacks;

import zelix.hack.*;

public class GhostMode extends Hack
{
    public static boolean enabled;
    
    public GhostMode() {
        super("GhostMode", HackCategory.ANOTHER);
    }
    
    @Override
    public String getDescription() {
        return "Disable all hacks.";
    }
    
    @Override
    public void onEnable() {
        if (this.getKey() == -1) {
            return;
        }
        GhostMode.enabled = true;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        GhostMode.enabled = false;
        super.onDisable();
    }
    
    static {
        GhostMode.enabled = false;
    }
}
