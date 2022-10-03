package zelix.gui.clickguis.huangbai;

import net.minecraft.util.Timer;
import zelix.gui.clickguis.huangbai.tessellate.*;

import java.util.List;
import java.util.function.*;
import net.minecraft.client.renderer.culling.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import zelix.utils.*;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.*;
import net.minecraft.util.*;
import net.minecraft.client.gui.*;
import java.awt.*;
import net.minecraft.util.math.*;
import zelix.gui.clickguis.huangbai.gl.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.player.*;
import zelix.utils.ReflectionHelper;
import zelix.utils.math.*;
import zelix.utils.math.Vec2f;

import java.util.*;

public class ClickGuiRender
{
    public static final Tessellation tessellator;
    private static final List<Integer> csBuffer;
    private static final Consumer<Integer> ENABLE_CLIENT_STATE;
    private static final Consumer<Integer> DISABLE_CLIENT_STATE;
    public static float delta;
    private static final Frustum frustum;
    public static Minecraft mc;
    public static Object Existance_60;
    public static ScaledResolution scaledresolution;
    
    public static int width() {
        return ClickGuiRender.scaledresolution.getScaledWidth();
    }
    
    public static int height() {
        return ClickGuiRender.scaledresolution.getScaledHeight();
    }
    
    public static int getHexRGB(final int hex) {
        return 0xFF000000 | hex;
    }
    
    public static boolean isInViewFrustrum(final Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }
    
    private static boolean isInViewFrustrum(final AxisAlignedBB bb) {
        final Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        ClickGuiRender.frustum.setPosition(current.posX, current.posY, current.posZ);
        return ClickGuiRender.frustum.isBoundingBoxInFrustum(bb);
    }
    
    public static double interpolate(final double newPos, final double oldPos) {
        final Timer timer = (Timer)ReflectionHelper.getPrivateValue((Class)Minecraft.class, (Object)Wrapper.INSTANCE.mc(), new String[] { Mappings.timer });
        return oldPos + (newPos - oldPos) * timer.renderPartialTicks;
    }
    
