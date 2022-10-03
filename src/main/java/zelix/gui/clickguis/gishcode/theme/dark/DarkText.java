package zelix.gui.clickguis.gishcode.theme.dark;

import zelix.gui.clickguis.gishcode.theme.*;
import zelix.gui.clickguis.gishcode.base.*;
import zelix.gui.clickguis.gishcode.elements.*;

public class DarkText extends ComponentRenderer
{
    public DarkText(final Theme theme) {
        super(ComponentType.TEXT, theme);
    }
    
    @Override
    public void drawComponent(final Component component, final int mouseX, final int mouseY) {
        final Text text = (Text)component;
        final String[] message = text.getMessage();
        int y = text.getY();
        for (final String s : message) {
            this.theme.fontRenderer.drawStringWithShadow(s, text.getX() - 4, y - 4, -1);
            y += 10;
        }
    }
    
    @Override
    public void doInteractions(final Component component, final int mouseX, final int mouseY) {
    }
}
