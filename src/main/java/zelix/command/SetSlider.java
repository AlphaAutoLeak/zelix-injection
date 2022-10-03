package zelix.command;

import zelix.value.*;
import zelix.utils.hooks.visual.*;
import zelix.managers.*;
import zelix.hack.*;
import java.util.*;

public class SetSlider extends Command
{
    public SetSlider() {
        super("sets");
    }
    
    @Override
    public void runCommand(final String s, final String[] subcommands) {
        final Hack hack = HackManager.getHack(subcommands[0]);
        if (!hack.getValues().isEmpty()) {
            for (final Value value : hack.getValues()) {
                if (value.getName().equalsIgnoreCase(subcommands[1]) && value instanceof NumberValue) {
                    value.setValue(Double.parseDouble(subcommands[2]));
                    ChatUtils.message("Number parameters set successfully!");
                    FileManager.saveHacks();
                    FileManager.saveClickGui();
                }
            }
        }
    }
    
    @Override
    public String getDescription() {
        return "Set the Number parameter";
    }
    
    @Override
    public String getSyntax() {
        return "sets <hack> <setting> <value>";
    }
}
