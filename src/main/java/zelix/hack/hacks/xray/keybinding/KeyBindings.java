package zelix.hack.hacks.xray.keybinding;

import net.minecraft.client.settings.*;
import net.minecraftforge.fml.client.registry.*;

public class KeyBindings
{
    static final int keyIndex_toggleXray = 0;
    static final int keyIndex_showXrayMenu = 1;
    static final int keyIndex_scan = 2;
    static final int keyIndex_removeblock = 3;
    static final int keyIndex_freeze = 4;
    private static final int[] keyBind_keyValues;
    private static final String[] keyBind_descriptions;
    static KeyBinding[] keyBind_keys;
    
    public static void setup() {
        KeyBindings.keyBind_keys = new KeyBinding[KeyBindings.keyBind_descriptions.length];
        for (int i = 0; i < KeyBindings.keyBind_descriptions.length; ++i) {
            ClientRegistry.registerKeyBinding(KeyBindings.keyBind_keys[i] = new KeyBinding(KeyBindings.keyBind_descriptions[i], KeyBindings.keyBind_keyValues[i], "X-Ray"));
        }
    }
    
    static {
        keyBind_keyValues = new int[] { 157, 44, 34, 47, 49 };
        keyBind_descriptions = new String[] { "Start Render", "Open Controller", "Scan Ore", "Remove Blocks", "Freeze" };
        KeyBindings.keyBind_keys = null;
    }
}
