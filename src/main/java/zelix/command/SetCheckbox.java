package zelix.command;

import zelix.value.*;
import zelix.utils.hooks.visual.*;
import zelix.managers.*;
import zelix.hack.*;
import java.util.*;

public class SetCheckbox extends Command
{
    public SetCheckbox() {
        super("setc");
    }
    
    @Override
    public void runCommand(final String s, final String[] subcommands) {
        final Hack hack = HackManager.getHack(subcommands[0]);
        if (!hack.getValues().isEmpty()) {
            for (final Value value : hack.getValues()) {
                if (value.getName().equalsIgnoreCase(subcommands[1]) && value instanceof BooleanValue) {
                    if (subcommands[2].equalsIgnoreCase("true") || subcommands[2].equalsIgnoreCase("false")) {
                        value.setValue(Boolean.parseBoolean(subcommands[2]));
                        ChatUtils.message("Boolean parameters set successfully!");
                        FileManager.saveHacks();
                        FileManager.saveClickGui();
                    }
                    else {
                        ChatUtils.error("Please use true or false for the third parameter");
                        ChatUtils.message(subcommands[2]);
                    }
                }
            }
        }
    }
    
    @Override
    public String getDescription() {
        return "Set the Boolean parameter";
    }
    
    @Override
    public String getSyntax() {
        return "setc <hack> <setting> <value>";
    }
}
