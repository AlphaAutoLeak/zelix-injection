package zelix.otherhacks.net.wurstclient.forge.utils;

import net.minecraft.client.*;
import net.minecraft.util.text.*;

public final class ChatUtils
{
    private static boolean enabled;
    
    public static void setEnabled(final boolean enabled) {
        ChatUtils.enabled = enabled;
    }
    
    public static void component(final ITextComponent component) {
        if (ChatUtils.enabled) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString("��c[��6Zelix��c]��r ").appendSibling(component));
        }
    }
    
    public static void message(final String message) {
        component((ITextComponent)new TextComponentString(message));
    }
    
    public static void warning(final String message) {
        message("��c[��6��lWARNING��c]��r " + message);
    }
    
    public static void error(final String message) {
        message("��c[��4��lERROR��c]��r " + message);
    }
    
    static {
        ChatUtils.enabled = true;
    }
}
