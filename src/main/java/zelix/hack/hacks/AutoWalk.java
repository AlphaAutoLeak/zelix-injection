package zelix.hack.hacks;

import zelix.hack.*;
import zelix.utils.*;
import net.minecraft.client.settings.*;
import net.minecraftforge.fml.common.gameevent.*;

public class AutoWalk extends Hack
{
    public AutoWalk() {
        super("AutoWalk", HackCategory.PLAYER);
    }
    
    @Override
    public String getDescription() {
        return "Automatic walking.";
    }
    
    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindForward.getKeyCode(), false);
        super.onDisable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindForward.getKeyCode(), true);
        super.onClientTick(event);
    }
}
