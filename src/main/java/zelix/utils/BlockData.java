package zelix.utils;

import net.minecraft.util.math.*;
import net.minecraft.util.*;
import net.minecraft.init.*;

public class BlockData
{
    public final BlockPos position;
    public final EnumFacing face;
    
    public BlockData(final BlockPos add, final EnumFacing up) {
        this.position = add;
        this.face = up;
    }
    
    public static BlockData getBlockData(final BlockPos pos) {
        if (Wrapper.INSTANCE.world().getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.AIR) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (Wrapper.INSTANCE.world().getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.AIR) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (Wrapper.INSTANCE.world().getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.AIR) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (Wrapper.INSTANCE.world().getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.AIR) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (Wrapper.INSTANCE.world().getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.AIR) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        return null;
    }
}