    public static double interpolate(final double current, final double old, final double scale) {
        return old + (current - old) * scale;
    }
    
    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glHint(3154, 4354);
    }
    
    public static void post3D() {
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void draw3DLine(final float x, final float y, final float z, final int color) {
        pre3D();
        GL11.glLoadIdentity();
        final Timer timer = (Timer) ReflectionHelper.getPrivateValue((Class)Minecraft.class, (Object)Wrapper.INSTANCE.mc(), new String[] { Mappings.timer });
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        GL11.glColor4f(var12, var13, var14, var11);
        GL11.glLineWidth(1.5f);
        GL11.glBegin(3);
        GL11.glVertex3d(0.0, (double)Minecraft.getMinecraft().player.getEyeHeight(), 0.0);
        GL11.glVertex3d((double)x, (double)y, (double)z);
        GL11.glEnd();
        post3D();
    }
    
    public static void drawCustomImage(final int x, final int y, final int width, final int height, final ResourceLocation image) {
        final ScaledResolution scaledResolution = new ScaledResolution(ClickGuiRender.mc);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float)width, (float)height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    public static void drawFullCircle(int cx, int cy, double r, final int segments, final float lineWidth, final int part, final int c) {
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        r *= 2.0;
        cx *= 2;
        cy *= 2;
        final float f2 = (c >> 24 & 0xFF) / 255.0f;
        final float f3 = (c >> 16 & 0xFF) / 255.0f;
        final float f4 = (c >> 8 & 0xFF) / 255.0f;
        final float f5 = (c & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glLineWidth(lineWidth);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f3, f4, f5, f2);
        GL11.glBegin(3);
        for (int i = segments - part; i <= segments; ++i) {
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
    
    public static void drawIcon(final float x, final float y, final int sizex, final int sizey, final ResourceLocation resourceLocation) {
        GL11.glPushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GL11.glTranslatef(x, y, 0.0f);
        drawScaledRect(0, 0, 0.0f, 0.0f, sizex, sizey, sizex, sizey, sizex, sizey);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.disableRescaleNormal();
        GL11.glDisable(2848);
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }
    
    public static void drawScaledRect(final int x, final int y, final float u, final float v, final int uWidth, final int vHeight, final int width, final int height, final float tileWidth, final float tileHeight) {
        Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
    }
    
    public static void drawCircle(final double x, final double y, final double radius, final int c) {
        final float f2 = (c >> 24 & 0xFF) / 255.0f;
        final float f3 = (c >> 16 & 0xFF) / 255.0f;
        final float f4 = (c >> 8 & 0xFF) / 255.0f;
        final float f5 = (c & 0xFF) / 255.0f;
        GlStateManager.alphaFunc(516, 0.001f);
        GlStateManager.color(f3, f4, f5, f2);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        final Tessellator tes = Tessellator.getInstance();
        for (double i = 0.0; i < 360.0; ++i) {
            final double f6 = Math.sin(i * 3.141592653589793 / 180.0) * radius;
            final double f7 = Math.cos(i * 3.141592653589793 / 180.0) * radius;
            GL11.glVertex2d(f4 + x, f5 + y);
        }
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.alphaFunc(516, 0.1f);
    }
    
    public static void drawHLine(float par1, float par2, final float par3, final int par4) {
        if (par2 < par1) {
            final float var5 = par1;
            par1 = par2;
            par2 = var5;
        }
        drawRect(par1, par3, par2 + 1.0f, par3 + 1.0f, par4);
    }
    
    public static void drawRect(double d, double e, double g, double h, final int color) {
        if (d < g) {
            final int i = (int)d;
            d = g;
            g = i;
        }
        if (e < h) {
            final int j = (int)e;
            e = h;
            h = j;
        }
        final float f2 = (color >> 24 & 0xFF) / 255.0f;
        final float f3 = (color >> 16 & 0xFF) / 255.0f;
        final float f4 = (color >> 8 & 0xFF) / 255.0f;
        final float f5 = (color & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f3, f4, f5, f2);
        GL11.glBegin(7);
        GL11.glVertex2d(g, e);
        GL11.glVertex2d(d, e);
        GL11.glVertex2d(d, h);
        GL11.glVertex2d(g, h);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void drawFilledCircle(final double x, final double y, final double r, final int c, final int id) {
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        GL11.glDisable(3042);
        GL11.glDisable(3553);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(9);
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
        GL11.glBegin(1);
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
    }
    
    public static void pre() {
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }
    
    public static void post() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glColor3d(1.0, 1.0, 1.0);
    }
    
    public static void startDrawing() {
        GL11.glEnable(3042);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
    }
    
    public static void stopDrawing() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    public static Color blend(final Color color1, final Color color2, final double ratio) {
        final float r = (float)ratio;
        final float ir = 1.0f - r;
        final float[] rgb1 = new float[3];
        final float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        final Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
        return color3;
    }
    
    public static void drawLine(final Vec2f start, final Vec2f end, final float width) {
        drawLine(start.getX(), start.getY(), end.getX(), end.getY(), width);
    }
    
    public static void drawLine(final Vec3f start, final Vec3f end, final float width) {
        drawLine((float)start.getX(), (float)start.getY(), (float)start.getZ(), (float)end.getX(), (float)end.getY(), (float)end.getZ(), width);
    }
    
    public static void drawLine(final float x, final float y, final float x1, final float y1, final float width) {
        drawLine(x, y, 0.0f, x1, y1, 0.0f, width);
    }
    
    public static void drawLine(final float x, final float y, final float z, final float x1, final float y1, final float z1, final float width) {
        GL11.glLineWidth(width);
        setupRender(true);
        setupClientState(GLClientState.VERTEX, true);
        ClickGuiRender.tessellator.addVertex(x, y, z).addVertex(x1, y1, z1).draw(3);
        setupClientState(GLClientState.VERTEX, false);
        setupRender(false);
    }
    
    public static void setupClientState(final GLClientState state, final boolean enabled) {
        ClickGuiRender.csBuffer.clear();
        if (state.ordinal() > 0) {
            ClickGuiRender.csBuffer.add(state.getCap());
        }
        ClickGuiRender.csBuffer.add(32884);
        ClickGuiRender.csBuffer.forEach(enabled ? ClickGuiRender.ENABLE_CLIENT_STATE : ClickGuiRender.DISABLE_CLIENT_STATE);
    }
    
    public static void setupRender(final boolean start) {
        if (start) {
            GlStateManager.enableBlend();
            GL11.glEnable(2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.blendFunc(770, 771);
            GL11.glHint(3154, 4354);
        }
        else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GL11.glDisable(2848);
            GlStateManager.enableDepth();
        }
        GlStateManager.depthMask(!start);
    }
    
    public static void drawOutlinedBoundingBox(final AxisAlignedBB aa) {
        final Tessellator a = Tessellator.getInstance();
        final BufferBuilder tessellator = a.getBuffer();
        tessellator.begin(3, DefaultVertexFormats.POSITION_TEX);
        tessellator.pos(aa.minX, aa.minY, aa.minZ);
        tessellator.pos(aa.maxX, aa.minY, aa.minZ);
        tessellator.pos(aa.maxX, aa.minY, aa.maxZ);
        tessellator.pos(aa.minX, aa.minY, aa.maxZ);
        tessellator.pos(aa.minX, aa.minY, aa.minZ);
        a.draw();
        tessellator.begin(3, DefaultVertexFormats.POSITION_TEX);
        tessellator.pos(aa.minX, aa.maxY, aa.minZ);
        tessellator.pos(aa.maxX, aa.maxY, aa.minZ);
        tessellator.pos(aa.maxX, aa.maxY, aa.maxZ);
        tessellator.pos(aa.minX, aa.maxY, aa.maxZ);
        tessellator.pos(aa.minX, aa.maxY, aa.minZ);
        a.draw();
        tessellator.begin(1, DefaultVertexFormats.POSITION_TEX);
        tessellator.pos(aa.minX, aa.minY, aa.minZ);
        tessellator.pos(aa.minX, aa.maxY, aa.minZ);
        tessellator.pos(aa.maxX, aa.minY, aa.minZ);
        tessellator.pos(aa.maxX, aa.maxY, aa.minZ);
        tessellator.pos(aa.maxX, aa.minY, aa.maxZ);
        tessellator.pos(aa.maxX, aa.maxY, aa.maxZ);
        tessellator.pos(aa.minX, aa.minY, aa.maxZ);
        tessellator.pos(aa.minX, aa.maxY, aa.maxZ);
        a.draw();
    }
    
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
        final Tessellator a = Tessellator.getInstance();
        final BufferBuilder worldRenderer = a.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var7, var8, var9, var6);
        Tessellator.getInstance().draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
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
        GL11.glBegin(7);
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
    }
    
    public static void drawCircle(float cx, float cy, float r, final int num_segments, final int c) {
        GL11.glPushMatrix();
        cx *= 2.0f;
        cy *= 2.0f;
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        final float theta = (float)(6.2831852 / num_segments);
        final float p = (float)Math.cos(theta);
        final float s = (float)Math.sin(theta);
        float x;
        r = (x = r * 2.0f);
        float y = 0.0f;
        enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(2);
        for (int ii = 0; ii < num_segments; ++ii) {
            GL11.glVertex2f(x + cx, y + cy);
            final float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
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
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static Vec3 interpolateRender(final EntityPlayer player) {
        final Timer timer = (Timer)ReflectionHelper.getPrivateValue((Class)Minecraft.class, (Object)Wrapper.INSTANCE.mc(), new String[] { Mappings.timer });
        final float part = timer.renderPartialTicks;
        final double interpX = player.lastTickPosX + (player.posX - player.lastTickPosX) * part;
        final double interpY = player.lastTickPosY + (player.posY - player.lastTickPosY) * part;
        final double interpZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * part;
        return new Vec3(interpX, interpY, interpZ);
    }
    
    public static double getAnimationState(double animation, final double finalState, final double speed) {
        final float add = (float)(0.01 * speed);
        if (animation < finalState) {
            if (animation + add < finalState) {
                animation += add;
            }
            else {
                animation = finalState;
            }
        }
        else if (animation - add > finalState) {
            animation -= add;
        }
        else {
            animation = finalState;
        }
        return animation;
    }
    
    public static void drawImage(final ResourceLocation image, final int x, final int y, final int width, final int height) {
        final ScaledResolution scaledResolution = new ScaledResolution(ClickGuiRender.mc);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float)width, (float)height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    public static void drawImage(final ResourceLocation image, final int x, final int y, final int width, final int height, final Color color) {
        final ScaledResolution scaledResolution = new ScaledResolution(ClickGuiRender.mc);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(color.getRed() / 255.0f, color.getBlue() / 255.0f, color.getRed() / 255.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float)width, (float)height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    public static void drawRoundedRect(final int i, final int j, final int scaledWidth, final int k, final int rgb, final int rgb2) {
    }
    
    public static int rainbow(final int i) {
        return 0;
    }
    
    public static void drawblock(final double a, final double a2, final double a3, final int a4, final int a5, final float a6) {
        final float a7 = (a4 >> 24 & 0xFF) / 255.0f;
        final float a8 = (a4 >> 16 & 0xFF) / 255.0f;
        final float a9 = (a4 >> 8 & 0xFF) / 255.0f;
        final float a10 = (a4 & 0xFF) / 255.0f;
        final float a11 = (a5 >> 24 & 0xFF) / 255.0f;
        final float a12 = (a5 >> 16 & 0xFF) / 255.0f;
        final float a13 = (a5 >> 8 & 0xFF) / 255.0f;
        final float a14 = (a5 & 0xFF) / 1.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(a8, a9, a10, a7);
        drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        GL11.glLineWidth(a6);
        GL11.glColor4f(a12, a13, a14, a11);
        drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawBorderedRect(final float g, final int categoryY, final int h, final int e, final float l1, final int rgb) {
    }
    
    public static void drawBorderedRect(final float g, final int categoryY, final int h, final int e, final float l1, final ResourceLocation resourceLocation, final ResourceLocation resourceLocation2) {
    }
    
    static {
        frustum = new Frustum();
        ClickGuiRender.mc = Minecraft.getMinecraft();
        ClickGuiRender.scaledresolution = new ScaledResolution(ClickGuiRender.mc);
        tessellator = Tessellation.createExpanding(4, 1.0f, 2.0f);
        csBuffer = new ArrayList<Integer>();
        ENABLE_CLIENT_STATE = GL11::glEnableClientState;
        DISABLE_CLIENT_STATE = GL11::glEnableClientState;
    }
}
