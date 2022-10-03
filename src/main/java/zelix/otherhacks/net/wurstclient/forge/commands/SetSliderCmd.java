package zelix.otherhacks.net.wurstclient.forge.commands;

import zelix.otherhacks.net.wurstclient.forge.settings.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import zelix.otherhacks.net.wurstclient.forge.*;

public final class SetSliderCmd extends Command
{
    public SetSliderCmd() {
        super("setslider", "Modifies a slider setting.", new String[] { "Syntax: .setslider <hack> <slider> <value>" });
    }
    
    @Override
    public void call(final String[] args) throws CmdException {
        if (args.length != 3) {
            throw new CmdSyntaxError();
        }
        final Hack hack = SetSliderCmd.wurst.getHax().get(args[0]);
        if (hack == null) {
            throw new CmdError("Hack \"" + args[0] + "\" could not be found.");
        }
        final Setting setting = hack.getSettings().get(args[1].toLowerCase().replace("_", " "));
        if (setting == null) {
            throw new CmdError("Setting \"" + args[0] + " " + args[1] + "\" could not be found.");
        }
        if (!(setting instanceof SliderSetting)) {
            throw new CmdError(hack.getName() + " " + setting.getName() + " is not a slider.");
        }
        final SliderSetting slider = (SliderSetting)setting;
        if (MathUtils.isDouble(args[2])) {
            slider.setValue(Double.parseDouble(args[2]));
        }
        else {
            if (!args[2].startsWith("~") || !MathUtils.isDouble(args[2].substring(1))) {
                throw new CmdSyntaxError("Not a number: " + args[2]);
            }
            slider.setValue(slider.getValue() + Double.parseDouble(args[2].substring(1)));
        }
    }
}
