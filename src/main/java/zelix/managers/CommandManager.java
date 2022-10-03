package zelix.managers;

import zelix.command.irc.*;
import zelix.command.*;
import zelix.utils.hooks.visual.*;
import java.util.*;

public class CommandManager
{
    public static ArrayList<Command> commands;
    private static volatile CommandManager instance;
    public static char cmdPrefix;
    
    public CommandManager() {
        this.addCommands();
    }
    
    public void addCommands() {
        CommandManager.commands.add(new Help());
        CommandManager.commands.add(new Hacks());
        CommandManager.commands.add(new Key());
        CommandManager.commands.add(new VClip());
        CommandManager.commands.add(new Login());
        CommandManager.commands.add(new Say());
        CommandManager.commands.add(new Effect());
        CommandManager.commands.add(new DumpPlayers());
        CommandManager.commands.add(new DumpClasses());
        CommandManager.commands.add(new SkinSteal());
        CommandManager.commands.add(new Friend());
        CommandManager.commands.add(new Enemy());
        CommandManager.commands.add(new Toggle());
        CommandManager.commands.add(new PFilter());
        CommandManager.commands.add(new OpenDir());
        CommandManager.commands.add(new SetName());
        CommandManager.commands.add(new Esu());
        CommandManager.commands.add(new SetCheckbox());
        CommandManager.commands.add(new SetSlider());
        CommandManager.commands.add(new SetMode());
        CommandManager.commands.add(new IRC());
        CommandManager.commands.add(new Config());
        CommandManager.commands.add(new TP());
        CommandManager.commands.add(new LoadHack());
        CommandManager.commands.add(new Cape());
    }
    
    public void runCommands(final String s) {
        final String readString = s.trim().substring(Character.toString(CommandManager.cmdPrefix).length()).trim();
        boolean commandResolved = false;
        final boolean hasArgs = readString.trim().contains(" ");
        final String commandName = hasArgs ? readString.split(" ")[0] : readString.trim();
        final String[] args = hasArgs ? readString.substring(commandName.length()).trim().split(" ") : new String[0];
        for (final Command command : CommandManager.commands) {
            if (command.getCommand().trim().equalsIgnoreCase(commandName.trim())) {
                command.runCommand(readString, args);
                commandResolved = true;
                break;
            }
        }
        if (!commandResolved) {
            ChatUtils.error("Cannot resolve internal command: ¡ìc" + commandName);
        }
    }
    
    public static CommandManager getInstance() {
        if (CommandManager.instance == null) {
            CommandManager.instance = new CommandManager();
        }
        return CommandManager.instance;
    }
    
    static {
        CommandManager.commands = new ArrayList<Command>();
        CommandManager.cmdPrefix = '.';
    }
}
