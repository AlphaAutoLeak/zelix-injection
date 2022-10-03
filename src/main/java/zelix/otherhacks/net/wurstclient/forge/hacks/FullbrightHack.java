package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;

public final class FullbrightHack extends Hack
{
    public FullbrightHack() {
        super("Fullbright", "Allows you to see in the dark.");
        this.setCategory(Category.RENDER);
    }
    
    @Override
    protected void onEnable() {
        FullbrightHack.wurst.register(this);
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        if (this.isEnabled()) {
            if (FullbrightHack.mc.gameSettings.gammaSetting < 16.0f) {
                FullbrightHack.mc.gameSettings.gammaSetting = Math.min(FullbrightHack.mc.gameSettings.gammaSetting + 0.5f, 16.0f);
            }
            return;
        }
        if (FullbrightHack.mc.gameSettings.gammaSetting > 0.5f) {
            FullbrightHack.mc.gameSettings.gammaSetting = Math.max(FullbrightHack.mc.gameSettings.gammaSetting - 0.5f, 0.5f);
        }
        else {
            MinecraftForge.EVENT_BUS.unregister((Object)this);
        }
    }
}
