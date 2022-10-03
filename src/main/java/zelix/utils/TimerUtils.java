package zelix.utils;

import net.minecraft.client.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.util.*;
import java.lang.reflect.*;

public class TimerUtils
{
    private long lastMS;
    private long prevMS;
    
    public TimerUtils() {
        this.lastMS = 0L;
        this.prevMS = 0L;
    }
    
    public boolean isDelay(final long delay) {
        return System.currentTimeMillis() - this.lastMS >= delay;
    }
    
    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    
    public void setLastMS(final long lastMS) {
        this.lastMS = lastMS;
    }
    
    public void setLastMS() {
        this.lastMS = System.currentTimeMillis();
    }
    
    public int convertToMS(final int d) {
        return 1000 / d;
    }
    
    public boolean hasReached(final float f) {
        return this.getCurrentMS() - this.lastMS >= f;
    }
    
    public void reset() {
        this.lastMS = this.getCurrentMS();
    }
    
    public boolean delay(final float milliSec) {
        return this.getTime() - this.prevMS >= milliSec;
    }
    
    public long getTime() {
        return System.nanoTime() / 1000000L;
    }
    
    public static void settimer(final float timerSpeed) {
        try {
            final Field field = ReflectionHelper.findField((Class)Minecraft.class, new String[] { "timer", "timer" });
            field.setAccessible(true);
            final Field field2 = ReflectionHelper.findField((Class)Timer.class, new String[] { "timerSpeed", "field_74278_d" });
            field2.setAccessible(true);
            field2.setFloat(field.get(Minecraft.getMinecraft()), timerSpeed);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
