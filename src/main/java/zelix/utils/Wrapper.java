package zelix.utils;

import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.settings.*;
import net.minecraft.client.gui.*;
import net.minecraft.network.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.multiplayer.*;
import zelix.utils.system.*;
import net.minecraftforge.fml.relauncher.*;
import java.lang.reflect.*;

public class Wrapper
{
    public static boolean SelfFontRender;
    public static volatile Wrapper INSTANCE;
    
    public Minecraft mc() {
        return Minecraft.getMinecraft();
    }
    
    public EntityPlayerSP player() {
        return Wrapper.INSTANCE.mc().player;
    }
    
    public WorldClient world() {
        return Wrapper.INSTANCE.mc().world;
    }
    
    public GameSettings mcSettings() {
        return Wrapper.INSTANCE.mc().gameSettings;
    }
    
    public FontRenderer fontRenderer() {
        return Wrapper.INSTANCE.mc().fontRenderer;
    }
    
    public void sendPacket(final Packet packet) {
        this.player().connection.sendPacket(packet);
    }
    
    public InventoryPlayer inventory() {
        return this.player().inventory;
    }
    
    public PlayerControllerMP controller() {
        return Wrapper.INSTANCE.mc().playerController;
    }
    
    public static double getRenderPosX() {
        return (double)getField("renderPosX", "renderPosX", Minecraft.getMinecraft().getRenderManager());
    }
    
    public static double getRenderPosY() {
        return (double)getField("renderPosY", "renderPosY", Minecraft.getMinecraft().getRenderManager());
    }
    
    public static double getRenderPosZ() {
        return (double)getField("renderPosZ", "renderPosZ", Minecraft.getMinecraft().getRenderManager());
    }
    
    public static Object getField(final String field, final String obfName, final Object instance) {
        final Class<?> class1 = instance.getClass();
        String[] array = new String[0];
        if (Mapping.isNotObfuscated()) {
            array = new String[] { field };
        }
        else {
            (new String[] { null })[0] = obfName;
        }
        final Field fField = ReflectionHelper.findField(class1, array);
        fField.setAccessible(true);
        try {
            return fField.get(instance);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    static {
        Wrapper.SelfFontRender = true;
        Wrapper.INSTANCE = new Wrapper();
    }
}
