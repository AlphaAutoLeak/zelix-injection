package zelix.gui.clickguis.N3ro.Utils;

import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import zelix.utils.hooks.visual.*;

public class RenderUtil
{
    public static void rectangleBordered(final double x, final double y, final double x1, final double y1, final double width, final int internalColor, final int borderColor) {
        rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawRoundRect_up(final double xPosition, final double yPosition, final double endX, final double endY, final int radius, final int color) {
        final double width = endX - xPosition;
        final double height = endY - yPosition;
        drawBorderedRect(xPosition + radius, yPosition + radius, xPosition + width - radius, yPosition + height - radius, 0.0f, 0, color);
        drawBorderedRect(xPosition, yPosition + radius, xPosition + radius, yPosition + height, 0.0f, 0, color);
        drawBorderedRect(xPosition + width - radius, yPosition + radius, xPosition + width, yPosition + height - radius, 0.0f, 0, color);
        drawBorderedRect(xPosition + radius, yPosition, xPosition + width - radius, yPosition + radius, 0.0f, color, color);
        drawBorderedRect(xPosition + radius, yPosition + height - radius, xPosition + width, yPosition + height, 0.0f, 0, color);
        drawFilledCircle(xPosition + radius, yPosition + radius, radius, color, 1);
        drawFilledCircle(xPosition + width - radius, yPosition + radius, radius, color, 3);
    }
    
    public static void drawCirCleBorder(final double x, final double y, final double height, final double width, final int radius, final int Color) {
        drawRoundRect_up(x, y, x + height, y + width / 2.0, radius, Color);
        drawRoundRect_down(x, y + width / 2.0, x + height, y + width, radius, Color);
    }
    
    public static void drawRoundRect_down(final double xPosition, final double yPosition, final double endX, final double endY, final int radius, final int color) {
        final double width = endX - xPosition;
        final double height = endY - yPosition;
        drawBorderedRect(xPosition + radius, yPosition + radius, xPosition + width - radius, yPosition + height - radius, 0.0f, 0, color);
        drawBorderedRect(xPosition, yPosition + radius, xPosition + radius, yPosition + height - radius, 0.0f, 0, color);
        drawBorderedRect(xPosition + width - radius, yPosition + radius, xPosition + width, yPosition + height - radius, 0.0f, 0, color);
        drawBorderedRect(xPosition, yPosition, xPosition + width, yPosition + radius, 0.0f, 0, color);
        drawBorderedRect(xPosition + radius, yPosition + height - radius, xPosition + width - radius, yPosition + height, 0.0f, 0, color);
        drawFilledCircle(xPosition + radius, yPosition + height - radius, radius, color, 2);
        drawFilledCircle(xPosition + width - radius, yPosition + height - radius, radius, color, 4);
    }
    
    public static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void drawGradientRect(final float x, final float y, final float x1, final float y1, final int topColor, final int bottomColor) {
        R2DUtils.enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        glColor(topColor);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        glColor(bottomColor);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        R2DUtils.disableGL2D();
    }
    
    public static void rectangle(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            final double var5 = top;
            top = bottom;
            bottom = var5;
        }
        final float var6 = (color >> 24 & 0xFF) / 255.0f;
        final float var7 = (color >> 16 & 0xFF) / 255.0f;
        final float var8 = (color >> 8 & 0xFF) / 255.0f;
        final float var9 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var7, var8, var9, var6);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(left, bottom, 0.0).endVertex();
        bufferBuilder.pos(right, bottom, 0.0).endVertex();
        bufferBuilder.pos(right, top, 0.0).endVertex();
        bufferBuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawBorderedRect(final double x, final double y, final double x2, final double d, final float l1, final int col1, final int col2) {
        drawRect(x, y, x2, d, col2);
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GlStateManager.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, d);
        GL11.glVertex2d(x2, d);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, d);
        GL11.glVertex2d(x2, d);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        Gui.drawRect(0, 0, 0, 0, 0);
    }
    
    public static void drawRect(final double d, final double e, final double f, final double g, final int color) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        color(color);
        GlStateManager.glBegin(7);
        GL11.glVertex2d(f, e);
        GL11.glVertex2d(d, e);
        GL11.glVertex2d(d, g);
        GL11.glVertex2d(f, g);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
        Gui.drawRect(0, 0, 0, 0, 0);
    }
    
    public static void color(final int color) {
        final float f = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f);
    }
    
    public static void drawGradientSideways(final double left, final double top, final double right, final double bottom, final int col1, final int col2) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
        final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
        final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
        final float f8 = (col2 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GlStateManager.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        Gui.drawRect(0, 0, 0, 0, 0);
    }
    
    public static void startGlScissor(final int x, final int y, final int width, int height) {
        final int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        GL11.glPushMatrix();
        GL11.glEnable(3089);
        final int n = x * scaleFactor;
        final int n2 = Minecraft.getMinecraft().displayHeight - (y + height) * scaleFactor;
        final int n3 = width * scaleFactor;
        height += 14;
        GL11.glScissor(n, n2, n3, height * scaleFactor);
    }
    
    public static void drawRoundRect(final double xPosition, final double yPosition, final double endX, final double endY, final int BorderThincess, final int radius, final int color) {
        final double width = endX - xPosition;
        final double height = endY - yPosition;
        drawBorderedRect(xPosition + radius, yPosition + radius, xPosition + width - radius, yPosition + height - radius, BorderThincess, color, color);
        drawBorderedRect(xPosition, yPosition + radius, xPosition + radius, yPosition + height - radius, BorderThincess, color, color);
        drawBorderedRect(xPosition + width - radius, yPosition + radius, xPosition + width, yPosition + height - radius, BorderThincess, color, color);
        drawBorderedRect(xPosition + radius, yPosition, xPosition + width - radius, yPosition + radius, BorderThincess, color, color);
        drawBorderedRect(xPosition + radius, yPosition + height - radius, xPosition + width - radius, yPosition + height, BorderThincess, color, color);
        drawFilledCircle(xPosition + radius, yPosition + radius, radius, color, 1);
        drawFilledCircle(xPosition + radius, yPosition + height - radius, radius, color, 2);
        drawFilledCircle(xPosition + width - radius, yPosition + radius, radius, color, 3);
        drawFilledCircle(xPosition + width - radius, yPosition + height - radius, radius, color, 4);
    }
    
    public static void drawRoundRect(final double xPosition, final double yPosition, final double endX, final double endY, final int radius, final int color) {
        final double width = endX - xPosition;
        final double height = endY - yPosition;
        drawRect(xPosition + radius, yPosition + radius, xPosition + width - radius, yPosition + height - radius, color);
        drawRect(xPosition, yPosition + radius, xPosition + radius, yPosition + height - radius, color);
        drawRect(xPosition + width - radius, yPosition + radius, xPosition + width, yPosition + height - radius, color);
        drawRect(xPosition + radius, yPosition, xPosition + width - radius, yPosition + radius, color);
        drawRect(xPosition + radius, yPosition + height - radius, xPosition + width - radius, yPosition + height, color);
        drawFilledCircle(xPosition + radius, yPosition + radius, radius, color, 1);
        drawFilledCircle(xPosition + radius, yPosition + height - radius, radius, color, 2);
        drawFilledCircle(xPosition + width - radius, yPosition + radius, radius, color, 3);
        drawFilledCircle(xPosition + width - radius, yPosition + height - radius, radius, color, 4);
    }
    
    public static void drawFilledCircle(final double x, final double y, final double r, final int c, final int id) {
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glColor4f(f2, f3, f4, f);
        GlStateManager.glBegin(9);
        if (id == 1) {
            GL11.glVertex2d(x, y);
            for (int i = 0; i <= 90; ++i) {
                final double x2 = Math.sin(i * 3.141526 / 180.0) * r;
                final double y2 = Math.cos(i * 3.141526 / 180.0) * r;
                GL11.glVertex2d(x - x2, y - y2);
            }
        }
        else if (id == 2) {
            GL11.glVertex2d(x, y);
            for (int i = 90; i <= 180; ++i) {
                final double x2 = Math.sin(i * 3.141526 / 180.0) * r;
                final double y2 = Math.cos(i * 3.141526 / 180.0) * r;
                GL11.glVertex2d(x - x2, y - y2);
            }
        }
        else if (id == 3) {
            GL11.glVertex2d(x, y);
            for (int i = 270; i <= 360; ++i) {
                final double x2 = Math.sin(i * 3.141526 / 180.0) * r;
                final double y2 = Math.cos(i * 3.141526 / 180.0) * r;
                GL11.glVertex2d(x - x2, y - y2);
            }
        }
        else if (id == 4) {
            GL11.glVertex2d(x, y);
            for (int i = 180; i <= 270; ++i) {
                final double x2 = Math.sin(i * 3.141526 / 180.0) * r;
                final double y2 = Math.cos(i * 3.141526 / 180.0) * r;
                GL11.glVertex2d(x - x2, y - y2);
            }
        }
        else {
            for (int i = 0; i <= 360; ++i) {
                final double x2 = Math.sin(i * 3.141526 / 180.0) * r;
                final double y2 = Math.cos(i * 3.141526 / 180.0) * r;
                GL11.glVertex2f((float)(x - x2), (float)(y - y2));
            }
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static void stopGlScissor() {
        GL11.glDisable(3089);
        GL11.glPopMatrix();
    }
    
    public static void drawRoundRectShadow(final double xPosition, final double yPosition, final double endX, final double endY, final int BorderThincess, final int radius, final int color) {
        final double width = endX - xPosition;
        final double height = endY - yPosition;
        drawBorderedRect(xPosition + radius, yPosition + radius, xPosition + width - radius, yPosition + height - radius, BorderThincess, color, color);
        drawBorderedRect(xPosition, yPosition + radius, xPosition + radius, yPosition + height - radius, BorderThincess, color, color);
        drawBorderedRect(xPosition + width - radius, yPosition + radius, xPosition + width, yPosition + height - radius, BorderThincess, color, color);
        drawBorderedRect(xPosition + radius, yPosition, xPosition + width - radius, yPosition + radius, BorderThincess, color, color);
        drawBorderedRect(xPosition + radius, yPosition + height - radius, xPosition + width - radius, yPosition + height, BorderThincess, color, color);
        drawFilledCircle(xPosition + radius, yPosition + radius, radius, color, 1);
        drawFilledCircle(xPosition + radius, yPosition + height - radius, radius, color, 2);
        drawFilledCircle(xPosition + width - radius, yPosition + radius, radius, color, 3);
        drawFilledCircle(xPosition + width - radius, yPosition + height - radius, radius, color, 4);
        RenderUtils.drawBorderedRect(xPosition + radius - 20.0, yPosition + radius - 20.0, xPosition + width - radius + 20.0, yPosition + height - radius + 20.0, BorderThincess, 1058872607, 1058872607);
        RenderUtils.drawBorderedRect(xPosition - 20.0, yPosition + radius - 20.0, xPosition + radius + 20.0, yPosition + height - radius + 20.0, BorderThincess, 1058872607, 1058872607);
        RenderUtils.drawBorderedRect(xPosition + width - radius - 20.0, yPosition + radius - 20.0, xPosition + width + 20.0, yPosition + height - radius + 20.0, BorderThincess, 1058872607, 1058872607);
        RenderUtils.drawBorderedRect(xPosition + radius - 20.0, yPosition - 20.0, xPosition + width - radius + 20.0, yPosition + radius + 20.0, BorderThincess, 1058872607, 1058872607);
        RenderUtils.drawBorderedRect(xPosition + radius - 20.0, yPosition + height - radius - 20.0, xPosition + width - radius + 20.0, yPosition + height + 20.0, BorderThincess, 1058872607, 1058872607);
        RenderUtils.drawFilledCircle(xPosition + radius - 20.0, yPosition + radius, radius - 20, 1058872607, 1);
        RenderUtils.drawFilledCircle(xPosition + radius - 20.0, yPosition + height - radius - 20.0, radius, 1058872607, 2);
        RenderUtils.drawFilledCircle(xPosition + width - radius - 20.0, yPosition + radius - 20.0, radius, 1058872607, 3);
        RenderUtils.drawFilledCircle(xPosition + width - radius - 20.0, yPosition + height - radius - 20.0, radius, 1058872607, 4);
    }
    
    public static void drawGradientSidewaysV(final double left, final double top, final double right, final double bottom, final int col1, final int col2) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
        final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
        final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
        final float f8 = (col2 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GlStateManager.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(left, bottom);
        GL11.glVertex2d(right, bottom);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(right, top);
        GL11.glVertex2d(left, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        Gui.drawRect(0, 0, 0, 0, 0);
    }
    
    public static class R2DUtils
    {
        public static void enableGL2D() {
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glDepthMask(true);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glHint(3155, 4354);
        }
        
        public static void disableGL2D() {
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glHint(3154, 4352);
            GL11.glHint(3155, 4352);
        }
        
        public static void drawRoundedRect(float x, float y, float x1, float y1, final int borderC, final int insideC) {
            enableGL2D();
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
            drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
            drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
            drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
            drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
            drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
            drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
            drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
            drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
            GL11.glScalef(2.0f, 2.0f, 2.0f);
            disableGL2D();
            Gui.drawRect(0, 0, 0, 0, 0);
        }
        
        public static void drawRect(final double x2, final double y2, final double x1, final double y1, final int color) {
            enableGL2D();
            glColor(color, color, color, color);
            drawRect(x2, y2, x1, y1);
            disableGL2D();
        }
        
        private static void drawRect(final double x2, final double y2, final double x1, final double y1) {
            GL11.glBegin(7);
            GL11.glVertex2d(x2, y1);
            GL11.glVertex2d(x1, y1);
            GL11.glVertex2d(x1, y2);
            GL11.glVertex2d(x2, y2);
            GL11.glEnd();
        }
        
        public static void drawHLine(float x, float y, final float x1, final int y1) {
            if (y < x) {
                final float var5 = x;
                x = y;
                y = var5;
            }
            drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
        }
        
        public static void drawVLine(final float x, float y, float x1, final int y1) {
            if (x1 < y) {
                final float var5 = y;
                y = x1;
                x1 = var5;
            }
            drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
        }
        
        public static void drawHLine(float x, float y, final float x1, final int y1, final int y2) {
            if (y < x) {
                final float var5 = x;
                x = y;
                y = var5;
            }
            drawGradientRect(x, x1, y + 1.0f, x1 + 1.0f, y1, y2);
        }
        
        public static void drawGradientRect(final float x, final float y, final float x1, final float y1, final int topColor, final int bottomColor) {
            enableGL2D();
            GL11.glShadeModel(7425);
            GL11.glBegin(7);
            RenderUtil.glColor(topColor);
            GL11.glVertex2f(x, y1);
            GL11.glVertex2f(x1, y1);
            RenderUtil.glColor(bottomColor);
            GL11.glVertex2f(x1, y);
            GL11.glVertex2f(x, y);
            GL11.glEnd();
            GL11.glShadeModel(7424);
            disableGL2D();
        }
        
        public static void glColor(final float alpha, final int redRGB, final int greenRGB, final int blueRGB) {
            final float red = 0.003921569f * redRGB;
            final float green = 0.003921569f * greenRGB;
            final float blue = 0.003921569f * blueRGB;
            GL11.glColor4f(red, green, blue, alpha);
        }
    }
}
