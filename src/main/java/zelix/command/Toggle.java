package zelix.command;

import zelix.managers.*;
import zelix.utils.hooks.visual.*;

public class Toggle extends Command
{
    public Toggle() {
        super("t");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        try {
            HackManager.getHack(args[0]).toggle();
        }
        catch (Exception e) {
            ChatUtils.error("Usage: " + this.getSyntax());
        }
    }
    
    @Override
    public String getDescription() {
        return "Toggling selected hack.";
    }
    
    @Override
    public String getSyntax() {
        return "t <hackname>";
    }
}
