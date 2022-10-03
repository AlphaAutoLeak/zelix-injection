package zelix.otherhacks.net.wurstclient.forge.commands;

import zelix.otherhacks.net.wurstclient.forge.*;
import net.minecraft.client.*;

public final class ClearCmd extends Command
{
    public ClearCmd() {
        super("clear", "Clears the chat completely.", new String[] { "Syntax: .clear" });
    }
    
    @Override
    public void call(final String[] args) throws CmdException {
        if (args.length > 0) {
            throw new CmdSyntaxError();
        }
        Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages(true);
    }
}
