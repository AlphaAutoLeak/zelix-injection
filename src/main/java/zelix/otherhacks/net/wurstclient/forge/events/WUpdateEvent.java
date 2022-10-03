package zelix.otherhacks.net.wurstclient.forge.events;

import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.client.entity.*;

public final class WUpdateEvent extends Event
{
    private final EntityPlayerSP player;
    
    public WUpdateEvent(final EntityPlayerSP player) {
        this.player = player;
    }
    
    public EntityPlayerSP getPlayer() {
        return this.player;
    }
}
