package zelix.otherhacks.net.wurstclient.forge.commands;

import zelix.otherhacks.net.wurstclient.forge.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public final class SayCmd extends Command
{
    public SayCmd() {
        super("say", "Sends the given chat message.", new String[] { "Syntax: .say <message>" });
    }
    
    @Override
    public void call(final String[] args) throws CmdException {
        if (args.length < 1) {
            throw new CmdSyntaxError();
        }
        final String message = String.join(" ", (CharSequence[])args);
        final CPacketChatMessage packet = new CPacketChatMessage(message);
        SayCmd.mc.getConnection().sendPacket((Packet)packet);
    }
}
