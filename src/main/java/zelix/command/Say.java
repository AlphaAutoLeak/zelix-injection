package zelix.command;

import zelix.utils.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import zelix.utils.hooks.visual.*;

public class Say extends Command
{
    public Say() {
        super("say");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        try {
            String content = "";
            for (int i = 0; i < args.length; ++i) {
                content = content + " " + args[i];
            }
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketChatMessage(content));
        }
        catch (Exception e) {
            ChatUtils.error("Usage: " + this.getSyntax());
        }
    }
    
    @Override
    public String getDescription() {
        return "Send message to chat.";
    }
    
    @Override
    public String getSyntax() {
        return "say <message>";
    }
}
