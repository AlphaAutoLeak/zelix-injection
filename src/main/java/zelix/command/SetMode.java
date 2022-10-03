package zelix.command;

import zelix.utils.hooks.visual.*;
import zelix.managers.*;
import zelix.hack.*;
import java.util.*;
import zelix.value.*;

public class SetMode extends Command
{
    public SetMode() {
        super("setm");
    }
    
    @Override
    public void runCommand(final String s, final String[] subcommands) {
        final Hack hack = HackManager.getHack(subcommands[0]);
        if (!hack.getValues().isEmpty()) {
            for (final Value value : hack.getValues()) {
                if (value instanceof ModeValue && ((ModeValue)value).getModeName().equalsIgnoreCase(subcommands[1])) {
                    final ModeValue modeValue = (ModeValue)value;
                    for (final Mode mode : modeValue.getModes()) {
                        if (mode.getName().equalsIgnoreCase(subcommands[2])) {
                            mode.setToggled(true);
                            ChatUtils.message("Mode parameters set successfully!");
                            FileManager.saveHacks();
                        }
                        else {
                            mode.setToggled(false);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public String getDescription() {
        return "Set the Mode parameter";
    }
    
    @Override
    public String getSyntax() {
        return "setm <hack> <mode> <modevalue> <true/false>";
    }
}
