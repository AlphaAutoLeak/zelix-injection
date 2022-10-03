package zelix.otherhacks.net.wurstclient.forge;

import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraft.client.*;
import net.minecraftforge.fml.common.eventhandler.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import java.util.*;

public final class CommandProcessor
{
    private final CommandList cmds;
    
    public CommandProcessor(final CommandList cmds) {
        this.cmds = cmds;
    }
    
    @SubscribeEvent
    public void onSentMessage(final WChatOutputEvent event) {
        final String message = event.getMessage().trim();
        if (!message.startsWith(".")) {
            return;
        }
        event.setCanceled(true);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(message);
        this.runCommand(message.substring(1));
    }
    
    public void runCommand(final String input) {
        final String[] parts = input.split(" ");
        final Command cmd = this.cmds.get(parts[0]);
        if (cmd == null) {
            ChatUtils.error("Unknown command: ." + parts[0]);
            if (input.startsWith("/")) {
                ChatUtils.message("Use \".say " + input + "\" to send it as a chat command.");
            }
            else {
                ChatUtils.message("Type \".help\" for a list of commands or \".say ." + input + "\" to send it as a chat message.");
            }
            return;
        }
        try {
            cmd.call(Arrays.copyOfRange(parts, 1, parts.length));
        }
        catch (Command.CmdException e) {
            e.printToChat();
        }
    }
}
