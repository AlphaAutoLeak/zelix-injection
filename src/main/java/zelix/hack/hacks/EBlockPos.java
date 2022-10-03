package zelix.hack.hacks;

import net.minecraft.util.math.*;

class EBlockPos extends BlockPos
{
    private int x;
    private int y;
    private int z;
    
    public EBlockPos() {
        super(0, 0, 0);
    }
    
    public void set(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public void setZ(final int z) {
        this.z = z;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
}
