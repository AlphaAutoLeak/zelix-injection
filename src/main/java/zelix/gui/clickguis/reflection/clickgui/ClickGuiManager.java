package zelix.gui.clickguis.reflection.clickgui;

import zelix.hack.*;
import net.minecraft.client.gui.*;
import zelix.utils.*;
import zelix.gui.clickguis.reflection.clickgui.component.*;
import java.util.*;
import java.io.*;

public class ClickGuiManager extends GuiScreen
{
    public static ArrayList<Frame> frames;
    public static int color;
    private ArrayList<Effect> effectList;
    
    public ClickGuiManager() {
        this.effectList = new ArrayList<Effect>();
        ClickGuiManager.frames = new ArrayList<Frame>();
        int frameX = 2;
        for (final HackCategory category : HackCategory.values()) {
            final Frame frame = new Frame(category);
            frame.setX(frameX);
            ClickGuiManager.frames.add(frame);
            frameX += frame.getWidth();
        }
    }
    
    public void initGui() {
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        Wrapper.INSTANCE.mc().fontRenderer.drawStringWithShadow("Press \"R\" to restore the position of frames", (float)((sr.getScaledWidth() - Wrapper.INSTANCE.mc().fontRenderer.getStringWidth("Press \"R\" to restore the position of frames")) * 2 + Wrapper.INSTANCE.mc().fontRenderer.getStringWidth("Press \"R\" to restore the position of frames") - 5), (float)((sr.getScaledHeight() - Wrapper.INSTANCE.mc().fontRenderer.FONT_HEIGHT - 1) * 2), -1);
        for (final Frame frame : ClickGuiManager.frames) {
            frame.renderFrame(this.fontRenderer);
            frame.updatePosition(mouseX, mouseY);
            for (final Component comp : frame.getComponents()) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (final Frame frame : ClickGuiManager.frames) {
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
                frame.setDrag(true);
                frame.dragX = mouseX - frame.getX();
                frame.dragY = mouseY - frame.getY();
            }
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
                frame.setOpen(!frame.isOpen());
            }
            if (frame.isOpen() && !frame.getComponents().isEmpty()) {
                for (final Component component : frame.getComponents()) {
                    component.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) {
        for (final Frame frame : ClickGuiManager.frames) {
            if (frame.isOpen() && keyCode != 1 && !frame.getComponents().isEmpty()) {
                for (final Component component : frame.getComponents()) {
                    component.keyTyped(typedChar, keyCode);
                }
            }
        }
        if (keyCode == 19) {
            ClickGuiManager.frames = new ArrayList<Frame>();
            int frameX = 2;
            for (final HackCategory category : HackCategory.values()) {
                final Frame frame2 = new Frame(category);
                final ScaledResolution sr = new ScaledResolution(this.mc);
                if (sr.getScaledWidth() >= frame2.getWidth() * 7) {
                    frame2.setX(frameX);
                    ClickGuiManager.frames.add(frame2);
                    frameX += frame2.getWidth();
                }
            }
        }
        if (keyCode == 1) {
            Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)null);
        }
    }
    
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        for (final Frame frame : ClickGuiManager.frames) {
            frame.setDrag(false);
        }
        for (final Frame frame : ClickGuiManager.frames) {
            if (frame.isOpen() && !frame.getComponents().isEmpty()) {
                for (final Component component : frame.getComponents()) {
                    component.mouseReleased(mouseX, mouseY, state);
                }
            }
        }
    }
    
    public boolean doesGuiPauseGame() {
        return true;
    }
}
