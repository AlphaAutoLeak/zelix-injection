package zelix.utils.visual;

import zelix.utils.system.*;
import net.minecraft.client.*;

public class AvaritiaColorUtil
{
    public static final EnumChatFormatting[] fabulousness;
    public static final EnumChatFormatting[] sanic;
    public static final EnumChatFormatting[] Azrael;
    public static final EnumChatFormatting[] Sense;
    
    public static String makeFabulous(final String input) {
        return ludicrousFormatting(input, AvaritiaColorUtil.fabulousness, 130.0, 1, 1);
    }
    
    public static String makeSANIC(final String input) {
        return ludicrousFormatting(input, AvaritiaColorUtil.sanic, 100.0, 2, 1);
    }
    
    public static String makeAzrael(final String input) {
        return ludicrousFormatting(input, AvaritiaColorUtil.Azrael, 200.0, 2, 1);
    }
    
    public static String ludicrousFormatting(final String input, final EnumChatFormatting[] colours, double delay, final int step, final int posstep) {
        final StringBuilder sb = new StringBuilder(input.length() * 3);
        if (delay <= 0.0) {
            delay = 0.001;
        }
        final int offset = (int)Math.floor(Minecraft.getSystemTime() / delay) % colours.length;
        for (int i = 0; i < input.length(); ++i) {
            final char c = input.charAt(i);
            final int col = (i * posstep + colours.length - offset) % colours.length;
            sb.append(colours[col].toString());
            sb.append(c);
        }
        return sb.toString();
    }
    
    static {
        fabulousness = new EnumChatFormatting[] { EnumChatFormatting.RED, EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW, EnumChatFormatting.GREEN, EnumChatFormatting.AQUA, EnumChatFormatting.BLUE, EnumChatFormatting.LIGHT_PURPLE };
        Azrael = new EnumChatFormatting[] { EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.AQUA, EnumChatFormatting.AQUA, EnumChatFormatting.AQUA, EnumChatFormatting.AQUA, EnumChatFormatting.YELLOW, EnumChatFormatting.YELLOW, EnumChatFormatting.YELLOW };
        sanic = new EnumChatFormatting[] { EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.WHITE, EnumChatFormatting.BLUE, EnumChatFormatting.WHITE, EnumChatFormatting.WHITE, EnumChatFormatting.BLUE, EnumChatFormatting.WHITE, EnumChatFormatting.WHITE, EnumChatFormatting.BLUE, EnumChatFormatting.RED, EnumChatFormatting.WHITE, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY };
        Sense = new EnumChatFormatting[] { EnumChatFormatting.DARK_GREEN, EnumChatFormatting.BOLD };
    }
}
