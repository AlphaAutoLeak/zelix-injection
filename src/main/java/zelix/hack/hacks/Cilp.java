package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import zelix.utils.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class Cilp extends Hack
{
    TimerUtils timer;
    NumberValue horizontalValue;
    NumberValue verticalValue;
    
    public Cilp() {
        super("Cilp", HackCategory.MOVEMENT);
        this.timer = new TimerUtils();
        this.horizontalValue = new NumberValue("Horizontal", 0.0, 0.0, 10.0);
        this.verticalValue = new NumberValue("Vertical", 5.0, 0.0, 10.0);
        this.addValue(this.horizontalValue, this.verticalValue);
    }
    
    @Override
    public void onEnable() {
        Utils.nullCheck();
        this.Jump();
        this.toggle();
        super.onEnable();
    }
    
    public void Jump() {
        final double yaw = Math.toRadians(Wrapper.INSTANCE.player().rotationYaw);
        final double x = -Math.sin(yaw) * this.horizontalValue.getValue();
        final double z = Math.cos(yaw) * this.horizontalValue.getValue();
        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ, true));
        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(0.5, 0.0, 0.5, true));
        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ, true));
        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX + x, Wrapper.INSTANCE.player().posY + this.verticalValue.getValue(), Wrapper.INSTANCE.player().posZ + z, true));
        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(0.5, 0.0, 0.5, true));
        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX + 0.5, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ + 0.5, true));
        Wrapper.INSTANCE.player().setPosition(Wrapper.INSTANCE.player().posX + -Math.sin(yaw) * 0.04, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ + Math.cos(yaw) * 0.04);
    }
}
