package zelix.otherhacks.net.wurstclient.forge.commands;

import zelix.otherhacks.net.wurstclient.forge.*;
import java.util.function.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import java.util.*;

public final class HelpCmd extends Command
{
    public HelpCmd() {
        super("help", "Shows help.", new String[] { "Syntax: .help <command>", "List commands: .help [<page>]" });
    }
    
    @Override
    public void call(final String[] args) throws CmdException {
        if (args.length > 1) {
            throw new CmdSyntaxError();
        }
        String arg;
        if (args.length < 1) {
            arg = "1";
        }
        else {
            arg = args[0];
        }
        if (MathUtils.isInteger(arg)) {
            this.list(Integer.parseInt(arg));
        }
        else {
            this.help(arg);
        }
    }
    
    private void list(final int page) throws CmdException {
        final List<Command> cmds = Arrays.asList(HelpCmd.wurst.getCmds().getValues().toArray(new Command[0]));
        cmds.sort(Comparator.comparing((Function<? super Command, ? extends Comparable>)Command::getName));
        final int size = cmds.size();
        final int pages = Math.max((int)Math.ceil(size / 8.0), 1);
        if (page > pages || page < 1) {
            throw new CmdSyntaxError("Invalid page: " + page);
        }
        ChatUtils.message("Total: " + size + ((size == 1) ? " command" : " commands"));
        ChatUtils.message("Command list (page " + page + "/" + pages + ")");
        for (int i = (page - 1) * 8; i < Math.min(page * 8, size); ++i) {
            final Command c = cmds.get(i);
            ChatUtils.message("." + c.getName() + " - " + c.getDescription());
        }
    }
    
    private void help(String cmdName) throws CmdException {
        if (cmdName.startsWith(".")) {
            cmdName = cmdName.substring(1);
        }
        final Command cmd = HelpCmd.wurst.getCmds().get(cmdName);
        if (cmd == null) {
            throw new CmdSyntaxError("Unknown command: ." + cmdName);
        }
        ChatUtils.message("." + cmd.getName() + " - " + cmd.getDescription());
        for (final String line : cmd.getSyntax()) {
            ChatUtils.message(line);
        }
    }
}
