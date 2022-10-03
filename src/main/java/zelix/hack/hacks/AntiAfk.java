package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;

public class AntiAfk extends Hack
{
    public NumberValue delay;
    public TimerUtils timer;
    
    public AntiAfk() {
        super("AntiAfk", HackCategory.ANOTHER);
        this.timer = new TimerUtils();
        this.delay = new NumberValue("DelaySec", 10.0, 1.0, 100.0);
        this.addValue(this.delay);
    }
    
    @Override
    public String getDescription() {
        return "Prevents from being kicked for AFK.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.timer.isDelay((long)(1000.0 * this.delay.getValue()))) {
            Wrapper.INSTANCE.player().jump();
            this.timer.setLastMS();
        }
        super.onClientTick(event);
    }
}
