package zelix.gui.clickguis.huangbai;

import net.minecraft.client.*;
import zelix.hack.*;
import zelix.utils.*;
import java.awt.*;
import zelix.utils.hooks.visual.*;
import zelix.*;
import org.lwjgl.input.*;
import zelix.managers.*;
import zelix.value.*;
import zelix.utils.resourceloader.*;
import net.minecraft.client.gui.*;
import zelix.utils.hooks.visual.font.*;
import java.util.*;
import java.io.*;
import java.util.List;

public class ClickGui extends GuiScreen
{
    public static Minecraft mc;
    public static ScaledResolution sr;
    HackCategory currentModuleType;
    Hack currentModule;
    public static float startX;
    public static float startY;
    public static int moduleStart;
    public static int valueStart;
    boolean previousmouse;
    boolean mouse;
    public float moveX;
    public float moveY;
    boolean bind;
    float hue;
    public static int alpha;
    public static int alphe;
    int time;
    int press;
    
    public ClickGui() {
        this.currentModuleType = HackCategory.VISUAL;
        this.currentModule = HackManager.getModulesInType(this.currentModuleType).get(0);
        this.previousmouse = true;
        this.moveX = 0.0f;
        this.moveY = 0.0f;
        this.bind = false;
        this.time = 0;
        this.press = 0;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final FontRenderer fontc = Wrapper.INSTANCE.fontRenderer();
        ClickGui.sr = new ScaledResolution(ClickGui.mc);
        if (ClickGui.alpha < 255) {
            ClickGui.alpha += 5;
        }
        if (this.hue > 255.0f) {
            this.hue = 0.0f;
        }
        float h = this.hue;
        float h2 = this.hue + 85.0f;
        float h3 = this.hue + 170.0f;
        if (h > 255.0f) {
            h = 0.0f;
        }
        if (h2 > 255.0f) {
            h2 -= 255.0f;
        }
        if (h3 > 255.0f) {
            h3 -= 255.0f;
        }
        final Color color33 = Color.getHSBColor(h / 255.0f, 0.9f, 1.0f);
        final Color color34 = Color.getHSBColor(h2 / 255.0f, 0.9f, 1.0f);
        final Color color35 = Color.getHSBColor(h3 / 255.0f, 0.9f, 1.0f);
        final int color36 = color33.getRGB();
        final int color37 = color34.getRGB();
        final int color38 = color35.getRGB();
        final int color39 = new Color(255, 255, 255, ClickGui.alpha).getRGB();
        this.hue += 0.1;
        HGLUtils.drawWindow(ClickGui.startX, ClickGui.startY, ClickGui.startX + 400.0f, ClickGui.startY + 300.0f, 0.5f, Colors2.getColor(90, ClickGui.alpha), Colors2.getColor(0, ClickGui.alpha));
        HGLUtils.drawWindow(ClickGui.startX + 1.0f, ClickGui.startY + 1.0f, ClickGui.startX + 400.0f - 1.0f, ClickGui.startY + 300.0f - 1.0f, 1.0f, Colors2.getColor(90, ClickGui.alpha), Colors2.getColor(61, ClickGui.alpha));
        HGLUtils.drawWindow(ClickGui.startX + 2.5, ClickGui.startY + 2.5, ClickGui.startX + 400.0f - 2.5, ClickGui.startY + 300.0f - 2.5, 0.5f, Colors2.getColor(61, ClickGui.alpha), Colors2.getColor(0, ClickGui.alpha));
        HGLUtils.drawWindow(ClickGui.startX + 3.0f, ClickGui.startY + 3.0f, ClickGui.startX + 400.0f - 3.0f, ClickGui.startY + 300.0f - 3.0f, 0.5f, Colors2.getColor(27, ClickGui.alpha), Colors2.getColor(61, ClickGui.alphe));
        if (ClickGui.alpha >= 55) {
            ClickGuiRender.drawGradientSideways(ClickGui.startX + 3.0f, ClickGui.startY + 3.0f, ClickGui.startX + 200.0f, ClickGui.startY + 4.0, color36, color37);
            ClickGuiRender.drawGradientSideways(ClickGui.startX + 200.0f, ClickGui.startY + 3.0f, ClickGui.startX + 400.0f - 3.1f, ClickGui.startY + 4.0, color37, color38);
        }
        ClickGuiRender.drawRect(ClickGui.startX + 98.0f, ClickGui.startY + 100.0f, ClickGui.startX + 290.0f, ClickGui.startY + 108.0f, new Color(30, 30, 30, ClickGui.alpha).getRGB());
        ClickGuiRender.drawRect(ClickGui.startX + 100.0f, ClickGui.startY + 40.0f, ClickGui.startX + 350.0f, ClickGui.startY + 277.0f, new Color(35, 35, 35, ClickGui.alpha).getRGB());
        ClickGuiRender.drawRect(ClickGui.startX + 200.0f, ClickGui.startY + 100.0f, ClickGui.startX + 350.0f, ClickGui.startY + 277.0f, new Color(37, 37, 37, ClickGui.alpha).getRGB());
        ClickGuiRender.drawRect(ClickGui.startX + 190.0f, ClickGui.startY + 40.0f, ClickGui.startX + 390.0f, ClickGui.startY + 277.0f, new Color(40, 40, 40, ClickGui.alpha).getRGB());
        final FontLoaders fontLoaders = Core.fontLoaders;
        FontLoaders.default18.drawString("ZELIX", (int)(ClickGui.startX + 10.0f), (int)ClickGui.startY + 15, new Color(180, 180, 180, ClickGui.alpha).getRGB());
        final FontLoaders fontLoaders2 = Core.fontLoaders;
        FontLoaders.default18.drawString("1.1.6", (int)ClickGui.startX + 57, (int)ClickGui.startY + 14, new Color(180, 180, 180, ClickGui.alpha).getRGB());
        final FontLoaders fontLoaders3 = Core.fontLoaders;
        final CFontRenderer default18 = FontLoaders.default18;
        final String string = "Hello, " + Core.UN + " !";
        final float n2 = ClickGui.startX + 430.0f - 100.0f;
        final FontLoaders fontLoaders4 = Core.fontLoaders;
        default18.drawString(string, (int)(n2 - FontLoaders.default18.getStringWidth("Hello, " + Core.UN + " !")), (int)ClickGui.startY + 15, new Color(200, 200, 200, ClickGui.alpha).getRGB());
        final int m = Mouse.getDWheel();
        if (this.isCategoryHovered(ClickGui.startX + 100.0f, ClickGui.startY + 40.0f, ClickGui.startX + 200.0f, ClickGui.startY + 315.0f, mouseX, mouseY)) {
            if (m < 0 && ClickGui.moduleStart < HackManager.getModulesInType(this.currentModuleType).size() - 1) {
                ++ClickGui.moduleStart;
            }
            if (m > 0 && ClickGui.moduleStart > 0) {
                --ClickGui.moduleStart;
            }
        }
        if (this.isCategoryHovered(ClickGui.startX + 200.0f, ClickGui.startY + 50.0f, ClickGui.startX + 430.0f, ClickGui.startY + 315.0f, mouseX, mouseY)) {
            if (m < 0 && ClickGui.valueStart < this.currentModule.getValues().size() - 1) {
                ++ClickGui.valueStart;
            }
            if (m > 0 && ClickGui.valueStart > 0) {
                --ClickGui.valueStart;
            }
        }
        float mY = ClickGui.startY - 4.2f;
        for (int i = 0; i < HackManager.getModulesInType(this.currentModuleType).size(); ++i) {
            final Hack module = HackManager.getModulesInType(this.currentModuleType).get(i);
            if (mY > ClickGui.startY + 250.0f) {
                break;
            }
            if (i >= ClickGui.moduleStart) {
                if (!module.isToggled()) {
                    ClickGuiRender.drawRect(ClickGui.startX + 100.0f, mY + 45.0f, ClickGui.startX + 100.0f, mY + 70.0f, this.isSettingsButtonHovered(ClickGui.startX + 100.0f, mY + 45.0f, ClickGui.startX + 200.0f, mY + 70.0f, mouseX, mouseY) ? new Color(60, 60, 60, ClickGui.alpha).getRGB() : new Color(35, 35, 35, ClickGui.alpha).getRGB());
                    ClickGuiRender.drawFilledCircle(ClickGui.startX + (this.isSettingsButtonHovered(ClickGui.startX + 100.0f, mY + 45.0f, ClickGui.startX + 200.0f, mY + 70.0f, mouseX, mouseY) ? 112 : 110), mY + 58.0f, 3.0, new Color(70, 70, 70, ClickGui.alpha).getRGB(), 5);
                    Wrapper.INSTANCE.fontRenderer().drawString(module.getRenderName(), (int)ClickGui.startX + (this.isSettingsButtonHovered(ClickGui.startX + 100.0f, mY + 45.0f, ClickGui.startX + 200.0f, mY + 70.0f, mouseX, mouseY) ? 122 : 120), (int)mY + 55, new Color(175, 175, 175, ClickGui.alpha).getRGB());
                }
                else {
                    ClickGuiRender.drawRect(ClickGui.startX + 100.0f, mY + 45.0f, ClickGui.startX + 190.0f, mY + 70.0f, this.isSettingsButtonHovered(ClickGui.startX + 100.0f, mY + 45.0f, ClickGui.startX + 200.0f, mY + 70.0f, mouseX, mouseY) ? new Color(60, 60, 60, ClickGui.alpha).getRGB() : new Color(35, 35, 35, ClickGui.alpha).getRGB());
                    ClickGuiRender.drawFilledCircle(ClickGui.startX + (this.isSettingsButtonHovered(ClickGui.startX + 100.0f, mY + 45.0f, ClickGui.startX + 200.0f, mY + 70.0f, mouseX, mouseY) ? 112 : 110), mY + 58.0f, 3.0, new Color(100, 255, 100, ClickGui.alpha).getRGB(), 5);
                    Wrapper.INSTANCE.fontRenderer().drawString(module.getRenderName(), (int)ClickGui.startX + (this.isSettingsButtonHovered(ClickGui.startX + 100.0f, mY + 45.0f, ClickGui.startX + 200.0f, mY + 70.0f, mouseX, mouseY) ? 122 : 120), (int)mY + 55, new Color(255, 255, 255, ClickGui.alpha).getRGB());
                }
                if (this.isSettingsButtonHovered(ClickGui.startX + 100.0f, mY + 45.0f, ClickGui.startX + 200.0f, mY + 70.0f, mouseX, mouseY)) {
                    if (!this.previousmouse && Mouse.isButtonDown(0)) {
                        if (module.isToggled()) {
                            module.setToggled(false);
                            module.onDisable();
                        }
                        else {
                            module.setToggled(true);
                            module.onEnable();
                        }
                        this.previousmouse = true;
                    }
                    if (!this.previousmouse && Mouse.isButtonDown(1)) {
                        this.previousmouse = true;
                    }
                }
                if (!Mouse.isButtonDown(0)) {
                    this.previousmouse = false;
                }
                if (this.isSettingsButtonHovered(ClickGui.startX + 100.0f, mY + 45.0f, ClickGui.startX + 200.0f, mY + 70.0f, mouseX, mouseY) && Mouse.isButtonDown(1)) {
                    for (Hack hack : HackManager.getModulesInType(this.currentModuleType)) {}
                    this.currentModule = module;
                    ClickGui.valueStart = 0;
                }
                mY += 25.0f;
            }
        }
        mY = ClickGui.startY + 12.0f;
        fontc.drawString(this.currentModule.getDescription(), (int)ClickGui.startX + 200, (int)mY + 36, new Color(170, 170, 170).getRGB());
        for (int i = 0; i < this.currentModule.getValues().size() && mY <= ClickGui.startY + 220.0f; ++i) {
            if (i >= ClickGui.valueStart) {
                final Value value = this.currentModule.getValues().get(i);
                if (value instanceof NumberValue) {
                    final float x = ClickGui.startX + 300.0f;
                    double render = 68.0f * ((float)(Object)((NumberValue)value).getValue() - (float)(Object)((NumberValue)value).getMin()) / ((float)(Object)((NumberValue)value).getMax() - (float)(Object)((NumberValue)value).getMin());
                    ClickGuiRender.drawRect(x + 2.0f, mY + 52.0f, (float)(x + 75.0), mY + 53.0f, (this.isButtonHovered(x, mY + 45.0f, x + 100.0f, mY + 57.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) ? new Color(80, 80, 80, ClickGui.alpha).getRGB() : new Color(30, 30, 30, ClickGui.alpha).getRGB());
                    ClickGuiRender.drawRect(x + 2.0f, mY + 52.0f, (float)(x + render + 6.5), mY + 53.0f, new Color(35, 35, 255, ClickGui.alpha).getRGB());
                    ClickGuiRender.drawFilledCircle((float)(x + render + 2.0) + 3.0f, mY + 52.25, 1.5, new Color(35, 35, 255, ClickGui.alpha).getRGB(), 5);
                    Wrapper.INSTANCE.fontRenderer().drawString(value.getRenderName(), (int)ClickGui.startX + 200, (int)mY + 50, new Color(175, 175, 175, ClickGui.alpha).getRGB());
                    Wrapper.INSTANCE.fontRenderer().drawString(value.getValue().toString(), (int)ClickGui.startX + 300 - Wrapper.INSTANCE.fontRenderer().getStringWidth(value.getValue().toString()), (int)mY + 50, new Color(255, 255, 255, ClickGui.alpha).getRGB());
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                    if (this.isButtonHovered(x, mY + 45.0f, x + 100.0f, mY + 57.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                        if (!this.previousmouse && Mouse.isButtonDown(0)) {
                            render = ((NumberValue)value).getMin();
                            final double max = ((NumberValue)value).getMax();
                            final double inc = 0.01;
                            final double valAbs = mouseX - (x + 1.0);
                            double perc = valAbs / 68.0;
                            perc = Math.min(Math.max(0.0, perc), 1.0);
                            final double valRel = (max - render) * perc;
                            double val = render + valRel;
                            val = Math.round(val * (1.0 / inc)) / (1.0 / inc);
                            value.setValue(val);
                        }
                        if (!Mouse.isButtonDown(0)) {
                            this.previousmouse = false;
                        }
                    }
                    mY += 20.0f;
                    FileManager.saveHacks();
                }
                if (value instanceof BooleanValue) {
                    final float x = ClickGui.startX + 300.0f;
                    final int xx = 30;
                    final int x2x = 45;
                    Wrapper.INSTANCE.fontRenderer().drawString(value.getRenderName(), (int)ClickGui.startX + 200, (int)mY + 50, new Color(175, 175, 175, ClickGui.alpha).getRGB());
                    if ((boolean)value.getValue()) {
                        ClickGuiRender.drawRect(x + xx, mY + 50.0f, x + x2x, mY + 59.0f, this.isCheckBoxHovered(x + xx - 5.0f, mY + 50.0f, x + x2x + 6.0f, mY + 59.0f, mouseX, mouseY) ? new Color(80, 80, 80, ClickGui.alpha).getRGB() : new Color(20, 20, 20, ClickGui.alpha).getRGB());
                        ClickGuiRender.drawFilledCircle(x + xx, mY + 54.5, 4.5, this.isCheckBoxHovered(x + xx - 5.0f, mY + 50.0f, x + x2x + 6.0f, mY + 59.0f, mouseX, mouseY) ? new Color(80, 80, 80, ClickGui.alpha).getRGB() : new Color(20, 20, 20, ClickGui.alpha).getRGB(), 10);
                        ClickGuiRender.drawFilledCircle(x + x2x, mY + 54.5, 4.5, this.isCheckBoxHovered(x + xx - 5.0f, mY + 50.0f, x + x2x + 6.0f, mY + 59.0f, mouseX, mouseY) ? new Color(80, 80, 80, ClickGui.alpha).getRGB() : new Color(20, 20, 20, ClickGui.alpha).getRGB(), 10);
                        ClickGuiRender.drawFilledCircle(x + x2x, mY + 54.5, 5.0, new Color(35, 35, 255, ClickGui.alpha).getRGB(), 10);
                    }
                    else {
                        ClickGuiRender.drawRect(x + xx, mY + 50.0f, x + x2x, mY + 59.0f, this.isCheckBoxHovered(x + xx - 5.0f, mY + 50.0f, x + x2x + 6.0f, mY + 59.0f, mouseX, mouseY) ? new Color(80, 80, 80, ClickGui.alpha).getRGB() : new Color(20, 20, 20, ClickGui.alpha).getRGB());
                        ClickGuiRender.drawFilledCircle(x + xx, mY + 54.5, 4.5, this.isCheckBoxHovered(x + xx - 5.0f, mY + 50.0f, x + x2x + 6.0f, mY + 59.0f, mouseX, mouseY) ? new Color(80, 80, 80, ClickGui.alpha).getRGB() : new Color(20, 20, 20, ClickGui.alpha).getRGB(), 10);
                        ClickGuiRender.drawFilledCircle(x + x2x, mY + 54.5, 4.5, this.isCheckBoxHovered(x + xx - 5.0f, mY + 50.0f, x + x2x + 6.0f, mY + 59.0f, mouseX, mouseY) ? new Color(80, 80, 80, ClickGui.alpha).getRGB() : new Color(20, 20, 20, ClickGui.alpha).getRGB(), 10);
                        ClickGuiRender.drawFilledCircle(x + xx, mY + 54.5, 5.0, new Color(56, 56, 56, ClickGui.alpha).getRGB(), 10);
                    }
                    if (this.isCheckBoxHovered(x + xx - 5.0f, mY + 50.0f, x + x2x + 6.0f, mY + 59.0f, mouseX, mouseY)) {
                        if (!this.previousmouse && Mouse.isButtonDown(0)) {
                            this.previousmouse = true;
                            this.mouse = true;
                        }
                        if (this.mouse) {
                            value.setValue(!(boolean)value.getValue());
                            this.mouse = false;
                        }
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                    mY += 20.0f;
                    FileManager.saveHacks();
                }
                if (value instanceof ModeValue) {
                    final float x = ClickGui.startX + 300.0f;
                    Wrapper.INSTANCE.fontRenderer().drawStringWithShadow(((ModeValue)value).getRenderName(), ClickGui.startX + 200.0f, mY + 52.0f, new Color(175, 175, 175, ClickGui.alpha).getRGB());
                    ClickGuiRender.drawRect(x + 5.0f, mY + 45.0f, x + 75.0f, mY + 65.0f, this.isStringHovered(x, mY + 45.0f, x + 75.0f, mY + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, ClickGui.alpha).getRGB() : new Color(56, 56, 56, ClickGui.alpha).getRGB());
                    ClickGuiRender.drawRect(x + 2.0f, mY + 48.0f, x + 78.0f, mY + 62.0f, this.isStringHovered(x, mY + 45.0f, x + 75.0f, mY + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, ClickGui.alpha).getRGB() : new Color(56, 56, 56, ClickGui.alpha).getRGB());
                    ClickGuiRender.drawFilledCircle(x + 5.0f, mY + 48.0f, 3.0, this.isStringHovered(x, mY + 45.0f, x + 75.0f, mY + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, ClickGui.alpha).getRGB() : new Color(56, 56, 56, ClickGui.alpha).getRGB(), 5);
                    ClickGuiRender.drawFilledCircle(x + 5.0f, mY + 62.0f, 3.0, this.isStringHovered(x, mY + 45.0f, x + 75.0f, mY + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, ClickGui.alpha).getRGB() : new Color(56, 56, 56, ClickGui.alpha).getRGB(), 5);
                    ClickGuiRender.drawFilledCircle(x + 75.0f, mY + 48.0f, 3.0, this.isStringHovered(x, mY + 45.0f, x + 75.0f, mY + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, ClickGui.alpha).getRGB() : new Color(56, 56, 56, ClickGui.alpha).getRGB(), 5);
                    ClickGuiRender.drawFilledCircle(x + 75.0f, mY + 62.0f, 3.0, this.isStringHovered(x, mY + 45.0f, x + 75.0f, mY + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, ClickGui.alpha).getRGB() : new Color(56, 56, 56, ClickGui.alpha).getRGB(), 5);
                    if (((ModeValue)value).getSelectMode() != null) {
                        Wrapper.INSTANCE.fontRenderer().drawStringWithShadow(((ModeValue)value).getSelectMode().getName(), x + 40.0f - Wrapper.INSTANCE.fontRenderer().getStringWidth(((ModeValue)value).getSelectMode().getName()) / 2, mY + 53.0f, new Color(255, 255, 255, ClickGui.alpha).getRGB());
                    }
                    if (this.isStringHovered(x, mY + 45.0f, x + 75.0f, mY + 65.0f, mouseX, mouseY) && Mouse.isButtonDown(0) && !this.previousmouse) {
                        ++this.press;
                        final ModeValue modeValue = (ModeValue)value;
                        final List<Mode> modes = new ArrayList<Mode>();
                        String t1 = null;
                        for (final Mode mode : modeValue.getModes()) {
                            modes.add(mode);
                        }
                        if (this.press <= modes.size()) {
                            modes.get(this.press - 1).setToggled(true);
                            t1 = modes.get(this.press - 1).getName();
                        }
                        else {
                            this.press = 0;
                        }
                        for (final Mode mode : modeValue.getModes()) {
                            if (mode.getName() != t1) {
                                mode.setToggled(false);
                            }
                        }
                        this.previousmouse = true;
                    }
                    mY += 25.0f;
                }
                FileManager.saveHacks();
            }
        }
        final float x2 = ClickGui.startX + 300.0f;
        final float yyy = ClickGui.startY + 200.0f;
        if ((this.isHovered(ClickGui.startX, ClickGui.startY, ClickGui.startX + 450.0f, ClickGui.startY + 50.0f, mouseX, mouseY) || this.isHovered(ClickGui.startX, ClickGui.startY + 315.0f, ClickGui.startX + 450.0f, ClickGui.startY + 350.0f, mouseX, mouseY) || this.isHovered(ClickGui.startX + 430.0f, ClickGui.startY, ClickGui.startX + 450.0f, ClickGui.startY + 350.0f, mouseX, mouseY)) && Mouse.isButtonDown(0)) {
            if (this.moveX == 0.0f && this.moveY == 0.0f) {
                this.moveX = mouseX - ClickGui.startX;
                this.moveY = mouseY - ClickGui.startY;
            }
            else {
                ClickGui.startX = mouseX - this.moveX;
                ClickGui.startY = mouseY - this.moveY;
            }
            this.previousmouse = true;
        }
        else if (this.moveX != 0.0f || this.moveY != 0.0f) {
            this.moveX = 0.0f;
            this.moveY = 0.0f;
        }
        if (this.isHovered(ClickGui.sr.getScaledWidth() / 2 - 40, 0.0f, ClickGui.sr.getScaledWidth() / 2 + 40, 20.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            ClickGui.alpha = 0;
            ClickGui.alphe = 0;
        }
        else {
            ClickGui.alphe = 121;
        }
        final int j = 59;
        final int l = 40;
        final float k = ClickGui.startY + 10.0f;
        final float xx2 = ClickGui.startX + 5.0f;
        for (int i2 = 0; i2 < HackCategory.values().length; ++i2) {
            final HackCategory[] iterator = HackCategory.values();
            if (iterator[i2] == this.currentModuleType) {
                final float typey = k + 5.0f + j + i2 * l;
                ClickGuiRender.drawRect(xx2 + 4.0f, typey, xx2 + 25.0f, typey + 2.0f, color39);
            }
            final String[] Hack = new String[HackCategory.values().length];
            for (int n = 0; n < HackCategory.values().length; ++n) {
                if (zelix.hack.hacks.ClickGui.language.getMode("Chinese").isToggled()) {
                    Hack[n] = Strings.Chinese_HackCategory.split("=")[n];
                }
                else {
                    Hack[n] = iterator[n].toString();
                }
            }
            Wrapper.INSTANCE.fontRenderer().drawStringWithShadow(Hack[i2], xx2 + (this.isCategoryHovered(xx2 + 8.0f, k - 10.0f + j + i2 * l, xx2 + 80.0f, k + 20.0f + j + i2 * l, mouseX, mouseY) ? 27 : 25), k + 50.0f + l * i2, new Color(255, 255, 255, ClickGui.alpha).getRGB());
            try {
                if (this.isCategoryHovered(xx2 + 8.0f, k - 10.0f + j + i2 * l, xx2 + 80.0f, k + 20.0f + j + i2 * l, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    this.currentModuleType = iterator[i2];
                    this.currentModule = HackManager.getModulesInType(this.currentModuleType).get(0);
                    ClickGui.moduleStart = 0;
                    ClickGui.valueStart = 0;
                    for (int x3 = 0; x3 < this.currentModule.getValues().size(); ++x3) {
                        final Value value2 = this.currentModule.getValues().get(x3);
                    }
                    for (Hack hack2 : HackManager.getModulesInType(this.currentModuleType)) {}
                }
            }
            catch (Exception e) {
                System.err.println(e);
            }
        }
    }
    
    public void initGui() {
        for (int i = 0; i < this.currentModule.getValues().size(); ++i) {
            final Value value = this.currentModule.getValues().get(i);
        }
        for (Hack hack : HackManager.getModulesInType(this.currentModuleType)) {}
        super.initGui();
    }
    
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.bind) {
            this.currentModule.setKey(keyCode);
            if (keyCode == 1) {
                this.currentModule.setKey(0);
            }
            this.bind = false;
        }
        else if (keyCode == 1) {
            Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)null);
            if (Wrapper.INSTANCE.mc().currentScreen == null) {
                Wrapper.INSTANCE.mc().setIngameFocus();
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int button) throws IOException {
        final float x = ClickGui.startX + 220.0f;
        float mY = ClickGui.startY + 30.0f;
        for (int i = 0; i < this.currentModule.getValues().size() && mY <= ClickGui.startY + 350.0f; ++i) {
            if (i >= ClickGui.valueStart) {
                final Value value = this.currentModule.getValues().get(i);
                if (value instanceof NumberValue) {
                    mY += 20.0f;
                }
                if (value instanceof BooleanValue) {
                    mY += 20.0f;
                }
                if (value instanceof ModeValue) {
                    mY += 25.0f;
                }
            }
        }
        final float x2 = ClickGui.startX + 300.0f;
        final float yyy = ClickGui.startY + 200.0f;
        if (this.isHovered(x2 + 2.0f, yyy + 40.0f, x2 + 78.0f, yyy + 70.0f, mouseX, mouseY)) {
            this.bind = true;
        }
        super.mouseClicked(mouseX, mouseY, button);
    }
    
    public boolean isStringHovered(final float f, final float y, final float g, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }
    
    public boolean isSettingsButtonHovered(final float x, final float y, final float x2, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
    
    public boolean isButtonHovered(final float f, final float y, final float g, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }
    
    public boolean isCheckBoxHovered(final float f, final float y, final float g, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }
    
    public boolean isCategoryHovered(final float x, final float y, final float x2, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
    
    public boolean isHovered(final float x, final float y, final float x2, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
    
    public void onGuiClosed() {
        ClickGui.alpha = 0;
    }
    
    static {
        ClickGui.mc = Minecraft.getMinecraft();
        ClickGui.sr = new ScaledResolution(ClickGui.mc);
        ClickGui.startX = ClickGui.sr.getScaledWidth() / 2 - 225;
        ClickGui.startY = ClickGui.sr.getScaledHeight() / 2 - 175;
        ClickGui.moduleStart = 0;
        ClickGui.valueStart = 0;
        ClickGui.alphe = 121;
    }
}
