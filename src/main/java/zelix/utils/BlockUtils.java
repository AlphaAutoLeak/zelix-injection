package zelix.utils;

import net.minecraft.client.*;
import net.minecraft.block.state.*;
import net.minecraft.block.material.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.network.play.client.*;
import net.minecraft.client.network.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.block.*;

public final class BlockUtils
{
    public static Minecraft mc;
    
    public static IBlockState getState(final BlockPos pos) {
        return Wrapper.INSTANCE.world().getBlockState(pos);
    }
    
    public static Block getBlock(final BlockPos pos) {
        return getState(pos).getBlock();
    }
    
    public static Material getMaterial(final BlockPos pos) {
        return getState(pos).getMaterial();
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }
    
    public static float getHardness(final BlockPos pos) {
        return getState(pos).getPlayerRelativeBlockHardness((EntityPlayer)Wrapper.INSTANCE.player(), (World)Wrapper.INSTANCE.world(), pos);
    }
    
    public static boolean isBlockMaterial(final BlockPos blockPos, final Block block) {
        return getBlock(blockPos) == Blocks.AIR;
    }
    
    public static boolean isBlockMaterial(final BlockPos blockPos, final Material material) {
        return getState(blockPos).getMaterial() == material;
    }
    
    public static boolean placeBlockLegit(final BlockPos pos) {
        final Vec3d eyesPos = new Vec3d(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight(), Wrapper.INSTANCE.player().posZ);
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3d((Vec3i)pos).addVector(0.5, 0.5, 0.5)) < eyesPos.squareDistanceTo(new Vec3d((Vec3i)neighbor).addVector(0.5, 0.5, 0.5))) {
                if (getBlock(neighbor).canCollideCheck(Wrapper.INSTANCE.world().getBlockState(neighbor), false)) {
                    final Vec3d hitVec = new Vec3d((Vec3i)neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
                    if (eyesPos.squareDistanceTo(hitVec) <= 18.0625) {
                        faceVectorPacket(hitVec);
                    }
                }
            }
        }
        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        Utils.swingMainHand();
        return true;
    }
    
    public static boolean placeBlockSimple(final BlockPos pos) {
        final Vec3d eyesPos = new Vec3d(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight(), Wrapper.INSTANCE.player().posZ);
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (getBlock(neighbor).canCollideCheck(getState(neighbor), false)) {
                final Vec3d hitVec = new Vec3d((Vec3i)neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
                if (eyesPos.squareDistanceTo(hitVec) <= 36.0) {
                    Wrapper.INSTANCE.controller().processRightClickBlock(Wrapper.INSTANCE.player(), Wrapper.INSTANCE.world(), neighbor, side2, hitVec, EnumHand.MAIN_HAND);
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void faceVectorPacket(final Vec3d vec) {
        final double diffX = vec.x - Wrapper.INSTANCE.player().posX;
        final double diffY = vec.y - (Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight());
        final double diffZ = vec.z - Wrapper.INSTANCE.player().posZ;
        final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, dist)));
        Wrapper.INSTANCE.player().connection.sendPacket((Packet)new CPacketPlayer.Rotation(Wrapper.INSTANCE.player().rotationYaw + MathHelper.wrapDegrees(yaw - Wrapper.INSTANCE.player().rotationYaw), Wrapper.INSTANCE.player().rotationPitch + MathHelper.wrapDegrees(pitch - Wrapper.INSTANCE.player().rotationPitch), Wrapper.INSTANCE.player().onGround));
    }
    
    public static void faceBlockClient(final BlockPos blockPos) {
        final double diffX = blockPos.getX() + 0.5 - Wrapper.INSTANCE.player().posX;
        final double diffY = blockPos.getY() + 0.0 - (Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight());
        final double diffZ = blockPos.getZ() + 0.5 - Wrapper.INSTANCE.player().posZ;
        final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        Wrapper.INSTANCE.player().rotationYaw += MathHelper.wrapDegrees(yaw - Wrapper.INSTANCE.player().rotationYaw);
        Wrapper.INSTANCE.player().rotationPitch += MathHelper.wrapDegrees(pitch - Wrapper.INSTANCE.player().rotationPitch);
    }
    
    public static void faceBlockPacket(final BlockPos blockPos) {
        final double diffX = blockPos.getX() + 0.5 - Wrapper.INSTANCE.player().posX;
        final double diffY = blockPos.getY() + 0.0 - (Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight());
        final double diffZ = blockPos.getZ() + 0.5 - Wrapper.INSTANCE.player().posZ;
        final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        Wrapper.INSTANCE.player().connection.sendPacket((Packet)new CPacketPlayer.Rotation(Wrapper.INSTANCE.player().rotationYaw + MathHelper.wrapDegrees(yaw - Wrapper.INSTANCE.player().rotationYaw), Wrapper.INSTANCE.player().rotationPitch + MathHelper.wrapDegrees(pitch - Wrapper.INSTANCE.player().rotationPitch), Wrapper.INSTANCE.player().onGround));
    }
    
    public static void faceBlockClientHorizontally(final BlockPos blockPos) {
        final double diffX = blockPos.getX() + 0.5 - Wrapper.INSTANCE.player().posX;
        final double diffZ = blockPos.getZ() + 0.5 - Wrapper.INSTANCE.player().posZ;
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        Wrapper.INSTANCE.player().rotationYaw += MathHelper.wrapDegrees(yaw - Wrapper.INSTANCE.player().rotationYaw);
    }
    
    public static float getPlayerBlockDistance(final BlockPos blockPos) {
        return getPlayerBlockDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
    
    public static float getPlayerBlockDistance(final double posX, final double posY, final double posZ) {
        final float xDiff = (float)(Wrapper.INSTANCE.player().posX - posX);
        final float yDiff = (float)(Wrapper.INSTANCE.player().posY - posY);
        final float zDiff = (float)(Wrapper.INSTANCE.player().posZ - posZ);
        return getBlockDistance(xDiff, yDiff, zDiff);
    }
    
    public static float getBlockDistance(final float xDiff, final float yDiff, final float zDiff) {
        return MathHelper.sqrt((xDiff - 0.5f) * (xDiff - 0.5f) + (yDiff - 0.5f) * (yDiff - 0.5f) + (zDiff - 0.5f) * (zDiff - 0.5f));
    }
    
    public static float getHorizontalPlayerBlockDistance(final BlockPos blockPos) {
        final float xDiff = (float)(Wrapper.INSTANCE.player().posX - blockPos.getX());
        final float zDiff = (float)(Wrapper.INSTANCE.player().posZ - blockPos.getZ());
        return MathHelper.sqrt((xDiff - 0.5f) * (xDiff - 0.5f) + (zDiff - 0.5f) * (zDiff - 0.5f));
    }
    
    public static boolean breakBlockSimple(final BlockPos pos) {
        EnumFacing side = null;
        final EnumFacing[] sides = EnumFacing.values();
        final Vec3d eyesPos = Utils.getEyesPos();
        final Vec3d relCenter = getState(pos).getBoundingBox((IBlockAccess)Wrapper.INSTANCE.world(), pos).getCenter();
        final Vec3d center = new Vec3d((Vec3i)pos).add(relCenter);
        final Vec3d[] hitVecs = new Vec3d[sides.length];
        for (int i = 0; i < sides.length; ++i) {
            final Vec3i dirVec = sides[i].getDirectionVec();
            final Vec3d relHitVec = new Vec3d(relCenter.x * dirVec.getX(), relCenter.y * dirVec.getY(), relCenter.z * dirVec.getZ());
            hitVecs[i] = center.add(relHitVec);
        }
        for (int i = 0; i < sides.length; ++i) {
            if (Wrapper.INSTANCE.world().rayTraceBlocks(eyesPos, hitVecs[i], false, true, false) == null) {
                side = sides[i];
                break;
            }
        }
        if (side == null) {
            final double distanceSqToCenter = eyesPos.squareDistanceTo(center);
            for (int j = 0; j < sides.length; ++j) {
                if (eyesPos.squareDistanceTo(hitVecs[j]) < distanceSqToCenter) {
                    side = sides[j];
                    break;
                }
            }
        }
        if (side == null) {
            side = sides[0];
        }
        Utils.faceVectorPacket(hitVecs[side.ordinal()]);
        if (!Wrapper.INSTANCE.controller().onPlayerDamageBlock(pos, side)) {
            return false;
        }
        Wrapper.INSTANCE.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        return true;
    }
    
    public static void breakBlocksPacketSpam(final Iterable<BlockPos> blocks) {
        final Vec3d eyesPos = Utils.getEyesPos();
        final NetHandlerPlayClient connection = Wrapper.INSTANCE.player().connection;
        for (final BlockPos pos : blocks) {
            final Vec3d posVec = new Vec3d((Vec3i)pos).addVector(0.5, 0.5, 0.5);
            final double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
            for (final EnumFacing side : EnumFacing.values()) {
                final Vec3d hitVec = posVec.add(new Vec3d(side.getDirectionVec()).scale(0.5));
                if (eyesPos.squareDistanceTo(hitVec) < distanceSqPosVec) {
                    connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, side));
                    connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, side));
                    break;
                }
            }
        }
    }
    
    public static LinkedList<BlockPos> findBlocksNearEntity(final EntityLivingBase entity, final int blockId, final int blockMeta, final int distance) {
        final LinkedList<BlockPos> blocks = new LinkedList<BlockPos>();
        for (int x = (int)Wrapper.INSTANCE.player().posX - distance; x <= (int)Wrapper.INSTANCE.player().posX + distance; ++x) {
            for (int z = (int)Wrapper.INSTANCE.player().posZ - distance; z <= (int)Wrapper.INSTANCE.player().posZ + distance; ++z) {
                for (int height = Wrapper.INSTANCE.world().getHeight(x, z), y = 0; y <= height; ++y) {
                    final BlockPos blockPos = new BlockPos(x, y, z);
                    final IBlockState blockState = Wrapper.INSTANCE.world().getBlockState(blockPos);
                    if (blockId == -1 || blockMeta == -1) {
                        blocks.add(blockPos);
                    }
                    else {
                        final int id = Block.getIdFromBlock(blockState.getBlock());
                        final int meta = blockState.getBlock().getMetaFromState(blockState);
                        if (id == blockId && meta == blockMeta) {
                            blocks.add(blockPos);
                        }
                    }
                }
            }
        }
        return blocks;
    }
    
    public static boolean isInLiquid() {
        if (Wrapper.INSTANCE.mc().player.isInWater()) {
            return true;
        }
        boolean inLiquid = false;
        final int y = (int)Wrapper.INSTANCE.mc().player.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor(Wrapper.INSTANCE.mc().player.getEntityBoundingBox().minX); x < MathHelper.floor(Wrapper.INSTANCE.mc().player.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor(Wrapper.INSTANCE.mc().player.getEntityBoundingBox().minZ); z < MathHelper.floor(Wrapper.INSTANCE.mc().player.getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = Wrapper.INSTANCE.mc().world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && block != Blocks.AIR) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }
    
    public static BlockPos getHypixelBlockpos(final String str) {
        int val = 89;
        if (str != null && str.length() > 1) {
            final char[] chs = str.toCharArray();
            for (int lenght = chs.length, i = 0; i < lenght; ++i) {
                val += chs[i] * str.length() * str.length() + str.charAt(0) + str.charAt(1);
            }
            val /= str.length();
        }
        return new BlockPos(val, -val % 255, val);
    }
    
    static {
        BlockUtils.mc = Minecraft.getMinecraft();
    }
}
