package zelix.hack.hacks;

import zelix.hack.*;
import zelix.utils.ReflectionHelper;
import zelix.value.*;
import net.minecraft.network.*;
import zelix.utils.*;
import zelix.utils.system.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.network.play.client.*;

public class AntiHunger extends Hack
{
    public static BooleanValue sprint;
    public static BooleanValue ground;
    
    public AntiHunger() {
        super("AntiHunger", HackCategory.PLAYER);
        this.addValue(AntiHunger.sprint, AntiHunger.ground);
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (((Packet)packet) instanceof CPacketPlayer && AntiHunger.ground.getValue() && !Wrapper.INSTANCE.player().isElytraFlying()) {
            final CPacketPlayer l_Packet = (CPacketPlayer)packet;
            if (Wrapper.INSTANCE.player().fallDistance > 0.0f || Wrapper.INSTANCE.controller().getIsHittingBlock()) {
                ReflectionHelper.setPrivateValue((Class)CPacketPlayer.class, (Object)l_Packet, (Object)true, new String[] { Mapping.onGround });
            }
            else {
                ReflectionHelper.setPrivateValue((Class)CPacketPlayer.class, (Object)l_Packet, (Object)false, new String[] { Mapping.onGround });
            }
        }
        if (((Packet)packet) instanceof CPacketEntityAction && AntiHunger.sprint.getValue()) {
            final CPacketEntityAction l_Packet2 = (CPacketEntityAction)packet;
            if (l_Packet2.getAction() == CPacketEntityAction.Action.START_SPRINTING || l_Packet2.getAction() == CPacketEntityAction.Action.STOP_SPRINTING) {
                return false;
            }
        }
        return true;
    }
    
    static {
        AntiHunger.sprint = new BooleanValue("Sprint", Boolean.valueOf(false));
        AntiHunger.ground = new BooleanValue("Ground", Boolean.valueOf(false));
    }
}
