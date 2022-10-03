package zelix.gui.clickguis;

import net.minecraft.client.gui.*;
import zelix.hack.hacks.*;
import zelix.utils.hooks.visual.*;

public class Tooltip
{
    private FontRenderer fontRenderer;
    private String text;
    private int x;
    private int y;
    
    public Tooltip(final String text, final int x, final int y, final FontRenderer fontRenderer) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.fontRenderer = fontRenderer;
    }
    
    public String getText() {
        return this.text;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void render() {
        final int textColor = ClickGui.isLight ? ColorUtils.color(0, 0, 0, 255) : ColorUtils.color(200, 200, 200, 255);
        final int rectColor = ClickGui.isLight ? ColorUtils.color(155, 155, 155, 155) : ColorUtils.color(100, 100, 100, 100);
        final int aboveMouse = 8;
        final int width = this.fontRenderer.getStringWidth(this.text);
        RenderUtils.drawStringWithRect(this.getText(), this.getX() + 2, this.getY() - aboveMouse + 2, textColor, rectColor, rectColor);
    }
}
