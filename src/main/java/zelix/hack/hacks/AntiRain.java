package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;

public class AntiRain extends Hack
{
    public AntiRain() {
        super("AntiRain", HackCategory.VISUAL);
    }
    
    @Override
    public String getDescription() {
        return "Stops rain.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        Wrapper.INSTANCE.world().setRainStrength(0.0f);
        super.onClientTick(event);
    }
}
