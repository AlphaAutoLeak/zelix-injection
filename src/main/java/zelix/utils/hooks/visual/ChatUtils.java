package zelix.utils.hooks.visual;

import net.minecraft.client.*;
import zelix.utils.system.*;
import net.minecraft.util.text.*;

public class ChatUtils
{
    public static void component(final ITextComponent component) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(EnumChatFormatting.RED + "[" + EnumChatFormatting.GOLD + "Zelix" + EnumChatFormatting.RED + "]" + EnumChatFormatting.WHITE + " ").appendSibling(component));
    }
    
    public static void componentIRC(final ITextComponent component) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString("").appendSibling(component));
    }
    
    public static void messageIRC(final String message) {
        componentIRC((ITextComponent)new TextComponentString(message));
    }
    
    public static void IRC(final String message) {
        messageIRC(EnumChatFormatting.RED + "[" + EnumChatFormatting.GOLD + "IRC" + EnumChatFormatting.RED + "]" + EnumChatFormatting.WHITE + " " + message);
    }
    
    public static void message(final String message) {
        component((ITextComponent)new TextComponentString(message));
    }
    
    public static void warning(final String message) {
        message(EnumChatFormatting.RED + "[" + EnumChatFormatting.GOLD + EnumChatFormatting.BOLD + "WARNING" + EnumChatFormatting.RED + "]" + EnumChatFormatting.WHITE + " " + message);
    }
    
    public static void error(final String message) {
        message(EnumChatFormatting.RED + "[" + EnumChatFormatting.DARK_RED + "" + EnumChatFormatting.BOLD + "ERROR" + EnumChatFormatting.RED + "]" + EnumChatFormatting.WHITE + " " + message);
    }
    
    public static void success(final String message) {
        message(EnumChatFormatting.GREEN + "[" + EnumChatFormatting.DARK_GREEN + EnumChatFormatting.BOLD + "SUCCESS" + EnumChatFormatting.GREEN + "]" + EnumChatFormatting.WHITE + " " + message);
    }
    
    public static void failure(final String message) {
        message("" + EnumChatFormatting.RED + "[" + EnumChatFormatting.DARK_RED + "" + EnumChatFormatting.BOLD + "FAILURE" + EnumChatFormatting.RED + "]" + EnumChatFormatting.WHITE + " " + message);
    }
}
