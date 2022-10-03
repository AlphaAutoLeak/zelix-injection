package zelix.hack.hacks.automap;

import zelix.hack.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class AutoMap extends Hack
{
    public AutoMap() {
        super("AutoMap", HackCategory.ANOTHER);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    public static void main(final String[] args) {
        getImagePixel("C:\\apple.png");
    }
    
    public static void getImagePixel(final String image) {
        final int[] rgb = new int[3];
        final File file = new File(image);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        final int width = bi.getWidth();
        final int height = bi.getHeight();
        final int minx = bi.getMinX();
        final int miny = bi.getMinY();
        for (int i = minx; i < width; ++i) {
            for (int j = miny; j < height; ++j) {
                final int pixel = bi.getRGB(i, j);
                rgb[0] = (pixel & 0xFF0000) >> 16;
                rgb[1] = (pixel & 0xFF00) >> 8;
                rgb[2] = (pixel & 0xFF);
            }
        }
    }
    
    public static void getBlockID(final int r, final int g, final int b) {
    }
}
