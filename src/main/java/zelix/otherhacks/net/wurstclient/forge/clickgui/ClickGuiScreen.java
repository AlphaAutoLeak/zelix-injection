package zelix.otherhacks.net.wurstclient.forge.clickgui;

import net.minecraft.client.gui.*;
import java.io.*;

public final class ClickGuiScreen extends GuiScreen
{
    private final ClickGui gui;
    
    public ClickGuiScreen(final ClickGui gui) {
        this.gui = gui;
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.gui.handleMouseClick(mouseX, mouseY, mouseButton);
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.gui.render(mouseX, mouseY, partialTicks);
    }
}
