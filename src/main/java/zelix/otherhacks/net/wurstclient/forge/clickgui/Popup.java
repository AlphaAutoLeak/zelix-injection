package zelix.otherhacks.net.wurstclient.forge.clickgui;

public abstract class Popup
{
    private final Component owner;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean closing;
    
    public Popup(final Component owner) {
        this.owner = owner;
    }
    
    public abstract void handleMouseClick(final int p0, final int p1, final int p2);
    
    public abstract void render(final int p0, final int p1);
    
    public abstract int getDefaultWidth();
    
    public abstract int getDefaultHeight();
    
    public Component getOwner() {
        return this.owner;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public boolean isClosing() {
        return this.closing;
    }
    
    public void close() {
        this.closing = true;
    }
}
