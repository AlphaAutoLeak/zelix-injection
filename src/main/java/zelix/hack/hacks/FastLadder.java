package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;

public class FastLadder extends Hack
{
    public FastLadder() {
        super("FastLadder", HackCategory.PLAYER);
    }
    
    @Override
    public String getDescription() {
        return "Allows you to climb up ladders faster.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (!Wrapper.INSTANCE.player().isOnLadder() || (Wrapper.INSTANCE.player().moveForward == 0.0f && Wrapper.INSTANCE.player().moveStrafing == 0.0f)) {
            return;
        }
        Wrapper.INSTANCE.player().motionY = 0.169;
        super.onClientTick(event);
    }
}
