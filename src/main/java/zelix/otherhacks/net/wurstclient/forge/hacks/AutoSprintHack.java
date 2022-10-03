package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.fml.common.eventhandler.*;

public final class AutoSprintHack extends Hack
{
    public AutoSprintHack() {
        super("AutoSprint", "Makes you sprint automatically.");
        this.setCategory(Category.MOVEMENT);
    }
    
    @Override
    protected void onEnable() {
        AutoSprintHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        final EntityPlayerSP player = event.getPlayer();
        if (player.collidedHorizontally || player.isSneaking()) {
            return;
        }
        if (player.moveForward > 0.0f) {
            player.setSprinting(true);
        }
    }
}
