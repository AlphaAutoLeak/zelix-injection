package zelix.otherhacks.net.wurstclient.forge.utils;

import net.minecraftforge.fml.common.*;
import net.minecraft.util.math.*;
import net.minecraft.client.entity.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import zelix.otherhacks.net.wurstclient.forge.*;
import net.minecraft.client.*;
import java.lang.reflect.*;

@Mod.EventBusSubscriber
public final class RotationUtils
{
    public static Vec3d getEyesPos() {
        return new Vec3d(WMinecraft.getPlayer().posX, WMinecraft.getPlayer().posY + WMinecraft.getPlayer().getEyeHeight(), WMinecraft.getPlayer().posZ);
    }
    
    public static Vec3d getClientLookVec() {
        final EntityPlayerSP player = WMinecraft.getPlayer();
        final float f = MathHelper.cos(-player.rotationYaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-player.rotationYaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-player.rotationPitch * 0.017453292f);
        final float f4 = MathHelper.sin(-player.rotationPitch * 0.017453292f);
        return new Vec3d((double)(f2 * f3), (double)f4, (double)(f * f3));
    }
    
    private static float[] getNeededRotations(final Vec3d vec) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = WVec3d.getX(vec) - WVec3d.getX(eyesPos);
        final double diffY = WVec3d.getY(vec) - WVec3d.getY(eyesPos);
        final double diffZ = WVec3d.getZ(vec) - WVec3d.getZ(eyesPos);
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch) };
    }
    
    public static double getAngleToLookVec(final Vec3d vec) {
        final float[] needed = getNeededRotations(vec);
        final EntityPlayerSP player = WMinecraft.getPlayer();
        final float currentYaw = MathHelper.wrapDegrees(player.rotationYaw);
        final float currentPitch = MathHelper.wrapDegrees(player.rotationPitch);
        final float diffYaw = currentYaw - needed[0];
        final float diffPitch = currentPitch - needed[1];
        return Math.sqrt(diffYaw * diffYaw + diffPitch * diffPitch);
    }
    
    public static double getAngleToLastReportedLookVec(final Vec3d vec) {
        final float[] needed = getNeededRotations(vec);
        final EntityPlayerSP player = WMinecraft.getPlayer();
        float lastReportedYaw;
        float lastReportedPitch;
        try {
            final Field yawField = EntityPlayerSP.class.getDeclaredField(ForgeWurst.getForgeWurst().isObfuscated() ? "lastReportedYaw" : "lastReportedYaw");
            yawField.setAccessible(true);
            lastReportedYaw = MathHelper.wrapDegrees(yawField.getFloat(player));
            final Field pitchField = EntityPlayerSP.class.getDeclaredField(ForgeWurst.getForgeWurst().isObfuscated() ? "lastReportedPitch" : "lastReportedPitch");
            pitchField.setAccessible(true);
            lastReportedPitch = MathHelper.wrapDegrees(pitchField.getFloat(player));
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        final float diffYaw = lastReportedYaw - needed[0];
        final float diffPitch = lastReportedPitch - needed[1];
        return Math.sqrt(diffYaw * diffYaw + diffPitch * diffPitch);
    }
    
    public static void faceVectorPacket(final Vec3d vec) {
        final float[] rotations = getNeededRotations(vec);
        final EntityPlayerSP pl = Minecraft.getMinecraft().player;
        final float preYaw = pl.rotationYaw;
        final float prePitch = pl.rotationPitch;
        pl.rotationYaw = rotations[0];
        pl.rotationPitch = rotations[1];
        try {
            final Method onUpdateWalkingPlayer = pl.getClass().getDeclaredMethod(ForgeWurst.getForgeWurst().isObfuscated() ? "onUpdateWalkingPlayer" : "onUpdateWalkingPlayer", (Class<?>[])new Class[0]);
            onUpdateWalkingPlayer.setAccessible(true);
            onUpdateWalkingPlayer.invoke(pl, new Object[0]);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        pl.rotationYaw = preYaw;
        pl.rotationPitch = prePitch;
    }
    
    public static void faceVectorForWalking(final Vec3d vec) {
        final float[] needed = getNeededRotations(vec);
        final EntityPlayerSP player = WMinecraft.getPlayer();
        player.rotationYaw = needed[0];
        player.rotationPitch = 0.0f;
    }
}
