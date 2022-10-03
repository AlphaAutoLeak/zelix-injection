package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;

public class Spider extends Hack
{
    public Spider() {
        super("Spider", HackCategory.PLAYER);
    }
    
    @Override
    public String getDescription() {
        return "Allows you to climb up walls like a spider.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (!Wrapper.INSTANCE.player().isOnLadder() && Wrapper.INSTANCE.player().collidedHorizontally && Wrapper.INSTANCE.player().motionY < 0.2) {
            Wrapper.INSTANCE.player().motionY = 0.2;
        }
        super.onClientTick(event);
    }
}
