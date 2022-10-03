package zelix.hack.hacks.xray.gui.utils;

import java.io.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import java.awt.*;
import javax.annotation.*;
import net.minecraft.util.*;
import net.minecraft.client.gui.*;

public class GuiBase extends GuiScreen
{
    private boolean hasSide;
    private String sideTitle;
    private int backgroundWidth;
    private int backgroundHeight;
    
    public GuiBase(final boolean hasSide) {
        this.sideTitle = "";
        this.backgroundWidth = 229;
        this.backgroundHeight = 235;
        this.hasSide = hasSide;
    }
    
    protected void keyTyped(final char par1, final int par2) throws IOException {
        super.keyTyped(par1, par2);
        if (par2 == 1) {
            this.mc.player.closeScreen();
        }
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public static void drawTexturedQuadFit(final double x, final double y, final double width, final double height, final int[] color, final float alpha) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder tessellate = tessellator.getBuffer();
        tessellate.begin(7, DefaultVertexFormats.POSITION_TEX);
        if (color != null) {
            GlStateManager.color(color[0] / 255.0f, color[1] / 255.0f, color[2] / 255.0f, alpha / 255.0f);
        }
        tessellate.pos(x + 0.0, y + height, 0.0).tex(0.0, 1.0).endVertex();
        tessellate.pos(x + width, y + height, 0.0).tex(1.0, 1.0).endVertex();
        tessellate.pos(x + width, y + 0.0, 0.0).tex(1.0, 0.0).endVertex();
        tessellate.pos(x + 0.0, y + 0.0, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
    }
    
    public static void drawTexturedQuadFit(final double x, final double y, final double width, final double height, final int[] color) {
        drawTexturedQuadFit(x, y, width, height, color, 255.0f);
    }
    
    public static void drawTexturedQuadFit(final double x, final double y, final double width, final double height, @Nullable final Color color) {
        int[] color2;
        if (color == null) {
            color2 = null;
        }
        else {
            final int[] array = color2 = new int[3];
            array[0] = color.getRed();
            array[1] = color.getGreen();
            array[2] = color.getBlue();
        }
        drawTexturedQuadFit(x, y, width, height, color2, 255.0f);
    }
    
    public void drawScreen(final int x, final int y, final float f) {
        this.drawDefaultBackground();
        final FontRenderer fr = this.mc.fontRenderer;
        if (this.colorBackground() == null) {
            this.mc.renderEngine.bindTexture(new ResourceLocation("xray:textures/gui/bg.png"));
        }
        else {
            GlStateManager.disableTexture2D();
        }
        if (this.hasSide) {
            drawTexturedQuadFit(this.width / 2.0 + 60.0, this.height / 2.0f - 90.0f, 150.0, 180.0, this.colorBackground());
            drawTexturedQuadFit(this.width / 2.0f - 150.0f, this.height / 2.0f - 118.0f, this.backgroundWidth, this.backgroundHeight, this.colorBackground());
            if (this.hasSideTitle()) {
                fr.drawStringWithShadow(this.sideTitle, this.width / 2.0f + 80.0f, this.height / 2.0f - 77.0f, 16776960);
            }
        }
        if (!this.hasSide) {
            drawTexturedQuadFit(this.width / 2.0f - this.backgroundWidth / 2.0f + 1.0f, this.height / 2.0f - this.backgroundHeight / 2.0f, this.backgroundWidth, this.backgroundHeight, this.colorBackground());
        }
        if (this.colorBackground() != null) {
            GlStateManager.enableTexture2D();
        }
        if (this.hasTitle()) {
            if (this.hasSide) {
                fr.drawStringWithShadow(this.title(), this.width / 2.0f - 138.0f, this.height / 2.0f - 105.0f, 16776960);
            }
            else {
                fr.drawStringWithShadow(this.title(), this.width / 2.0f - this.backgroundWidth / 2.0f + 14.0f, this.height / 2.0f - this.backgroundHeight / 2.0f + 13.0f, 16776960);
            }
        }
        super.drawScreen(x, y, f);
    }
    
    public Color colorBackground() {
        return null;
    }
    
    public void mouseClicked(final int x, final int y, final int mouse) throws IOException {
        super.mouseClicked(x, y, mouse);
    }
    
    public boolean hasTitle() {
        return false;
    }
    
    public String title() {
        return "";
    }
    
    private boolean hasSideTitle() {
        return !this.sideTitle.isEmpty();
    }
    
    protected void setSideTitle(final String title) {
        this.sideTitle = title;
    }
    
    public void setSize(final int width, final int height) {
        this.backgroundWidth = width;
        this.backgroundHeight = height;
    }
    
    public FontRenderer getFontRender() {
        return this.mc.fontRenderer;
    }
}
