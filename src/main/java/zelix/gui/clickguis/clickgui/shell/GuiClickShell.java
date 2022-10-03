package zelix.gui.clickguis.clickgui.shell;

import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import zelix.gui.Notification.Utils.*;
import zelix.hack.*;
import zelix.*;
import org.lwjgl.opengl.*;
import zelix.utils.hooks.visual.*;
import java.awt.*;
import zelix.managers.*;
import zelix.utils.*;
import zelix.value.*;
import zelix.utils.hooks.visual.font.*;
import zelix.hack.hacks.*;
import org.lwjgl.input.*;

public class GuiClickShell extends GuiScreen
{
    public static Minecraft mc;
    public static ScaledResolution sr;
    public AnimationUtils animationUtils;
    public static HackCategory currentModuleType;
    public static Hack currentModule;
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
    int main_width;
    int main_height;
    int press;
    private int modeIndex;
    private boolean animate;
    private float target;
    private float h;
    
    public GuiClickShell() {
        this.animationUtils = new AnimationUtils();
        this.previousmouse = true;
        this.moveX = 0.0f;
        this.moveY = 0.0f;
        this.bind = false;
        this.time = 0;
        this.main_width = 450;
        this.main_height = 296;
        this.press = 0;
        this.animate = false;
        this.target = GuiClickShell.startY + 60.0f;
        this.h = GuiClickShell.startY + 60.0f;
        this.modeIndex = 0;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final FontLoaders fontLoaders = Core.fontLoaders;
        final CFontRenderer font = FontLoaders.default16;
        GuiClickShell.sr = new ScaledResolution(GuiClickShell.mc);
        GL11.glPushMatrix();
        HGLUtils.drawRoundedRect2(GuiClickShell.startX, GuiClickShell.startY, GuiClickShell.startX + this.main_width, GuiClickShell.startY + this.main_height, 4.0f, Colors.getColor(29, 29, 31));
        RenderUtils.drawRect(GuiClickShell.startX + 101.0f, GuiClickShell.startY + 51.0f, GuiClickShell.startX + this.main_width, GuiClickShell.startY + this.main_height, Colors.getColor(38, 38, 41));
        final FontLoaders fontLoaders2 = Core.fontLoaders;
        FontLoaders.icon24.drawString("Zelix", GuiClickShell.startX + 10.0f, GuiClickShell.startY + 20.0f, Color.white.getRGB());
        GL11.glPopMatrix();
        RenderUtils.drawRect(GuiClickShell.startX, GuiClickShell.startY + 50.0f, GuiClickShell.startX + this.main_width, GuiClickShell.startY + 51.0f, Colors.getColor(78, 78, 78));
        if (this.isCategoryHovered(GuiClickShell.startX + 110.0f, GuiClickShell.startY + 10.0f, GuiClickShell.startX + 160.0f, GuiClickShell.startY + 40.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            GuiClickShell.currentModuleType = HackCategory.COMBAT;
            final HackManager hackManager = Core.hackManager;
            GuiClickShell.currentModule = HackManager.getModulesInType(GuiClickShell.currentModuleType).get(0);
            GuiClickShell.moduleStart = 0;
            GuiClickShell.valueStart = 0;
            for (int x1 = 0; x1 < GuiClickShell.currentModule.getValues().size(); ++x1) {
                final Value value2 = GuiClickShell.currentModule.getValues().get(x1);
            }
        }
        if (GuiClickShell.currentModuleType == HackCategory.COMBAT) {
            final FontLoaders fontLoaders3 = Core.fontLoaders;
            FontLoaders.default20.drawString("COMBAT", GuiClickShell.startX + 113.0f, GuiClickShell.startY + 20.0f, Colors.getColor(245, 245, 247));
        }
        else {
            final FontLoaders fontLoaders4 = Core.fontLoaders;
            FontLoaders.default20.drawString("COMBAT", GuiClickShell.startX + 113.0f, GuiClickShell.startY + 20.0f, Colors.getColor(165, 165, 165));
        }
        if (this.isCategoryHovered(GuiClickShell.startX + 170.0f, GuiClickShell.startY + 10.0f, GuiClickShell.startX + 220.0f, GuiClickShell.startY + 40.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            GuiClickShell.currentModuleType = HackCategory.MOVEMENT;
            final HackManager hackManager2 = Core.hackManager;
            GuiClickShell.currentModule = HackManager.getModulesInType(GuiClickShell.currentModuleType).get(0);
            GuiClickShell.moduleStart = 0;
            GuiClickShell.valueStart = 0;
            for (int x1 = 0; x1 < GuiClickShell.currentModule.getValues().size(); ++x1) {
                final Value value3 = GuiClickShell.currentModule.getValues().get(x1);
            }
        }
        if (GuiClickShell.currentModuleType == HackCategory.MOVEMENT) {
            final FontLoaders fontLoaders5 = Core.fontLoaders;
            FontLoaders.default20.drawString("MOVEMENT", GuiClickShell.startX + 166.0f, GuiClickShell.startY + 20.0f, Colors.getColor(245, 245, 247));
        }
        else {
            final FontLoaders fontLoaders6 = Core.fontLoaders;
            FontLoaders.default20.drawString("MOVEMENT", GuiClickShell.startX + 166.0f, GuiClickShell.startY + 20.0f, Colors.getColor(165, 165, 165));
        }
        if (this.isCategoryHovered(GuiClickShell.startX + 230.0f, GuiClickShell.startY + 10.0f, GuiClickShell.startX + 280.0f, GuiClickShell.startY + 40.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            GuiClickShell.currentModuleType = HackCategory.PLAYER;
            final HackManager hackManager3 = Core.hackManager;
            GuiClickShell.currentModule = HackManager.getModulesInType(GuiClickShell.currentModuleType).get(0);
            GuiClickShell.moduleStart = 0;
            GuiClickShell.valueStart = 0;
            if (GuiClickShell.currentModule.getValues() != null) {
                for (int x1 = 0; x1 < GuiClickShell.currentModule.getValues().size(); ++x1) {
                    final Value value4 = GuiClickShell.currentModule.getValues().get(x1);
                }
            }
        }
        if (GuiClickShell.currentModuleType == HackCategory.PLAYER) {
            final FontLoaders fontLoaders7 = Core.fontLoaders;
            FontLoaders.default20.drawString("PLAYER", GuiClickShell.startX + 235.0f, GuiClickShell.startY + 20.0f, Colors.getColor(245, 245, 247));
        }
        else {
            final FontLoaders fontLoaders8 = Core.fontLoaders;
            FontLoaders.default20.drawString("PLAYER", GuiClickShell.startX + 235.0f, GuiClickShell.startY + 20.0f, Colors.getColor(165, 165, 165));
        }
        if (this.isCategoryHovered(GuiClickShell.startX + 290.0f, GuiClickShell.startY + 10.0f, GuiClickShell.startX + 340.0f, GuiClickShell.startY + 40.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            GuiClickShell.currentModuleType = HackCategory.ANOTHER;
            final HackManager hackManager4 = Core.hackManager;
            GuiClickShell.currentModule = HackManager.getModulesInType(GuiClickShell.currentModuleType).get(0);
            GuiClickShell.moduleStart = 0;
            GuiClickShell.valueStart = 0;
            for (int x1 = 0; x1 < GuiClickShell.currentModule.getValues().size(); ++x1) {
                final Value value5 = GuiClickShell.currentModule.getValues().get(x1);
            }
        }
        if (GuiClickShell.currentModuleType == HackCategory.ANOTHER) {
            final FontLoaders fontLoaders9 = Core.fontLoaders;
            FontLoaders.default20.drawString("CLIENT", GuiClickShell.startX + 292.0f, GuiClickShell.startY + 20.0f, Colors.getColor(245, 245, 247));
        }
        else {
            final FontLoaders fontLoaders10 = Core.fontLoaders;
            FontLoaders.default20.drawString("CLIENT", GuiClickShell.startX + 292.0f, GuiClickShell.startY + 20.0f, Colors.getColor(165, 165, 165));
        }
        if (this.isCategoryHovered(GuiClickShell.startX + 350.0f, GuiClickShell.startY + 10.0f, GuiClickShell.startX + 400.0f, GuiClickShell.startY + 40.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            GuiClickShell.currentModuleType = HackCategory.VISUAL;
            final HackManager hackManager5 = Core.hackManager;
            GuiClickShell.currentModule = HackManager.getModulesInType(GuiClickShell.currentModuleType).get(0);
            GuiClickShell.moduleStart = 0;
            GuiClickShell.valueStart = 0;
            if (GuiClickShell.currentModule.getValues() != null) {
                for (int x1 = 0; x1 < GuiClickShell.currentModule.getValues().size(); ++x1) {
                    final Value value6 = GuiClickShell.currentModule.getValues().get(x1);
                }
            }
        }
        if (GuiClickShell.currentModuleType == HackCategory.VISUAL) {
            final FontLoaders fontLoaders11 = Core.fontLoaders;
            FontLoaders.default20.drawString("VISUAL", GuiClickShell.startX + 352.0f, GuiClickShell.startY + 20.0f, Colors.getColor(245, 245, 247));
        }
        else {
            final FontLoaders fontLoaders12 = Core.fontLoaders;
            FontLoaders.default20.drawString("VISUAL", GuiClickShell.startX + 352.0f, GuiClickShell.startY + 20.0f, Colors.getColor(165, 165, 165));
        }
        if (GuiClickShell.currentModule == null) {
            return;
        }
        final int m = Mouse.getDWheel();
        RenderUtils.drawRect(GuiClickShell.startX + 100.0f, GuiClickShell.startY + 51.0f, GuiClickShell.startX + 101.0f, GuiClickShell.startY + this.main_height, Colors.getColor(78, 78, 78));
        if (this.isCategoryHovered(GuiClickShell.startX, GuiClickShell.startY + 51.0f, GuiClickShell.startX + 100.0f, GuiClickShell.startY + this.main_height, mouseX, mouseY)) {
            final HackManager hackManager6 = Core.hackManager;
            if (HackManager.getModulesInType(GuiClickShell.currentModuleType) != null) {
                if (m < 0) {
                    final int moduleStart = GuiClickShell.moduleStart;
                    final HackManager hackManager7 = Core.hackManager;
                    if (moduleStart < HackManager.getModulesInType(GuiClickShell.currentModuleType).size() - 1) {
                        ++GuiClickShell.moduleStart;
                    }
                }
                if (m > 0 && GuiClickShell.moduleStart > 0) {
                    --GuiClickShell.moduleStart;
                }
            }
        }
        if (this.isCategoryHovered(GuiClickShell.startX + 102.0f, GuiClickShell.startY + 51.0f, GuiClickShell.startX + this.main_width, GuiClickShell.startY + this.main_height, mouseX, mouseY) && GuiClickShell.currentModule.getValues() != null) {
            if (m < 0 && GuiClickShell.valueStart < GuiClickShell.currentModule.getValues().size() - 1) {
                ++GuiClickShell.valueStart;
            }
            if (m > 0 && GuiClickShell.valueStart > 0) {
                --GuiClickShell.valueStart;
            }
        }
        float mY = GuiClickShell.startY + 6.0f;
        int i = 0;
        while (true) {
            final int n = i;
            final HackManager hackManager8 = Core.hackManager;
            if (n >= HackManager.getModulesInType(GuiClickShell.currentModuleType).size()) {
                break;
            }
            final HackManager hackManager9 = Core.hackManager;
            final Hack module = HackManager.getModulesInType(GuiClickShell.currentModuleType).get(i);
            if (mY > GuiClickShell.startY + this.main_height - 70.0f) {
                break;
            }
            if (i >= GuiClickShell.moduleStart) {
                if (!module.isToggled()) {
                    RenderUtils.drawRoundRect(GuiClickShell.startX + 400.0f, GuiClickShell.startY + 260.0f, GuiClickShell.startX + 440.0f, GuiClickShell.startY + 280.0f, 4, Colors.getColor(100, 100, 100));
                    GL11.glEnable(3042);
                    FontLoaders.default18.drawCenteredString(this.bind ? "Press" : Keyboard.getKeyName(module.getKey()), GuiClickShell.startX + 420.0f, GuiClickShell.startY + 270.0f, Colors.getColor(255, 255, 255));
                    GL11.glDisable(3042);
                    final boolean animate = false;
                    this.animate = animate;
                    if (animate) {
                        RenderUtils.drawRoundRect(GuiClickShell.startX + 12.0f, mY + 55.0f - 4.0f, GuiClickShell.startX + 14.0f, mY + 55.0f + 10.0f, 1, Colors.getColor(245, 245, 245));
                    }
                    GL11.glEnable(3042);
                    font.drawString(module.getName(), (int)GuiClickShell.startX + 20, (int)mY + 55, new Color(165, 165, 165).getRGB());
                    GL11.glDisable(3042);
                }
                else {
                    RenderUtils.drawRoundRect(GuiClickShell.startX + 400.0f, GuiClickShell.startY + 260.0f, GuiClickShell.startX + 440.0f, GuiClickShell.startY + 280.0f, 4, Colors.getColor(100, 100, 100));
                    GL11.glEnable(3042);
                    FontLoaders.default18.drawCenteredString(this.bind ? "Press" : Keyboard.getKeyName(module.getKey()), GuiClickShell.startX + 420.0f, GuiClickShell.startY + 270.0f, Colors.getColor(255, 255, 255));
                    GL11.glDisable(3042);
                    final boolean animate2 = false;
                    this.animate = animate2;
                    if (animate2) {
                        RenderUtils.drawRoundRect(GuiClickShell.startX + 12.0f, mY + 55.0f - 4.0f, GuiClickShell.startX + 14.0f, mY + 55.0f + 10.0f, 1, Colors.getColor(245, 245, 245));
                    }
                    GL11.glEnable(3042);
                    font.drawString(module.getName(), (int)GuiClickShell.startX + 20, (int)mY + 55, new Color(245, 245, 247).getRGB());
                    GL11.glDisable(3042);
                }
                if (this.isSettingsButtonHovered(GuiClickShell.startX, mY + 45.0f, GuiClickShell.startX + 100.0f, mY + 65.0f, mouseX, mouseY)) {
                    if (!this.previousmouse && Mouse.isButtonDown(0)) {
                        module.toggle();
                        final FileManager fileManager = Core.fileManager;
                        FileManager.saveHacks();
                        this.previousmouse = true;
                    }
                    if (!this.previousmouse && Mouse.isButtonDown(1)) {
                        this.previousmouse = true;
                    }
                }
                if (!Mouse.isButtonDown(0)) {
                    this.previousmouse = false;
                }
                if (Mouse.isButtonDown(0)) {}
                if (this.isHovered(GuiClickShell.startX + 400.0f, GuiClickShell.startY + 260.0f, GuiClickShell.startX + 440.0f, GuiClickShell.startY + 280.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    this.bind = true;
                }
                if (this.isSettingsButtonHovered(GuiClickShell.startX, mY + 45.0f, GuiClickShell.startX + 100.0f, mY + 70.0f, mouseX, mouseY) && Mouse.isButtonDown(1)) {
                    this.animate = true;
                    this.target = mY + 54.0f;
                    GuiClickShell.currentModule = module;
                    GuiClickShell.valueStart = 0;
                }
                mY += 20.0f;
            }
            ++i;
        }
        mY = GuiClickShell.startY + 12.0f;
        final boolean animate3 = true;
        this.animate = animate3;
        if (animate3) {
            this.h = this.animationUtils.animate(this.target, this.h, 0.1f);
            RenderUtils.drawRoundRect(GuiClickShell.startX + 12.0f, this.h - 4.0f, GuiClickShell.startX + 14.0f, this.h + 10.0f, 1, Colors.getColor(245, 245, 245));
            if (this.h == this.target) {
                this.animate = false;
            }
        }
        if (GuiClickShell.currentModule != null && GuiClickShell.currentModule.getDescription() != null) {
            Wrapper.INSTANCE.mc().fontRenderer.drawString(GuiClickShell.currentModule.getDescription(), (int)GuiClickShell.startX + 110, (int)mY + 275, new Color(245, 245, 245).getRGB());
        }
        if (GuiClickShell.currentModule != null && GuiClickShell.currentModule.getValues() != null) {
            for (i = 0; i < GuiClickShell.currentModule.getValues().size() && mY <= GuiClickShell.startY + 220.0f; ++i) {
                if (i >= GuiClickShell.valueStart) {
                    final Value value = GuiClickShell.currentModule.getValues().get(i);
                    if (value instanceof NumberValue) {
                        final float x2 = GuiClickShell.startX + 150.0f;
                        double render = 200.0 * (((NumberValue)value).getValue() - ((NumberValue)value).getMin()) / (((NumberValue)value).getMax() - ((NumberValue)value).getMin());
                        RenderUtils.drawRoundRect(x2 + 2.0f, mY + 65.0f, x2 + 202.0f, mY + 70.0f, 3, this.isButtonHovered(x2 + 2.0f, mY + 65.0f, x2 + 202.0f, mY + 70.0f, mouseX, mouseY) ? Colors.getColor(120, 120, 120) : Colors.getColor(81, 81, 84));
                        RenderUtils.drawRoundRect(x2 + 2.0f, mY + 65.0f, x2 + render + 6.5, mY + 70.0f, 3, Colors.getColor(245, 245, 245));
                        RenderUtils.drawRoundRect(x2 + render + 2.0 + 1.0, mY + 61.0f, x2 + render + 2.0 + 5.0, mY + 65.0f + 9.0f, 2, Colors.getColor(245, 245, 245));
                        GL11.glPushMatrix();
                        GL11.glEnable(3042);
                        font.drawString(value.getName(), (int)GuiClickShell.startX + 130, (int)mY + 50, new Color(245, 245, 245).getRGB());
                        font.drawString("" + value.getValue(), (int)GuiClickShell.startX + 140 - font.getStringWidth("" + value.getValue()), (int)mY + 65, new Color(245, 245, 245).getRGB());
                        GL11.glDisable(3042);
                        GL11.glPopMatrix();
                        if (!Mouse.isButtonDown(0)) {
                            this.previousmouse = false;
                        }
                        if (this.isButtonHovered(x2 + 2.0f, mY + 65.0f, x2 + 202.0f, mY + 70.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                            if (!this.previousmouse && Mouse.isButtonDown(0)) {
                                render = ((NumberValue)value).getMin();
                                final double max = ((NumberValue)value).getMax();
                                final double inc = 0.01;
                                final double valAbs = mouseX - (x2 + 1.0);
                                double perc = valAbs / 200.0;
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
                        mY += 30.0f;
                    }
                    if (value instanceof BooleanValue) {
                        final float x2 = GuiClickShell.startX + 180.0f;
                        final int xx = 30;
                        final int x2x = 45;
                        GL11.glEnable(3042);
                        font.drawString(value.getName(), (int)GuiClickShell.startX + 150, (int)mY + 50, new Color(245, 245, 245).getRGB());
                        GL11.glDisable(3042);
                        final BooleanValue S = (BooleanValue)value;
                        if (S.getValue()) {
                            RenderUtils.drawRoundRect(GuiClickShell.startX + 130.0f, mY + 46.0f, GuiClickShell.startX + 130.0f + 14.0f, mY + 50.0f + 10.0f, 2, Colors.getColor(0, 0, 0));
                            RenderUtils.drawRoundRect(GuiClickShell.startX + 130.0f + 1.0f, mY + 46.0f + 1.0f, GuiClickShell.startX + 130.0f + 14.0f - 1.0f, mY + 50.0f + 10.0f - 1.0f, 2, Colors.getColor(200, 200, 200));
                        }
                        else {
                            RenderUtils.drawRoundRect(GuiClickShell.startX + 130.0f, mY + 46.0f, GuiClickShell.startX + 130.0f + 14.0f, mY + 50.0f + 10.0f, 2, Colors.getColor(0, 0, 0));
                        }
                        if (this.isCheckBoxHovered(GuiClickShell.startX + 130.0f, mY + 46.0f, GuiClickShell.startX + 130.0f + 14.0f, mY + 50.0f + 10.0f, mouseX, mouseY)) {
                            RenderUtils.drawRect(GuiClickShell.startX + 130.0f + 1.0f, mY + 46.0f + 1.0f, GuiClickShell.startX + 130.0f + 14.0f - 1.0f, mY + 50.0f + 10.0f - 1.0f, Colors.getColor(60, 60, 60));
                            if (!this.previousmouse && Mouse.isButtonDown(0)) {
                                this.previousmouse = true;
                                this.mouse = true;
                            }
                            if (this.mouse) {
                                S.setValue(!S.getValue());
                                this.mouse = false;
                            }
                        }
                        if (!Mouse.isButtonDown(0)) {
                            this.previousmouse = false;
                        }
                        mY += 20.0f;
                    }
                    if (value instanceof ModeValue) {
                        final float x2 = GuiClickShell.startX + 130.0f;
                        GL11.glEnable(3042);
                        font.drawStringWithShadow(value.getName(), GuiClickShell.startX + 130.0f, mY + 52.0f, new Color(245, 245, 245).getRGB());
                        GL11.glDisable(3042);
                        RenderUtils.drawRoundRect(x2, mY + 62.0f, x2 + 100.0f, mY + 76.0f, 2, Colors.getColor(80, 80, 80));
                        final ModeValue M = (ModeValue)value;
                        if (M.getValue() != null) {
                            GL11.glEnable(3042);
                            font.drawStringWithShadow(M.getValue().getName(), x2 + 18.0f - font.getStringWidth(M.getValue().getName()) / 2, mY + 66.0f, new Color(220, 220, 220).getRGB());
                            GL11.glDisable(3042);
                        }
                        if (this.isStringHovered(x2, mY + 62.0f, x2 + 100.0f, mY + 76.0f, mouseX, mouseY)) {
                            if (!this.previousmouse && Mouse.isButtonDown(0)) {
                                this.previousmouse = true;
                                this.mouse = true;
                            }
                            if (this.mouse) {
                                final int maxIndex = M.getModes().length;
                                ++this.modeIndex;
                                if (this.modeIndex + 1 > maxIndex) {
                                    this.modeIndex = 0;
                                }
                                M.setValue(M.getModes()[this.modeIndex]);
                                this.mouse = false;
                            }
                            if (!Mouse.isButtonDown(0)) {
                                this.previousmouse = false;
                            }
                        }
                        mY += 30.0f;
                    }
                }
            }
            if ((this.isHovered(GuiClickShell.startX, GuiClickShell.startY, GuiClickShell.startX + 100.0f, GuiClickShell.startY + 50.0f, mouseX, mouseY) || this.isHovered(GuiClickShell.startX, GuiClickShell.startY + 315.0f, GuiClickShell.startX + 450.0f, GuiClickShell.startY + 350.0f, mouseX, mouseY) || this.isHovered(GuiClickShell.startX + 430.0f, GuiClickShell.startY, GuiClickShell.startX + 450.0f, GuiClickShell.startY + 350.0f, mouseX, mouseY)) && Mouse.isButtonDown(0)) {
                if (this.moveX == 0.0f && this.moveY == 0.0f) {
                    this.moveX = mouseX - GuiClickShell.startX;
                    this.moveY = mouseY - GuiClickShell.startY;
                }
                else {
                    GuiClickShell.startX = mouseX - this.moveX;
                    GuiClickShell.startY = mouseY - this.moveY;
                }
                this.previousmouse = true;
            }
            else if (this.moveX != 0.0f || this.moveY != 0.0f) {
                this.moveX = 0.0f;
                this.moveY = 0.0f;
            }
            final int j = 59;
            final int l = 40;
            final float k = GuiClickShell.startY + 10.0f;
            final float n2 = GuiClickShell.startX + 5.0f;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void onGuiClosed() {
        super.onGuiClosed();
        ClickGui.shell_x = GuiClickShell.startX;
        ClickGui.shell_y = GuiClickShell.startY;
        ClickGui.shell_category = GuiClickShell.currentModuleType;
        ClickGui.shell_module = GuiClickShell.currentModule;
        final FileManager fileManager = Core.fileManager;
        FileManager.saveClickGui();
        final FileManager fileManager2 = Core.fileManager;
        FileManager.saveHacks();
        try {
            Mouse.setNativeCursor(null);
        }
        catch (Throwable t) {}
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
    
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.bind) {
            GuiClickShell.currentModule.setKey(keyCode);
            if (keyCode == 1) {
                GuiClickShell.currentModule.setKey(0);
            }
            this.bind = false;
        }
        else if (keyCode == 1) {
            GuiClickShell.mc.displayGuiScreen((GuiScreen)null);
            if (GuiClickShell.mc.currentScreen == null) {
                GuiClickShell.mc.setIngameFocus();
            }
        }
    }
    
    public static void setMod(final Hack mod) {
        GuiClickShell.currentModule = mod;
    }
    
    public static void setX(final float shell_x) {
        GuiClickShell.startX = shell_x;
    }
    
    public static void setY(final float shell_y) {
        GuiClickShell.startY = shell_y;
    }
    
    public static void setCategory(final HackCategory state) {
        GuiClickShell.currentModuleType = state;
    }
    
    static {
        GuiClickShell.mc = Minecraft.getMinecraft();
        GuiClickShell.sr = new ScaledResolution(GuiClickShell.mc);
        GuiClickShell.startX = 100.0f;
        GuiClickShell.startY = 40.0f;
        GuiClickShell.moduleStart = 0;
        GuiClickShell.valueStart = 0;
        GuiClickShell.alphe = 121;
    }
}
