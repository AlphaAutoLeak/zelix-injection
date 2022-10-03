package zelix.gui.clickguis.caesium.components;

import zelix.gui.clickguis.caesium.Panel;
import zelix.gui.clickguis.caesium.components.listeners.*;
import zelix.gui.clickguis.caesium.*;
import org.lwjgl.input.*;
import java.awt.*;
import zelix.gui.clickguis.caesium.util.*;
import java.util.*;

public class GuiGetKey implements GuiComponent
{
    private boolean wasChanged;
    private boolean allowChange;
    private int key;
    private int posX;
    private int posY;
    private int width;
    private String text;
    private ArrayList<KeyListener> keylisteners;
    
    public GuiGetKey(final String text, final int key) {
        this.keylisteners = new ArrayList<KeyListener>();
        this.text = text;
        this.key = key;
        if (key < 0) {
            this.key = 0;
        }
    }
    
    public void addKeyListener(final KeyListener listener) {
        this.keylisteners.add(listener);
    }
    
    @Override
    public void render(final int posX, final int posY, final int width, final int mouseX, final int mouseY) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        final String theme = Panel.theme;
        switch (theme) {
            case "Caesium": {
                this.renderCaesium(posX, posY);
                break;
            }
        }
    }
    
    private void renderCaesium(final int posX, final int posY) {
        final String keyString = Keyboard.getKeyName(this.key);
        if (this.allowChange) {
            this.wasChanged = !this.wasChanged;
        }
        else {
            this.wasChanged = true;
        }
        RenderUtil.drawVerticalGradient(posX, posY, this.width, 14.0f, new Color(Panel.color).darker().getRGB(), new Color(Panel.color).brighter().getRGB());
        Panel.fR.drawStringWithShadow(this.text + ":", (float)(posX + 3), (float)(posY + 3), Panel.fontColor);
        if (this.wasChanged) {
            Panel.fR.drawStringWithShadow(keyString, (float)(posX + this.width - Panel.fR.getStringWidth(keyString) - 3), (float)(posY + 3), Panel.fontColor);
        }
        else {
            Panel.fR.drawStringWithShadow(keyString, posX + this.width - Panel.fR.getStringWidth(keyString) - 3, posY + 3, Panel.fontColor);
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        final String keyString = Keyboard.getKeyName(this.key);
        final int w = Panel.fR.getStringWidth(this.text);
        if (RenderUtil.isHovered(this.posX, this.posY, this.width, 11, mouseX, mouseY)) {
            this.allowChange = true;
        }
        else {
            this.allowChange = false;
        }
    }
    
    @Override
    public void keyTyped(final int keyCode, final char typedChar) {
        if (this.allowChange) {
            for (final KeyListener listener : this.keylisteners) {
                listener.keyChanged(keyCode);
            }
            this.key = keyCode;
            this.allowChange = false;
        }
    }
    
    @Override
    public int getWidth() {
        return Panel.fR.getStringWidth(this.text + Keyboard.getKeyName(this.key)) + 15;
    }
    
    @Override
    public int getHeight() {
        return Panel.fR.FONT_HEIGHT + 4;
    }
}
