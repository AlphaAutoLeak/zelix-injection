package zelix.gui.clickguis.caesium.components;

import zelix.gui.clickguis.caesium.Panel;
import zelix.gui.clickguis.caesium.components.listeners.*;
import zelix.gui.clickguis.caesium.*;
import zelix.gui.clickguis.caesium.util.*;
import java.awt.*;
import zelix.value.*;
import java.util.*;

public class GuiComboBox implements GuiComponent
{
    private ModeValue setting;
    private boolean extended;
    private int height;
    private int posX;
    private int posY;
    private int width;
    private ArrayList<ComboListener> comboListeners;
    
    public GuiComboBox(final ModeValue setting) {
        this.comboListeners = new ArrayList<ComboListener>();
        this.setting = setting;
    }
    
    public void addComboListener(final ComboListener comboListener) {
        this.comboListeners.add(comboListener);
    }
    
    @Override
    public void render(final int posX, final int posY, final int width, final int mouseX, final int mouseY) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        final String theme = Panel.theme;
        switch (theme) {
            case "Caesium": {
                this.renderCaesium();
                break;
            }
        }
    }
    
    public void renderCaesium() {
        if (this.extended) {
            RenderUtil.drawRect(this.posX, this.posY, this.posX + this.width, this.posY + Panel.fR.FONT_HEIGHT + 2, Panel.grey40_240);
            RenderUtil.drawHorizontalLine(this.posX, this.posX + this.width, this.posY, Panel.black195);
            RenderUtil.drawHorizontalLine(this.posX, this.posX + this.width, this.posY + Panel.fR.FONT_HEIGHT + 2, new Color(0, 0, 0, 150).getRGB());
            Panel.fR.drawStringWithShadow(this.setting.getRenderName() + "  -", (float)(this.posX + this.width / 2 - Panel.fR.getStringWidth(this.setting.getRenderName() + "  -") / 2), (float)(this.posY + 2), Panel.fontColor);
            int innerHeight = Panel.fR.FONT_HEIGHT + 5;
            for (final Mode comb : this.setting.getModes()) {
                final String combo = comb.getName();
                if (this.setting.getSelectMode().getName().equalsIgnoreCase(combo)) {
                    Panel.fR.drawStringWithShadow(combo, (float)(this.posX + 10), (float)(this.posY + innerHeight), Panel.color);
                }
                else {
                    Panel.fR.drawStringWithShadow(combo, (float)(this.posX + 5), (float)(this.posY + innerHeight), Panel.fontColor);
                }
                innerHeight += Panel.fR.FONT_HEIGHT + 2;
            }
            this.height = innerHeight + 2;
        }
        else {
            RenderUtil.drawRect(this.posX, this.posY, this.posX + this.width, this.posY + Panel.fR.FONT_HEIGHT + 2, Panel.grey40_240);
            RenderUtil.drawHorizontalLine(this.posX, this.posX + this.width, this.posY, Panel.black195);
            RenderUtil.drawHorizontalLine(this.posX, this.posX + this.width, this.posY + Panel.fR.FONT_HEIGHT + 2, Panel.black195);
            Panel.fR.drawStringWithShadow(this.setting.getRenderName() + "  +", (float)(this.posX + this.width / 2 - Panel.fR.getStringWidth(this.setting.getRenderName() + "  +") / 2), (float)(this.posY + 2), Panel.fontColor);
            this.height = Panel.fR.FONT_HEIGHT + 4;
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (RenderUtil.isHovered(this.posX, this.posY, this.width, Panel.fR.FONT_HEIGHT + 2, mouseX, mouseY)) {
            this.extended = !this.extended;
        }
        if (this.extended && RenderUtil.isHovered(this.posX, this.posY + Panel.fR.FONT_HEIGHT + 2, this.width, (Panel.fR.FONT_HEIGHT + 2) * this.setting.getModes().length, mouseX, mouseY) && mouseButton == 0) {
            final int h = Panel.fR.FONT_HEIGHT + 2;
            for (int i = 1; i <= this.setting.getModes().length + 1; ++i) {
                if (RenderUtil.isHovered(this.posX, this.posY + h * i, this.width, h * i, mouseX, mouseY)) {
                    for (final Mode m : this.setting.getModes()) {
                        if (m == this.setting.getModes()[i - 1]) {
                            m.setToggled(true);
                        }
                        else {
                            m.setToggled(false);
                        }
                    }
                }
            }
            for (final ComboListener comboListener : this.comboListeners) {
                comboListener.comboChanged(this.setting.getSelectMode().getName());
            }
        }
    }
    
    @Override
    public void keyTyped(final int keyCode, final char typedChar) {
    }
    
    @Override
    public int getWidth() {
        return 0;
    }
    
    @Override
    public int getHeight() {
        return this.height;
    }
    
    public ModeValue getSetting() {
        return this.setting;
    }
    
    public void setSetting(final ModeValue setting) {
        this.setting = setting;
    }
}
