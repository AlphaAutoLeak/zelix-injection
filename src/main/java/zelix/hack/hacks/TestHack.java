package zelix.hack.hacks;

import zelix.hack.*;
import zelix.managers.*;
import zelix.*;
import zelix.utils.hooks.visual.*;

public class TestHack extends Hack
{
    public TestHack() {
        super("RefreshCape", HackCategory.ANOTHER);
    }
    
    @Override
    public String getDescription() {
        return "Refresh Your CapeManager";
    }
    
    @Override
    public void onEnable() {
        Core.capeManager = new CapeManager();
        ChatUtils.message("Refreshed!");
        super.onEnable();
    }
    
    void processAttack_mzby() {
    }
}
