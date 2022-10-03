package zelix.command;

import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.utils.hooks.visual.*;

public class LoadHack extends Command
{
    public LoadHack() {
        super("loadWURST");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        try {
            new ForgeWurst();
        }
        catch (Exception e) {
            ChatUtils.error("Usage: " + this.getSyntax());
        }
    }
    
    @Override
    public String getDescription() {
        return "loadhack other hacks";
    }
    
    @Override
    public String getSyntax() {
        return "loadhack WURST";
    }
}
