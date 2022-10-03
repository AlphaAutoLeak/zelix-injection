package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import java.lang.reflect.*;
import net.minecraftforge.fml.common.eventhandler.*;

public final class FastPlaceHack extends Hack
{
    public FastPlaceHack() {
        super("FastPlace", "Allows you to place blocks 5 times faster.");
        this.setCategory(Category.BLOCKS);
    }
    
    @Override
    protected void onEnable() {
        FastPlaceHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        try {
            final Field rightClickDelayTimer = FastPlaceHack.mc.getClass().getDeclaredField(FastPlaceHack.wurst.isObfuscated() ? "rightClickDelayTimer" : "rightClickDelayTimer");
            rightClickDelayTimer.setAccessible(true);
            rightClickDelayTimer.setInt(FastPlaceHack.mc, 0);
        }
        catch (ReflectiveOperationException e) {
            this.setEnabled(false);
            throw new RuntimeException(e);
        }
    }
}
