package zelix.hack.hacks;

import zelix.hack.*;
import java.util.*;

import zelix.utils.ReflectionHelper;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.init.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.client.multiplayer.*;
import net.minecraftforge.fml.relauncher.*;
import java.lang.reflect.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.*;
import net.minecraft.block.state.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;

public class BedNuker extends Hack
{
    public static BlockPos blockBreaking;
    TimerUtils timer;
    List<BlockPos> beds;
    public BooleanValue instant;
    
    public BedNuker() {
        super("BedNuker", HackCategory.PLAYER);
        this.timer = new TimerUtils();
        this.beds = new ArrayList<BlockPos>();
        this.instant = new BooleanValue("Instant", Boolean.valueOf(false));
        this.addValue(this.instant);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Wrapper.INSTANCE.world() == null || Wrapper.INSTANCE.player() == null) {
            return;
        }
        int y;
        for (int reach = y = 6; y >= -reach; --y) {
            for (int x = -reach; x <= reach; ++x) {
                for (int z = -reach; z <= reach; ++z) {
                    if (Wrapper.INSTANCE.player().isSneaking()) {
                        return;
                    }
                    final BlockPos pos = new BlockPos(Wrapper.INSTANCE.player().posX + x, Wrapper.INSTANCE.player().posY + y, Wrapper.INSTANCE.player().posZ + z);
                    if (this.blockChecks(Wrapper.INSTANCE.world().getBlockState(pos).getBlock()) && Wrapper.INSTANCE.player().getDistance(Wrapper.INSTANCE.player().posX + x, Wrapper.INSTANCE.player().posY + y, Wrapper.INSTANCE.player().posZ + z) < Wrapper.INSTANCE.controller().getBlockReachDistance() - 0.2 && !this.beds.contains(pos)) {
                        this.beds.add(pos);
                    }
                }
            }
        }
        BlockPos closest = null;
        if (!this.beds.isEmpty()) {
            for (int i = 0; i < this.beds.size(); ++i) {
                final BlockPos bed = this.beds.get(i);
                if (Wrapper.INSTANCE.player().getDistance((double)bed.getX(), (double)bed.getY(), (double)bed.getZ()) > Wrapper.INSTANCE.controller().getBlockReachDistance() - 0.2 || Wrapper.INSTANCE.world().getBlockState(bed).getBlock() != Blocks.BED) {
                    this.beds.remove(i);
                }
                if (closest == null || (Wrapper.INSTANCE.player().getDistance((double)bed.getX(), (double)bed.getY(), (double)bed.getZ()) < Wrapper.INSTANCE.player().getDistance((double)closest.getX(), (double)closest.getY(), (double)closest.getZ()) && Wrapper.INSTANCE.player().ticksExisted % 50 == 0)) {
                    closest = bed;
                }
            }
        }
        if (closest != null) {
            final float[] rot = this.getRotations(closest, this.getClosestEnum(closest));
            BedNuker.blockBreaking = closest;
            return;
        }
        BedNuker.blockBreaking = null;
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (BedNuker.blockBreaking != null) {
            if (this.instant.getValue()) {
                Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, BedNuker.blockBreaking, EnumFacing.DOWN));
                Wrapper.INSTANCE.player().swingArm(EnumHand.MAIN_HAND);
                Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, BedNuker.blockBreaking, EnumFacing.DOWN));
            }
            else {
                final Field field = ReflectionHelper.findField((Class)PlayerControllerMP.class, new String[] { "curBlockDamageMP", "curBlockDamageMP" });
                final Field blockdelay = ReflectionHelper.findField((Class)PlayerControllerMP.class, new String[] { "blockHitDelay", "blockHitDelay" });
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    if (!blockdelay.isAccessible()) {
                        blockdelay.setAccessible(true);
                    }
                    if (field.getFloat(Wrapper.INSTANCE.mc().playerController) > 1.0f) {
                        blockdelay.setInt(Wrapper.INSTANCE.mc().playerController, 1);
                    }
                    Wrapper.INSTANCE.player().swingArm(EnumHand.MAIN_HAND);
                    final EnumFacing direction = this.getClosestEnum(BedNuker.blockBreaking);
                    if (direction != null) {
                        Wrapper.INSTANCE.controller().onPlayerDamageBlock(BedNuker.blockBreaking, direction);
                    }
                }
                catch (Exception ex) {}
            }
        }
        super.onPlayerTick(event);
    }
    
    @Override
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (BedNuker.blockBreaking != null) {
            GlStateManager.pushMatrix();
            GlStateManager.disableDepth();
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
        super.onRenderWorldLast(event);
    }
    
    private boolean blockChecks(final Block block) {
        return block == Blocks.BED;
    }
    
    public float[] getRotations(final BlockPos block, final EnumFacing face) {
        final double x = block.getX() + 0.5 - Wrapper.INSTANCE.player().posX;
        final double z = block.getZ() + 0.5 - Wrapper.INSTANCE.player().posZ;
        final double d1 = Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight() - (block.getY() + 0.5);
        final double d2 = MathHelper.sqrt(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(Math.atan2(d1, d2) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[] { yaw, pitch };
    }
    
    private EnumFacing getClosestEnum(final BlockPos pos) {
        EnumFacing closestEnum = EnumFacing.UP;
        final float rotations = MathHelper.wrapDegrees(this.getRotations(pos, EnumFacing.UP)[0]);
        if (rotations >= 45.0f && rotations <= 135.0f) {
            closestEnum = EnumFacing.EAST;
        }
        else if ((rotations >= 135.0f && rotations <= 180.0f) || (rotations <= -135.0f && rotations >= -180.0f)) {
            closestEnum = EnumFacing.SOUTH;
        }
        else if (rotations <= -45.0f && rotations >= -135.0f) {
            closestEnum = EnumFacing.WEST;
        }
        else if ((rotations >= -45.0f && rotations <= 0.0f) || (rotations <= 45.0f && rotations >= 0.0f)) {
            closestEnum = EnumFacing.NORTH;
        }
        if (MathHelper.wrapDegrees(this.getRotations(pos, EnumFacing.UP)[1]) > 75.0f || MathHelper.wrapDegrees(this.getRotations(pos, EnumFacing.UP)[1]) < -75.0f) {
            closestEnum = EnumFacing.UP;
        }
        return closestEnum;
    }
    
    private EnumFacing getFacingDirection(final BlockPos pos) {
        EnumFacing direction = null;
        if (!Wrapper.INSTANCE.world().getBlockState(pos.add(0, 1, 0)).getBlock().isFullCube((IBlockState)null) && !(Wrapper.INSTANCE.world().getBlockState(pos.add(0, 1, 0)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.UP;
        }
        else if (!Wrapper.INSTANCE.world().getBlockState(pos.add(0, -1, 0)).getBlock().isFullCube((IBlockState)null) && !(Wrapper.INSTANCE.world().getBlockState(pos.add(0, -1, 0)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.DOWN;
        }
        else if (!Wrapper.INSTANCE.world().getBlockState(pos.add(1, 0, 0)).getBlock().isFullCube((IBlockState)null) && !(Wrapper.INSTANCE.world().getBlockState(pos.add(1, 0, 0)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.EAST;
        }
        else if (!Wrapper.INSTANCE.world().getBlockState(pos.add(-1, 0, 0)).getBlock().isFullCube((IBlockState)null) && !(Wrapper.INSTANCE.world().getBlockState(pos.add(-1, 0, 0)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.WEST;
        }
        else if (!Wrapper.INSTANCE.world().getBlockState(pos.add(0, 0, 1)).getBlock().isFullCube((IBlockState)null) && !(Wrapper.INSTANCE.world().getBlockState(pos.add(0, 0, 1)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.SOUTH;
        }
        else if (!Wrapper.INSTANCE.world().getBlockState(pos.add(0, 0, 1)).getBlock().isFullCube((IBlockState)null) && !(Wrapper.INSTANCE.world().getBlockState(pos.add(0, 0, -1)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.NORTH;
        }
        final RayTraceResult rayResult = Wrapper.INSTANCE.world().rayTraceBlocks(new Vec3d(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight(), Wrapper.INSTANCE.player().posZ), new Vec3d(pos.getX() + 0.5, (double)pos.getY(), pos.getZ() + 0.5));
        if (rayResult != null && rayResult.getBlockPos() == pos) {
            return rayResult.sideHit;
        }
        return direction;
    }
}
