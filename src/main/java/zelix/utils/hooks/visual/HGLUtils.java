package zelix.utils.hooks.visual;

import net.minecraft.client.*;
import net.minecraft.client.renderer.texture.*;
import java.nio.*;
import net.minecraft.client.renderer.*;
import net.minecraft.network.*;
import zelix.utils.*;
import java.net.*;
import zelix.utils.math.*;
import org.lwjgl.util.glu.*;
import org.lwjgl.opengl.*;
import java.io.*;
import org.lwjgl.*;
import java.util.*;

public class HGLUtils
{
    private static Minecraft mc;
    public static TextureManager renderEngine;
    public static final FloatBuffer MODELVIEW;
    public static final FloatBuffer PROJECTION;
    public static final IntBuffer VIEWPORT;
    public static final FloatBuffer TO_SCREEN_BUFFER;
    private static Map<Integer, Boolean> glCapMap;
    public static final FloatBuffer TO_WORLD_BUFFER;
    private static final Sphere sphere;
    static Sphere s;
    protected float zLevel;
    
    public static void drawRoundedRect2(float x, float y, float x2, float y2, final float round, final int color) {
        x += (float)(round / 2.0f + 0.5);
        y += (float)(round / 2.0f + 0.5);
        x2 -= (float)(round / 2.0f + 0.5);
        y2 -= (float)(round / 2.0f + 0.5);
        drawRect(x, y, x2, y2, color);
        circle(x2 - round / 2.0f, y + round / 2.0f, round, color);
        circle(x + round / 2.0f, y2 - round / 2.0f, round, color);
        circle(x + round / 2.0f, y + round / 2.0f, round, color);
        circle(x2 - round / 2.0f, y2 - round / 2.0f, round, color);
        drawRect(x - round / 2.0f - 0.5f, y + round / 2.0f, x2, y2 - round / 2.0f, color);
        drawRect(x, y + round / 2.0f, x2 + round / 2.0f + 0.5f, y2 - round / 2.0f, color);
        drawRect(x + round / 2.0f, y - round / 2.0f - 0.5f, x2 - round / 2.0f, y2 - round / 2.0f, color);
        drawRect(x + round / 2.0f, y, x2 - round / 2.0f, y2 + round / 2.0f + 0.5f, color);
    }
    
    public static void circle(final float x, final float y, final float radius, final int fill) {
        GL11.glEnable(3042);
        arc(x, y, 0.0f, 360.0f, radius, fill);
        GL11.glDisable(3042);
    }
    
    public static void arc(final float x, final float y, final float start, final float end, final float radius, final int color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }
    
