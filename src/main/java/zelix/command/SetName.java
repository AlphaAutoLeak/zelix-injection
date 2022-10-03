package zelix.command;

import net.minecraft.client.*;
import zelix.utils.hooks.visual.*;
import java.lang.reflect.*;

public class SetName extends Command
{
    public SetName() {
        super("setname");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        try {
            String message = "";
            for (int i = 0; i < args.length; ++i) {
                final String str = message = args[i];
            }
            final Class var51 = Minecraft.getMinecraft().getSession().getClass();
            final Field f = var51.getDeclaredFields()[0];
            f.setAccessible(true);
            f.set(Minecraft.getMinecraft().getSession(), message);
            ChatUtils.message("Successfully set the name");
        }
        catch (Exception var52) {
            var52.printStackTrace();
        }
    }
    
    @Override
    public String getDescription() {
        return "Reset your name";
    }
    
    @Override
    public String getSyntax() {
        return "setname <name>";
    }
}
