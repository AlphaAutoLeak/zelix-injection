package zelix.otherhacks.net.wurstclient.forge.utils;

import zelix.otherhacks.net.wurstclient.forge.*;
import net.minecraft.client.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.multiplayer.*;
import java.lang.reflect.*;

public final class PlayerControllerUtils
{
    private static final ForgeWurst wurst;
    private static final Minecraft mc;
    
    public static ItemStack windowClick_PICKUP(final int slot) {
        return PlayerControllerUtils.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)WMinecraft.getPlayer());
    }
    
    public static ItemStack windowClick_QUICK_MOVE(final int slot) {
        return PlayerControllerUtils.mc.playerController.windowClick(0, slot, 0, ClickType.QUICK_MOVE, (EntityPlayer)WMinecraft.getPlayer());
    }
    
    public static ItemStack windowClick_THROW(final int slot) {
        return PlayerControllerUtils.mc.playerController.windowClick(0, slot, 1, ClickType.THROW, (EntityPlayer)WMinecraft.getPlayer());
    }
    
    public static float getCurBlockDamageMP() throws ReflectiveOperationException {
        final Field field = PlayerControllerMP.class.getDeclaredField(PlayerControllerUtils.wurst.isObfuscated() ? "curBlockDamageMP" : "curBlockDamageMP");
        field.setAccessible(true);
        return field.getFloat(PlayerControllerUtils.mc.playerController);
    }
    
    public static void setBlockHitDelay(final int blockHitDelay) throws ReflectiveOperationException {
        final Field field = PlayerControllerMP.class.getDeclaredField(PlayerControllerUtils.wurst.isObfuscated() ? "blockHitDelay" : "blockHitDelay");
        field.setAccessible(true);
        field.setInt(PlayerControllerUtils.mc.playerController, blockHitDelay);
    }
    
    public static void setIsHittingBlock(final boolean isHittingBlock) throws ReflectiveOperationException {
        final Field field = PlayerControllerMP.class.getDeclaredField(PlayerControllerUtils.wurst.isObfuscated() ? "isHittingBlock" : "isHittingBlock");
        field.setAccessible(true);
        field.setBoolean(PlayerControllerUtils.mc.playerController, isHittingBlock);
    }
    
    static {
        wurst = ForgeWurst.getForgeWurst();
        mc = Minecraft.getMinecraft();
    }
}
