package zelix.gui.clickguis.gishcode.elements;

import zelix.gui.clickguis.gishcode.base.*;
import java.util.*;

public class Dropdown extends Container
{
    private boolean maximized;
    private int dropdownHeight;
    
    public Dropdown(final int xPos, final int yPos, final int width, final int dropdownHeight, final Component component, final String text) {
        super(xPos, yPos, width, 0, ComponentType.DROPDOWN, component, text);
        this.maximized = false;
        this.dropdownHeight = dropdownHeight;
    }
    
    @Override
    public void render(final int x, final int y) {
        int height = this.dropdownHeight;
        if (this.maximized) {
            for (final Component component : this.getComponents()) {
                component.setxPos(this.getX());
                component.setyPos(this.getY() + height + 1);
                height += component.getDimension().height;
                component.getDimension().setSize(this.getDimension().width, component.getDimension().height);
            }
        }
        this.getDimension().setSize(this.getDimension().width, height);
        super.render(x, y);
    }
    
    @Override
    public void onUpdate() {
        int height = this.dropdownHeight;
        if (this.maximized) {
            for (final Component component : this.getComponents()) {
                component.setyPos(this.getY() + height + 1);
                height += component.getDimension().height;
                component.getDimension().setSize(this.getDimension().width, component.getDimension().height);
            }
        }
        this.getDimension().setSize(this.getDimension().width, height);
    }
    
    @Override
    public void onMouseDrag(final int x, final int y) {
        if (this.isMouseOver(x, y)) {
            for (final Component component : this.getComponents()) {
                if (component.isMouseOver(x, y)) {
                    component.onMouseDrag(x, y);
                }
            }
        }
    }
    
    @Override
    public void onMouseRelease(final int x, final int y, final int buttonID) {
        if (this.isMouseOver(x, y)) {
            for (final Component component : this.getComponents()) {
                if (component.isMouseOver(x, y)) {
                    component.onMouseRelease(x, y, buttonID);
                }
            }
        }
    }
    
    @Override
    public void onMousePress(final int x, final int y, final int buttonID) {
        if (x >= this.getX() && y >= this.getY() && x <= this.getX() + this.getDimension().width && y <= this.getY() + this.dropdownHeight) {
            if (buttonID == 1) {
                this.maximized = !this.maximized;
            }
        }
        else if (this.isMouseOver(x, y)) {
            for (final Component component : this.getComponents()) {
                if (component.isMouseOver(x, y)) {
                    component.onMousePress(x, y, buttonID);
                }
            }
        }
    }
    
    public boolean isMaximized() {
        return this.maximized;
    }
    
    public void setMaximized(final boolean maximized) {
        this.maximized = maximized;
    }
    
    public int getDropdownHeight() {
        return this.dropdownHeight;
    }
    
    public void setDropdownHeight(final int dropdownHeight) {
        this.dropdownHeight = dropdownHeight;
    }
}
