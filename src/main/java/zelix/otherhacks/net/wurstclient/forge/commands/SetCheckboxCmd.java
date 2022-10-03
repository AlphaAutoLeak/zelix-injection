package zelix.otherhacks.net.wurstclient.forge.commands;

import zelix.otherhacks.net.wurstclient.forge.settings.*;
import zelix.otherhacks.net.wurstclient.forge.*;

public final class SetCheckboxCmd extends Command
{
    public SetCheckboxCmd() {
        super("setcheckbox", "Modifies a checkbox setting.", new String[] { "Syntax: .setcheckbox <hack> <checkbox> <value>" });
    }
    
    @Override
    public void call(final String[] args) throws CmdException {
        if (args.length != 3) {
            throw new CmdSyntaxError();
        }
        final Hack hack = SetCheckboxCmd.wurst.getHax().get(args[0]);
        if (hack == null) {
            throw new CmdError("Hack \"" + args[0] + "\" could not be found.");
        }
        final Setting setting = hack.getSettings().get(args[1].toLowerCase().replace("_", " "));
        if (setting == null) {
            throw new CmdError("Setting \"" + args[0] + " " + args[1] + "\" could not be found.");
        }
        if (!(setting instanceof CheckboxSetting)) {
            throw new CmdError(hack.getName() + " " + setting.getName() + " is not a checkbox.");
        }
        final CheckboxSetting e = (CheckboxSetting)setting;
        if (!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false")) {
            throw new CmdSyntaxError("Not a boolean: " + args[2]);
        }
        e.setChecked(Boolean.parseBoolean(args[2]));
    }
}
