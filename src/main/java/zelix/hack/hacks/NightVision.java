package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import zelix.utils.*;
import net.minecraftforge.fml.common.gameevent.*;

public class NightVision extends Hack
{
    public ModeValue mode;
    
    public NightVision() {
        super("NightVision", HackCategory.VISUAL);
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("Brightness", true), new Mode("Effect", false) });
        this.addValue(this.mode);
    }
    
    @Override
    public String getDescription() {
        return "Gets you night vision.";
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        if (this.mode.getMode("Brightness").isToggled()) {
            Wrapper.INSTANCE.mcSettings().gammaSetting = 1.0f;
        }
        else {
            Utils.removeEffect(16);
        }
        super.onDisable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.mode.getMode("Brightness").isToggled()) {
            Wrapper.INSTANCE.mcSettings().gammaSetting = 10.0f;
        }
        else {
            Utils.addEffect(16, 1000, 3);
        }
        super.onClientTick(event);
    }
}
