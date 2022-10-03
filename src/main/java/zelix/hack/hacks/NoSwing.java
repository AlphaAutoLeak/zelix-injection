package zelix.hack.hacks;

import zelix.hack.*;
import zelix.utils.system.*;
import net.minecraft.network.play.client.*;

public class NoSwing extends Hack
{
    public NoSwing() {
        super("NoSwing", HackCategory.PLAYER);
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        return side != Connection.Side.OUT || !(packet instanceof CPacketAnimation);
    }
}
