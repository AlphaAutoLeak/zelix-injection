package zelix.hack.hacks.automine;

public class Vec3
{
    public double x;
    public double y;
    public double z;
    public static Vec3 instace;
    
    public Vec3(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public Vec3 addVector(final double x, final double y, final double z) {
        return new Vec3(this.x + x, this.y + y, this.z + z);
    }
    
    public Vec3 floor() {
        return new Vec3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
    }
    
    public double squareDistanceTo(final Vec3 v) {
        return Math.pow(v.x - this.x, 2.0) + Math.pow(v.y - this.y, 2.0) + Math.pow(v.z - this.z, 2.0);
    }
    
    public Vec3 add(final Vec3 v) {
        return this.addVector(v.getX(), v.getY(), v.getZ());
    }
    
    public Vec3 mc() {
        return new Vec3(this.x, this.y, this.z);
    }
    
    @Override
    public String toString() {
        return "[" + this.x + ";" + this.y + ";" + this.z + "]";
    }
}
