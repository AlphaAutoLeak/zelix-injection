package zelix.hack.hacks.xray.reference.block;

import net.minecraft.util.math.*;

public class BlockInfo extends Vec3i
{
    public int[] color;
    public double alpha;
    
    public BlockInfo(final int x, final int y, final int z, final int[] color, final double alpha) {
        super(x, y, z);
        this.color = color;
        this.alpha = alpha;
    }
    
    public BlockInfo(final Vec3i pos, final int[] c, final double alpha) {
        this(pos.getX(), pos.getY(), pos.getZ(), c, alpha);
    }
}
