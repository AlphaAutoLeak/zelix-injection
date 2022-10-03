package zelix.gui.clickguis.caesium.components;

import zelix.gui.clickguis.caesium.components.listeners.ComponentListener;
import zelix.hack.*;
import zelix.gui.clickguis.caesium.*;
import zelix.gui.clickguis.caesium.util.*;
import java.awt.event.*;
import zelix.gui.clickguis.caesium.components.listeners.*;
import java.util.*;

public class GuiButton implements GuiComponent
{
    public static int expandedID;
    private int id;
    private Hack text;
    private ArrayList<ActionListener> clickListeners;
    private ArrayList<GuiComponent> guiComponents;
    private int width;
    private int posX;
    private int posY;
    
    public GuiButton(final Hack text) {
        this.clickListeners = new ArrayList<ActionListener>();
        this.guiComponents = new ArrayList<GuiComponent>();
        this.text = text;
        this.id = ++ClickGui.compID;
    }
    
    @Override
    public void render(final int posX, final int posY, final int width, final int mouseX, final int mouseY) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        final int height = this.getHeight();
        final String theme = Panel.theme;
        switch (theme) {
            case "Caesium": {
                this.renderCaesium(posX, posY, width, height);
                break;
            }
        }
    }
    
    private void renderCaesium(final int posX, final int posY, final int width, final int height) {
        RenderUtil.drawRect(posX, posY, posX + width - 1, posY + height, Panel.black100);
        int color = Panel.fontColor;
        if (this.text.isToggled()) {
            color = Panel.color;
        }
        Panel.fR.drawStringWithShadow(this.getText(), (float)(posX + width / 2 - Panel.fR.getStringWidth(this.getText()) / 2), (float)(posY + 2), color);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (GuiFrame.dragID == -1 && RenderUtil.isHovered(this.posX, this.posY, this.width, this.getHeight(), mouseX, mouseY)) {
            if (mouseButton == 1) {
                if (GuiButton.expandedID != this.id) {
                    GuiButton.expandedID = this.id;
                }
                else {
                    GuiButton.expandedID = -1;
                }
            }
            else if (mouseButton == 0) {
                for (final ActionListener listener : this.clickListeners) {
                    listener.actionPerformed(new ActionEvent(this, this.id, "click", System.currentTimeMillis(), 0));
                }
            }
        }
        if (GuiButton.expandedID == this.id) {
            for (final GuiComponent component : this.guiComponents) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }
    
    @Override
    public void keyTyped(final int keyCode, final char typedChar) {
        if (GuiButton.expandedID == this.id) {
            for (final GuiComponent component : this.guiComponents) {
                component.keyTyped(keyCode, typedChar);
            }
        }
    }
    
    @Override
    public int getWidth() {
        return 5 + Panel.fR.getStringWidth(this.text.getRenderName());
    }
    
    @Override
    public int getHeight() {
        return Panel.fR.FONT_HEIGHT + 3;
    }
    
    public String getText() {
        return this.text.getRenderName();
    }
    
    public int getButtonID() {
        return this.id;
    }
    
    public ArrayList<GuiComponent> getComponents() {
        return this.guiComponents;
    }
    
    public void addClickListener(final ActionListener actionlistener) {
        this.clickListeners.add(actionlistener);
    }
    
    public void addExtendListener(final ComponentListener listener) {
        listener.addComponents();
        this.guiComponents.addAll(listener.getComponents());
    }
    
    static {
        GuiButton.expandedID = -1;
    }
}
