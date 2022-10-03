package zelix.otherhacks.net.wurstclient.forge.clickgui;

import zelix.otherhacks.net.wurstclient.forge.settings.*;
import net.minecraft.client.gui.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import java.io.*;

public final class EditSliderScreen extends GuiScreen
{
    private final GuiScreen prevScreen;
    private final SliderSetting slider;
    private GuiTextField valueField;
    private GuiButton doneButton;
    
    public EditSliderScreen(final GuiScreen prevScreen, final SliderSetting slider) {
        this.prevScreen = prevScreen;
        this.slider = slider;
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public void initGui() {
        (this.valueField = new GuiTextField(1, WMinecraft.getFontRenderer(), this.width / 2 - 100, 60, 200, 20)).setText(SliderSetting.ValueDisplay.DECIMAL.getValueString(this.slider.getValue()));
        this.valueField.setSelectionPos(0);
        this.valueField.setFocused(true);
        this.buttonList.add(this.doneButton = new GuiButton(0, this.width / 2 - 100, this.height / 3 * 2, "Done"));
    }
    
    protected void actionPerformed(final GuiButton button) throws IOException {
        final String value = this.valueField.getText();
        if (MathUtils.isDouble(value)) {
            this.slider.setValue(Double.parseDouble(value));
        }
        this.mc.displayGuiScreen(this.prevScreen);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.valueField.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.valueField.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == 28) {
            this.actionPerformed(this.doneButton);
        }
        else if (keyCode == 1) {
            this.mc.displayGuiScreen(this.prevScreen);
        }
    }
    
    public void updateScreen() {
        this.valueField.updateCursorCounter();
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(WMinecraft.getFontRenderer(), this.slider.getName(), this.width / 2, 20, 16777215);
        this.valueField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
