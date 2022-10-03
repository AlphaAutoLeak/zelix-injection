package zelix.otherhacks.net.wurstclient.forge.clickgui;

public abstract class Component
{
    private int x;
    private int y;
    private int width;
    private int height;
    private Window parent;
    
    public void handleMouseClick(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    public abstract void render(final int p0, final int p1, final float p2);
    
    public abstract int getDefaultWidth();
    
    public abstract int getDefaultHeight();
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        if (this.x != x) {
            this.invalidateParent();
        }
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        if (this.y != y) {
            this.invalidateParent();
        }
        this.y = y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        if (this.width != width) {
            this.invalidateParent();
        }
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        if (this.height != height) {
            this.invalidateParent();
        }
        this.height = height;
    }
    
    public Window getParent() {
        return this.parent;
    }
    
    public void setParent(final Window parent) {
        this.parent = parent;
    }
    
    private void invalidateParent() {
        if (this.parent != null) {
            this.parent.invalidate();
        }
    }
}
