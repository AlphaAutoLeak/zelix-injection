package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.fml.common.eventhandler.*;

public final class SpiderHack extends Hack
{
    public SpiderHack() {
        super("Spider", "Allows you to climb up walls like a spider.");
        this.setCategory(Category.MOVEMENT);
    }
    
    @Override
    protected void onEnable() {
        SpiderHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        final EntityPlayerSP player = event.getPlayer();
        if (!player.collidedHorizontally) {
            return;
        }
        if (player.motionY < 0.2) {
            player.motionY = 0.2;
        }
    }
}
