package zelix.command;

import zelix.*;
import zelix.utils.hooks.visual.*;

public class Cape extends Command
{
    public Cape() {
        super("Cape");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        try {
            if (args[0].equals("reload")) {
                Core.capeManager.reload();
            }
        }
        catch (Exception e) {
            ChatUtils.error("Usage: " + this.getSyntax());
        }
    }
    
    @Override
    public String getDescription() {
        return "Opening directory of config.";
    }
    
    @Override
    public String getSyntax() {
        return "opendir";
    }
}
