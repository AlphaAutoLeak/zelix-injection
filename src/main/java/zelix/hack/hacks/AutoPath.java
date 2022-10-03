package zelix.hack.hacks;

import zelix.hack.*;
import zelix.utils.hooks.visual.*;
import net.minecraftforge.fml.common.gameevent.*;

public class AutoPath extends Hack
{
    public boolean pathsetting;
    
    public AutoPath() {
        super("AutoPath", HackCategory.MOVEMENT);
        this.pathsetting = false;
    }
    
    @Override
    public void onEnable() {
        ChatUtils.message("Start Setting The Path");
        ChatUtils.message("Please Walk And This Module Will Record Your Walking Path And Repeat");
        ChatUtils.message("After Walking, Just Type START To Continue");
        this.pathsetting = true;
        super.onEnable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.pathsetting) {}
        super.onClientTick(event);
    }
}
