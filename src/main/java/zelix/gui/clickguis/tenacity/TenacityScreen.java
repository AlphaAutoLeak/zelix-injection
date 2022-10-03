package zelix.gui.clickguis.tenacity;

import zelix.gui.clickguis.tenacity.Frame.*;
import java.io.*;
import net.minecraft.client.gui.*;

public class TenacityScreen extends GuiScreen
{
    public tenacity frame;
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.frame.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.frame.mouseReleased(mouseX, mouseY, state);
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.frame.keyTyped(typedChar, keyCode);
    }
    
    public TenacityScreen() {
        this.frame = new tenacity();
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sR = new ScaledResolution(this.mc);
        this.frame.render(mouseX, mouseY);
    }
    
    public void initGui() {
        this.frame.initialize();
    }
    
    public void onGuiClosed() {
        super.onGuiClosed();
        this.mc.entityRenderer.stopUseShader();
    }
}
