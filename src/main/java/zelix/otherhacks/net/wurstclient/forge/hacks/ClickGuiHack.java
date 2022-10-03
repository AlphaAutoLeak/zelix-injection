package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import zelix.otherhacks.net.wurstclient.forge.clickgui.*;
import net.minecraft.client.gui.*;

@Hack.DontSaveState
public final class ClickGuiHack extends Hack
{
    private final SliderSetting opacity;
    private final SliderSetting maxHeight;
    private final SliderSetting bgRed;
    private final SliderSetting bgGreen;
    private final SliderSetting bgBlue;
    private final SliderSetting acRed;
    private final SliderSetting acGreen;
    private final SliderSetting acBlue;
    
    public ClickGuiHack() {
        super("ClickGUI", "");
        this.opacity = new SliderSetting("Opacity", 0.5, 0.15, 0.85, 0.01, SliderSetting.ValueDisplay.PERCENTAGE);
        this.maxHeight = new SliderSetting("Max height", "Maximum window height\n0 = no limit", 200.0, 0.0, 1000.0, 25.0, SliderSetting.ValueDisplay.INTEGER);
        this.bgRed = new SliderSetting("BG red", "Background red", 64.0, 0.0, 255.0, 1.0, SliderSetting.ValueDisplay.INTEGER);
        this.bgGreen = new SliderSetting("BG green", "Background green", 64.0, 0.0, 255.0, 1.0, SliderSetting.ValueDisplay.INTEGER);
        this.bgBlue = new SliderSetting("BG blue", "Background blue", 64.0, 0.0, 255.0, 1.0, SliderSetting.ValueDisplay.INTEGER);
        this.acRed = new SliderSetting("AC red", "Accent red", 16.0, 0.0, 255.0, 1.0, SliderSetting.ValueDisplay.INTEGER);
        this.acGreen = new SliderSetting("AC green", "Accent green", 16.0, 0.0, 255.0, 1.0, SliderSetting.ValueDisplay.INTEGER);
        this.acBlue = new SliderSetting("AC blue", "Accent blue", 16.0, 0.0, 255.0, 1.0, SliderSetting.ValueDisplay.INTEGER);
        this.addSetting(this.opacity);
        this.addSetting(this.maxHeight);
        this.addSetting(this.bgRed);
        this.addSetting(this.bgGreen);
        this.addSetting(this.bgBlue);
        this.addSetting(this.acRed);
        this.addSetting(this.acGreen);
        this.addSetting(this.acBlue);
    }
    
    @Override
    protected void onEnable() {
        ClickGuiHack.mc.displayGuiScreen((GuiScreen)new ClickGuiScreen(ClickGuiHack.wurst.getGui()));
        this.setEnabled(false);
    }
    
    public float getOpacity() {
        return this.opacity.getValueF();
    }
    
    public int getMaxHeight() {
        return this.maxHeight.getValueI();
    }
    
    public void setMaxHeight(final int maxHeight) {
        this.maxHeight.setValue(maxHeight);
    }
    
    public float[] getBgColor() {
        return new float[] { this.bgRed.getValueI() / 255.0f, this.bgGreen.getValueI() / 255.0f, this.bgBlue.getValueI() / 255.0f };
    }
    
    public float[] getAcColor() {
        return new float[] { this.acRed.getValueI() / 255.0f, this.acGreen.getValueI() / 255.0f, this.acBlue.getValueI() / 255.0f };
    }
}
