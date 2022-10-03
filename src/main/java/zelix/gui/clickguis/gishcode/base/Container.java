package zelix.gui.clickguis.gishcode.base;

import java.util.*;

public class Container extends Component
{
    private ArrayList<Component> components;
    
    public Container(final int xPos, final int yPos, final int width, final int height, final ComponentType componentType, final Component component, final String text) {
        super(xPos, yPos, width, height, componentType, component, text);
        this.components = new ArrayList<Component>();
    }
    
    public void addComponent(final Component c) {
        this.components.add(c);
    }
    
    public void removeCompoent(final Component c) {
        this.components.remove(c);
    }
    
    public void renderChildren(final int mouseX, final int mouseY) {
        for (final Component c : this.getComponents()) {
            c.render(mouseX, mouseY);
        }
    }
    
    public ArrayList<Component> getComponents() {
        return this.components;
    }
}
