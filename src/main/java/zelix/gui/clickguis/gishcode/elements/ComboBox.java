package zelix.gui.clickguis.gishcode.elements;

import zelix.gui.clickguis.gishcode.listener.*;
import zelix.gui.clickguis.gishcode.base.*;
import net.minecraft.client.*;
import java.util.*;

public class ComboBox extends Container
{
    public ArrayList<ComboBoxListener> listeners;
    private String[] elements;
    private int selectedIndex;
    private boolean selected;
    
    public ComboBox(final int xPos, final int yPos, final int width, final int height, final Component component, final String text, final String... elements) {
        super(xPos, yPos, width, height, ComponentType.COMBO_BOX, component, text);
        this.listeners = new ArrayList<ComboBoxListener>();
        this.elements = elements;
    }
    
    @Override
    public void onMousePress(final int x, final int y, final int buttonID) {
        if (this.isMouseOver(x, y)) {
            if (buttonID == 1) {
                this.selected = !this.selected;
            }
            if (buttonID == 0) {
                int offset = this.getDimension().height + 2;
                final String[] elements = this.getElements();
                for (int i = 0; i < elements.length; ++i) {
                    if (i != this.getSelectedIndex()) {
                        if (y >= offset && y <= offset + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT) {
                            this.setSelectedIndex(i);
                            this.setSelected(false);
                            break;
                        }
                        offset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 2;
                    }
                }
            }
        }
    }
    
    public String[] getElements() {
        return this.elements;
    }
    
    public void setElements(final String[] elements) {
        this.elements = elements;
        this.selectedIndex = 0;
    }
    
    public int getSelectedIndex() {
        return this.selectedIndex;
    }
    
    public void setSelectedIndex(final int selectedIndex) {
        this.selectedIndex = selectedIndex;
        for (final ComboBoxListener listener : this.listeners) {
            listener.onComboBoxSelectionChange(this);
        }
    }
    
    public String getSelectedElement() {
        return this.elements[this.selectedIndex];
    }
    
    public boolean isSelected() {
        return this.selected;
    }
    
    public void setSelected(final boolean selected) {
        this.selected = selected;
        for (final ComboBoxListener listener : this.listeners) {
            listener.onComboBoxSelectionChange(this);
        }
    }
    
    public ArrayList<ComboBoxListener> getListeners() {
        return this.listeners;
    }
    
    public void addListeners(final ComboBoxListener listeners) {
        this.listeners.add(listeners);
    }
}
