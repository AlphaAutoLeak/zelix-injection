package zelix.command;

import zelix.managers.*;
import zelix.hack.*;
import org.lwjgl.input.*;
import zelix.utils.hooks.visual.*;
import java.util.*;

public class Key extends Command
{
    public Key() {
        super("key");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        try {
            for (final Hack hack : HackManager.getHacks()) {
                if (hack.getName().equalsIgnoreCase(args[1])) {
                    hack.setKey(Keyboard.getKeyIndex(args[0].toUpperCase()));
                    ChatUtils.message(hack.getName() + " key changed to ¡ì9" + Keyboard.getKeyName(hack.getKey()));
                }
            }
        }
        catch (Exception e) {
            ChatUtils.error("Usage: " + this.getSyntax());
        }
    }
    
    @Override
    public String getDescription() {
        return "Change key for hack.";
    }
    
    @Override
    public String getSyntax() {
        return "key <key> <hack>";
    }
}
