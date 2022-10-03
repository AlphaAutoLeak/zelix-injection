package zelix.otherhacks.net.wurstclient.forge.commands;

import zelix.otherhacks.net.wurstclient.forge.settings.*;
import zelix.otherhacks.net.wurstclient.forge.*;

public final class SetEnumCmd extends Command
{
    public SetEnumCmd() {
        super("setenum", "Modifies an enum setting.", new String[] { "Syntax: .setenum <hack> <enum> <value>" });
    }
    
    @Override
    public void call(final String[] args) throws CmdException {
        if (args.length != 3) {
            throw new CmdSyntaxError();
        }
        final Hack hack = SetEnumCmd.wurst.getHax().get(args[0]);
        if (hack == null) {
            throw new CmdError("Hack \"" + args[0] + "\" could not be found.");
        }
        final Setting setting = hack.getSettings().get(args[1].toLowerCase().replace("_", " "));
        if (setting == null) {
            throw new CmdError("Setting \"" + args[0] + " " + args[1] + "\" could not be found.");
        }
        if (!(setting instanceof EnumSetting)) {
            throw new CmdError(hack.getName() + " " + setting.getName() + " is not an enum.");
        }
        final EnumSetting e = (EnumSetting)setting;
        e.setSelected(args[2].replace("_", " "));
    }
}
