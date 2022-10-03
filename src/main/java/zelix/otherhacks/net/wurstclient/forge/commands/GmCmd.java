package zelix.otherhacks.net.wurstclient.forge.commands;

import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;

public final class GmCmd extends Command
{
    public GmCmd() {
        super("gm", "Shortcut for /gamemode.", new String[] { "Syntax: .gm <gamemode>" });
    }
    
    @Override
    public void call(final String[] args) throws CmdException {
        if (args.length < 1) {
            throw new CmdSyntaxError();
        }
        final String message = "/gamemode " + String.join(" ", (CharSequence[])args);
        WMinecraft.getPlayer().sendChatMessage(message);
    }
}
