package zelix.utils.hooks.visual;

import java.awt.*;

public class ColorUtils
{
    public static Color rainbow() {
        final long offset = 999999999999L;
        final float fade = 1.0f;
        final float hue = (System.nanoTime() + offset) / 1.0E10f % 1.0f;
        final long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        final Color c = new Color((int)color);
        return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
    }
    
    public static int rainbow(final int delay) {
        double rState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rState %= 360.0;
        return Color.getHSBColor((float)(rState / 360.0), 0.6f, 1.0f).getRGB();
    }

    public static int color(final double r, final double g, final double b, final double a) {
        return new Color((int) r, (int) g, (int) b, (int) a).getRGB();
    }

    public static int color(final int r, final int g, final int b, final int a) {
        return new Color(r, g, b, a).getRGB();
    }
    
    public static int color(final float r, final float g, final float b, final float a) {
        return new Color(r, g, b, a).getRGB();
    }
    
    public static int getColor(final int a, final int r, final int g, final int b) {
        return a << 24 | r << 16 | g << 8 | b;
    }
    
    public static int getColor(final int r, final int g, final int b) {
        return 0xFF000000 | r << 16 | g << 8 | b;
    }
    
    public static int reAlpha(final int color, final float alpha) {
        final Color c = new Color(color);
        final float r = 0.003921569f * c.getRed();
        final float g = 0.003921569f * c.getGreen();
        final float b = 0.003921569f * c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }
}
