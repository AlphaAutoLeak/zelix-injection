package zelix.gui.clickguis.caesium.components;

import zelix.gui.clickguis.caesium.Panel;
import zelix.value.*;
import zelix.gui.clickguis.caesium.*;
import java.awt.*;
import zelix.gui.clickguis.caesium.util.*;
import java.awt.event.*;
import java.util.*;

public class GuiToggleButton implements GuiComponent
{
    private BooleanValue text;
    private boolean toggled;
    private int posX;
    private int posY;
    private ArrayList<ActionListener> clickListeners;
    
    public GuiToggleButton(final BooleanValue text) {
        this.clickListeners = new ArrayList<ActionListener>();
        this.text = text;
    }
    
    @Override
    public void render(final int posX, final int posY, final int width, final int mouseX, final int mouseY) {
        this.posX = posX;
        this.posY = posY;
        final String theme = Panel.theme;
        switch (theme) {
            case "Caesium": {
                this.renderCaesium(posX, posY);
                break;
            }
        }
    }
    
    private void renderCaesium(final int posX, final int posY) {
        RenderUtil.drawFilledCircle(posX + 8, posY + 7, 6.0f, new Color(Panel.color));
        if (!this.toggled) {
            RenderUtil.drawFilledCircle(posX + 8, posY + 7, 5.0f, new Color(Panel.grey40_240));
        }
        Panel.fR.drawStringWithShadow(this.text.getRenderName(), (float)(posX + 17), (float)(posY + 3), Panel.fontColor);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        final int width = Panel.fR.getStringWidth(this.text.getRenderName()) + 10;
        if (RenderUtil.isHovered(this.posX, this.posY + 2, width, this.getHeight(), mouseX, mouseY)) {
            this.toggled = !this.toggled;
            for (final ActionListener listener : this.clickListeners) {
                listener.actionPerformed(new ActionEvent(this, this.hashCode(), "click", System.currentTimeMillis(), 0));
            }
        }
    }
    
    @Override
    public void keyTyped(final int keyCode, final char typedChar) {
    }
    
    @Override
    public int getWidth() {
        return Panel.fR.getStringWidth(this.text.getRenderName()) + 20;
    }
    
    @Override
    public int getHeight() {
        return Panel.fR.FONT_HEIGHT + 5;
    }
    
    public boolean isToggled() {
        return this.toggled;
    }
    
    public void setToggled(final boolean toggled) {
        this.toggled = toggled;
    }
    
    public void addClickListener(final ActionListener actionlistener) {
        this.clickListeners.add(actionlistener);
    }
}
