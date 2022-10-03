package zelix.gui.clickguis.gishcode.theme.dark;

import zelix.gui.clickguis.gishcode.theme.*;
import net.minecraft.client.*;
import zelix.gui.clickguis.gishcode.base.*;

public class DarkTheme extends Theme
{
    public DarkTheme() {
        super("GishCodeDark");
        this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
        this.addRenderer(ComponentType.FRAME, new DarkFrame(this));
        this.addRenderer(ComponentType.BUTTON, new DarkButton(this));
        this.addRenderer(ComponentType.SLIDER, new DarkSlider(this));
        this.addRenderer(ComponentType.CHECK_BUTTON, new DarkCheckButton(this));
        this.addRenderer(ComponentType.EXPANDING_BUTTON, new DarkExpandingButton(this));
        this.addRenderer(ComponentType.TEXT, new DarkText(this));
        this.addRenderer(ComponentType.KEYBIND, new DarkKeybinds(this));
        this.addRenderer(ComponentType.DROPDOWN, new DarkDropDown(this));
        this.addRenderer(ComponentType.COMBO_BOX, new DarkComboBox(this));
    }
}
