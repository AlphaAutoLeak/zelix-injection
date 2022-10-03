package zelix.gui.clickguis.gishcode.base;

import zelix.gui.clickguis.gishcode.*;

public class Component extends Interactable
{
    private ComponentType componentType;
    private Component component;
    private String text;
    
    public Component(final int xPos, final int yPos, final int width, final int height, final ComponentType componentType, final Component component, final String text) {
        super(xPos, yPos, width, height);
        this.componentType = componentType;
        this.component = component;
        this.text = text;
    }
    
    public void render(final int x, final int y) {
        ClickGui.getTheme().getRenderer().get(this.componentType).drawComponent(this, x, y);
    }
    
    public void onUpdate() {
    }
    
    public ComponentType getComponentType() {
        return this.componentType;
    }
    
    public void setComponentType(final ComponentType componentType) {
        this.componentType = componentType;
    }
    
    public Component getComponent() {
        return this.component;
    }
    
    public void setComponent(final Component component) {
        this.component = component;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
}
