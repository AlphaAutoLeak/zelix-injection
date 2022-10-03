package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraftforge.fml.common.eventhandler.*;

public final class AutoWalkHack extends Hack
{
    public AutoWalkHack() {
        super("AutoWalk", "Makes you walk automatically.");
        this.setCategory(Category.MOVEMENT);
    }
    
    @Override
    protected void onEnable() {
        AutoWalkHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        KeyBindingUtils.resetPressed(AutoWalkHack.mc.gameSettings.keyBindForward);
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        KeyBindingUtils.setPressed(AutoWalkHack.mc.gameSettings.keyBindForward, true);
    }
}
