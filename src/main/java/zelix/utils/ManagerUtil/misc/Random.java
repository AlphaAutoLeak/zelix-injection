package zelix.utils.ManagerUtil.misc;

import java.security.*;

public class Random
{
    public static double dbRandom(final double min, final double max) {
        final SecureRandom rng = new SecureRandom();
        return rng.nextDouble() * (max - min) + min;
    }
    
    public static float flRandom(final float min, final float max) {
        final SecureRandom rng = new SecureRandom();
        return rng.nextFloat() * (max - min) + min;
    }
}
