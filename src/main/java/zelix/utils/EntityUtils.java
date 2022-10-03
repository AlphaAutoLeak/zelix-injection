package zelix.utils;

import net.minecraft.entity.*;

public class EntityUtils
{
    public static float getDistance(final Entity e1, final Entity e2) {
        return e1.getDistance(e2);
    }
    
    public static double getDistanceSq(final Entity e1, final Entity e2) {
        return e1.getDistanceSq(e2);
    }
}
