package zelix.utils;

import net.minecraft.client.*;
import net.minecraft.init.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;

public class MoveUtils
{
    private static Minecraft mc;
    
    public static double defaultSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().player.isPotionActive(MobEffects.SPEED)) {
            final int amplifier = Minecraft.getMinecraft().player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public static void strafe(final double speed) {
        final float a = Wrapper.INSTANCE.mc().player.rotationYaw * 0.017453292f;
        final float l = Wrapper.INSTANCE.mc().player.rotationYaw * 0.017453292f - 4.712389f;
        final float r = Wrapper.INSTANCE.mc().player.rotationYaw * 0.017453292f + 4.712389f;
        final float rf = Wrapper.INSTANCE.mc().player.rotationYaw * 0.017453292f + 0.5969026f;
        final float lf = Wrapper.INSTANCE.mc().player.rotationYaw * 0.017453292f - 0.5969026f;
        final float lb = Wrapper.INSTANCE.mc().player.rotationYaw * 0.017453292f - 2.3876104f;
        final float rb = Wrapper.INSTANCE.mc().player.rotationYaw * 0.017453292f + 2.3876104f;
        if (Wrapper.INSTANCE.mc().gameSettings.keyBindForward.isPressed()) {
            if (Wrapper.INSTANCE.mc().gameSettings.keyBindLeft.isPressed() && !Wrapper.INSTANCE.mc().gameSettings.keyBindRight.isPressed()) {
                final EntityPlayerSP player = Wrapper.INSTANCE.mc().player;
                player.motionX -= MathHelper.sin(lf) * speed;
                final EntityPlayerSP player2 = Wrapper.INSTANCE.mc().player;
                player2.motionZ += MathHelper.cos(lf) * speed;
            }
            else if (Wrapper.INSTANCE.mc().gameSettings.keyBindRight.isPressed() && !Wrapper.INSTANCE.mc().gameSettings.keyBindLeft.isPressed()) {
                final EntityPlayerSP player3 = Wrapper.INSTANCE.mc().player;
                player3.motionX -= MathHelper.sin(rf) * speed;
                final EntityPlayerSP player4 = Wrapper.INSTANCE.mc().player;
                player4.motionZ += MathHelper.cos(rf) * speed;
            }
            else {
                final EntityPlayerSP player5 = Wrapper.INSTANCE.mc().player;
                player5.motionX -= MathHelper.sin(a) * speed;
                final EntityPlayerSP player6 = Wrapper.INSTANCE.mc().player;
                player6.motionZ += MathHelper.cos(a) * speed;
            }
        }
        else if (Wrapper.INSTANCE.mc().gameSettings.keyBindBack.isPressed()) {
            if (Wrapper.INSTANCE.mc().gameSettings.keyBindLeft.isPressed() && !Wrapper.INSTANCE.mc().gameSettings.keyBindRight.isPressed()) {
                final EntityPlayerSP player7 = Wrapper.INSTANCE.mc().player;
                player7.motionX -= MathHelper.sin(lb) * speed;
                final EntityPlayerSP player8 = Wrapper.INSTANCE.mc().player;
                player8.motionZ += MathHelper.cos(lb) * speed;
            }
            else if (Wrapper.INSTANCE.mc().gameSettings.keyBindRight.isPressed() && !Wrapper.INSTANCE.mc().gameSettings.keyBindLeft.isPressed()) {
                final EntityPlayerSP player9 = Wrapper.INSTANCE.mc().player;
                player9.motionX -= MathHelper.sin(rb) * speed;
                final EntityPlayerSP player10 = Wrapper.INSTANCE.mc().player;
                player10.motionZ += MathHelper.cos(rb) * speed;
            }
            else {
                final EntityPlayerSP player11 = Wrapper.INSTANCE.mc().player;
                player11.motionX += MathHelper.sin(a) * speed;
                final EntityPlayerSP player12 = Wrapper.INSTANCE.mc().player;
                player12.motionZ -= MathHelper.cos(a) * speed;
            }
        }
        else if (Wrapper.INSTANCE.mc().gameSettings.keyBindLeft.isPressed() && !Wrapper.INSTANCE.mc().gameSettings.keyBindRight.isPressed() && !Wrapper.INSTANCE.mc().gameSettings.keyBindForward.isPressed() && !Wrapper.INSTANCE.mc().gameSettings.keyBindBack.isPressed()) {
            final EntityPlayerSP player13 = Wrapper.INSTANCE.mc().player;
            player13.motionX += MathHelper.sin(l) * speed;
            final EntityPlayerSP player14 = Wrapper.INSTANCE.mc().player;
            player14.motionZ -= MathHelper.cos(l) * speed;
        }
        else if (Wrapper.INSTANCE.mc().gameSettings.keyBindRight.isPressed() && !Wrapper.INSTANCE.mc().gameSettings.keyBindLeft.isPressed() && !Wrapper.INSTANCE.mc().gameSettings.keyBindForward.isPressed() && !Wrapper.INSTANCE.mc().gameSettings.keyBindBack.isPressed()) {
            final EntityPlayerSP player15 = Wrapper.INSTANCE.mc().player;
            player15.motionX += MathHelper.sin(r) * speed;
            final EntityPlayerSP player16 = Wrapper.INSTANCE.mc().player;
            player16.motionZ -= MathHelper.cos(r) * speed;
        }
    }
    
    public static void setMotion(final double speed) {
        double forward = Wrapper.INSTANCE.mc().player.movementInput.moveForward;
        double strafe = Wrapper.INSTANCE.mc().player.movementInput.moveStrafe;
        float yaw = Wrapper.INSTANCE.mc().player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            Wrapper.INSTANCE.mc().player.motionX = 0.0;
            Wrapper.INSTANCE.mc().player.motionZ = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            Wrapper.INSTANCE.mc().player.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            Wrapper.INSTANCE.mc().player.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }
    
    public static boolean checkTeleport(final double x, final double y, final double z, final double distBetweenPackets) {
        final double distx = Wrapper.INSTANCE.mc().player.posX - x;
        final double disty = Wrapper.INSTANCE.mc().player.posY - y;
        final double distz = Wrapper.INSTANCE.mc().player.posZ - z;
        final double dist = Math.sqrt(Wrapper.INSTANCE.mc().player.getDistanceSq(x, y, z));
        final double nbPackets = Math.round(dist / distBetweenPackets + 0.49999999999) - 1L;
        double xtp = Wrapper.INSTANCE.mc().player.posX;
        double ytp = Wrapper.INSTANCE.mc().player.posY;
        double ztp = Wrapper.INSTANCE.mc().player.posZ;
        for (int i = 1; i < nbPackets; ++i) {
            final double xdi = (x - Wrapper.INSTANCE.mc().player.posX) / nbPackets;
            xtp += xdi;
            final double zdi = (z - Wrapper.INSTANCE.mc().player.posZ) / nbPackets;
            ztp += zdi;
            final double ydi = (y - Wrapper.INSTANCE.mc().player.posY) / nbPackets;
            ytp += ydi;
            final AxisAlignedBB bb = new AxisAlignedBB(xtp - 0.3, ytp, ztp - 0.3, xtp + 0.3, ytp + 1.8, ztp + 0.3);
            if (!Wrapper.INSTANCE.mc().world.getCollisionBoxes((Entity)Wrapper.INSTANCE.mc().player, bb).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isOnGround(final double height) {
        return !Wrapper.INSTANCE.mc().world.getCollisionBoxes((Entity)Wrapper.INSTANCE.mc().player, Wrapper.INSTANCE.mc().player.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public static int getJumpEffect() {
        if (Wrapper.INSTANCE.mc().player.isPotionActive(MobEffects.JUMP_BOOST)) {
            return Wrapper.INSTANCE.mc().player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1;
        }
        return 0;
    }
    
    public static int getSpeedEffect() {
        if (Wrapper.INSTANCE.mc().player.isPotionActive(MobEffects.SPEED)) {
            return Wrapper.INSTANCE.mc().player.getActivePotionEffect(MobEffects.SPEED).getAmplifier() + 1;
        }
        return 0;
    }
    
    public static Block getBlockUnderPlayer(final EntityPlayer inPlayer, final double height) {
        return Minecraft.getMinecraft().world.getBlockState(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ)).getBlock();
    }
    
    public static Block getBlockAtPosC(final double x, final double y, final double z) {
        final EntityPlayer inPlayer = (EntityPlayer)Minecraft.getMinecraft().player;
        return Minecraft.getMinecraft().world.getBlockState(new BlockPos(inPlayer.posX + x, inPlayer.posY + y, inPlayer.posZ + z)).getBlock();
    }
    
    public static float[] getRotationsBlock(final BlockPos block, final EnumFacing face) {
        final double x = block.getX() + 0.5 - Wrapper.INSTANCE.mc().player.posX + face.getFrontOffsetX() / 2.0;
        final double z = block.getZ() + 0.5 - Wrapper.INSTANCE.mc().player.posZ + face.getFrontOffsetZ() / 2.0;
        final double y = block.getY() + 0.5;
        final double d1 = Wrapper.INSTANCE.mc().player.posY + Wrapper.INSTANCE.mc().player.getEyeHeight() - y;
        final double d2 = MathHelper.sqrt(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(Math.atan2(d1, d2) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[] { yaw, pitch };
    }
    
    public static boolean isBlockAboveHead() {
        final AxisAlignedBB bb = new AxisAlignedBB(Wrapper.INSTANCE.mc().player.posX - 0.3, Wrapper.INSTANCE.mc().player.posY + Wrapper.INSTANCE.mc().player.getEyeHeight(), Wrapper.INSTANCE.mc().player.posZ + 0.3, Wrapper.INSTANCE.mc().player.posX + 0.3, Wrapper.INSTANCE.mc().player.posY + 2.5, Wrapper.INSTANCE.mc().player.posZ - 0.3);
        return !Wrapper.INSTANCE.mc().world.getCollisionBoxes((Entity)Wrapper.INSTANCE.mc().player, bb).isEmpty();
    }
    
    public static boolean isCollidedH(final double dist) {
        final AxisAlignedBB bb = new AxisAlignedBB(Wrapper.INSTANCE.mc().player.posX - 0.3, Wrapper.INSTANCE.mc().player.posY + 2.0, Wrapper.INSTANCE.mc().player.posZ + 0.3, Wrapper.INSTANCE.mc().player.posX + 0.3, Wrapper.INSTANCE.mc().player.posY + 3.0, Wrapper.INSTANCE.mc().player.posZ - 0.3);
        return !Wrapper.INSTANCE.mc().world.getCollisionBoxes((Entity)Wrapper.INSTANCE.mc().player, bb.offset(0.3 + dist, 0.0, 0.0)).isEmpty() || !Wrapper.INSTANCE.mc().world.getCollisionBoxes((Entity)Wrapper.INSTANCE.mc().player, bb.offset(-0.3 - dist, 0.0, 0.0)).isEmpty() || !Wrapper.INSTANCE.mc().world.getCollisionBoxes((Entity)Wrapper.INSTANCE.mc().player, bb.offset(0.0, 0.0, 0.3 + dist)).isEmpty() || !Wrapper.INSTANCE.mc().world.getCollisionBoxes((Entity)Wrapper.INSTANCE.mc().player, bb.offset(0.0, 0.0, -0.3 - dist)).isEmpty();
    }
    
    public static boolean isRealCollidedH(final double dist) {
        final AxisAlignedBB bb = new AxisAlignedBB(Wrapper.INSTANCE.mc().player.posX - 0.3, Wrapper.INSTANCE.mc().player.posY + 0.5, Wrapper.INSTANCE.mc().player.posZ + 0.3, Wrapper.INSTANCE.mc().player.posX + 0.3, Wrapper.INSTANCE.mc().player.posY + 1.9, Wrapper.INSTANCE.mc().player.posZ - 0.3);
        return !Wrapper.INSTANCE.mc().world.getCollisionBoxes((Entity)Wrapper.INSTANCE.mc().player, bb.offset(0.3 + dist, 0.0, 0.0)).isEmpty() || !Wrapper.INSTANCE.mc().world.getCollisionBoxes((Entity)Wrapper.INSTANCE.mc().player, bb.offset(-0.3 - dist, 0.0, 0.0)).isEmpty() || !Wrapper.INSTANCE.mc().world.getCollisionBoxes((Entity)Wrapper.INSTANCE.mc().player, bb.offset(0.0, 0.0, 0.3 + dist)).isEmpty() || !Wrapper.INSTANCE.mc().world.getCollisionBoxes((Entity)Wrapper.INSTANCE.mc().player, bb.offset(0.0, 0.0, -0.3 - dist)).isEmpty();
    }
    
    public static boolean isMoving() {
        return Wrapper.INSTANCE.mc().player != null && (Wrapper.INSTANCE.mc().player.movementInput.moveForward != 0.0f || Wrapper.INSTANCE.mc().player.movementInput.moveStrafe != 0.0f);
    }
    
    public static void strafeHYT(final float speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getDirection();
        Wrapper.INSTANCE.mc().player.motionX = -Math.sin(yaw) * speed;
        Wrapper.INSTANCE.mc().player.motionZ = Math.cos(yaw) * speed;
    }
    
    public static double getDirection() {
        float rotationYaw = Wrapper.INSTANCE.mc().player.rotationYaw;
        if (Wrapper.INSTANCE.mc().player.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (Wrapper.INSTANCE.mc().player.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (Wrapper.INSTANCE.mc().player.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Wrapper.INSTANCE.mc().player.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (Wrapper.INSTANCE.mc().player.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
    
    public static void strafe() {
        strafe(getSpeed());
    }
    
    public static float getSpeed() {
        return (float)Math.sqrt(Wrapper.INSTANCE.mc().player.motionX * Wrapper.INSTANCE.mc().player.motionX + Wrapper.INSTANCE.mc().player.motionZ * Wrapper.INSTANCE.mc().player.motionZ);
    }
    
    static {
        MoveUtils.mc = Minecraft.getMinecraft();
    }
}
