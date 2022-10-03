package zelix.gui.clickguis.caesium.util;

import java.math.*;

public class MathUtil
{
    public static float round(final float toRound, final int scale) {
        return new BigDecimal(toRound).setScale(scale, RoundingMode.HALF_EVEN).floatValue();
    }
    
    public static double round(final double toRound, final int scale) {
        return new BigDecimal(toRound).setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
    }
}
