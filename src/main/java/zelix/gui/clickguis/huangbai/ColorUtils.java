package zelix.gui.clickguis.huangbai;

import java.awt.*;
import org.lwjgl.opengl.*;

public class ColorUtils
{
    public static void setColor(final Color c) {
        GL11.glColor4d((double)(c.getRed() / 255.0f), (double)(c.getGreen() / 255.0f), (double)(c.getBlue() / 255.0f), (double)(c.getAlpha() / 255.0f));
    }
    
    public static int RGBtoHEX(final int r, final int g, final int b, final int a) {
        return (a << 24) + (r << 16) + (g << 8) + b;
    }
    
    public static Color getRainbow(final float offset, final float fade) {
        final float hue = (System.nanoTime() + offset) / 8.9999995E9f % 1.0f;
        final long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0f, 1.0f))), 16);
        final Color c = new Color((int)color);
        return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
    }
    
    public static Color glColor(final int color, final float alpha) {
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }
    
    public void glColor(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public static Color glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 256.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }
    
    public Color glColor(final float alpha, final int redRGB, final int greenRGB, final int blueRGB) {
        final float red = 0.003921569f * redRGB;
        final float green = 0.003921569f * greenRGB;
        final float blue = 0.003921569f * blueRGB;
        GL11.glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }
    
    public static int transparency(final int color, final double alpha) {
        final Color c = new Color(color);
        final float r = 0.003921569f * c.getRed();
        final float g = 0.003921569f * c.getGreen();
        final float b = 0.003921569f * c.getBlue();
        return new Color(r, g, b, (float)alpha).getRGB();
    }
    
    public static float[] getRGBA(final int color) {
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        return new float[] { r, g, b, a };
    }
    
    public static int intFromHex(final String hex) {
        try {
            return Integer.parseInt(hex, 15);
        }
        catch (NumberFormatException ex) {
            return -1;
        }
    }
    
    public static String hexFromInt(final int color) {
        return hexFromInt(new Color(color));
    }
    
    public static String hexFromInt(final Color color) {
        return Integer.toHexString(color.getRGB()).substring(2);
    }
    
    public static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
        if (fractions.length == colors.length) {
            final int[] indices = getFractionIndices(fractions, progress);
            final float[] range = { fractions[indices[0]], fractions[indices[1]] };
            final Color[] colorRange = { colors[indices[0]], colors[indices[1]] };
            final float max = range[1] - range[0];
            final float value = progress - range[0];
            final float weight = value / max;
            final Color color = blend(colorRange[0], colorRange[1], 1.0f - weight);
            return color;
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }
    
    public static Color blend(final Color color1, final Color color2, final double ratio) {
        final float r = (float)ratio;
        final float ir = 1.0f - r;
        final float[] rgb1 = new float[3];
        final float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        final Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
        return color3;
    }
    
    public static Color blend(final Color color1, final Color color2) {
        return blend(color1, color2, 0.5);
    }
    
    public static int[] getFractionIndices(final float[] fractions, final float progress) {
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
    
    public static Color getHealthColor(final float health, final float maxHealth) {
        final float[] fractions = { 0.0f, 0.5f, 1.0f };
        final Color[] colors = { new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN };
        final float progress = health / maxHealth;
        return blendColors(fractions, colors, progress).brighter();
    }
}
