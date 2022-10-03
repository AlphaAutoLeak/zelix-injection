package zelix.gui.clickguis.caesium.components;

import zelix.gui.clickguis.caesium.Panel;
import zelix.value.*;
import zelix.gui.clickguis.caesium.components.listeners.*;
import zelix.gui.clickguis.caesium.*;
import org.lwjgl.input.*;
import net.minecraft.util.math.*;
import java.util.*;
import zelix.gui.clickguis.caesium.util.*;
import java.awt.*;

public class GuiSlider implements GuiComponent
{
    private static int dragId;
    private int round;
    private int id;
    private int width;
    private int posX;
    private int posY;
    private double min;
    private double max;
    private double current;
    private double c;
    private boolean wasSliding;
    private NumberValue text;
    private ArrayList<ValueListener> valueListeners;
    
    public GuiSlider(final NumberValue text, final double min, final double max, final double current, final int round) {
        this.valueListeners = new ArrayList<ValueListener>();
        this.text = text;
        this.min = min;
        this.max = max;
        this.current = current;
        this.c = current / max;
        this.round = round;
        this.id = ++Panel.compID;
    }
    
    public void addValueListener(final ValueListener vallistener) {
        this.valueListeners.add(vallistener);
    }
    
    @Override
    public void render(final int posX, final int posY, final int width, final int mouseX, final int mouseY) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        final boolean hovered = RenderUtil.isHovered(posX, posY, width, this.getHeight(), mouseX, mouseY);
        if (Mouse.isButtonDown(0) && (GuiSlider.dragId == this.id || GuiSlider.dragId == -1) && hovered) {
            if (mouseX < posX + 2) {
                this.current = this.min;
            }
            else if (mouseX > posX + width) {
                this.current = this.max;
            }
            else {
                final double diff = this.max - this.min;
                final double val = this.min + MathHelper.clamp((mouseX - posX + 3) / width, 0.0, 1.0) * diff;
                if (this.round == 0) {
                    this.current = (int)val;
                }
                else {
                    this.current = (float)val;
                }
            }
            GuiSlider.dragId = this.id;
            for (final ValueListener listener : this.valueListeners) {
                listener.valueUpdated(this.current);
            }
            this.wasSliding = true;
        }
        else if (!Mouse.isButtonDown(0) && this.wasSliding) {
            for (final ValueListener listener : this.valueListeners) {
                listener.valueChanged(this.current);
            }
            GuiSlider.dragId = -1;
            this.wasSliding = false;
        }
        final double percent = (this.current - this.min) / (this.max - this.min);
        final String theme = Panel.theme;
        switch (theme) {
            case "Caesium": {
                this.renderCaesium(percent);
                break;
            }
        }
    }
    
    private void renderCaesium(final double percent) {
        String value;
        if (this.round == 0) {
            value = "" + Math.round(this.current);
        }
        else {
            value = "" + MathUtil.round(this.current, this.round);
        }
        final Color color = new Color(Panel.color);
        Panel.fR.drawStringWithShadow(this.text.getRenderName() + ":", (float)(this.posX + 3), (float)(this.posY + 3), Panel.fontColor);
        Panel.fR.drawStringWithShadow(value, (float)(this.posX + this.width - Panel.fR.getStringWidth(value) - 3), (float)(this.posY + 3), Panel.fontColor);
        RenderUtil.drawRect(this.posX, this.posY + Panel.fR.FONT_HEIGHT + 3, this.posX + this.width - 2, this.posY + Panel.fR.FONT_HEIGHT + 5, Color.black.getRGB());
        RenderUtil.drawHorizontalGradient(this.posX, this.posY + Panel.fR.FONT_HEIGHT + 3, (float)(percent * this.width - 2.0), 2.0f, color.darker().getRGB(), color.brighter().getRGB());
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public void keyTyped(final int keyCode, final char typedChar) {
    }
    
    @Override
    public int getWidth() {
        return Panel.fR.getStringWidth(this.text.getRenderName() + ((this.round == 0) ? Math.round(this.current) : MathUtil.round(this.current, this.round))) + 68;
    }
    
    @Override
    public int getHeight() {
        return Panel.fR.FONT_HEIGHT + 6;
    }
    
    static {
        GuiSlider.dragId = -1;
    }
}
