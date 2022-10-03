package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import zelix.utils.system.*;
import net.minecraft.network.play.client.*;

public class Disabler extends Hack
{
    public ModeValue mode;
    
    public Disabler() {
        super("Disabler", HackCategory.COMBAT);
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("Hyt", true) });
        this.addValue(this.mode);
    }
    
    @Override
    public String getDescription() {
        return "Hyt Disabler.";
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        return !(packet instanceof CPacketPlayer);
    }
}
