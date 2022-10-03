package zelix.otherhacks.net.wurstclient.forge.compatibility;

import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.common.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;

public class WEventFactory
{
    @SubscribeEvent
    public void onPlayerPreTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        final EntityPlayer player = event.player;
        if (player != WMinecraft.getPlayer()) {
            return;
        }
        if (!player.world.isRemote) {
            return;
        }
        MinecraftForge.EVENT_BUS.post((Event)new WUpdateEvent((EntityPlayerSP)player));
    }
    
    @SubscribeEvent
    public void onClientSentMessage(final ClientChatEvent event) {
        final WChatOutputEvent event2 = new WChatOutputEvent(event.getOriginalMessage());
        if (MinecraftForge.EVENT_BUS.post((Event)event2)) {
            event.setCanceled(true);
        }
        event.setMessage(event2.getMessage());
    }
}
