package zelix.command;

import net.minecraft.client.*;
import zelix.utils.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class TP extends Command
{
    public TP() {
        super("tp");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        if (args[0] != null && args[1] != null && args[2] != null) {
            final int x = Integer.parseInt(args[0]);
            final int y = Integer.parseInt(args[1]);
            final int z = Integer.parseInt(args[2]);
            Minecraft.getMinecraft().player.setPosition((double)x, (double)y, (double)z);
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position((double)x, (double)y, (double)z, Wrapper.INSTANCE.player().onGround));
        }
    }
    
    @Override
    public String getDescription() {
        return "TP Anywhere";
    }
    
    @Override
    public String getSyntax() {
        return "tp <X> <Y> <Z>tp <PlayerID>";
    }
}
