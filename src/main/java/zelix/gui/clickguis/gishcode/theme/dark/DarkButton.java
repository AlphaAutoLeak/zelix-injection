package zelix.gui.clickguis.gishcode.theme.dark;

import zelix.gui.clickguis.gishcode.theme.*;
import zelix.gui.clickguis.gishcode.base.*;
import zelix.gui.clickguis.gishcode.elements.*;
import zelix.utils.hooks.visual.*;

public class DarkButton extends ComponentRenderer
{
    public DarkButton(final Theme theme) {
        super(ComponentType.BUTTON, theme);
    }
    
    @Override
    public void drawComponent(final Component component, final int mouseX, final int mouseY) {
        final Button button = (Button)component;
        final String text = button.getText();
        int color = ColorUtils.color(50, 50, 50, 100);
        final int enable = ColorUtils.color(255, 255, 255, 255);
        if (GLUtils.isHovered(button.getX(), button.getY(), button.getDimension().width, button.getDimension().height, mouseX, mouseY)) {
            color = ColorUtils.color(70, 70, 70, 255);
        }
        if (button.isEnabled()) {
            RenderUtils.drawRect(button.getX(), button.getY(), button.getX() + button.getDimension().width - 1, button.getY() + button.getDimension().height, enable);
        }
        else {
            RenderUtils.drawRect(button.getX(), button.getY(), button.getX() + button.getDimension().width - 1, button.getY() + button.getDimension().height, color);
        }
        this.theme.fontRenderer.drawStringWithShadow(text, button.getX() + 5, button.getY() + (button.getDimension().height / 2 - this.theme.fontRenderer.FONT_HEIGHT / 4), ColorUtils.color(255, 255, 255, 255));
    }
    
    @Override
    public void doInteractions(final Component component, final int mouseX, final int mouseY) {
    }
}
