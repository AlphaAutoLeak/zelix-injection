package zelix.hack.hacks.xray.utils;

import net.minecraft.util.math.*;

public class WorldRegion
{
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;
    public int minChunkX;
    public int minChunkY;
    public int minChunkZ;
    public int maxChunkX;
    public int maxChunkY;
    public int maxChunkZ;
    
    public WorldRegion(final Vec3i pos, final int radius) {
        this.minX = pos.getX() - radius;
        this.maxX = pos.getX() + radius;
        this.minY = Math.max(0, pos.getY() - 92);
        this.maxY = Math.min(255, pos.getY() + 32);
        this.minZ = pos.getZ() - radius;
        this.maxZ = pos.getZ() + radius;
        this.minChunkX = this.minX >> 4;
        this.maxChunkX = this.maxX >> 4;
        this.minChunkY = this.minY >> 4;
        this.maxChunkY = this.maxY >> 4;
        this.minChunkZ = this.minZ >> 4;
        this.maxChunkZ = this.maxZ >> 4;
    }
}
