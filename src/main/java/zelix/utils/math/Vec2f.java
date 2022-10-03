package zelix.utils.math;

import zelix.utils.hooks.visual.*;

public final class Vec2f
{
    private float x;
    private float y;
    
    public Vec2f() {
        this(0.0f, 0.0f);
    }
    
    public Vec2f(final Vec2f vec) {
        this(vec.x, vec.y);
    }
    
    public Vec2f(final double x, final double y) {
        this((float)x, (float)y);
    }
    
    public Vec2f(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public final Vec2f setX(final float x) {
        this.x = x;
        return this;
    }
    
    public final Vec2f setY(final float y) {
        this.y = y;
        return this;
    }
    
    public final float getX() {
        return this.x;
    }
    
    public final float getY() {
        return this.y;
    }
    
    public final Vec2f add(final Vec2f vec) {
        return new Vec2f(this.x + vec.x, this.y + vec.y);
    }
    
    public final Vec2f add(final double x, final double y) {
        return this.add(new Vec2f(x, y));
    }
    
    public final Vec2f add(final float x, final float y) {
        return this.add(new Vec2f(x, y));
    }
    
    public final Vec2f sub(final Vec2f vec) {
        return new Vec2f(this.x - vec.x, this.y - vec.y);
    }
    
    public final Vec2f sub(final double x, final double y) {
        return this.sub(new Vec2f(x, y));
    }
    
    public final Vec2f sub(final float x, final float y) {
        return this.sub(new Vec2f(x, y));
    }
    
    public final Vec2f scale(final float scale) {
        return new Vec2f(this.x * scale, this.y * scale);
    }
    
    public final Vec3f toVec3() {
        return new Vec3f(this.x, this.y, 0.0);
    }
    
    public final Vec2f copy() {
        return new Vec2f(this);
    }
    
    public final Vec2f transfer(final Vec2f vec) {
        this.x = vec.x;
        this.y = vec.y;
        return this;
    }
    
    public final float distanceTo(final Vec2f vec) {
        final double dx = this.x - vec.x;
        final double dy = this.y - vec.y;
        return (float)Math.sqrt(dx * dx + dy * dy);
    }
    
    public final Vec3f toScreen() {
        return HGLUtils.toWorld(this.toVec3());
    }
    
    @Override
    public String toString() {
        return "Vec2{x=" + this.x + ", y=" + this.y + '}';
    }
}
