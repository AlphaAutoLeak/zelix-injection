package zelix.gui.clickguis.caesium.components;

import zelix.gui.clickguis.caesium.*;

public class GuiLabel implements GuiComponent
{
    private String text;
    
    public GuiLabel(final String text) {
        this.text = text;
    }
    
    @Override
    public void render(final int posX, final int posY, final int width, final int mouseX, final int mouseY) {
        Panel.fR.drawStringWithShadow(this.text, (float)(posX + width / 2 - Panel.fR.getStringWidth(this.text) / 2), (float)(posY + 2), Panel.fontColor);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public void keyTyped(final int keyCode, final char typedChar) {
    }
    
    @Override
    public int getWidth() {
        return Panel.fR.getStringWidth(this.text) + 4;
    }
    
    @Override
    public int getHeight() {
        return Panel.fR.FONT_HEIGHT + 2;
    }
}
