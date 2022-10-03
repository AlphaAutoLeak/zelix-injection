package zelix.gui.clickguis.Astolfo.ClickGui;

import net.minecraft.client.*;
import org.lwjgl.opengl.*;

public class Limitation
{
    public int startX;
    public int startY;
    public int endX;
    public int endY;
    
    public Limitation(final int x1, final int y1, final int x2, final int y2) {
        this.startX = x1;
        this.startY = y1;
        this.endX = x2;
        this.endY = y2;
    }
    
    public void cut() {
        doGlScissor(this.startX, this.startY, this.endX - this.startX, this.endY - this.startY);
    }
    
    public static void doGlScissor(final int x, final int y, final int width, final int height) {
        final Minecraft mc = Minecraft.getMinecraft();
        int scaleFactor = 1;
        int k = mc.gameSettings.guiScale;
        if (k == 0) {
            k = 1000;
        }
        while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        GL11.glScissor(x * scaleFactor, mc.displayHeight - (y + height) * scaleFactor, width * scaleFactor, height * scaleFactor);
    }
}
