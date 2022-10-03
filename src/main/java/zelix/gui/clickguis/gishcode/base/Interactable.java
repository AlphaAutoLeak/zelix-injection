package zelix.gui.clickguis.gishcode.base;

import java.awt.*;

public class Interactable
{
    private int xPos;
    private int yPos;
    private int yBase;
    private Dimension dimension;
    
    public Interactable(final int xPos, final int yPos, final int width, final int height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.dimension = new Dimension(width, height);
    }
    
    public void onMousePress(final int x, final int y, final int buttonID) {
    }
    
    public void onMouseRelease(final int x, final int y, final int buttonID) {
    }
    
    public void onMouseDrag(final int x, final int y) {
    }
    
    public void onMouseScroll(final int scroll) {
    }
    
    public boolean isMouseOver(final int x, final int y) {
        return x >= this.xPos && y >= this.yPos && x <= this.xPos + this.dimension.width && y <= this.yPos + this.dimension.height;
    }
    
    public void onKeyPressed(final int key, final char character) {
    }
    
    public void onKeyReleased(final int key, final char character) {
    }
    
    public int getX() {
        return this.xPos;
    }
    
    public void setxPos(final int xPos) {
        this.xPos = xPos;
    }
    
    public int getY() {
        return this.yPos;
    }
    
    public void setyPos(final int yPos) {
        this.yPos = yPos;
    }
    
    public int getyBase() {
        return this.yBase;
    }
    
    public void setyBase(final int yBase) {
        this.yBase = yBase;
    }
    
    public Dimension getDimension() {
        return this.dimension;
    }
    
    public void setDimension(final Dimension dimension) {
        this.dimension = dimension;
    }
}
