package zelix.gui.clickguis.gishcode.elements;

import zelix.gui.clickguis.gishcode.listener.*;
import zelix.gui.clickguis.gishcode.base.*;
import zelix.value.*;
import java.util.*;

public class CheckButton extends Component
{
    public ArrayList<CheckButtonClickListener> listeners;
    private boolean enabled;
    private ModeValue modeValue;
    
    public CheckButton(final int xPos, final int yPos, final int width, final int height, final Component component, final String text, final boolean enabled, final ModeValue modeValue) {
        super(xPos, yPos, width, height, ComponentType.CHECK_BUTTON, component, text);
        this.listeners = new ArrayList<CheckButtonClickListener>();
        this.enabled = false;
        this.modeValue = null;
        this.enabled = enabled;
        this.modeValue = modeValue;
    }
    
    @Override
    public void onMousePress(final int x, final int y, final int buttonID) {
        if (this.modeValue != null) {
            for (final Mode mode : this.modeValue.getModes()) {
                mode.setToggled(false);
            }
            this.enabled = true;
        }
        else {
            this.enabled = !this.enabled;
        }
        for (final CheckButtonClickListener listener : this.listeners) {
            listener.onCheckButtonClick(this);
        }
    }
    
    public ArrayList<CheckButtonClickListener> getListeners() {
        return this.listeners;
    }
    
    public void addListeners(final CheckButtonClickListener listener) {
        this.listeners.add(listener);
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public ModeValue getModeValue() {
        return this.modeValue;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
