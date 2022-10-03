package zelix.utils.visual;

import java.awt.*;
import java.text.*;

public enum Colors
{
    BLACK(-16711423), 
    BLUE(-12028161), 
    DARKBLUE(-12621684), 
    GREEN(-9830551), 
    DARKGREEN(-9320847), 
    WHITE(-65794), 
    AQUA(-7820064), 
    DARKAQUA(-12621684), 
    GREY(-9868951), 
    DARKGREY(-14342875), 
    RED(-65536), 
    DARKRED(-8388608), 
    ORANGE(-29696), 
    DARKORANGE(-2263808), 
    YELLOW(-256), 
    DARKYELLOW(-2702025), 
    MAGENTA(-18751), 
    DARKMAGENTA(-2252579);
    
    public int c;
    
    private Colors(final int co) {
        this.c = co;
    }
    
    public static int getColor(final Color color) {
        return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    
    public static int getColor(final int brightness) {
        return getColor(brightness, brightness, brightness, 255);
    }
    
    public static int getColor(final int brightness, final int alpha) {
        return getColor(brightness, brightness, brightness, alpha);
    }
    
    public static int getColor(final int red, final int green, final int blue) {
        return getColor(red, green, blue, 255);
    }
    
    public static int getColor(final int red, final int green, final int blue, final int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        color |= blue;
        return color;
    }
    
    public static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
        if (fractions == null) {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        if (colors == null) {
            throw new IllegalArgumentException("Colours can't be null");
        }
        if (fractions.length != colors.length) {
            throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
        }
        final int[] indicies = getFractionIndicies(fractions, progress);
        if (indicies[0] < 0 || indicies[0] >= fractions.length || indicies[1] < 0 || indicies[1] >= fractions.length) {
            return colors[0];
        }
        final float[] range = { fractions[indicies[0]], fractions[indicies[1]] };
        final Color[] colorRange = { colors[indicies[0]], colors[indicies[1]] };
        final float max = range[1] - range[0];
        final float value = progress - range[0];
        final float weight = value / max;
        final Color color = blend(colorRange[0], colorRange[1], 1.0f - weight);
        return color;
    }
    
    public static Color blend(final Color color1, final Color color2, final double ratio) {
        final float r = (float)ratio;
        final float ir = 1.0f - r;
        final float[] rgb1 = new float[3];
        final float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        }
        else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        }
        else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        }
        else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(red, green, blue);
        }
        catch (IllegalArgumentException exp) {
            final NumberFormat nf = NumberFormat.getNumberInstance();
            exp.printStackTrace();
        }
        return color3;
    }
    
    public static int[] getFractionIndicies(final float[] fractions, final float progress) {
        final int[] range = new int[2];
        int startPoint;
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {}
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }
    
    public static Color getDarker(final Color before, final int dark, final int alpha) {
        final int rDank = Math.max(before.getRed() - dark, 0);
        final int gDank = Math.max(before.getGreen() - dark, 0);
        final int bDank = Math.max(before.getBlue() - dark, 0);
        return new Color(rDank, gDank, bDank, alpha);
    }
}
