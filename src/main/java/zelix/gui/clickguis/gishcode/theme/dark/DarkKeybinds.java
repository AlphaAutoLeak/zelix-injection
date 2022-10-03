package zelix.gui.clickguis.gishcode.theme.dark;

import zelix.gui.clickguis.gishcode.theme.*;
import zelix.gui.clickguis.gishcode.base.*;
import zelix.gui.clickguis.gishcode.elements.*;
import zelix.hack.hacks.*;
import zelix.utils.hooks.visual.*;
import org.lwjgl.input.*;

public class DarkKeybinds extends ComponentRenderer
{
    public DarkKeybinds(final Theme theme) {
        super(ComponentType.KEYBIND, theme);
    }
    
    @Override
    public void drawComponent(final Component component, final int mouseX, final int mouseY) {
        final KeybindMods keybind = (KeybindMods)component;
        final int mainColor = ClickGui.isLight ? ColorUtils.color(255, 255, 255, 55) : ColorUtils.color(0, 0, 0, 55);
        final int mainColorInv = ClickGui.isLight ? ColorUtils.color(0, 0, 0, 255) : ColorUtils.color(255, 255, 255, 255);
        this.theme.fontRenderer.drawStringWithShadow("Key", keybind.getX() + 4, keybind.getY() + 2, mainColorInv);
        final int nameWidth = this.theme.fontRenderer.getStringWidth("Key") + 7;
        RenderUtils.drawRect(keybind.getX() + nameWidth, keybind.getY(), keybind.getX() + keybind.getDimension().width, keybind.getY() + 12, mainColor);
        if (keybind.getMod().getKey() == -1) {
            this.theme.fontRenderer.drawStringWithShadow(keybind.isEditing() ? "|" : "NONE", keybind.getX() + keybind.getDimension().width / 2 + nameWidth / 2 - this.theme.fontRenderer.getStringWidth("NONE") / 2, keybind.getY() + 2, keybind.isEditing() ? mainColorInv : ColorUtils.color(0.4f, 0.4f, 0.4f, 1.0f));
        }
        else {
            this.theme.fontRenderer.drawStringWithShadow(keybind.isEditing() ? "|" : Keyboard.getKeyName(keybind.getMod().getKey()), keybind.getX() + keybind.getDimension().width / 2 + nameWidth / 2 - this.theme.fontRenderer.getStringWidth(Keyboard.getKeyName(keybind.getMod().getKey())) / 2, keybind.getY() + 2, keybind.isEditing() ? mainColorInv : mainColorInv);
        }
    }
    
    @Override
    public void doInteractions(final Component component, final int mouseX, final int mouseY) {
    }
}
