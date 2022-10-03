package zelix.gui.clickguis.gishcode.elements;

import zelix.gui.clickguis.gishcode.listener.*;
import zelix.hack.*;
import zelix.gui.clickguis.gishcode.base.*;
import java.util.*;

public class Button extends Component
{
    public ArrayList<ComponentClickListener> listeners;
    private Hack mod;
    private boolean enabled;
    
    public Button(final int xPos, final int yPos, final int width, final int height, final Component component, final String text) {
        super(xPos, yPos, width, height, ComponentType.BUTTON, component, text);
        this.listeners = new ArrayList<ComponentClickListener>();
        this.enabled = false;
    }
    
    public Button(final int xPos, final int yPos, final int width, final int height, final Component component, final String text, final Hack mod) {
        super(xPos, yPos, width, height, ComponentType.BUTTON, component, text);
        this.listeners = new ArrayList<ComponentClickListener>();
        this.enabled = false;
        this.mod = mod;
    }
    
    public void addListeners(final ComponentClickListener listener) {
        this.listeners.add(listener);
    }
    
    @Override
    public void onMousePress(final int x, final int y, final int button) {
        if (button != 0) {
            return;
        }
        this.enabled = !this.enabled;
        for (final ComponentClickListener listener : this.listeners) {
            listener.onComponenetClick(this, button);
        }
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public ArrayList<ComponentClickListener> getListeners() {
        return this.listeners;
    }
    
    public Hack getMod() {
        return this.mod;
    }
}
