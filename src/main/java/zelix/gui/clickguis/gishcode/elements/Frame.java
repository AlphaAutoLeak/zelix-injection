package zelix.gui.clickguis.gishcode.elements;

import org.lwjgl.opengl.*;
import java.util.*;
import zelix.gui.clickguis.gishcode.base.*;
import zelix.gui.clickguis.gishcode.*;
import zelix.gui.clickguis.gishcode.base.Component;
import zelix.gui.clickguis.gishcode.base.Container;
import zelix.utils.*;
import java.awt.*;

public class Frame extends Container
{
    private boolean pinned;
    private boolean maximized;
    private boolean maximizible;
    private boolean visable;
    private boolean pinnable;
    private int ticksSinceScroll;
    private int scrollAmmount;
    
    public Frame(final int xPos, final int yPos, final int width, final int height, final String title) {
        super(xPos, yPos, width, height, ComponentType.FRAME, null, title);
        this.maximizible = true;
        this.visable = true;
        this.pinnable = true;
        this.ticksSinceScroll = 100;
        this.scrollAmmount = 0;
    }
    
    @Override
    public void renderChildren(final int mouseX, final int mouseY) {
        if (this.isMaximized()) {
            GL11.glEnable(3089);
            GL11.glScissor(this.getX() * 2, Display.getHeight() - (this.getY() + this.getDimension().height) * 2, this.getDimension().width * 2, (this.getDimension().height - this.getFrameBoxHeight()) * 2);
            for (final Component component : this.getComponents()) {
                component.render(mouseX, mouseY);
            }
            GL11.glDisable(3089);
        }
    }
    
    @Override
    public void onMousePress(final int x, final int y, final int buttonID) {
        if (this.isMouseOverBar(x, y)) {
            ClickGui.getTheme().getRenderer().get(this.getComponentType()).doInteractions(this, x, y);
        }
        if (x >= this.getX() && y >= this.getY() + this.getFrameBoxHeight() && x <= this.getX() + this.getDimension().getWidth() && y <= this.getY() + this.getDimension().getHeight()) {
            for (final Component c : this.getComponents()) {
                if (c.isMouseOver(x, y) && this.maximized) {
                    c.onMousePress(x, y, buttonID);
                    ClickGui.getTheme().getRenderer().get(this.getComponentType()).doInteractions(this, x, y);
                }
            }
        }
    }
    
    @Override
    public void onMouseRelease(final int x, final int y, final int buttonID) {
        if (x >= this.getX() && y >= this.getY() + this.getFrameBoxHeight() && x <= this.getX() + this.getDimension().getWidth() && y <= this.getY() + this.getDimension().getHeight()) {
            for (final Component c : this.getComponents()) {
                if (c.isMouseOver(x, y) && this.maximized) {
                    c.onMouseRelease(x, y, buttonID);
                }
            }
        }
    }
    
    @Override
    public void onMouseDrag(final int x, final int y) {
        if (this.isMouseOverBar(x, y)) {
            ClickGui.getTheme().getRenderer().get(this.getComponentType()).doInteractions(this, x, y);
        }
        if (x >= this.getX() && y >= this.getY() + this.getFrameBoxHeight() && x <= this.getX() + this.getDimension().getWidth() && y <= this.getY() + this.getDimension().getHeight()) {
            for (final Component c : this.getComponents()) {
                if (c.isMouseOver(x, y) && this.maximized) {
                    c.onMouseDrag(x, y);
                    ClickGui.getTheme().getRenderer().get(this.getComponentType()).doInteractions(this, x, y);
                }
            }
        }
    }
    
    @Override
    public void onKeyPressed(final int key, final char character) {
        for (final Component c : this.getComponents()) {
            c.onKeyPressed(key, character);
        }
    }
    
    @Override
    public void onKeyReleased(final int key, final char character) {
        for (final Component c : this.getComponents()) {
            c.onKeyReleased(key, character);
        }
    }
    
    public boolean isMouseOverBar(final int x, final int y) {
        return x >= this.getX() && y >= this.getY() && x <= this.getX() + this.getDimension().getWidth() && y <= this.getY() + this.getFrameBoxHeight();
    }
    
    public void scrollFrame(final int ammount) {
        this.scrollAmmount += ammount;
        this.ticksSinceScroll = 0;
    }
    
    public void updateComponents() {
        ++this.ticksSinceScroll;
        if (this.scrollAmmount < this.getMaxScroll()) {
            this.scrollAmmount = this.getMaxScroll();
        }
        if (this.scrollAmmount > 0) {
            this.scrollAmmount = 0;
        }
        for (final Component c : this.getComponents()) {
            c.onUpdate();
            if (c instanceof Container) {
                final Container container = (Container)c;
                for (final Component component1 : container.getComponents()) {
                    component1.onUpdate();
                }
            }
            int yCount = this.getY() + this.getFrameBoxHeight();
            for (final Component component1 : this.getComponents()) {
                if (this.getComponents().indexOf(component1) < this.getComponents().indexOf(c)) {
                    yCount += (int)(component1.getDimension().getHeight() + 2.0);
                }
            }
            c.setyBase(yCount + 2);
            c.setyPos(c.getyBase() + this.scrollAmmount);
        }
        final int height = Wrapper.INSTANCE.mc().displayHeight / 3 + this.getComponents().size();
        this.setDimension(new Dimension(this.getDimension().width, height));
    }
    
    public int getMaxScroll() {
        if (this.getComponents().size() == 0) {
            return 0;
        }
        final Component last = this.getComponents().get(this.getComponents().size() - 1);
        final int maxLast = (int)(last.getyBase() + last.getDimension().getHeight());
        return this.getMaxY() - maxLast;
    }
    
    public int getMaxY() {
        return (int)(this.getY() + this.getDimension().getHeight());
    }
    
    public int getFrameBoxHeight() {
        return ClickGui.getTheme().getFrameHeight();
    }
    
    public boolean isPinned() {
        return this.pinned;
    }
    
    public void setPinned(final boolean pinned) {
        this.pinned = pinned;
    }
    
    public boolean isMaximized() {
        return this.maximized;
    }
    
    public void setMaximized(final boolean maximized) {
        this.maximized = maximized;
    }
    
    public boolean isMaximizible() {
        return this.maximizible;
    }
    
    public void setMaximizible(final boolean maximizible) {
        this.maximizible = maximizible;
    }
    
    public boolean isVisable() {
        return this.visable;
    }
    
    public void setVisable(final boolean visable) {
        this.visable = visable;
    }
    
    public boolean isPinnable() {
        return this.pinnable;
    }
    
    public void setPinnable(final boolean pinnable) {
        this.pinnable = pinnable;
    }
    
    public int getTicksSinceScroll() {
        return this.ticksSinceScroll;
    }
    
    public void setTicksSinceScroll(final int ticksSinceScroll) {
        this.ticksSinceScroll = ticksSinceScroll;
    }
    
    public int getScrollAmmount() {
        return this.scrollAmmount;
    }
    
    public void setScrollAmmount(final int scrollAmmount) {
        this.scrollAmmount = scrollAmmount;
    }
}
