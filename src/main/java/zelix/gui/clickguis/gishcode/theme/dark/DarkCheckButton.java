package zelix.gui.clickguis.gishcode.theme.dark;

import zelix.gui.clickguis.gishcode.theme.*;
import zelix.gui.clickguis.gishcode.base.*;
import zelix.gui.clickguis.gishcode.elements.*;
import zelix.hack.hacks.*;
import zelix.utils.hooks.visual.*;
import zelix.utils.*;
import zelix.value.*;

public class DarkCheckButton extends ComponentRenderer
{
    public DarkCheckButton(final Theme theme) {
        super(ComponentType.CHECK_BUTTON, theme);
    }
    
    @Override
    public void drawComponent(final Component component, final int mouseX, final int mouseY) {
        final CheckButton button = (CheckButton)component;
        final String text = button.getText();
        final int mainColor = ClickGui.isLight ? ColorUtils.color(255, 255, 255, 255) : ColorUtils.color(0, 0, 0, 255);
        final int mainColorInv = ClickGui.isLight ? ColorUtils.color(0, 0, 0, 255) : ColorUtils.color(255, 255, 255, 255);
        final int strColor = ClickGui.isLight ? ColorUtils.color(0.3f, 0.3f, 0.3f, 1.0f) : ColorUtils.color(0.5f, 0.5f, 0.5f, 1.0f);
        if (button.getModeValue() == null) {
            this.theme.fontRenderer.drawStringWithShadow("> " + text, button.getX() + 5, MathUtils.getMiddle(button.getY(), button.getY() + button.getDimension().height) - this.theme.fontRenderer.FONT_HEIGHT / 3 - 1, button.isEnabled() ? mainColorInv : strColor);
            return;
        }
        for (final Mode mode : button.getModeValue().getModes()) {
            if (mode.getName().equals(text)) {
                this.theme.fontRenderer.drawStringWithShadow("- " + text, button.getX() + 5, MathUtils.getMiddle(button.getY(), button.getY() + button.getDimension().height) - this.theme.fontRenderer.FONT_HEIGHT / 3 - 1, mode.isToggled() ? mainColorInv : strColor);
            }
        }
    }
    
    @Override
    public void doInteractions(final Component component, final int mouseX, final int mouseY) {
    }
}
