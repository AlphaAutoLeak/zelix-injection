package zelix.utils;

import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import java.util.*;
import org.lwjgl.util.vector.*;

public class PlayerUtil
{
    private static Minecraft mc;
    
    public static float getDirection() {
        float yaw = PlayerUtil.mc.player.rotationYaw;
        if (PlayerUtil.mc.player.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (PlayerUtil.mc.player.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (PlayerUtil.mc.player.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (PlayerUtil.mc.player.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (PlayerUtil.mc.player.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= 0.017453292f;
    }
    
    public static void forward(final double length) {
        final double yaw = Math.toRadians(PlayerUtil.mc.player.rotationYaw);
        PlayerUtil.mc.player.setPosition(PlayerUtil.mc.player.posX + -Math.sin(yaw) * length, PlayerUtil.mc.player.posY, PlayerUtil.mc.player.posZ + Math.cos(yaw) * length);
    }
    
    public static void toFwd(final double speed) {
        final float yaw = PlayerUtil.mc.player.rotationYaw * 0.017453292f;
        final EntityPlayerSP player = PlayerUtil.mc.player;
        player.motionX -= MathHelper.sin(yaw) * speed;
        final EntityPlayerSP player2 = PlayerUtil.mc.player;
        player2.motionZ += MathHelper.cos(yaw) * speed;
    }
    
    public static void setSpeed(final double speed) {
        PlayerUtil.mc.player.motionX = -Math.sin(getDirection()) * speed;
        PlayerUtil.mc.player.motionZ = Math.cos(getDirection()) * speed;
    }
    
    public static double getSpeed() {
        return Math.sqrt(Minecraft.getMinecraft().player.motionX * Minecraft.getMinecraft().player.motionX + Minecraft.getMinecraft().player.motionZ * Minecraft.getMinecraft().player.motionZ);
    }
    
    public static Block getBlockUnderPlayer(final EntityPlayer inPlayer) {
        return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - 1.0, inPlayer.posZ));
    }
    
    public static Block getBlock(final BlockPos pos) {
        return Minecraft.getMinecraft().world.getBlockState(pos).getBlock();
    }
    
    public static Block getBlockAtPosC(final EntityPlayer inPlayer, final double x, final double y, final double z) {
        return getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
    }
    
    public static ArrayList<Vector3f> vanillaTeleportPositions(final double tpX, final double tpY, final double tpZ, final double speed) {
        final ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
        final Minecraft mc = Minecraft.getMinecraft();
        final double posX = tpX - mc.player.posX;
        final double posY = tpY - (mc.player.posY + mc.player.getEyeHeight() + 1.1);
        final double posZ = tpZ - mc.player.posZ;
        final float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / 3.141592653589793 - 90.0);
        final float pitch = (float)(-Math.atan2(posY, Math.sqrt(posX * posX + posZ * posZ)) * 180.0 / 3.141592653589793);
        double tmpX = mc.player.posX;
        double tmpY = mc.player.posY;
        double tmpZ = mc.player.posZ;
        double steps = 1.0;
        for (double d = speed; d < getDistance(mc.player.posX, mc.player.posY, mc.player.posZ, tpX, tpY, tpZ); d += speed) {
            ++steps;
        }
        for (double d = speed; d < getDistance(mc.player.posX, mc.player.posY, mc.player.posZ, tpX, tpY, tpZ); d += speed) {
            tmpX = mc.player.posX - Math.sin(getDirection(yaw)) * d;
            tmpZ = mc.player.posZ + Math.cos(getDirection(yaw)) * d;
            positions.add(new Vector3f((float)tmpX, (float)(tmpY -= (mc.player.posY - tpY) / steps), (float)tmpZ));
        }
        positions.add(new Vector3f((float)tpX, (float)tpY, (float)tpZ));
        return positions;
    }
    
    public static float getDirection(float yaw) {
        if (Minecraft.getMinecraft().player.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (Minecraft.getMinecraft().player.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (Minecraft.getMinecraft().player.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Minecraft.getMinecraft().player.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (Minecraft.getMinecraft().player.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= 0.017453292f;
    }
    
    public static double getDistance(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        final double d0 = x1 - x2;
        final double d2 = y1 - y2;
        final double d3 = z1 - z2;
        return MathHelper.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
    }
    
    public static boolean MovementInput() {
        return PlayerUtil.mc.gameSettings.keyBindForward.isPressed() || PlayerUtil.mc.gameSettings.keyBindLeft.isPressed() || PlayerUtil.mc.gameSettings.keyBindRight.isPressed() || PlayerUtil.mc.gameSettings.keyBindBack.isPressed();
    }
    
    static {
        PlayerUtil.mc = Minecraft.getMinecraft();
    }
}
