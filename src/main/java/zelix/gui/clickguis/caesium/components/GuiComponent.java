package zelix.gui.clickguis.caesium.components;

public interface GuiComponent
{
    void render(final int p0, final int p1, final int p2, final int p3, final int p4);
    
    void mouseClicked(final int p0, final int p1, final int p2);
    
    void keyTyped(final int p0, final char p1);
    
    int getWidth();
    
    int getHeight();
}
