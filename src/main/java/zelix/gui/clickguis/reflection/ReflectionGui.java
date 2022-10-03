package zelix.gui.clickguis.reflection;

import java.awt.*;
import zelix.utils.hooks.visual.*;
import org.lwjgl.input.*;
import net.minecraft.client.gui.*;
import zelix.utils.*;

public class ReflectionGui extends GuiScreen
{
    private final GuiScreen parentScreen;
    protected boolean hovered;
    
    public ReflectionGui() {
        this.parentScreen = (GuiScreen)new GuiMainMenu();
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), Color.BLACK.getRGB(), ColorUtils.rainbow(300));
        drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), -1627389952);
        drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 2130706432);
        this.drawCenteredString(this.fontRenderer, "Reflection Client", this.width / 2, 17, ColorUtils.rainbow(300));
        this.drawCenteredString(this.fontRenderer, "Version: 1.1.6", this.width / 2, 30, 10526880);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void updateScreen() {
        super.updateScreen();
    }
    
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        super.initGui();
    }
    
    protected void actionPerformed(final GuiButton button) {
        if (button.id == 1) {
            Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)new GuiMainMenu());
        }
        else if (button.id == 0) {}
    }
}
