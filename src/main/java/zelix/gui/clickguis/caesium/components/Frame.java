package zelix.gui.clickguis.caesium.components;

public interface Frame
{
    void initialize();
    
    void render(final int p0, final int p1);
    
    void mouseClicked(final int p0, final int p1, final int p2);
    
    void keyTyped(final int p0, final char p1);
}
