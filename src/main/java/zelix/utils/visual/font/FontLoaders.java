package zelix.utils.visual.font;

import java.awt.*;
import java.io.*;

public abstract class FontLoaders
{
    public static CFontRenderer kiona12;
    public static CFontRenderer kiona14;
    public static CFontRenderer kiona16;
    public static CFontRenderer kiona18;
    public static CFontRenderer kiona20;
    public static CFontRenderer kiona22;
    public static CFontRenderer kiona24;
    public static CFontRenderer kiona26;
    public static CFontRenderer kiona28;
    public static CFontRenderer kiona30;
    public static CFontRenderer kiona32;
    public static CFontRenderer kiona34;
    public static CFontRenderer kiona36;
    public static CFontRenderer default16;
    public static CFontRenderer default18;
    public static CFontRenderer default14;
    public static CFontRenderer default30;
    public static CFontRenderer SFB6;
    public static CFontRenderer default20;
    public static CFontRenderer SFB8;
    public static CFontRenderer SFB9;
    public static CFontRenderer SFB11;
    public static CFontRenderer icon18;
    public static CFontRenderer icon24;
    public static CFontRenderer CN18;
    
    private static Font getKiona(final int size) {
        Font font = new Font("default", 0, size);
        try {
            final File a = new File("C:\\Thanatos\\font.ttf");
            font = Font.createFont(0, a);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {}
        return font;
    }
    
    public static Font getCN(final int size) {
        Font font = new Font("default", 0, size);
        try {
            final File a = new File("C:\\Zelix\\Chinese.ttf");
            font = Font.createFont(0, a);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            font = new Font("default", 0, size);
        }
        return font;
    }
    
    private static Font getSFB(final int size) {
        Font font = new Font("default", 0, size);
        try {
            final File a = new File("C:\\Thanatos\\SF-UI-Display-Bold.otf");
            font = Font.createFont(0, a);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            font = new Font("default", 0, size);
        }
        return font;
    }
    
    private static Font getIcon(final int size) {
        Font font = new Font("default", 0, size);
        try {
            final File a = new File("C:\\Thanatos\\icon.ttf");
            font = Font.createFont(0, a);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            font = new Font("default", 0, size);
        }
        return font;
    }
    
    static {
        FontLoaders.kiona12 = new CFontRenderer(getKiona(12), true, true);
        FontLoaders.kiona14 = new CFontRenderer(getKiona(14), true, true);
        FontLoaders.kiona16 = new CFontRenderer(getKiona(16), true, true);
        FontLoaders.kiona18 = new CFontRenderer(getKiona(18), true, true);
        FontLoaders.kiona20 = new CFontRenderer(getKiona(20), true, true);
        FontLoaders.kiona22 = new CFontRenderer(getKiona(22), true, true);
        FontLoaders.kiona24 = new CFontRenderer(getKiona(24), true, true);
        FontLoaders.kiona26 = new CFontRenderer(getKiona(26), true, true);
        FontLoaders.kiona28 = new CFontRenderer(getKiona(28), true, true);
        FontLoaders.kiona30 = new CFontRenderer(getKiona(30), true, true);
        FontLoaders.kiona32 = new CFontRenderer(getKiona(32), true, true);
        FontLoaders.kiona34 = new CFontRenderer(getKiona(34), true, true);
        FontLoaders.kiona36 = new CFontRenderer(getKiona(36), true, true);
        FontLoaders.default16 = new CFontRenderer(new Font("default", 0, 16), true, true);
        FontLoaders.default18 = new CFontRenderer(new Font("default", 0, 18), true, true);
        FontLoaders.default14 = new CFontRenderer(new Font("default", 0, 14), true, true);
        FontLoaders.default30 = new CFontRenderer(new Font("default", 0, 30), true, true);
        FontLoaders.SFB6 = new CFontRenderer(getSFB(6), true, true);
        FontLoaders.default20 = new CFontRenderer(new Font("default", 0, 20), true, true);
        FontLoaders.SFB8 = new CFontRenderer(getSFB(8), true, true);
        FontLoaders.SFB9 = new CFontRenderer(getSFB(9), true, true);
        FontLoaders.SFB11 = new CFontRenderer(getSFB(11), true, true);
        FontLoaders.icon18 = new CFontRenderer(getIcon(18), true, true);
        FontLoaders.icon24 = new CFontRenderer(getIcon(24), true, true);
        FontLoaders.CN18 = new CFontRenderer(getCN(18), true, true);
    }
}
