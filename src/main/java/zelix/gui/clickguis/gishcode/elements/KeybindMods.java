package zelix.gui.clickguis.gishcode.elements;

import zelix.hack.*;
import zelix.gui.clickguis.gishcode.base.*;
import org.lwjgl.input.*;
import zelix.utils.*;

public class KeybindMods extends Component
{
    private Hack mod;
    private boolean editing;
    
    public KeybindMods(final int xPos, final int yPos, final int width, final int height, final Component component, final Hack mod) {
        super(xPos, yPos, width, height, ComponentType.KEYBIND, component, "");
        this.mod = mod;
    }
    
    @Override
    public void onUpdate() {
        if (Keyboard.getEventKeyState() && this.editing) {
            if (Keyboard.getEventKey() == 211) {
                this.mod.setKey(-1);
            }
            else {
                this.mod.setKey(Keyboard.getEventKey());
            }
            this.editing = false;
        }
    }
    
    @Override
    public void onMousePress(final int x, final int y, final int buttonID) {
        if (x > this.getX() + Wrapper.INSTANCE.fontRenderer().getStringWidth("Key") + 6 && x < this.getX() + this.getDimension().width && y > this.getY() && y < this.getY() + this.getDimension().height) {
            this.editing = !this.editing;
        }
    }
    
    public Hack getMod() {
        return this.mod;
    }
    
    public boolean isEditing() {
        return this.editing;
    }
    
    public void setEditing(final boolean editing) {
        this.editing = editing;
    }
}
