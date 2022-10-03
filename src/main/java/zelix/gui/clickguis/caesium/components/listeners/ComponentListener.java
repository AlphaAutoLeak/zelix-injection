package zelix.gui.clickguis.caesium.components.listeners;

import java.util.*;
import zelix.gui.clickguis.caesium.components.*;

public abstract class ComponentListener
{
    private final ArrayList<GuiComponent> components;
    
    public ComponentListener() {
        this.components = new ArrayList<GuiComponent>();
    }
    
    protected void add(final GuiComponent component) {
        this.components.add(component);
    }
    
    public void clearComponents() {
        this.components.clear();
    }
    
    public ArrayList<GuiComponent> getComponents() {
        return this.components;
    }
    
    public abstract void addComponents();
}
