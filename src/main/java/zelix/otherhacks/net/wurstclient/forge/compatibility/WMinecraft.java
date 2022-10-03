package zelix.otherhacks.net.wurstclient.forge.compatibility;

import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.gui.*;

public final class WMinecraft
{
    public static final String VERSION = "1.12.2";
    private static final Minecraft mc;
    
    public static EntityPlayerSP getPlayer() {
        return WMinecraft.mc.player;
    }
    
    public static WorldClient getWorld() {
        return WMinecraft.mc.world;
    }
    
    public static FontRenderer getFontRenderer() {
        return WMinecraft.mc.fontRenderer;
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
