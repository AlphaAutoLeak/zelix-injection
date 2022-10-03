package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.client.*;
import zelix.utils.system.*;
import net.minecraft.util.*;
import java.lang.reflect.*;

public class FastUse extends Hack
{
    Number ticks;
    BooleanValue onGround;
    ModeValue mode;
    public static Boolean eated;
    
    public FastUse() {
        super("FastUse", HackCategory.PLAYER);
        this.ticks = 0.49;
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("HytTimer", true) });
        this.onGround = new BooleanValue("OnGround", Boolean.valueOf(true));
        this.addValue(this.mode, this.onGround);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.setTickLength(50.0f);
        super.onDisable();
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (this.mode.getMode("HytTimer").isToggled() && Wrapper.INSTANCE.player().isHandActive() && !Wrapper.INSTANCE.player().isRiding()) {
            if (this.onGround.getValue()) {
                if (Wrapper.INSTANCE.player().isHandActive() && !Wrapper.INSTANCE.player().isRiding()) {
                    this.setTickLength(50.0f / this.ticks.floatValue());
                    final String check = null;
                    for (int n3 = 2, j = 0; j < n3; ++j) {
                        final int it = j;
                        final int n4 = 0;
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer(Wrapper.INSTANCE.player().onGround));
                    }
                }
            }
            else {
                this.setTickLength(50.0f / this.ticks.floatValue());
                final int n5 = 2;
                final int i = 0;
                if (i < n5) {
                    final int it2 = i;
                    final int n6 = 0;
                    Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer(Wrapper.INSTANCE.player().onGround));
                }
            }
        }
    }
    
    private void setTickLength(final float tickLength) {
        try {
            final Field fTimer = Minecraft.getMinecraft().getClass().getDeclaredField(Mapping.timer);
            fTimer.setAccessible(true);
            final Field fTickLength = Timer.class.getDeclaredField(Mapping.tickLength);
            fTickLength.setAccessible(true);
            fTickLength.setFloat(fTimer.get(Minecraft.getMinecraft()), tickLength);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
    
    static {
        FastUse.eated = false;
    }
}
