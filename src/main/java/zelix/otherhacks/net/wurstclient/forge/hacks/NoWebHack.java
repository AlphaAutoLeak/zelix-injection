package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraft.entity.*;
import net.minecraft.client.entity.*;
import java.lang.reflect.*;
import net.minecraftforge.fml.common.eventhandler.*;

public final class NoWebHack extends Hack
{
    public NoWebHack() {
        super("NoWeb", "Prevents you from getting slowed down in webs.");
        this.setCategory(Category.MOVEMENT);
    }
    
    @Override
    protected void onEnable() {
        NoWebHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        final EntityPlayerSP player = event.getPlayer();
        try {
            final Field isInWeb = Entity.class.getDeclaredField(NoWebHack.wurst.isObfuscated() ? "isInWeb" : "isInWeb");
            isInWeb.setAccessible(true);
            isInWeb.setBoolean(player, false);
        }
        catch (ReflectiveOperationException e) {
            this.setEnabled(false);
            throw new RuntimeException(e);
        }
    }
}
