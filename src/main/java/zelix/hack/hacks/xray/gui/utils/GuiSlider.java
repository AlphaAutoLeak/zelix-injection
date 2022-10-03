package zelix.hack.hacks.xray.gui.utils;

import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import org.lwjgl.opengl.*;

public class GuiSlider extends GuiButton
{
    public float sliderValue;
    private float sliderMaxValue;
    private boolean dragging;
    private String label;
    
    public GuiSlider(final int id, final int x, final int y, final String label, final float startingValue, final float maxValue) {
        super(id, x, y, 202, 20, label);
        this.dragging = false;
        this.label = label;
        this.sliderValue = startingValue;
        this.sliderMaxValue = maxValue;
    }
    
    public int getHoverState(final boolean par1) {
        return 0;
    }
    
    protected void mouseDragged(final Minecraft par1Minecraft, final int par2, final int par3) {
        if (this.dragging) {
            this.updateValue(par2);
        }
        this.displayString = this.label + ": " + (int)(this.sliderValue * this.sliderMaxValue);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (this.width - 8)), this.y, 0, 66, 4, 20);
        this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
    }
    
    private void updateValue(final int value) {
        this.sliderValue = (value - (this.x + 4)) / (this.width - 8);
        if (this.sliderValue < 0.0f) {
            this.sliderValue = 0.0f;
        }
        if (this.sliderValue > 1.0f) {
            this.sliderValue = 1.0f;
        }
    }
    
    public boolean mousePressed(final Minecraft par1Minecraft, final int par2, final int par3) {
        if (super.mousePressed(par1Minecraft, par2, par3)) {
            this.updateValue(par2);
            return this.dragging = true;
        }
        return false;
    }
    
    public void mouseReleased(final int par1, final int par2) {
        this.dragging = false;
    }
}
