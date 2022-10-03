package zelix.otherhacks.net.wurstclient.forge.commands;

import zelix.otherhacks.net.wurstclient.forge.*;

public final class TCmd extends Command
{
    public TCmd() {
        super("t", "Toggles a hack.", new String[] { "Syntax: .t <hack> [on|off]" });
    }
    
    @Override
    public void call(final String[] args) throws CmdException {
        if (args.length < 1 || args.length > 2) {
            throw new CmdSyntaxError();
        }
        final Hack hack = TCmd.wurst.getHax().get(args[0]);
        if (hack == null) {
            throw new CmdError("Unknown hack: " + args[0]);
        }
        if (args.length == 1) {
            hack.setEnabled(!hack.isEnabled());
        }
        else {
            final String lowerCase = args[1].toLowerCase();
            switch (lowerCase) {
                case "on": {
                    hack.setEnabled(true);
                    break;
                }
                case "off": {
                    hack.setEnabled(false);
                    break;
                }
                default: {
                    throw new CmdSyntaxError();
                }
            }
        }
    }
}
