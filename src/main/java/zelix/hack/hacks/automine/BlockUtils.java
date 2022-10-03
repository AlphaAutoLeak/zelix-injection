package zelix.hack.hacks.automine;

import net.minecraft.client.*;
import net.minecraft.util.math.*;
import net.minecraft.block.*;
import zelix.utils.*;
import net.minecraft.block.state.*;

public class BlockUtils
{
    private static Minecraft mc;
    
    public static Block getBlock(final BlockPos pos) {
        return Wrapper.INSTANCE.mc().world.getBlockState(pos).getBlock();
    }
    
    public static int getBlockMeta(final BlockPos pos) {
        final IBlockState blockState = Wrapper.INSTANCE.mc().world.getBlockState(pos);
        return blockState.getBlock().getMetaFromState(blockState);
    }
    
    public static void faceBlockClient(final BlockPos blockPos) {
        final double diffX = blockPos.getX() + 0.5 - Wrapper.INSTANCE.mc().player.posX;
        final double diffY = blockPos.getY() + 0.0 - (Wrapper.INSTANCE.mc().player.posY + Wrapper.INSTANCE.mc().player.getEyeHeight());
        final double diffZ = blockPos.getZ() + 0.5 - Wrapper.INSTANCE.mc().player.posZ;
        final double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        Wrapper.INSTANCE.mc().player.rotationYaw += wrapDegrees(yaw - Wrapper.INSTANCE.mc().player.rotationYaw);
        Wrapper.INSTANCE.mc().player.rotationPitch += wrapDegrees(pitch - Wrapper.INSTANCE.mc().player.rotationPitch);
    }
    
    public static float getNeaestPlayerBlockDistance(final double posX, final double posY, final double posZ) {
        return getBlockDistance((float)(Wrapper.INSTANCE.mc().player.posX - posX), (float)(Wrapper.INSTANCE.mc().player.posY + 1.0 - posY), (float)(Wrapper.INSTANCE.mc().player.posZ - posZ));
    }
    
    public static float getBlockDistance(final float xDiff, final float yDiff, final float zDiff) {
        return (float)Math.sqrt((xDiff - 0.5f) * (xDiff - 0.5f) + (yDiff - 0.5f) * (yDiff - 0.5f) + (zDiff - 0.5f) * (zDiff - 0.5f));
    }
    
    public static float wrapDegrees(float value) {
        value %= 360.0f;
        if (value >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }
    
    static {
        BlockUtils.mc = Minecraft.getMinecraft();
    }
}
