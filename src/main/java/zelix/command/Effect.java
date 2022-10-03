package zelix.command;

import net.minecraft.potion.*;
import zelix.utils.hooks.visual.*;
import zelix.utils.*;

public class Effect extends Command
{
    public Effect() {
        super("effect");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        try {
            if (args[0].equalsIgnoreCase("add")) {
                final int id = Integer.parseInt(args[1]);
                final int duration = Integer.parseInt(args[2]);
                final int amplifier = Integer.parseInt(args[3]);
                if (Potion.getPotionById(id) == null) {
                    ChatUtils.error("Potion is null");
                    return;
                }
                Utils.addEffect(id, duration, amplifier);
            }
            else if (args[0].equalsIgnoreCase("remove")) {
                final int id = Integer.parseInt(args[1]);
                if (Potion.getPotionById(id) == null) {
                    ChatUtils.error("Potion is null");
                    return;
                }
                Utils.removeEffect(id);
            }
            else if (args[0].equalsIgnoreCase("clear")) {
                Utils.clearEffects();
            }
        }
        catch (Exception e) {
            ChatUtils.error("Usage: " + this.getSyntax());
        }
    }
    
    @Override
    public String getDescription() {
        return "Effect manager.";
    }
    
    @Override
    public String getSyntax() {
        return "effect <add/remove/clear> <id> <duration> <amplifier>";
    }
}
