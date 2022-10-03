package zelix.utils.ManagerUtil.render;

import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;

public class GLUtil
{
    public static void render(final int mode, final Runnable render) {
        GL11.glBegin(mode);
        render.run();
        GL11.glEnd();
    }
    
    public static void setup2DRendering(final Runnable f) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        f.run();
        GL11.glEnable(3553);
        GlStateManager.disableBlend();
    }
    
    public static void rotate(final float x, final float y, final float rotate, final Runnable f) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0.0f);
        GlStateManager.rotate(rotate, 0.0f, 0.0f, -1.0f);
        GlStateManager.translate(-x, -y, 0.0f);
        f.run();
        GlStateManager.popMatrix();
    }
}
