package zelix.gui.clickguis.gishcode.base;

import java.awt.*;
import zelix.gui.clickguis.gishcode.theme.*;
import zelix.utils.hooks.visual.*;
import org.lwjgl.opengl.*;

public abstract class ComponentRenderer
{
    protected static final Color tooltipColor;
    public Theme theme;
    private ComponentType type;
    
    public ComponentRenderer(final ComponentType type, final Theme theme) {
        this.type = type;
        this.theme = theme;
    }
    
    public abstract void drawComponent(final Component p0, final int p1, final int p2);
    
    public abstract void doInteractions(final Component p0, final int p1, final int p2);
    
    public void drawExpanded(final int x, final int y, final int size, final boolean expanded, final int color) {
        GLUtils.glColor(color);
    }
    
    public void drawPin(final int x, final int y, final int size, final boolean expanded, final int color) {
        GLUtils.glColor(color);
    }
    
    public void drawArrow(final int x, final int y, final int size, final boolean right, final int color) {
        GLUtils.glColor(color);
    }
    
    public void drawArrow(final int x, final int y, final int size, final boolean right) {
        this.drawArrow(x, y, size, right, -1);
    }
    
    public void drawFilledRect(final float x, final float y, final float x1, final float y1) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glBegin(7);
        GL11.glVertex3f(x, y1, 1.0f);
        GL11.glVertex3f(x1, y1, 1.0f);
        GL11.glVertex3f(x1, y, 1.0f);
        GL11.glVertex3f(x, y, 1.0f);
        GL11.glEnd();
        GL11.glEnable(3553);
    }
    
    public void drawRect(final float x, final float y, final float x1, final float y1, final float thickness) {
        this.drawFilledRect(x + thickness, y, x1 - thickness, y + thickness);
        this.drawFilledRect(x, y, x + thickness, y1);
        this.drawFilledRect(x1 - thickness, y, x1, y1);
        this.drawFilledRect(x + thickness, y1 - thickness, x1 - thickness, y1);
    }
    
    static {
        tooltipColor = new Color(0.0f, 0.5f, 1.0f, 0.75f);
    }
}
