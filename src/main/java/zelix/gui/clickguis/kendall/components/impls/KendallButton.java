package zelix.gui.clickguis.kendall.components.impls;

import zelix.gui.clickguis.kendall.components.Component;
import zelix.hack.*;
import zelix.gui.clickguis.kendall.frame.*;
import java.awt.*;
import zelix.gui.clickguis.kendall.components.*;
import zelix.value.*;
import java.util.*;
import zelix.utils.hooks.visual.*;
import zelix.gui.clickguis.N3ro.Utils.*;
import zelix.hack.hacks.*;
import zelix.utils.hooks.visual.font.*;
import zelix.utils.*;

public class KendallButton
{
    public float x;
    public float y;
    public boolean showSettings;
    public Hack mod;
    public KendallFrame parent;
    public static int drawColor;
    public Color color;
    public ArrayList<Component> components;
    int r1;
    int r2;
    public int task;
    int i;
    
    public KendallButton(final Hack cheat, final float x, final float y, final KendallFrame parent) {
        this.color = new Color(-1);
        this.components = new ArrayList<Component>();
        this.r1 = new Color(-1).getRed();
        this.r2 = new Color(-16746051).getRed();
        this.task = 10;
        this.i = 0;
        this.x = x;
        this.y = y;
        this.mod = cheat;
        this.parent = parent;
        this.init();
    }
    
    public void init() {
        this.components.clear();
        for (final Value v : this.mod.getValues()) {
            if (v instanceof BooleanValue) {
                this.components.add(new KendallOption((BooleanValue)v, this));
            }
            if (v instanceof NumberValue) {
                this.components.add(new KendallSIlder((NumberValue)v, this));
            }
            if (v instanceof ModeValue) {
                this.components.add(new KendallMode((ModeValue)v, this));
            }
        }
    }
    
    public void onRender(final int mouseX, final int mouseY, final boolean last) {
        this.x = this.parent.x;
        this.color = new Color(this.r1, this.color.getGreen(), this.color.getBlue());
        if (this.mod.isToggled()) {
            if (last) {
                RenderUtil.drawRoundRect_down(this.x, this.y, this.x + 100.0, this.y + 18.0, 2, ColorUtils.rainbow(this.task));
            }
            else {
                RenderUtil.drawBorderedRect(this.x, this.y, this.x + 100.0f, this.y + 18.0f, 0.0f, 0, ColorUtils.rainbow(this.task));
            }
            if (!ClickGui.language.getMode("Chinese").isToggled()) {
                FontLoaders.default14.drawStringWithShadow(this.mod.getRenderName(), this.x + 10.0f, this.y + 7.0f, new Color(KendallButton.drawColor).getRGB());
            }
            else {
                Wrapper.INSTANCE.fontRenderer().drawStringWithShadow(this.mod.getRenderName(), this.x + 10.0f, this.y + 5.0f, new Color(KendallButton.drawColor).getRGB());
            }
        }
        else {
            if (last) {
                RenderUtil.drawRoundRect_down(this.x, this.y, this.x + 100.0, this.y + 18.0, 2, isButtonHovered(this.x, this.y, this.x + 90.0f, this.y + 16.0f, mouseX, mouseY) ? -14869217 : -15329770);
            }
            else {
                RenderUtil.drawBorderedRect(this.x, this.y, this.x + 100.0f, this.y + 18.0f, 0.0f, 0, isButtonHovered(this.x, this.y, this.x + 90.0f, this.y + 16.0f, mouseX, mouseY) ? -14869217 : -15329770);
            }
            if (!ClickGui.language.getMode("Chinese").isToggled()) {
                FontLoaders.default14.drawStringWithShadow(this.mod.getRenderName(), this.x + 10.0f, this.y + 7.0f, -855638017);
            }
            else {
                Wrapper.INSTANCE.fontRenderer().drawStringWithShadow(this.mod.getRenderName(), this.x + 10.0f, this.y + 5.0f, new Color(KendallButton.drawColor).getRGB());
            }
        }
    }
    
    public void onClick(final int mouseX, final int mouseY, final int mouseButton) {
        System.out.println("x,u = " + mouseButton);
        if (mouseButton == 0 && isButtonHovered(this.x, this.y, this.x + 90.0f, this.y + 16.0f, mouseX, mouseY)) {
            this.mod.setToggled(!this.mod.isToggled());
        }
        if (mouseButton == 1 && KendallFrame.isHovered(this.x, this.y, this.x + 90.0f, this.y + 16.0f, mouseX, mouseY)) {
            ClickGui.KendallMyGod.isVIsableSW = true;
            ClickGui.KendallMyGod.targetbt = this;
        }
    }
    
    public static boolean isButtonHovered(final float f, final float y, final float g, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }
}
