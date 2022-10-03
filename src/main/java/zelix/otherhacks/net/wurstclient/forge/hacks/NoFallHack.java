package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.eventhandler.*;

public final class NoFallHack extends Hack
{
    public NoFallHack() {
        super("NoFall", "Protects you from fall damage.");
        this.setCategory(Category.MOVEMENT);
    }
    
    @Override
    protected void onEnable() {
        NoFallHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        if (event.getPlayer().fallDistance > 2.0f) {
            NoFallHack.mc.getConnection().sendPacket((Packet)new CPacketPlayer(true));
        }
    }
}
