package zelix.command;

import zelix.utils.*;
import java.math.*;
import zelix.utils.hooks.visual.*;

public class VClip extends Command
{
    public VClip() {
        super("vclip");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        try {
            Wrapper.INSTANCE.player().setPosition(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + new BigInteger(args[0]).longValue(), Wrapper.INSTANCE.player().posZ);
            ChatUtils.message("Height teleported to " + new BigInteger(args[0]).longValue());
        }
        catch (Exception e) {
            ChatUtils.error("Usage: " + this.getSyntax());
        }
    }
    
    @Override
    public String getDescription() {
        return "Teleports you up/down.";
    }
    
    @Override
    public String getSyntax() {
        return "vclip <height>";
    }
}
