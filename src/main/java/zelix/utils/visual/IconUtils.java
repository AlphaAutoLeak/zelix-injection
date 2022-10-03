package zelix.utils.visual;

import net.minecraftforge.fml.relauncher.*;
import java.nio.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

@SideOnly(Side.CLIENT)
public final class IconUtils
{
    public static ByteBuffer[] getFavicon() {
        try {
            return new ByteBuffer[] { readImageToBuffer(null) };
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static ByteBuffer readImageToBuffer(final InputStream imageStream) throws IOException {
        if (imageStream == null) {
            return null;
        }
        final BufferedImage bufferedImage = ImageIO.read(imageStream);
        final int[] rgb = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        final ByteBuffer byteBuffer = ByteBuffer.allocate(4 * rgb.length);
        for (final int i : rgb) {
            byteBuffer.putInt(i << 8 | (i >> 24 & 0xFF));
        }
        byteBuffer.flip();
        return byteBuffer;
    }
}
