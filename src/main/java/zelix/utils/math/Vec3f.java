package zelix.utils.math;

import zelix.utils.hooks.visual.*;

public final class Vec3f
{
    private double x;
    private double y;
    private double z;
    
    public Vec3f() {
        this(0.0, 0.0, 0.0);
    }
    
    public Vec3f(final Vec3f vec) {
        this(vec.x, vec.y, vec.z);
    }
    
    public Vec3f(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public final Vec3f setX(final double x) {
        this.x = x;
        return this;
    }
    
    public final Vec3f setY(final double y) {
        this.y = y;
        return this;
    }
    
    public final Vec3f setZ(final double z) {
        this.z = z;
        return this;
    }
    
    public final double getX() {
        return this.x;
    }
    
    public final double getY() {
        return this.y;
    }
    
    public final double getZ() {
        return this.z;
    }
    
    public final Vec3f add(final Vec3f vec) {
        return this.add(vec.x, vec.y, vec.z);
    }
    
    public final Vec3f add(final double x, final double y, final double z) {
        return new Vec3f(this.x + x, this.y + y, this.z + z);
    }
    
    public final Vec3f sub(final Vec3f vec) {
        return new Vec3f(this.x - vec.x, this.y - vec.y, this.z - vec.z);
    }
    
    public final Vec3f sub(final double x, final double y, final double z) {
        return new Vec3f(this.x - x, this.y - y, this.z - z);
    }
    
    public final Vec3f scale(final float scale) {
        return new Vec3f(this.x * scale, this.y * scale, this.z * scale);
    }
    
    public final Vec3f copy() {
        return new Vec3f(this);
    }
    
    public final Vec3f transfer(final Vec3f vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        return this;
    }
    
    public final double distanceTo(final Vec3f vec) {
        final double dx = this.x - vec.x;
        final double dy = this.y - vec.y;
        final double dz = this.z - vec.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    public final Vec2f rotationsTo(final Vec3f vec) {
        final double[] diff = { vec.x - this.x, vec.y - this.y, vec.z - this.z };
        final double hDist = Math.sqrt(diff[0] * diff[0] + diff[2] * diff[2]);
        return new Vec2f(Math.toDegrees(Math.atan2(diff[2], diff[0])) - 90.0, -Math.toDegrees(Math.atan2(diff[1], hDist)));
    }
    
    public final Vec3f toScreen() {
        return HGLUtils.toScreen(this);
    }
    
    @Override
    public String toString() {
        return "Vec3{x=" + this.x + ", y=" + this.y + ", z=" + this.z + '}';
    }
    
    public static Vec3f createVectorHelper(final double p_72443_0_, final double p_72443_2_, final double p_72443_4_) {
        return new Vec3f(p_72443_0_, p_72443_2_, p_72443_4_);
    }
}
