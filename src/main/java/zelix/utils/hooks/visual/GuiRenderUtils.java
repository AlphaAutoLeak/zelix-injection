package zelix.utils.hooks.visual;

import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import zelix.gui.clickguis.N3ro.Utils.*;

public class GuiRenderUtils
{
    public static void drawRect(final float x, final float y, final float width, final float height, final Color color) {
        drawRect(x, y, width, height, color.getRGB());
    }
    
    public static void drawRect(final float x, final float y, final float width, final float height, final int color) {
        enableRender2D();
        setColor(color);
        GL11.glBegin(7);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glEnd();
        disableRender2D();
    }
    
    public static void enableRender2D() {
        GL11.glEnable(3042);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.0f);
    }
    
    public static void disableRender2D() {
        GL11.glDisable(3042);
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }
    
    public static void setColor(final int colorHex) {
        final float alpha = (colorHex >> 24 & 0xFF) / 255.0f;
        final float red = (colorHex >> 16 & 0xFF) / 255.0f;
        final float green = (colorHex >> 8 & 0xFF) / 255.0f;
        final float blue = (colorHex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void drawRoundedRect(final float x, final float y, final float width, final float height, float edgeRadius, int color, final float borderWidth, int borderColor) {
        if (color == 16777215) {
            color = Colors.WHITE.c;
        }
        if (borderColor == 16777215) {
            borderColor = Colors.WHITE.c;
        }
        if (edgeRadius < 0.0f) {
            edgeRadius = 0.0f;
        }
        if (edgeRadius > width / 2.0f) {
            edgeRadius = width / 2.0f;
        }
        if (edgeRadius > height / 2.0f) {
            edgeRadius = height / 2.0f;
        }
        drawRect(x + edgeRadius, y + edgeRadius, width - edgeRadius * 2.0f, height - edgeRadius * 2.0f, color);
        drawRect(x + edgeRadius, y, width - edgeRadius * 2.0f, edgeRadius, color);
        drawRect(x + edgeRadius, y + height - edgeRadius, width - edgeRadius * 2.0f, edgeRadius, color);
        drawRect(x, y + edgeRadius, edgeRadius, height - edgeRadius * 2.0f, color);
        drawRect(x + width - edgeRadius, y + edgeRadius, edgeRadius, height - edgeRadius * 2.0f, color);
        enableRender2D();
        RenderUtil.color(color);
        GL11.glBegin(6);
        float centerX = x + edgeRadius;
        float centerY = y + edgeRadius;
        GL11.glVertex2d((double)centerX, (double)centerY);
        for (int vertices = (int)Math.min(Math.max(edgeRadius, 10.0f), 90.0f), i = 0; i < vertices + 1; ++i) {
            final double angleRadians = 6.283185307179586 * (i + 180) / (vertices * 4);
            GL11.glVertex2d(centerX + Math.sin(angleRadians) * edgeRadius, centerY + Math.cos(angleRadians) * edgeRadius);
        }
        GL11.glEnd();
        GL11.glBegin(6);
        centerX = x + width - edgeRadius;
        centerY = y + edgeRadius;
        GL11.glVertex2d((double)centerX, (double)centerY);
        for (int vertices = (int)Math.min(Math.max(edgeRadius, 10.0f), 90.0f), i = 0; i < vertices + 1; ++i) {
            final double angleRadians = 6.283185307179586 * (i + 90) / (vertices * 4);
            GL11.glVertex2d(centerX + Math.sin(angleRadians) * edgeRadius, centerY + Math.cos(angleRadians) * edgeRadius);
        }
        GL11.glEnd();
        GL11.glBegin(6);
        centerX = x + edgeRadius;
        centerY = y + height - edgeRadius;
        GL11.glVertex2d((double)centerX, (double)centerY);
        for (int vertices = (int)Math.min(Math.max(edgeRadius, 10.0f), 90.0f), i = 0; i < vertices + 1; ++i) {
            final double angleRadians = 6.283185307179586 * (i + 270) / (vertices * 4);
            GL11.glVertex2d(centerX + Math.sin(angleRadians) * edgeRadius, centerY + Math.cos(angleRadians) * edgeRadius);
        }
        GL11.glEnd();
        GL11.glBegin(6);
        centerX = x + width - edgeRadius;
        centerY = y + height - edgeRadius;
        GL11.glVertex2d((double)centerX, (double)centerY);
        for (int vertices = (int)Math.min(Math.max(edgeRadius, 10.0f), 90.0f), i = 0; i < vertices + 1; ++i) {
            final double angleRadians = 6.283185307179586 * i / (vertices * 4);
            GL11.glVertex2d(centerX + Math.sin(angleRadians) * edgeRadius, centerY + Math.cos(angleRadians) * edgeRadius);
        }
        GL11.glEnd();
        RenderUtil.color(borderColor);
        GL11.glLineWidth(borderWidth);
        GL11.glBegin(3);
        centerX = x + edgeRadius;
        centerY = y + edgeRadius;
        int vertices;
        int i;
        for (vertices = (i = (int)Math.min(Math.max(edgeRadius, 10.0f), 90.0f)); i >= 0; --i) {
            final double angleRadians = 6.283185307179586 * (i + 180) / (vertices * 4);
            GL11.glVertex2d(centerX + Math.sin(angleRadians) * edgeRadius, centerY + Math.cos(angleRadians) * edgeRadius);
        }
        GL11.glVertex2d((double)(x + edgeRadius), (double)y);
        GL11.glVertex2d((double)(x + width - edgeRadius), (double)y);
        centerX = x + width - edgeRadius;
        centerY = y + edgeRadius;
        for (i = vertices; i >= 0; --i) {
            final double angleRadians = 6.283185307179586 * (i + 90) / (vertices * 4);
            GL11.glVertex2d(centerX + Math.sin(angleRadians) * edgeRadius, centerY + Math.cos(angleRadians) * edgeRadius);
        }
        GL11.glVertex2d((double)(x + width), (double)(y + edgeRadius));
        GL11.glVertex2d((double)(x + width), (double)(y + height - edgeRadius));
        centerX = x + width - edgeRadius;
        centerY = y + height - edgeRadius;
        for (i = vertices; i >= 0; --i) {
            final double angleRadians = 6.283185307179586 * i / (vertices * 4);
            GL11.glVertex2d(centerX + Math.sin(angleRadians) * edgeRadius, centerY + Math.cos(angleRadians) * edgeRadius);
        }
        GL11.glVertex2d((double)(x + width - edgeRadius), (double)(y + height));
        GL11.glVertex2d((double)(x + edgeRadius), (double)(y + height));
        centerX = x + edgeRadius;
        centerY = y + height - edgeRadius;
        for (i = vertices; i >= 0; --i) {
            final double angleRadians = 6.283185307179586 * (i + 270) / (vertices * 4);
            GL11.glVertex2d(centerX + Math.sin(angleRadians) * edgeRadius, centerY + Math.cos(angleRadians) * edgeRadius);
        }
        GL11.glVertex2d((double)x, (double)(y + height - edgeRadius));
        GL11.glVertex2d((double)x, (double)(y + edgeRadius));
        GL11.glEnd();
        disableRender2D();
    }
}
