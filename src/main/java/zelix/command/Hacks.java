package zelix.command;

import zelix.managers.*;
import zelix.hack.*;
import zelix.utils.hooks.visual.*;
import java.util.*;

public class Hacks extends Command
{
    public Hacks() {
        super("hacks");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        for (final Hack hack : HackManager.getHacks()) {
            ChatUtils.message(String.format("%s ¡ì9| ¡ìf%s ¡ì9| ¡ìf%s ¡ì9| ¡ìf%s", hack.getName(), hack.getCategory(), hack.getKey(), hack.isToggled()));
        }
        ChatUtils.message("Loaded " + HackManager.getHacks().size() + " Hacks.");
    }
    
    @Override
    public String getDescription() {
        return "Lists all hacks.";
    }
    
    @Override
    public String getSyntax() {
        return "hacks";
    }
}
