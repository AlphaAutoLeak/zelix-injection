package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.event.entity.player.*;
import zelix.utils.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;

public class SuperKick extends Hack
{
    NumberValue hurtTimeValue;
    
    public SuperKick() {
        super("SuperKick", HackCategory.COMBAT);
        this.hurtTimeValue = new NumberValue("HurtTime", 10.0, 0.0, 10.0);
        this.addValue(this.hurtTimeValue);
    }
    
    @Override
    public void onAttackEntity(final AttackEntityEvent event) {
        if (event.getTarget().hurtResistantTime > (int)(Object)this.hurtTimeValue.getValue() && Wrapper.INSTANCE.player().isSprinting()) {
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketEntityAction((Entity)Wrapper.INSTANCE.player(), CPacketEntityAction.Action.STOP_SPRINTING));
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketEntityAction((Entity)Wrapper.INSTANCE.player(), CPacketEntityAction.Action.START_SPRINTING));
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketEntityAction((Entity)Wrapper.INSTANCE.player(), CPacketEntityAction.Action.STOP_SPRINTING));
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketEntityAction((Entity)Wrapper.INSTANCE.player(), CPacketEntityAction.Action.START_SPRINTING));
            Wrapper.INSTANCE.player().setSprinting(true);
        }
        super.onAttackEntity(event);
    }
}
