package zelix.gui.clickguis.N3ro.Utils;

public class TranslateUtil
{
    private float x;
    private float y;
    private long lastMS;
    
    public TranslateUtil(final float x, final float y) {
        this.x = x;
        this.y = y;
        this.lastMS = System.currentTimeMillis();
    }
    
    public void interpolate(final float targetX, final float targetY, final float smoothing) {
        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        final int deltaX = (int)(Math.abs(targetX - this.x) * smoothing);
        final int deltaY = (int)(Math.abs(targetY - this.y) * smoothing);
        this.x = this.calculateCompensation(targetX, this.x, delta, deltaX);
        this.y = this.calculateCompensation(targetY, this.y, delta, deltaY);
    }
    
    public float getX() {
        return this.x;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
    
    public float calculateCompensation(final float target, float current, long delta, final int speed) {
        final float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }
        if (diff > speed) {
            final double dell = (speed * delta / 16L < 0.25) ? 0.5 : (speed * delta / 16L);
            current -= (float)dell;
            if (current < target) {
                current = target;
            }
        }
        else if (diff < -speed) {
            final double dell = (speed * delta / 16L < 0.25) ? 0.5 : (speed * delta / 16L);
            current += (float)dell;
            if (current > target) {
                current = target;
            }
        }
        else {
            current = target;
        }
        return current;
    }
}
