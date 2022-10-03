package zelix.utils.tenacityutils.render;

import zelix.utils.ManagerUtil.*;
import net.minecraft.client.shader.*;
import org.lwjgl.opengl.*;
import zelix.utils.ManagerUtil.render.*;
import net.minecraft.client.renderer.*;
import java.awt.*;
import net.minecraft.client.gui.*;

public class RenderUtil implements Utils
{
    public static Framebuffer createFrameBuffer(final Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.framebufferWidth != RenderUtil.mc.displayWidth || framebuffer.framebufferHeight != RenderUtil.mc.displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(RenderUtil.mc.displayWidth, RenderUtil.mc.displayHeight, true);
        }
        return framebuffer;
    }

    public static void drawBorderedRect(float x, float y, float width, float height, float outlineThickness, int rectColor, int outlineColor) {
        RenderUtil.drawRect2(x, y, width, height, rectColor);
        GL11.glEnable((int)2848);
        GLUtil.setup2DRendering(() -> {
            RenderUtil.color(outlineColor);
            GL11.glLineWidth((float)outlineThickness);
            float cornerValue = (float)((double)outlineThickness * 0.19);
            GLUtil.render((int)1, () -> {
                GL11.glVertex2d((double)x, (double)(y - cornerValue));
                GL11.glVertex2d((double)x, (double)(y + height + cornerValue));
                GL11.glVertex2d((double)(x + width), (double)(y + height + cornerValue));
                GL11.glVertex2d((double)(x + width), (double)(y - cornerValue));
                GL11.glVertex2d((double)x, (double)y);
                GL11.glVertex2d((double)(x + width), (double)y);
                GL11.glVertex2d((double)x, (double)(y + height));
                GL11.glVertex2d((double)(x + width), (double)(y + height));
            });
        });
        GL11.glDisable((int)2848);
    }
    
    public static void renderRoundedRect(final float x, final float y, final float width, final float height, final float radius, final int color) {
        drawGoodCircle(x + radius, y + radius, radius, color);
        drawGoodCircle(x + width - radius, y + radius, radius, color);
        drawGoodCircle(x + radius, y + height - radius, radius, color);
        drawGoodCircle(x + width - radius, y + height - radius, radius, color);
        drawRect2(x + radius, y, width - radius * 2.0f, height, color);
        drawRect2(x, y + radius, width, height - radius * 2.0f, color);
    }
    
    public static void renderRoundedRect(final float x, final float y, final float width, final float height, final float radius, final int c, final boolean dropShadow) {
        int color = c;
        if (dropShadow) {
            color = ((color & 0xFCFCFC) >> 2 | (color & 0xFF000000));
        }
        drawGoodCircle(x + radius, y + radius, radius, color);
        drawGoodCircle(x + width - radius, y + radius, radius, color);
        drawGoodCircle(x + radius, y + height - radius, radius, color);
        drawGoodCircle(x + width - radius, y + height - radius, radius, color);
        drawRect2(x + radius, y, width - radius * 2.0f, height, color);
        drawRect2(x, y + radius, width, height - radius * 2.0f, color);
    }
    
    public static void drawRect2(final double x, final double y, final double width, final double height, final int color) {
        resetColor();
        GLUtil.setup2DRendering(() -> GLUtil.render(7, () -> {
            color(color);
            GL11.glVertex2d(x, y);
            GL11.glVertex2d(x, y + height);
            GL11.glVertex2d(x + width, y + height);
            GL11.glVertex2d(x + width, y);
        }));
    }
    
    public static void scale(final float x, final float y, final float scale, final Runnable data) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0f);
        GL11.glScalef(scale, scale, 1.0f);
        GL11.glTranslatef(-x, -y, 0.0f);
        data.run();
        GL11.glPopMatrix();
    }
    
    public static void scaleStart(final float x, final float y, final float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0.0f);
        GlStateManager.scale(scale, scale, 1.0f);
        GlStateManager.translate(-x, -y, 0.0f);
    }
    
    public static void scaleEnd() {
        GlStateManager.popMatrix();
    }
    
    public static void drawGoodCircle(final double x, final double y, final float radius, final int color) {
        color(color);
        GLUtil.setup2DRendering(() -> {
            GL11.glEnable(2832);
            GL11.glHint(3153, 4354);
            GL11.glPointSize(radius * (2 * RenderUtil.mc.gameSettings.guiScale));
            GLUtil.render(0, () -> GL11.glVertex2d(x, y));
        });
    }
    
//    public static void fakeCircleGlow(final float posX, final float posY, final float radius, final Color color, final float maxAlpha) {
//        setAlphaLimit(0.0f);
//        GL11.glShadeModel(7425);
//        int i;
//        double angle;
//        double x2;
//        double y2;
//        GLUtil.setup2DRendering(() -> GLUtil.render(6, () -> {
//            color(color.getRGB(), maxAlpha);
//            GL11.glVertex2d((double)posX, (double)posY);
//            color(color.getRGB(), 0.0f);
//            for (i = 0; i <= 100; ++i) {
//                angle = i * 0.06283 + 3.1415;
//                x2 = Math.sin(angle) * radius;
//                y2 = Math.cos(angle) * radius;
//                GL11.glVertex2d(posX + x2, posY + y2);
//            }
//        }));
//        GL11.glShadeModel(7424);
//        setAlphaLimit(1.0f);
//    }
    
    public static double animate(final double endPoint, final double current, double speed) {
        final boolean shouldContinueAnimation = endPoint > current;
        if (speed < 0.0) {
            speed = 0.0;
        }
        else if (speed > 1.0) {
            speed = 1.0;
        }
        final double dif = Math.max(endPoint, current) - Math.min(endPoint, current);
        final double factor = dif * speed;
        return current + (shouldContinueAnimation ? factor : (-factor));
    }
    
    public static void drawCircleNotSmooth(final double x, final double y, double radius, final int color) {
        radius /= 2.0;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        color(color);
        GL11.glBegin(6);
        for (double i = 0.0; i <= 360.0; ++i) {
            final double angle = i * 0.01745;
            GL11.glVertex2d(x + radius * Math.cos(angle) + radius, y + radius * Math.sin(angle) + radius);
        }
        GL11.glEnd();
        GL11.glEnable(2884);
        GL11.glEnable(3553);
    }
    
    public static void scissor(final double x, final double y, final double width, final double height, final Runnable data) {
        GL11.glEnable(3089);
        scissor(x, y, width, height);
        data.run();
        GL11.glDisable(3089);
    }
    
    public static void scissor(final double x, final double y, final double width, final double height) {
        final ScaledResolution sr = new ScaledResolution(RenderUtil.mc);
        final double scale = sr.getScaleFactor();
        final double finalHeight = height * scale;
        final double finalY = (sr.getScaledHeight() - y) * scale;
        final double finalX = x * scale;
        final double finalWidth = width * scale;
        GL11.glScissor((int)finalX, (int)(finalY - finalHeight), (int)finalWidth, (int)finalHeight);
    }
    
    public static void setAlphaLimit(final float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, (float)(limit * 0.01));
    }
    
    public static void color(final int color, final float alpha) {
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        GlStateManager.color(r, g, b, alpha);
    }
    
    public static void color(final int color) {
        color(color, (color >> 24 & 0xFF) / 255.0f);
    }
    
    public static void bindTexture(final int texture) {
        GL11.glBindTexture(3553, texture);
    }
    
    public static void resetColor() {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static boolean isHovered(final float mouseX, final float mouseY, final float x, final float y, final float width, final float height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
}
