package zelix.utils.hooks.visual;

import net.minecraft.client.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import org.lwjgl.*;
import java.nio.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public final class GLUtils
{
    private static final Random random;
    private static final Tessellator tessellator;
    public static List<Integer> vbos;
    public static List<Integer> textures;
    
    public static void glScissor(final int[] rect) {
        glScissor(rect[0], rect[1], rect[0] + rect[2], rect[1] + rect[3]);
    }
    
    public static void glScissor(final float x, final float y, final float x1, final float y1) {
        final int scaleFactor = getScaleFactor();
        GL11.glScissor((int)(x * scaleFactor), (int)(Minecraft.getMinecraft().displayHeight - y1 * scaleFactor), (int)((x1 - x) * scaleFactor), (int)((y1 - y) * scaleFactor));
    }
    
    public static int getScaleFactor() {
        int scaleFactor = 1;
        final boolean isUnicode = Minecraft.getMinecraft().isUnicode();
        int guiScale = Minecraft.getMinecraft().gameSettings.guiScale;
        if (guiScale == 0) {
            guiScale = 1000;
        }
        while (scaleFactor < guiScale && Minecraft.getMinecraft().displayWidth / (scaleFactor + 1) >= 320 && Minecraft.getMinecraft().displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        if (isUnicode && scaleFactor % 2 != 0 && scaleFactor != 1) {
            --scaleFactor;
        }
        return scaleFactor;
    }
    
    public static int getMouseX() {
        return Mouse.getX() * getScreenWidth() / Minecraft.getMinecraft().displayWidth;
    }
    
    public static int getMouseY() {
        return getScreenHeight() - Mouse.getY() * getScreenHeight() / Minecraft.getMinecraft().displayWidth - 1;
    }
    
    public static int getScreenWidth() {
        return Minecraft.getMinecraft().displayWidth / getScaleFactor();
    }
    
    public static int getScreenHeight() {
        return Minecraft.getMinecraft().displayHeight / getScaleFactor();
    }
    
    public static boolean isHovered(final int x, final int y, final int width, final int height, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY < y + height;
    }
    
    public static int genVBO() {
        final int id = GL15.glGenBuffers();
        GLUtils.vbos.add(id);
        GL15.glBindBuffer(34962, id);
        return id;
    }
    
    public static int getTexture() {
        final int textureID = GL11.glGenTextures();
        GLUtils.textures.add(textureID);
        return textureID;
    }
    
    public static int applyTexture(final int texId, final File file, final int filter, final int wrap) throws IOException {
        applyTexture(texId, ImageIO.read(file), filter, wrap);
        return texId;
    }
    
    public static int applyTexture(final int texId, final BufferedImage image, final int filter, final int wrap) {
        final int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        final ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                final int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte)(pixel >> 16 & 0xFF));
                buffer.put((byte)(pixel >> 8 & 0xFF));
                buffer.put((byte)(pixel & 0xFF));
                buffer.put((byte)(pixel >> 24 & 0xFF));
            }
        }
        buffer.flip();
        applyTexture(texId, image.getWidth(), image.getHeight(), buffer, filter, wrap);
        return texId;
    }
    
    public static int applyTexture(final int texId, final int width, final int height, final ByteBuffer pixels, final int filter, final int wrap) {
        GL11.glBindTexture(3553, texId);
        GL11.glTexParameteri(3553, 10241, filter);
        GL11.glTexParameteri(3553, 10240, filter);
        GL11.glTexParameteri(3553, 10242, wrap);
        GL11.glTexParameteri(3553, 10243, wrap);
        GL11.glPixelStorei(3317, 1);
        GL11.glTexImage2D(3553, 0, 32856, width, height, 0, 6408, 5121, pixels);
        GL11.glBindTexture(3553, 0);
        return texId;
    }
    
    public static void cleanup() {
        GL15.glBindBuffer(34962, 0);
        GL11.glBindTexture(3553, 0);
        for (final int vbo : GLUtils.vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        for (final int texture : GLUtils.textures) {
            GL11.glDeleteTextures(texture);
        }
    }
    
    public static void drawBorderRect(final float x, final float y, final float x1, final float y1, final float borderSize) {
        drawBorder(borderSize, x, y, x1, y1);
        drawRect(x, y, x1, y1);
    }
    
    public static void drawBorder(final float size, final float x, final float y, final float x1, final float y1) {
        GL11.glLineWidth(size);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        final BufferBuilder vertexBuffer = GLUtils.tessellator.getBuffer();
        vertexBuffer.begin(2, DefaultVertexFormats.POSITION);
        vertexBuffer.pos((double)x, (double)y, 0.0).endVertex();
        vertexBuffer.pos((double)x, (double)y1, 0.0).endVertex();
        vertexBuffer.pos((double)x1, (double)y1, 0.0).endVertex();
        vertexBuffer.pos((double)x1, (double)y, 0.0).endVertex();
        GLUtils.tessellator.draw();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawRect(final float x, final float y, final float w, final float h) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        final BufferBuilder vertexBuffer = GLUtils.tessellator.getBuffer();
        vertexBuffer.begin(7, DefaultVertexFormats.POSITION);
        vertexBuffer.pos((double)x, (double)h, 0.0).endVertex();
        vertexBuffer.pos((double)w, (double)h, 0.0).endVertex();
        vertexBuffer.pos((double)w, (double)y, 0.0).endVertex();
        vertexBuffer.pos((double)x, (double)y, 0.0).endVertex();
        GLUtils.tessellator.draw();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawGradientRect(final int x, final int y, final int w, final int h, final int startColor, final int endColor) {
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(x + w, (double)y, 0.0).color(f2, f3, f4, f).endVertex();
        vertexbuffer.pos((double)x, (double)y, 0.0).color(f2, f3, f4, f).endVertex();
        vertexbuffer.pos((double)x, y + h, 0.0).color(f6, f7, f8, f5).endVertex();
        vertexbuffer.pos(x + w, y + h, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
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
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static void glColor(final float red, final float green, final float blue, final float alpha) {
        GlStateManager.color(red, green, blue, alpha);
    }
    
    public static void glColor(final Color color) {
        GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public static void glColor(final int color) {
        GlStateManager.color((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, (color >> 24 & 0xFF) / 255.0f);
    }
    
    public static Color getHSBColor(final float hue, final float sturation, final float luminance) {
        return Color.getHSBColor(hue, sturation, luminance);
    }
    
    public static Color getRandomColor(final int saturationRandom, final float luminance) {
        final float hue = GLUtils.random.nextFloat();
        final float saturation = (GLUtils.random.nextInt(saturationRandom) + saturationRandom) / saturationRandom + saturationRandom;
        return getHSBColor(hue, saturation, luminance);
    }
    
    public static Color getRandomColor() {
        return getRandomColor(1000, 0.6f);
    }
    
    static {
        random = new Random();
        tessellator = Tessellator.getInstance();
        GLUtils.vbos = new ArrayList<Integer>();
        GLUtils.textures = new ArrayList<Integer>();
    }
}
