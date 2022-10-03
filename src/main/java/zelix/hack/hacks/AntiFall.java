package zelix.hack.hacks;

import zelix.hack.*;
import zelix.utils.ReflectionHelper;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import zelix.utils.system.*;
import net.minecraftforge.fml.relauncher.*;
import java.lang.reflect.*;

public class AntiFall extends Hack
{
    public ModeValue mode;
    
    public AntiFall() {
        super("NoFall", HackCategory.PLAYER);
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("AAC", true), new Mode("Simple", false) });
        this.addValue(this.mode);
    }
    
    @Override
    public String getDescription() {
        return "Gives you zero damage on fall.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.mode.getMode("Simple").isToggled() && Wrapper.INSTANCE.player().fallDistance > 2.0f) {
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer(true));
        }
        super.onClientTick(event);
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (side == Connection.Side.OUT && packet instanceof CPacketPlayer) {
            final CPacketPlayer p = (CPacketPlayer)packet;
            if (this.mode.getMode("AAC").isToggled()) {
                final Field field = ReflectionHelper.findField((Class)CPacketPlayer.class, new String[] { "onGround", "onGround" });
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    field.setBoolean(p, true);
                }
                catch (Exception ex) {}
            }
        }
        return true;
    }
}
