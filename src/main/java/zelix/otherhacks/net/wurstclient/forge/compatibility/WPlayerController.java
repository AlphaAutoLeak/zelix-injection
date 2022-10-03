package zelix.otherhacks.net.wurstclient.forge.compatibility;

import net.minecraft.client.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;

public final class WPlayerController
{
    private static final Minecraft mc;
    
    public static void processRightClickBlock(final BlockPos pos, final EnumFacing side, final Vec3d hitVec) {
        WPlayerController.mc.playerController.processRightClickBlock(WMinecraft.getPlayer(), WMinecraft.getWorld(), pos, side, hitVec, EnumHand.MAIN_HAND);
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
