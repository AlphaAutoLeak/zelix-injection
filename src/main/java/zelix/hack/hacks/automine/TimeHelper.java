package zelix.hack.hacks.automine;

public class TimeHelper
{
    public long lastMs;
    
    public TimeHelper() {
        this.lastMs = 0L;
    }
    
    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }
    
    public boolean hasReach(final double milliseconds) {
        if (System.currentTimeMillis() - this.lastMs >= milliseconds) {
            this.reset();
            return true;
        }
        return false;
    }
}
