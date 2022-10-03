package zelix.otherhacks.net.wurstclient.forge.utils;

import zelix.otherhacks.net.wurstclient.forge.*;
import java.lang.reflect.*;
import net.minecraft.client.settings.*;

public final class KeyBindingUtils
{
    private static ForgeWurst wurst;
    
    public static void setPressed(final KeyBinding binding, final boolean pressed) {
        try {
            final Field field = binding.getClass().getDeclaredField(KeyBindingUtils.wurst.isObfuscated() ? "pressed" : "pressed");
            field.setAccessible(true);
            field.setBoolean(binding, pressed);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void resetPressed(final KeyBinding binding) {
        setPressed(binding, GameSettings.isKeyDown(binding));
    }
    
    static {
        KeyBindingUtils.wurst = ForgeWurst.getForgeWurst();
    }
}
