package zelix.utils.ManagerUtil.misc;

import java.security.*;
import java.math.*;

public class MathUtils
{
    public static int getRandomInRange(final int min, final int max) {
        return (int)(Math.random() * (max - min) + min);
    }
    
    public static float getRandomInRange(final float min, final float max) {
        final SecureRandom random = new SecureRandom();
        return random.nextFloat() * (max - min) + min;
    }
    
    public static double getRandomInRange(final double min, final double max) {
        final SecureRandom random = new SecureRandom();
        return random.nextDouble() * (max - min) + min;
    }
    
    public static double lerp(final double old, final double newVal, final double amount) {
        return (1.0 - amount) * old + amount * newVal;
    }
    
    public static Double interpolate(final double oldValue, final double newValue, final double interpolationValue) {
        return oldValue + (newValue - oldValue) * interpolationValue;
    }
    
    public static float interpolateFloat(final float oldValue, final float newValue, final double interpolationValue) {
        return (float)(Object)interpolate(oldValue, newValue, (float)interpolationValue);
    }
    
    public static int interpolateInt(final int oldValue, final int newValue, final double interpolationValue) {
        return (int)(Object)interpolate(oldValue, newValue, (float)interpolationValue);
    }
    
    public static float calculateGaussianValue(final float x, final float sigma) {
        final double PI = 3.141592653;
        final double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
        return (float)(output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }
    
    public static double roundToHalf(final double d) {
        return Math.round(d * 2.0) / 2.0;
    }
    
    public static double round(final double num, final double increment) {
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale((int)increment, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static float getRandomFloat(final float max, final float min) {
        final SecureRandom random = new SecureRandom();
        return random.nextFloat() * (max - min) + min;
    }
}
