package zelix.gui.clickguis.kendall;

import zelix.gui.clickguis.caesium.Panel;
import zelix.gui.clickguis.kendall.components.Component;
import zelix.gui.clickguis.kendall.frame.*;
import net.minecraft.client.gui.*;
import net.minecraft.util.*;
import net.minecraft.client.*;
import zelix.hack.hacks.ClickGui;
import zelix.utils.*;
import zelix.hack.*;
import java.util.*;
import zelix.gui.clickguis.kendall.components.*;
import java.io.*;
import zelix.gui.clickguis.caesium.*;
import zelix.hack.hacks.*;
import zelix.*;
import zelix.managers.*;
import org.lwjgl.input.*;
import java.awt.*;
import zelix.utils.tenacityutils.render.*;
import zelix.utils.hooks.visual.font.*;
import zelix.gui.clickguis.kendall.components.impls.*;
import zelix.utils.visual.*;

public class KendallScreen extends GuiScreen
{
    public ArrayList<KendallFrame> frames;
    public boolean onMoving;
    public ScaledResolution scaledResolution;
    public double x;
    public double y;
    public float moveX;
    public float moveY;
    private final FontRenderer fr;
    public boolean isVIsableSW;
    public KendallButton targetbt;
    private ResourceLocation res;
    
    protected ArrayList<KendallFrame> getFrames() {
        return this.frames;
    }
    
    protected void addFrame(final KendallFrame frame) {
        if (!this.frames.contains(frame)) {
            this.frames.add(frame);
        }
    }
    
    public KendallScreen() {
        this.frames = new ArrayList<KendallFrame>();
        this.onMoving = false;
        this.scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        this.x = 0.0;
        this.y = 0.0;
        this.moveX = 0.0f;
        this.moveY = 0.0f;
        this.fr = Wrapper.INSTANCE.fontRenderer();
        this.isVIsableSW = false;
        this.targetbt = null;
        this.res = new ResourceLocation("blur.png");
        int x = 20;
        for (final HackCategory category : HackCategory.values()) {
            this.addFrame(new KendallFrame(category, x, 20));
            x += 110;
        }
    }
    
    public void initGui() {
        for (final KendallFrame frame : this.frames) {
            frame.initialize();
        }
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (isHovered((float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + 25.0), mouseX, mouseY) && mouseButton == 0) {
            this.onMoving = true;
        }
        if (this.isVIsableSW && this.targetbt != null) {
            for (final Component cp : this.targetbt.components) {
                cp.mouseClicked(mouseX, mouseY, mouseButton);
            }
            return;
        }
        for (final KendallFrame frame : this.frames) {
            frame.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.onMoving = false;
        for (final KendallFrame frame : this.frames) {
            frame.mouseReleased(mouseX, mouseY, state);
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.isVIsableSW && keyCode == 1) {
            this.isVIsableSW = false;
            return;
        }
        super.keyTyped(typedChar, keyCode);
        for (final KendallFrame frame : this.frames) {
            frame.keyTyped(typedChar, keyCode);
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sR = new ScaledResolution(this.mc);
        this.fr.drawString("Thanks For Using!", 2, sR.getScaledHeight() - this.fr.FONT_HEIGHT, Panel.fontColor);
        for (final KendallFrame frame : this.frames) {
            frame.render(mouseX, mouseY);
        }
        if (ClickGui.KendallMyGod.isVIsableSW) {
            this.isVIsableSW = false;
            this.ShowSettingsWindow(mouseX, mouseY);
        }
    }
    
    public void onGuiClosed() {
        super.onGuiClosed();
        this.mc.entityRenderer.stopUseShader();
        final FileManager fileManager = Core.fileManager;
        FileManager.saveClickGui_Kendall();
        final FileManager fileManager2 = Core.fileManager;
        FileManager.saveHacks();
    }
    
    public void ShowSettingsWindow(final int mouseX, final int mouseY) {
        if (this.targetbt == null) {
            this.isVIsableSW = false;
            return;
        }
        this.isVIsableSW = true;
        this.scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        final double width = 250.0;
        final double height = 34 + 29 * this.targetbt.components.size() - 2;
        if (this.x <= 0.0 && this.y <= 0.0) {
            this.x = this.scaledResolution.getScaledWidth() / 2 - width / 2.0;
            this.y = this.scaledResolution.getScaledHeight() / 2 - height / 2.0;
        }
        if (isHovered((float)this.x, (float)this.y, (float)(this.x + width), (float)(this.y + 25.0), mouseX, mouseY) && Mouse.isButtonDown(0) && this.onMoving) {
            if (this.moveX == 0.0f && this.moveY == 0.0f) {
                this.moveX = (float)(mouseX - this.x);
                this.moveY = (float)(mouseY - this.y);
            }
            else {
                this.x = (int)(mouseX - this.moveX);
                this.y = (int)(mouseY - this.moveY);
                final float n = (float)(this.y + 20.0);
            }
        }
        else if (this.moveX != 0.0f || this.moveY != 0.0f) {
            this.moveX = 0.0f;
            this.moveY = 0.0f;
        }
        final float alphaAnimation = 1.0f;
        RenderUtil.drawRect2(this.x, this.y, width, height, ColorUtil.applyOpacity(new Color(10, 10, 10), (float)(0.8 * alphaAnimation)).getRGB());
        if (!ClickGui.language.getMode("Chinese").isToggled()) {
            FontLoaders.default18.drawString(this.targetbt.mod.getRenderName(), (float)this.x + 10.0f, (float)this.y + 12.0f, -657929);
        }
        else {
            Wrapper.INSTANCE.fontRenderer().drawString(this.targetbt.mod.getRenderName(), (int)((float)this.x + 10.0f), (int)((float)this.y + 12.0f), -657929);
        }
        final float ssl = (float)this.x;
        float hqc = (float)this.y + 24.0f;
        for (final Component sjh : this.targetbt.components) {
            if (sjh instanceof KendallMode) {
                final KendallMode fuckssl = (KendallMode)sjh;
                fuckssl.render(ssl, hqc, mouseX, mouseY);
            }
            if (sjh instanceof KendallSIlder) {
                final KendallSIlder fuckrcx = (KendallSIlder)sjh;
                fuckrcx.render(ssl, hqc, mouseX, mouseY);
            }
            if (sjh instanceof KendallOption) {
                final KendallOption fuckhqc = (KendallOption)sjh;
                fuckhqc.render(ssl, hqc, mouseX, mouseY);
            }
            hqc += 29.0f;
        }
    }
    
    public static boolean isHovered(final float x, final float y, final float x2, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
    
    public void renderBLUR(final int x, final int y, final int width, final int height) {
        RenderUtils.drawImage(this.res, x, y, width, height);
    }
}
