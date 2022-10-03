package zelix.gui.clickguis.caesium.util;

import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import java.awt.*;
import net.minecraft.client.gui.*;

public final class RenderUtil
{
    public static void drawRect(final float x, final float y, final float width, final float height, final int color) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        glColor(color);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }
    
    public static void relativeRect(final float left, final float top, final float right, final float bottom, final int color) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder worldRenderer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glColor(color);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos((double)left, (double)bottom, 0.0).endVertex();
        worldRenderer.pos((double)right, (double)bottom, 0.0).endVertex();
        worldRenderer.pos((double)right, (double)top, 0.0).endVertex();
        worldRenderer.pos((double)left, (double)top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawFilledCircle(final float xx, final float yy, final float radius, final Color color) {
        final int sections = 1920;
        final double dAngle = 6.283185307179586 / sections;
        GL11.glPushAttrib(8192);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);
        for (int i = 0; i < sections; ++i) {
            final float x = (float)(radius * Math.sin(i * dAngle));
            final float y = (float)(radius * Math.cos(i * dAngle));
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
            GL11.glVertex2f(xx + x, yy + y);
        }
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glPopAttrib();
    }
    
    public static void drawHorizontalGradient(final float x, final float y, final float width, final float height, final int leftColor, final int rightColor) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        glColor(leftColor);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)(y + height));
        glColor(rightColor);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }
    
    public static void drawVerticalGradient(final float x, final float y, final float width, final float height, final int topColor, final int bottomColor) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        glColor(topColor);
        GL11.glVertex2f(x, y + height);
        GL11.glVertex2f(x + width, y + height);
        glColor(bottomColor);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }
    
    public static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void drawRect(final int left, final int top, final int right, final int bottom, final int color) {
        Gui.drawRect(left, top, right, bottom, color);
    }
    
    public static void drawHorizontalLine(int startX, int endX, final int y, final int color) {
        if (endX < startX) {
            final int i = startX;
            startX = endX;
            endX = i;
        }
        drawRect(startX, y, endX + 1, y + 1, color);
    }
    
    public static void drawVerticalLine(final int x, int startY, int endY, final int color) {
        if (endY < startY) {
            final int i = startY;
            startY = endY;
            endY = i;
        }
        drawRect(x, startY + 1, x + 1, endY, color);
    }
    
    public static boolean isHovered(final int x, final int y, final int width, final int height, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY < y + height;
    }
}
