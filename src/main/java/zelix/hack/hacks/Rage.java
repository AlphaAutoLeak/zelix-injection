package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class Rage extends Hack
{
    public TimerUtils timer;
    public NumberValue delay;
    
    public Rage() {
        super("Rage", HackCategory.PLAYER);
        this.timer = new TimerUtils();
        this.delay = new NumberValue("Delay", 0.0, 0.0, 1000.0);
        this.addValue(this.delay);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.timer.isDelay((long)(Object)this.delay.getValue())) {
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Rotation((float)Utils.random(-160, 160), (float)Utils.random(-160, 160), true));
            this.timer.setLastMS();
        }
        super.onClientTick(event);
    }
}
