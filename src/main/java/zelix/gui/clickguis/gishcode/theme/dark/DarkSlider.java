package zelix.gui.clickguis.gishcode.theme.dark;

import zelix.gui.clickguis.gishcode.theme.*;
import zelix.gui.clickguis.gishcode.base.*;
import zelix.gui.clickguis.gishcode.elements.*;
import zelix.hack.hacks.*;
import zelix.utils.hooks.visual.*;

public class DarkSlider extends ComponentRenderer
{
    public DarkSlider(final Theme theme) {
        super(ComponentType.SLIDER, theme);
    }
    
    @Override
    public void drawComponent(final Component component, final int mouseX, final int mouseY) {
        final Slider slider = (Slider)component;
        final int width = (int)(slider.getDimension().getWidth() * slider.getPercent());
        final int mainColor = ClickGui.isLight ? ColorUtils.color(255, 255, 255, 255) : ColorUtils.color(0, 0, 0, 255);
        final int mainColorInv = ClickGui.isLight ? ColorUtils.color(0, 0, 0, 255) : ColorUtils.color(255, 255, 255, 255);
        final int strColor = ClickGui.isLight ? ColorUtils.color(0.3f, 0.3f, 0.3f, 1.0f) : ColorUtils.color(0.5f, 0.5f, 0.5f, 1.0f);
        this.theme.fontRenderer.drawStringWithShadow(slider.getText(), slider.getX() + 4, slider.getY() + 2, strColor);
        this.theme.fontRenderer.drawStringWithShadow(slider.getValue() + "", slider.getX() + slider.getDimension().width - this.theme.fontRenderer.getStringWidth(slider.getValue() + "") - 2, slider.getY() + 2, mainColorInv);
        RenderUtils.drawRect(slider.getX(), slider.getY() + slider.getDimension().height / 2 + 3, slider.getX() + width + 3, slider.getY() + slider.getDimension().height / 2 + 6, mainColorInv);
        RenderUtils.drawRect(slider.getX(), slider.getY() + slider.getDimension().height / 2 + 3, slider.getX() + width, slider.getY() + slider.getDimension().height / 2 + 6, ClickGui.getColor());
    }
    
    @Override
    public void doInteractions(final Component component, final int mouseX, final int mouseY) {
    }
}
