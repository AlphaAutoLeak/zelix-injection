package zelix.hack.hacks;

import zelix.hack.*;
import zelix.utils.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class SelfKick extends Hack
{
    public SelfKick() {
        super("SelfKick", HackCategory.ANOTHER);
    }
    
    @Override
    public String getDescription() {
        return "Kick you from Server.";
    }
    
    @Override
    public void onEnable() {
        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Rotation(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, false));
        super.onEnable();
    }
}
