package zelix.hack.hacks.skinchanger.resources;

import net.minecraft.client.renderer.*;
import java.awt.image.*;
import java.awt.*;

public class CapeBuffer implements IImageBuffer
{
    public BufferedImage parseUserSkin(final BufferedImage img) {
        int imageWidth = 64;
        int imageHeight = 32;
        for (int srcWidth = img.getWidth(), srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}
        final BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
        final Graphics g = imgNew.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return imgNew;
    }
    
    public void skinAvailable() {
    }
}
