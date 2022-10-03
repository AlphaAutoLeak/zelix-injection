package zelix.utils.tenacityutils.render;

import java.awt.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;

public class RoundedUtil
{
    public static ShaderUtil roundedShader;
    public static ShaderUtil roundedOutlineShader;
    private static final ShaderUtil roundedTexturedShader;
    private static final ShaderUtil roundedGradientShader;
    
    public static void drawRound(final float x, final float y, final float width, final float height, final float radius, final Color color) {
        drawRound(x, y, width, height, radius, false, color);
    }
    
    public static void drawRoundScale(final float x, final float y, final float width, final float height, final float radius, final Color color, final float scale) {
        drawRound(x + width - width * scale, y + height / 2.0f - height / 2.0f * scale, width * scale, height * scale, radius, false, color);
    }
    
    public static void drawGradientHorizontal(final float x, final float y, final float width, final float height, final float radius, final Color left, final Color right) {
        drawGradientRound(x, y, width, height, radius, left, left, right, right);
    }
    
    public static void drawGradientVertical(final float x, final float y, final float width, final float height, final float radius, final Color top, final Color bottom) {
        drawGradientRound(x, y, width, height, radius, bottom, top, bottom, top);
    }
    
    public static void drawGradientCornerLR(final float x, final float y, final float width, final float height, final float radius, final Color topLeft, final Color bottomRight) {
        final Color mixedColor = ColorUtil.interpolateColorC(topLeft, bottomRight, 0.5f);
        drawGradientRound(x, y, width, height, radius, mixedColor, topLeft, bottomRight, mixedColor);
    }
    
    public static void drawGradientCornerRL(final float x, final float y, final float width, final float height, final float radius, final Color bottomLeft, final Color topRight) {
        final Color mixedColor = ColorUtil.interpolateColorC(topRight, bottomLeft, 0.5f);
        drawGradientRound(x, y, width, height, radius, bottomLeft, mixedColor, mixedColor, topRight);
    }
    
    public static void drawGradientRound(final float x, final float y, final float width, final float height, final float radius, final Color bottomLeft, final Color topLeft, final Color bottomRight, final Color topRight) {
        RenderUtil.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        RoundedUtil.roundedGradientShader.init();
        setupRoundedRectUniforms(x, y, width, height, radius, RoundedUtil.roundedGradientShader);
        RoundedUtil.roundedGradientShader.setUniformf("color1", bottomLeft.getRed() / 255.0f, bottomLeft.getGreen() / 255.0f, bottomLeft.getBlue() / 255.0f, bottomLeft.getAlpha() / 255.0f);
        RoundedUtil.roundedGradientShader.setUniformf("color2", topLeft.getRed() / 255.0f, topLeft.getGreen() / 255.0f, topLeft.getBlue() / 255.0f, topLeft.getAlpha() / 255.0f);
        RoundedUtil.roundedGradientShader.setUniformf("color3", bottomRight.getRed() / 255.0f, bottomRight.getGreen() / 255.0f, bottomRight.getBlue() / 255.0f, bottomRight.getAlpha() / 255.0f);
        RoundedUtil.roundedGradientShader.setUniformf("color4", topRight.getRed() / 255.0f, topRight.getGreen() / 255.0f, topRight.getBlue() / 255.0f, topRight.getAlpha() / 255.0f);
        ShaderUtil.drawQuads(x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f);
        RoundedUtil.roundedGradientShader.unload();
        GlStateManager.disableBlend();
    }
    
    public static void drawRound(final float x, final float y, final float width, final float height, final float radius, final boolean blur, final Color color) {
        RenderUtil.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        RoundedUtil.roundedShader.init();
        setupRoundedRectUniforms(x, y, width, height, radius, RoundedUtil.roundedShader);
        roundedShader.setUniformi("blur", new int[]{blur ? 1 : 0});
        RoundedUtil.roundedShader.setUniformf("color", color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        ShaderUtil.drawQuads(x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f);
        RoundedUtil.roundedShader.unload();
        GlStateManager.disableBlend();
    }
    
    public static void drawRoundOutline(final float x, final float y, final float width, final float height, final float radius, final float outlineThickness, final Color color, final Color outlineColor) {
        RenderUtil.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        RoundedUtil.roundedOutlineShader.init();
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        setupRoundedRectUniforms(x, y, width, height, radius, RoundedUtil.roundedOutlineShader);
        RoundedUtil.roundedOutlineShader.setUniformf("outlineThickness", outlineThickness * sr.getScaleFactor());
        RoundedUtil.roundedOutlineShader.setUniformf("color", color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        RoundedUtil.roundedOutlineShader.setUniformf("outlineColor", outlineColor.getRed() / 255.0f, outlineColor.getGreen() / 255.0f, outlineColor.getBlue() / 255.0f, outlineColor.getAlpha() / 255.0f);
        ShaderUtil.drawQuads(x - (2.0f + outlineThickness), y - (2.0f + outlineThickness), width + (4.0f + outlineThickness * 2.0f), height + (4.0f + outlineThickness * 2.0f));
        RoundedUtil.roundedOutlineShader.unload();
        GlStateManager.disableBlend();
    }
    
    public static void drawRoundTextured(final float x, final float y, final float width, final float height, final float radius, final float alpha) {
        RenderUtil.resetColor();
        RoundedUtil.roundedTexturedShader.init();
        RoundedUtil.roundedTexturedShader.setUniformi("textureIn", 0);
        setupRoundedRectUniforms(x, y, width, height, radius, RoundedUtil.roundedTexturedShader);
        RoundedUtil.roundedTexturedShader.setUniformf("alpha", alpha);
        ShaderUtil.drawQuads(x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f);
        RoundedUtil.roundedTexturedShader.unload();
        GlStateManager.disableBlend();
    }
    
    private static void setupRoundedRectUniforms(final float x, final float y, final float width, final float height, final float radius, final ShaderUtil roundedTexturedShader) {
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        roundedTexturedShader.setUniformf("location", x * sr.getScaleFactor(), Minecraft.getMinecraft().displayHeight - height * sr.getScaleFactor() - y * sr.getScaleFactor());
        roundedTexturedShader.setUniformf("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        roundedTexturedShader.setUniformf("radius", radius * sr.getScaleFactor());
    }
    
    static {
        RoundedUtil.roundedShader = new ShaderUtil("roundedRect");
        RoundedUtil.roundedOutlineShader = new ShaderUtil("Tenacity/Shaders/roundRectOutline.frag");
        roundedTexturedShader = new ShaderUtil("Tenacity/Shaders/roundRectTextured.frag");
        roundedGradientShader = new ShaderUtil("roundedRectGradient");
    }
}
