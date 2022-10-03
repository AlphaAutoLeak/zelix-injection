package zelix.command;

import net.minecraft.client.network.*;
import zelix.utils.hooks.visual.*;
import zelix.utils.*;
import java.util.*;

public class DumpPlayers extends Command
{
    public DumpPlayers() {
        super("dumpplayers");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        try {
            final ArrayList<String> list = new ArrayList<String>();
            if (args[0].equalsIgnoreCase("all")) {
                for (final NetworkPlayerInfo npi : Wrapper.INSTANCE.mc().getConnection().getPlayerInfoMap()) {
                    list.add("\n" + npi.getGameProfile().getName());
                }
            }
            else if (args[0].equalsIgnoreCase("creatives")) {
                for (final NetworkPlayerInfo npi : Wrapper.INSTANCE.mc().getConnection().getPlayerInfoMap()) {
                    if (npi.getGameType().isCreative()) {
                        list.add("\n" + npi.getGameProfile().getName());
                    }
                }
            }
            if (list.isEmpty()) {
                ChatUtils.error("List is empty.");
            }
            else {
                Utils.copy(list.toString());
                ChatUtils.message("List copied to clipboard.");
            }
        }
        catch (Exception e) {
            ChatUtils.error("Usage: " + this.getSyntax());
        }
    }
    
    @Override
    public String getDescription() {
        return "Get list of players.";
    }
    
    @Override
    public String getSyntax() {
        return "dumpplayers <all/creatives>";
    }
}
