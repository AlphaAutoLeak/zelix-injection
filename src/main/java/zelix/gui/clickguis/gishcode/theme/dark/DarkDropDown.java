package zelix.gui.clickguis.gishcode.theme.dark;

import zelix.gui.clickguis.gishcode.theme.*;
import zelix.gui.clickguis.gishcode.base.*;
import zelix.gui.clickguis.gishcode.elements.*;
import zelix.hack.hacks.*;

public class DarkDropDown extends ComponentRenderer
{
    public DarkDropDown(final Theme theme) {
        super(ComponentType.DROPDOWN, theme);
    }
    
    @Override
    public void drawComponent(final Component component, final int mouseX, final int mouseY) {
        final Dropdown dropdown = (Dropdown)component;
        final String text = dropdown.getText();
        this.theme.fontRenderer.drawStringWithShadow(text, dropdown.getX() + 5, dropdown.getY() + (dropdown.getDropdownHeight() / 2 - this.theme.fontRenderer.FONT_HEIGHT / 4), ClickGui.getColor());
        if (dropdown.isMaximized()) {
            dropdown.renderChildren(mouseX, mouseY);
        }
    }
    
    @Override
    public void doInteractions(final Component component, final int mouseX, final int mouseY) {
    }
}
