package zelix.command;

import zelix.managers.*;
import zelix.utils.hooks.visual.*;
import java.util.*;

public class Help extends Command
{
    public Help() {
        super("help");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        for (final Command cmd : CommandManager.commands) {
            if (cmd != this) {
                ChatUtils.message(cmd.getSyntax().replace("<", "<¡ì9").replace(">", "¡ì7>") + " - " + cmd.getDescription());
            }
        }
    }
    
    @Override
    public String getDescription() {
        return "Lists all commands.";
    }
    
    @Override
    public String getSyntax() {
        return "help";
    }
}
