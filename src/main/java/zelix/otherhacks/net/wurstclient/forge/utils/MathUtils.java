package zelix.otherhacks.net.wurstclient.forge.utils;

public final class MathUtils
{
    public static boolean isInteger(final String s) {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isDouble(final String s) {
        try {
            Double.parseDouble(s);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static int floor(final float value) {
        final int i = (int)value;
        return (value < i) ? (i - 1) : i;
    }
    
    public static int floor(final double value) {
        final int i = (int)value;
        return (value < i) ? (i - 1) : i;
    }
    
    public static int clamp(final int num, final int min, final int max) {
        return (num < min) ? min : ((num > max) ? max : num);
    }
    
    public static float clamp(final float num, final float min, final float max) {
        return (num < min) ? min : ((num > max) ? max : num);
    }
    
    public static double clamp(final double num, final double min, final double max) {
        return (num < min) ? min : ((num > max) ? max : num);
    }
}
