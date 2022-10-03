package zelix.hack.hacks;

import net.minecraft.network.play.client.*;
import zelix.hack.*;
import java.util.*;
import zelix.utils.system.*;
import zelix.utils.hooks.visual.*;
import zelix.utils.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;

public class BlinkAttack extends Hack
{
    public Entity301 entity301;
    Queue<CPacketUseEntity> packets;
    boolean send;
    
    public BlinkAttack() {
        super("BlinkAttack", HackCategory.COMBAT);
        this.entity301 = null;
        this.packets = new LinkedList<CPacketUseEntity>();
        this.send = false;
    }
    
    @Override
    public String getDescription() {
        return "Allows you to move without sending it to the server.";
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (side == Connection.Side.OUT && packet instanceof CPacketUseEntity) {
            ChatUtils.message("AttackPackets:" + String.valueOf(this.packets.size()));
            this.send = false;
            this.packets.add((CPacketUseEntity)packet);
            return this.send;
        }
        return this.send = true;
    }
    
    @Override
    public void onEnable() {
        if (Wrapper.INSTANCE.player() != null && Wrapper.INSTANCE.world() != null) {
            (this.entity301 = new Entity301((World)Wrapper.INSTANCE.world(), Wrapper.INSTANCE.player().getGameProfile())).setPosition(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ);
            this.entity301.inventory = Wrapper.INSTANCE.inventory();
            this.entity301.rotationPitch = Wrapper.INSTANCE.player().rotationPitch;
            this.entity301.rotationYaw = Wrapper.INSTANCE.player().rotationYaw;
            this.entity301.rotationYawHead = Wrapper.INSTANCE.player().rotationYawHead;
            Wrapper.INSTANCE.world().spawnEntity((Entity)this.entity301);
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        while (!this.packets.isEmpty()) {
            Wrapper.INSTANCE.player().connection.sendPacket((Packet)this.packets.poll());
        }
        if (this.entity301 != null && Wrapper.INSTANCE.world() != null) {
            Wrapper.INSTANCE.world().removeEntity((Entity)this.entity301);
            this.entity301 = null;
        }
        super.onDisable();
    }
}
