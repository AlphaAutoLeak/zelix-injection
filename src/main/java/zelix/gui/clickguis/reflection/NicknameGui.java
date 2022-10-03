package zelix.gui.clickguis.reflection;

import org.lwjgl.input.*;
import net.minecraft.client.gui.*;
import zelix.utils.*;
import java.io.*;

public class NicknameGui extends GuiScreen
{
    private final GuiScreen parentScreen;
    private GuiTextField nameField;
    
    public NicknameGui() {
        this.parentScreen = (GuiScreen)new GuiMultiplayer((GuiScreen)new GuiMainMenu());
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Username Changer", this.width / 2, 17, 16777215);
        this.drawString(this.fontRenderer, "Nickname:", this.width / 2 - 100, this.width / 2 - 180, 10526880);
        this.nameField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void updateScreen() {
        this.nameField.updateCursorCounter();
        super.updateScreen();
    }
    
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 18, "Done"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 18, "Cancel"));
        (this.nameField = new GuiTextField(0, this.fontRenderer, this.width / 2 - 100, this.height / 4 + 72, 200, 20)).setFocused(true);
        this.nameField.setText(Wrapper.INSTANCE.mc().getSession().getUsername());
        super.initGui();
    }
    
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }
    
    protected void actionPerformed(final GuiButton button) {
        if (button.id == 1) {
            Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)new GuiMainMenu()));
        }
        else if (button.id == 0) {}
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.nameField.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == 1) {
            this.actionPerformed(this.buttonList.get(1));
        }
        if (keyCode == 15) {
            this.nameField.setFocused(!this.nameField.isFocused());
        }
        if (keyCode == 28 || keyCode == 156) {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
}
