package zelix.hack.hacks;

import zelix.hack.*;
import zelix.utils.system.*;
import net.minecraft.network.play.client.*;

public class PortalGodMode extends Hack
{
    public PortalGodMode() {
        super("PortalGodMode", HackCategory.ANOTHER);
    }
    
    @Override
    public String getDescription() {
        return "Portal God Mode, cancels the CPacketConfirmTeleport packet.";
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        return !(packet instanceof CPacketConfirmTeleport) && !(packet instanceof CPacketKeepAlive);
    }
}
