package zelix.gui.clickguis.gishcode.theme.dark;

import zelix.gui.clickguis.gishcode.base.Component;
import zelix.gui.clickguis.gishcode.theme.*;
import zelix.gui.clickguis.gishcode.base.*;
import zelix.gui.clickguis.gishcode.elements.*;
import zelix.hack.hacks.*;
import zelix.hack.hacks.ClickGui;
import zelix.utils.hooks.visual.*;
import java.awt.*;
import zelix.managers.*;
import zelix.gui.clickguis.*;
import zelix.gui.clickguis.gishcode.*;

public class DarkExpandingButton extends ComponentRenderer
{
    public DarkExpandingButton(final Theme theme) {
        super(ComponentType.EXPANDING_BUTTON, theme);
    }
    
    @Override
    public void drawComponent(final Component component, final int mouseX, final int mouseY) {
        final ExpandingButton button = (ExpandingButton)component;
        final String text = button.getText();
        final int mainColor = ClickGui.isLight ? ColorUtils.color(255, 255, 255, 155) : ColorUtils.color(0, 0, 0, 155);
        final int mainColorInv = ClickGui.isLight ? ColorUtils.color(0, 0, 0, 255) : ColorUtils.color(255, 255, 255, 255);
        if (button.isMaximized()) {
            RenderUtils.drawRect(button.getX(), button.getY(), button.getX() + button.getDimension().width - 1, button.getY() + button.getDimension().height - 1, mainColor);
        }
        if (button.isEnabled()) {
            RenderUtils.drawRect(button.getX(), button.getY(), button.getX() + button.getDimension().width - 1, button.getY() + 14, mainColor);
            RenderUtils.drawRect(button.getX(), button.getY(), button.getX() + button.getDimension().width - 95, button.getY() + 14, ClickGui.getColor());
            this.theme.fontRenderer.drawStringWithShadow(text, button.getX() + button.getDimension().width / 2 - this.theme.fontRenderer.getStringWidth(text) / 2, button.getY() + (button.getButtonHeight() / 2 - this.theme.fontRenderer.FONT_HEIGHT / 4) - 1, ClickGui.getColor());
        }
        else {
            RenderUtils.drawRect(button.getX(), button.getY(), button.getX() + button.getDimension().width - 1, button.getY() + 14, mainColor);
            this.theme.fontRenderer.drawStringWithShadow(text, button.getX() + button.getDimension().width / 2 - this.theme.fontRenderer.getStringWidth(text) / 2, button.getY() + (button.getButtonHeight() / 2 - this.theme.fontRenderer.FONT_HEIGHT / 4) - 1, mainColorInv);
        }
        if (button.isMaximized()) {
            RenderUtils.drawRect(button.getX(), button.getY() + button.getButtonHeight() - 1, button.getX() + button.getDimension().width, button.getY() + button.getButtonHeight(), ClickGui.getColor());
            RenderUtils.drawRect(button.getX(), button.getY() + button.getDimension().height - 1, button.getX() + button.getDimension().width, button.getY() + button.getDimension().height, ClickGui.getColor());
        }
        if (!button.isMaximized()) {
            this.drawExpanded(button.getX() + button.getDimension().width - 15, button.getY() + 3, 13, false, new Color(255, 255, 255, 255).hashCode());
        }
        else {
            this.drawExpanded(button.getX() + button.getDimension().width - 15, button.getY() + 3, 13, true, new Color(255, 255, 255, 255).hashCode());
        }
        if (button.isMaximized()) {
            button.renderChildren(mouseX, mouseY);
        }
        final String description = button.hack.getDescription();
        if (description != null && button.isMouseOver(mouseX, mouseY) && !button.isMaximized() && HackManager.getHack("ClickGui").isToggledValue("Tooltip")) {
            ClickGuiScreen.tooltip = new Tooltip(description, mouseX, mouseY, this.theme.fontRenderer);
        }
    }
    
    @Override
    public void doInteractions(final Component component, final int mouseX, final int mouseY) {
    }
}
