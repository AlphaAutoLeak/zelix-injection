package zelix.gui.clickguis.gishcode;

import zelix.gui.clickguis.gishcode.theme.*;
import javax.vecmath.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import java.util.*;
import zelix.gui.clickguis.gishcode.elements.*;
import zelix.gui.clickguis.gishcode.base.*;

public class ClickGui extends ClickGuiScreen
{
    private static Theme theme;
    private static ArrayList<Frame> frames;
    private Frame currentFrame;
    private boolean dragging;
    private Vector2f draggingOffset;
    
    public ClickGui() {
        this.dragging = false;
    }
    
    public static void renderPinned() {
        final Minecraft mc = Minecraft.getMinecraft();
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        final float scale = scaledResolution.getScaleFactor() / (float)Math.pow(scaledResolution.getScaleFactor(), 2.0);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.0f, 1000.0f);
        GlStateManager.scale(scale * 2.0f, scale * 2.0f, scale * 2.0f);
        for (final Frame frame : ClickGui.frames) {
            if (frame.isPinned()) {
                frame.render(ClickGui.mouse[0], ClickGui.mouse[1]);
            }
        }
        GlStateManager.popMatrix();
    }
    
    public static Theme getTheme() {
        return ClickGui.theme;
    }
    
    public void setTheme(final Theme theme) {
        ClickGui.theme = theme;
    }
    
    public void render() {
        for (final Frame frame : ClickGui.frames) {
            frame.render(ClickGui.mouse[0], ClickGui.mouse[1]);
        }
    }
    
    public void onMouseUpdate(final int x, final int y) {
        for (final Frame frame : ClickGui.frames) {
            for (final Component component : frame.getComponents()) {
                if (component.isMouseOver(x, y)) {
                    component.onMouseDrag(x, y);
                }
                else {
                    if (!(component instanceof Slider)) {
                        continue;
                    }
                    final Slider s = (Slider)component;
                    s.dragging = false;
                }
            }
        }
        if (this.dragging && this.currentFrame != null) {
            final int yOffset = (int)(y - this.draggingOffset.getY() - this.currentFrame.getY());
            this.currentFrame.setxPos((int)(x - this.draggingOffset.getX()));
            this.currentFrame.setyPos((int)(y - this.draggingOffset.getY()));
            for (final Component component2 : this.currentFrame.getComponents()) {
                component2.setyBase(component2.getyBase() + yOffset);
                if (component2 instanceof Container) {
                    final Container container = (Container)component2;
                    int height = 0;
                    for (final Component component3 : container.getComponents()) {
                        component3.setxPos(component2.getX());
                        component3.setyPos(component2.getY());
                        height += component3.getDimension().height;
                    }
                }
            }
        }
    }
    
    public boolean onMouseScroll(final int ammount) {
        boolean overFrame = false;
        for (final Frame frame : ClickGui.frames) {
            if (frame.isMouseOver(ClickGui.mouse[0], ClickGui.mouse[1])) {
                frame.scrollFrame(ammount * 4);
                overFrame = true;
            }
            frame.onMouseScroll(ammount * 4);
        }
        return overFrame;
    }
    
    public void onMouseRelease(final int x, final int y) {
        for (final Frame frame : ClickGui.frames) {
            if (frame.isMouseOver(x, y)) {
                this.currentFrame = frame;
                if (frame.isMouseOverBar(x, y)) {
                    this.dragging = false;
                }
                frame.onMouseRelease(x, y, 0);
            }
        }
    }
    
    public void onMouseClick(final int x, final int y, final int buttonID) {
        for (final Frame frame : ClickGui.frames) {
            if (frame.isMouseOver(x, y)) {
                this.currentFrame = frame;
                if (frame.isMouseOverBar(x, y)) {
                    this.dragging = true;
                    this.draggingOffset = new Vector2f((float)(x - frame.getX()), (float)(y - frame.getY()));
                }
                frame.onMousePress(x, y, buttonID);
            }
        }
    }
    
    public void onUpdate() {
        for (final Frame frame : ClickGui.frames) {
            frame.updateComponents();
        }
    }
    
    public void addFrame(final Frame frame) {
        ClickGui.frames.add(frame);
    }
    
    public ArrayList<Frame> getFrames() {
        return ClickGui.frames;
    }
    
    public void onKeyRelease(final int eventKey, final char eventCharacter) {
        for (final Frame frame : ClickGui.frames) {
            frame.onKeyReleased(eventKey, eventCharacter);
        }
    }
    
    public void onkeyPressed(final int eventKey, final char eventCharacter) {
        for (final Frame frame : ClickGui.frames) {
            frame.onKeyPressed(eventKey, eventCharacter);
        }
    }
    
    static {
        ClickGui.frames = new ArrayList<Frame>();
    }
}