    public static void arcEllipse(final float x, final float y, float start, float end, final float w, final float h, final int color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        float temp = 0.0f;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        final Tessellator var15 = Tessellator.getInstance();
        final BufferBuilder var16 = var15.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var12, var13, var14, var11);
        if (var11 > 0.5f) {
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (float i = end; i >= start; i -= 4.0f) {
                final float ldx = (float)Math.cos(i * 3.141592653589793 / 180.0) * (w * 1.001f);
                final float ldy = (float)Math.sin(i * 3.141592653589793 / 180.0) * (h * 1.001f);
                GL11.glVertex2f(x + ldx, y + ldy);
            }
            GL11.glEnd();
            GL11.glDisable(2848);
        }
        GL11.glBegin(6);
        for (float i = end; i >= start; i -= 4.0f) {
            final float ldx = (float)Math.cos(i * 3.141592653589793 / 180.0) * w;
            final float ldy = (float)Math.sin(i * 3.141592653589793 / 180.0) * h;
            GL11.glVertex2f(x + ldx, y + ldy);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void glColor(final int hex) {
        final float[] color = getColor(hex);
        GlStateManager.color(color[0], color[1], color[2], color[3]);
    }
    
    public static float[] getColor(final int hex) {
        return new float[] { (hex >> 16 & 0xFF) / 255.0f, (hex >> 8 & 0xFF) / 255.0f, (hex & 0xFF) / 255.0f, (hex >> 24 & 0xFF) / 255.0f };
    }
    
    public static void drawMovingString(final String s, final int height, final int displaywidth, final int color) {
        Integer widthmover = null;
        if (widthmover == null || widthmover >= displaywidth + 1) {
            widthmover = -Minecraft.getMinecraft().fontRenderer.getStringWidth(s);
        }
        if (widthmover != null) {
            Minecraft.getMinecraft().fontRenderer.drawString(s, (int)widthmover, height, color);
            Integer.valueOf((widthmover + 1));
        }
    }
    
    public static void drawRoundedRect(float x, float y, float x1, float y1, final int borderC, final int insideC) {
        x *= 2.0f;
        y *= 2.0f;
        x1 *= 2.0f;
        y1 *= 2.0f;
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(x, y + 1.0f, y1 - 2.0f, borderC);
        drawVLine(x1 - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
        drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
        drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
        drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
        drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
        drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }
    
    public static void drawBorderedRect(float x, float y, float x1, float y1, final int borderC, final int insideC) {
        x *= 2.0f;
        x1 *= 2.0f;
        y *= 2.0f;
        y1 *= 2.0f;
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(x, y, y1 - 1.0f, borderC);
        drawVLine(x1 - 1.0f, y, y1, borderC);
        drawHLine(x, x1 - 1.0f, y, borderC);
        drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
        drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }
    
    public static void sendPacket(final Packet p) {
        Wrapper.INSTANCE.mc().player.connection.sendPacket(p);
    }
    
    public static boolean stringListContains(final List<String> list, final String needle) {
        for (final String s : list) {
            if (s.trim().equalsIgnoreCase(needle.trim())) {
                return true;
            }
        }
        return false;
    }
    
    public static void drawBorderedRect(final double x, final double y, final double x2, final double y2, final float l1, final int col1, final int col2) {
        drawRect((float)x, (float)y, (float)x2, (float)y2, col2);
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
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void drawHLine(float par1, float par2, final float par3, final int par4) {
        if (par2 < par1) {
            final float var5 = par1;
            par1 = par2;
            par2 = var5;
        }
        drawRect(par1, par3, par2 + 1.0f, par3 + 1.0f, par4);
    }
    
    public static void drawVLine(final float par1, float par2, float par3, final int par4) {
        if (par3 < par2) {
            final float var5 = par2;
            par2 = par3;
            par3 = var5;
        }
        drawRect(par1, par2 + 1.0f, par1 + 1.0f, par3, par4);
    }
    
    public static void drawRect(final float nameXStart, final float nameYStart, final float nameXEnd, final float nameYEnd, final int nameColor) {
        final float alpha = (nameColor >> 24 & 0xFF) / 255.0f;
        final float red = (nameColor >> 16 & 0xFF) / 255.0f;
        final float green = (nameColor >> 8 & 0xFF) / 255.0f;
        final float blue = (nameColor & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(7);
        GL11.glVertex2d((double)nameXEnd, (double)nameYStart);
        GL11.glVertex2d((double)nameXStart, (double)nameYStart);
        GL11.glVertex2d((double)nameXStart, (double)nameYEnd);
        GL11.glVertex2d((double)nameXEnd, (double)nameYEnd);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void drawGradientRect(final double x, final double y, final double x2, final double y2, final int col1) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }
    
    public static void drawGradientRect(final double x, final double y, final double x2, final double y2, final int col1, final int col2) {
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
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }
    
    public static void drawGradientBorderedRect(final double x, final double y, final double x2, final double y2, final float l1, final int col1, final int col2, final int col3) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3042);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        drawGradientRect(x, y, x2, y2, col2, col3);
        GL11.glEnable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
    }
    
    public static void drawWindow(final double x, final double y, final double x2, final double y2, final float l1, final int col1, final int col2, final int col3) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3042);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        drawGradientRect(x, y, x2, y2, col2, col3);
        GL11.glEnable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
    }
    
