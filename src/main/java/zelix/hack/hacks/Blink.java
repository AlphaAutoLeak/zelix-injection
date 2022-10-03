package zelix.hack.hacks;

import net.minecraft.network.play.client.*;
import zelix.hack.*;
import java.util.*;
import zelix.utils.system.*;
import zelix.utils.hooks.visual.*;
import zelix.utils.*;
import net.minecraft.network.*;

public class Blink extends Hack
{
    Queue<CPacketPlayer> packets;
    boolean send;
    
    public Blink() {
        super("Blink", HackCategory.PLAYER);
        this.packets = new LinkedList<CPacketPlayer>();
        this.send = false;
    }
    
    @Override
    public String getDescription() {
        return "Allows you to move without sending it to the server.";
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (side == Connection.Side.OUT && packet instanceof CPacketPlayer) {
            ChatUtils.message("Packets:" + String.valueOf(this.packets.size()));
            this.send = false;
            this.packets.add((CPacketPlayer)packet);
            return this.send;
        }
        return this.send = true;
    }
    
    @Override
    public void onEnable() {
        if (Wrapper.INSTANCE.player() == null || Wrapper.INSTANCE.world() != null) {}
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        while (!this.packets.isEmpty()) {
            Wrapper.INSTANCE.sendPacket((Packet)this.packets.poll());
        }
        super.onDisable();
    }
}
