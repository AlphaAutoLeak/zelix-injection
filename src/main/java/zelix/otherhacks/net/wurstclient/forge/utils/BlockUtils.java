package zelix.otherhacks.net.wurstclient.forge.utils;

import net.minecraft.client.*;
import net.minecraft.block.state.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.util.math.*;
import zelix.otherhacks.net.wurstclient.forge.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import java.lang.reflect.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import net.minecraft.network.play.client.*;
import net.minecraft.client.network.*;
import java.util.*;

public final class BlockUtils
{
    private static final Minecraft mc;
    
    public static IBlockState getState(final BlockPos pos) {
        return WMinecraft.getWorld().getBlockState(pos);
    }
    
    public static Block getBlock(final BlockPos pos) {
        return getState(pos).getBlock();
    }
    
    public static int getId(final BlockPos pos) {
        return Block.getIdFromBlock(getBlock(pos));
    }
    
    public static String getName(final Block block) {
        return "" + Block.REGISTRY.getNameForObject(block);
    }
    
    public static Material getMaterial(final BlockPos pos) {
        return getState(pos).getMaterial();
    }
    
    public static AxisAlignedBB getBoundingBox(final BlockPos pos) {
        return getState(pos).getBoundingBox((IBlockAccess)WMinecraft.getWorld(), pos).offset(pos);
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }
    
    public static float getHardness(final BlockPos pos) {
        return getState(pos).getPlayerRelativeBlockHardness((EntityPlayer)WMinecraft.getPlayer(), (World)WMinecraft.getWorld(), pos);
    }
    
    public static void placeBlockSimple(final BlockPos pos) {
        EnumFacing side = null;
        final EnumFacing[] sides = EnumFacing.values();
        final Vec3d eyesPos = RotationUtils.getEyesPos();
        final Vec3d posVec = new Vec3d((Vec3i)pos).addVector(0.5, 0.5, 0.5);
        final double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
        final Vec3d[] hitVecs = new Vec3d[sides.length];
        for (int i = 0; i < sides.length; ++i) {
            hitVecs[i] = posVec.add(new Vec3d(sides[i].getDirectionVec()).scale(0.5));
        }
        for (int i = 0; i < sides.length; ++i) {
            if (canBeClicked(pos.offset(sides[i]))) {
                if (WMinecraft.getWorld().rayTraceBlocks(eyesPos, hitVecs[i], false, true, false) == null) {
                    side = sides[i];
                    break;
                }
            }
        }
        if (side == null) {
            for (int i = 0; i < sides.length; ++i) {
                if (canBeClicked(pos.offset(sides[i]))) {
                    if (distanceSqPosVec <= eyesPos.squareDistanceTo(hitVecs[i])) {
                        side = sides[i];
                        break;
                    }
                }
            }
        }
        if (side == null) {
            return;
        }
        final Vec3d hitVec = hitVecs[side.ordinal()];
        RotationUtils.faceVectorPacket(hitVec);
        if (RotationUtils.getAngleToLastReportedLookVec(hitVec) > 1.0) {
            return;
        }
        try {
            final Field rightClickDelayTimer = BlockUtils.mc.getClass().getDeclaredField(ForgeWurst.getForgeWurst().isObfuscated() ? "rightClickDelayTimer" : "rightClickDelayTimer");
            rightClickDelayTimer.setAccessible(true);
            if (rightClickDelayTimer.getInt(BlockUtils.mc) > 0) {
                return;
            }
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        WPlayerController.processRightClickBlock(pos.offset(side), side.getOpposite(), hitVec);
        WMinecraft.getPlayer().connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        try {
            final Field rightClickDelayTimer = BlockUtils.mc.getClass().getDeclaredField(ForgeWurst.getForgeWurst().isObfuscated() ? "rightClickDelayTimer" : "rightClickDelayTimer");
            rightClickDelayTimer.setAccessible(true);
            rightClickDelayTimer.setInt(BlockUtils.mc, 4);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static boolean breakBlockSimple(final BlockPos pos) {
        EnumFacing side = null;
        final EnumFacing[] sides = EnumFacing.values();
        final Vec3d eyesPos = RotationUtils.getEyesPos();
        final Vec3d relCenter = getState(pos).getBoundingBox((IBlockAccess)WMinecraft.getWorld(), pos).getCenter();
        final Vec3d center = new Vec3d((Vec3i)pos).add(relCenter);
        final Vec3d[] hitVecs = new Vec3d[sides.length];
        for (int i = 0; i < sides.length; ++i) {
            final Vec3i dirVec = sides[i].getDirectionVec();
            final Vec3d relHitVec = new Vec3d(WVec3d.getX(relCenter) * dirVec.getX(), WVec3d.getY(relCenter) * dirVec.getY(), WVec3d.getZ(relCenter) * dirVec.getZ());
            hitVecs[i] = center.add(relHitVec);
        }
        for (int i = 0; i < sides.length; ++i) {
            if (WMinecraft.getWorld().rayTraceBlocks(eyesPos, hitVecs[i], false, true, false) == null) {
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
        RotationUtils.faceVectorPacket(hitVecs[side.ordinal()]);
        if (!BlockUtils.mc.playerController.onPlayerDamageBlock(pos, side)) {
            return false;
        }
        WMinecraft.getPlayer().connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        return true;
    }
    
    public static void breakBlocksPacketSpam(final Iterable<BlockPos> blocks) {
        final Vec3d eyesPos = RotationUtils.getEyesPos();
        final NetHandlerPlayClient connection = WMinecraft.getPlayer().connection;
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
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