    public static void drawWindow(final double x, final double y, final double x2, final double y2, final float l1, final int col1, final int col2) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3042);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        drawGradientRect(x, y, x2, y2, col2);
        GL11.glEnable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
    }
    
    public static Date getInternetTime() throws IOException {
        final URL url = new URL("http://www.baidu.com");
        final URLConnection uc = url.openConnection();
        uc.connect();
        final long ld = uc.getDate();
        return new Date(ld);
    }
    
    public static Vec3f toWorld(final Vec3f pos) {
        return toWorld(pos.getX(), pos.getY(), pos.getZ());
    }
    
    public static Vec3f toWorld(final double x, final double y, final double z) {
        final boolean result = GLU.gluUnProject((float)x, (float)y, (float)z, HGLUtils.MODELVIEW, HGLUtils.PROJECTION, HGLUtils.VIEWPORT, (FloatBuffer)HGLUtils.TO_WORLD_BUFFER.clear());
        if (result) {
            return new Vec3f(HGLUtils.TO_WORLD_BUFFER.get(0), HGLUtils.TO_WORLD_BUFFER.get(1), HGLUtils.TO_WORLD_BUFFER.get(2));
        }
        return null;
    }
    
    public static Vec3f toScreen(final Vec3f pos) {
        return toScreen(pos.getX(), pos.getY(), pos.getZ());
    }
    
    public static Vec3f toScreen(final double x, final double y, final double z) {
        final boolean result = GLU.gluProject((float)x, (float)y, (float)z, HGLUtils.MODELVIEW, HGLUtils.PROJECTION, HGLUtils.VIEWPORT, (FloatBuffer)HGLUtils.TO_SCREEN_BUFFER.clear());
        if (result) {
            return new Vec3f(HGLUtils.TO_SCREEN_BUFFER.get(0), Display.getHeight() - HGLUtils.TO_SCREEN_BUFFER.get(1), HGLUtils.TO_SCREEN_BUFFER.get(2));
        }
        return null;
    }
    
    public static void drawCircle(float cx, float cy, float r, final int num_segments, final int c) {
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        r *= 2.0f;
        cx *= 2.0f;
        cy *= 2.0f;
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        final float theta = (float)(6.283185307179586 / num_segments);
        final float p = (float)Math.cos(theta);
        final float s = (float)Math.sin(theta);
        GL11.glColor4f(f2, f3, f4, f);
        float x = r;
        float y = 0.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glBegin(2);
        for (int ii = 0; ii < num_segments; ++ii) {
            GL11.glVertex2f(x + cx, y + cy);
            final float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }
    
    public static void drawFullCircle(int cx, int cy, double r, final int c) {
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        r *= 2.0;
        cx *= 2;
        cy *= 2;
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 360; ++i) {
            final double x = Math.sin(i * 3.141592653589793 / 180.0) * r;
            final double y = Math.cos(i * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(cx + x, cy + y);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }
    
    public static String sendGet(final String url) {
        final StringBuffer sb = new StringBuffer();
        BufferedReader in = null;
        try {
            final URL result1 = new URL(url);
            final URLConnection connection = result1.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setRequestProperty("Content-type", "text/html;");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:55.0) Gecko/20100101 Firefox/55.0");
            connection.setRequestProperty("charset", "UTF-8");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append("\r\n" + line);
            }
        }
        catch (Exception var14) {
            var14.printStackTrace();
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (Exception var15) {
                var15.printStackTrace();
            }
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (Exception var16) {
                var16.printStackTrace();
            }
        }
        final String result2 = sb.toString();
        return result2.substring(0);
    }
    
    public static void startSmooth() {
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
    }
    
    public static void endSmooth() {
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GL11.glEnable(2832);
    }
    
    static {
        HGLUtils.mc = Minecraft.getMinecraft();
        HGLUtils.renderEngine = new TextureManager(Minecraft.getMinecraft().getResourceManager());
        MODELVIEW = BufferUtils.createFloatBuffer(16);
        PROJECTION = BufferUtils.createFloatBuffer(16);
        VIEWPORT = BufferUtils.createIntBuffer(16);
        TO_SCREEN_BUFFER = BufferUtils.createFloatBuffer(3);
        HGLUtils.glCapMap = new HashMap<Integer, Boolean>();
        TO_WORLD_BUFFER = BufferUtils.createFloatBuffer(3);
        sphere = new Sphere();
        HGLUtils.s = new Sphere();
    }
}
