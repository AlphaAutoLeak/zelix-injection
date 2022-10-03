package zelix.gui.clickguis.gishcode.elements;

import zelix.gui.clickguis.gishcode.listener.*;
import zelix.hack.*;
import zelix.gui.clickguis.gishcode.base.*;
import java.util.*;

public class ExpandingButton extends Container
{
    public ArrayList<ComponentClickListener> listeners;
    private boolean enabled;
    private boolean maximized;
    private int buttonHeight;
    private Component component;
    public Hack hack;
    
    public ExpandingButton(final int xPos, final int yPos, final int width, final int buttonHeight, final Component component, final String text) {
        super(xPos, yPos, width, 0, ComponentType.EXPANDING_BUTTON, component, text);
        this.listeners = new ArrayList<ComponentClickListener>();
        this.enabled = false;
        this.maximized = false;
        this.buttonHeight = buttonHeight;
        this.component = component;
    }
    
    public ExpandingButton(final int xPos, final int yPos, final int width, final int buttonHeight, final Component component, final String text, final Hack hack) {
        super(xPos, yPos, width, 0, ComponentType.EXPANDING_BUTTON, component, text);
        this.listeners = new ArrayList<ComponentClickListener>();
        this.enabled = false;
        this.maximized = false;
        this.buttonHeight = buttonHeight;
        this.component = component;
        this.hack = hack;
    }
    
    @Override
    public void render(final int x, final int y) {
        int height = this.buttonHeight;
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
        int height = this.buttonHeight;
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
    public void onKeyPressed(final int key, final char character) {
        for (final Component component : this.getComponents()) {
            component.onKeyPressed(key, character);
        }
    }
    
    @Override
    public void onKeyReleased(final int key, final char character) {
        for (final Component component : this.getComponents()) {
            component.onKeyReleased(key, character);
        }
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
        if (this.isMouseOverButton(x, y)) {
            if (buttonID == 0) {
                this.enabled = !this.enabled;
                for (final ComponentClickListener listener : this.listeners) {
                    listener.onComponenetClick(this, buttonID);
                }
            }
            else if (buttonID == 1) {
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
    
    public boolean isMouseOverButton(final int x, final int y) {
        return x >= this.getX() && y >= this.getY() && x <= this.getX() + this.getDimension().width && y <= this.getY() + this.buttonHeight;
    }
    
    public void addListner(final ComponentClickListener listener) {
        this.listeners.add(listener);
    }
    
    public ArrayList<ComponentClickListener> getListeners() {
        return this.listeners;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isMaximized() {
        return this.maximized;
    }
    
    public void setMaximized(final boolean maximized) {
        this.maximized = maximized;
    }
    
    public int getButtonHeight() {
        return this.buttonHeight;
    }
    
    public void setButtonHeight(final int buttonHeight) {
        this.buttonHeight = buttonHeight;
    }
    
    public Hack getMod() {
        return this.hack;
    }
}
