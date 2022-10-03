package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.fml.common.eventhandler.*;

public final class FastLadderHack extends Hack
{
    public FastLadderHack() {
        super("FastLadder", "Allows you to climb up ladders faster.");
        this.setCategory(Category.MOVEMENT);
    }
    
    @Override
    protected void onEnable() {
        FastLadderHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        final EntityPlayerSP player = event.getPlayer();
        if (!player.isOnLadder() || !player.collidedHorizontally) {
            return;
        }
        if (player.movementInput.moveForward == 0.0f && player.movementInput.moveStrafe == 0.0f) {
            return;
        }
        if (player.motionY < 0.25) {
            player.motionY = 0.25;
        }
    }
}
