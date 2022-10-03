package zelix.gui.clickguis.caesium.util;

public class FramePosition
{
    private int posX;
    private int posY;
    private boolean isExpanded;
    
    public FramePosition(final int posX, final int posY, final boolean isExpanded) {
        this.posX = posX;
        this.posY = posY;
        this.isExpanded = isExpanded;
    }
    
    public int getPosX() {
        return this.posX;
    }
    
    public void setPosX(final int posX) {
        this.posX = posX;
    }
    
    public int getPosY() {
        return this.posY;
    }
    
    public void setPosY(final int posY) {
        this.posY = posY;
    }
    
    public boolean isExpanded() {
        return this.isExpanded;
    }
    
    public void setExpanded(final boolean isExpanded) {
        this.isExpanded = isExpanded;
    }
}
