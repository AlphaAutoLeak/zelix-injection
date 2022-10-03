package zelix.otherhacks.net.wurstclient.forge.commands;

import org.lwjgl.input.*;
import java.util.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import zelix.otherhacks.net.wurstclient.forge.*;

public final class BindsCmd extends Command
{
    public BindsCmd() {
        super("binds", "Manages keybinds.", new String[] { "Syntax: .binds add <key> <hacks>", ".binds add <key> <commands>", ".binds remove <key>", ".binds list [<page>]", ".binds remove-all", ".binds reset", "Multiple hacks/commands must be separated by ';'." });
    }
    
    @Override
    public void call(final String[] args) throws CmdException {
        if (args.length < 1) {
            throw new CmdSyntaxError();
        }
        final String lowerCase = args[0].toLowerCase();
        switch (lowerCase) {
            case "add": {
                this.add(args);
                break;
            }
            case "remove": {
                this.remove(args);
                break;
            }
            case "list": {
                this.list(args);
                break;
            }
            case "remove-all": {
                BindsCmd.wurst.getKeybinds().removeAll();
                ChatUtils.message("All keybinds removed.");
                break;
            }
            case "reset": {
                BindsCmd.wurst.getKeybinds().loadDefaults();
                ChatUtils.message("All keybinds reset to defaults.");
                break;
            }
            default: {
                throw new CmdSyntaxError();
            }
        }
    }
    
    private void add(final String[] args) throws CmdException {
        if (args.length < 3) {
            throw new CmdSyntaxError();
        }
        final String key = args[1].toUpperCase();
        if (Keyboard.getKeyIndex(key) == 0) {
            throw new CmdSyntaxError("Unknown key: " + key);
        }
        final String commands = String.join(" ", (CharSequence[])Arrays.copyOfRange(args, 2, args.length));
        BindsCmd.wurst.getKeybinds().add(key, commands);
        ChatUtils.message("Keybind set: " + key + " -> " + commands);
    }
    
    private void remove(final String[] args) throws CmdException {
        if (args.length != 2) {
            throw new CmdSyntaxError();
        }
        final String key = args[1].toUpperCase();
        if (Keyboard.getKeyIndex(key) == 0) {
            throw new CmdSyntaxError("Unknown key: " + key);
        }
        final String oldCommands = BindsCmd.wurst.getKeybinds().getCommands(key);
        if (oldCommands == null) {
            throw new CmdError("Nothing to remove.");
        }
        BindsCmd.wurst.getKeybinds().remove(key);
        ChatUtils.message("Keybind removed: " + key + " -> " + oldCommands);
    }
    
    private void list(final String[] args) throws CmdException {
        if (args.length > 2) {
            throw new CmdSyntaxError();
        }
        int page;
        if (args.length < 2) {
            page = 1;
        }
        else {
            if (!MathUtils.isInteger(args[1])) {
                throw new CmdSyntaxError("Not a number: " + args[1]);
            }
            page = Integer.parseInt(args[1]);
        }
        final int keybinds = BindsCmd.wurst.getKeybinds().size();
        final int pages = Math.max((int)Math.ceil(keybinds / 8.0), 1);
        if (page > pages || page < 1) {
            throw new CmdSyntaxError("Invalid page: " + page);
        }
        ChatUtils.message("Total: " + keybinds + ((keybinds == 1) ? " keybind" : " keybinds"));
        ChatUtils.message("Keybind list (page " + page + "/" + pages + ")");
        for (int i = (page - 1) * 8; i < Math.min(page * 8, keybinds); ++i) {
            final KeybindList.Keybind k = BindsCmd.wurst.getKeybinds().get(i);
            ChatUtils.message(k.getKey() + " -> " + k.getCommands());
        }
    }
}
