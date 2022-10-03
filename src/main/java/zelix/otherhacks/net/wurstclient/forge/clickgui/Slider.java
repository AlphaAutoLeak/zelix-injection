package zelix.otherhacks.net.wurstclient.forge.clickgui;

import zelix.otherhacks.net.wurstclient.forge.settings.*;
import net.minecraft.client.*;
import org.lwjgl.input.*;
import zelix.otherhacks.net.wurstclient.forge.*;
import org.lwjgl.opengl.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import net.minecraft.client.gui.*;

public final class Slider extends Component
{
    private final SliderSetting setting;
    private boolean dragging;
    
    public Slider(final SliderSetting setting) {
        this.setting = setting;
        this.setWidth(this.getDefaultWidth());
        this.setHeight(this.getDefaultHeight());
    }
    
    @Override
    public void handleMouseClick(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseY < this.getY() + 11) {
            return;
        }
        if (mouseButton == 0) {
            if (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) {
                Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new EditSliderScreen(Minecraft.getMinecraft().currentScreen, this.setting));
            }
            else {
                this.dragging = true;
            }
        }
        else if (mouseButton == 1) {
            this.setting.setValue(this.setting.getDefaultValue());
        }
    }
    
    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.dragging) {
            if (Mouse.isButtonDown(0)) {
                final double mousePercentage = (mouseX - (this.getX() + 2)) / (this.getWidth() - 4);
                final double value = this.setting.getMin() + (this.setting.getMax() - this.setting.getMin()) * mousePercentage;
                this.setting.setValue(value);
            }
            else {
                this.dragging = false;
            }
        }
        final ClickGui gui = ForgeWurst.getForgeWurst().getGui();
        final float[] bgColor = gui.getBgColor();
        final float[] acColor = gui.getAcColor();
        final float opacity = gui.getOpacity();
        final int x1 = this.getX();
        final int x2 = x1 + this.getWidth();
        final int x3 = x1 + 2;
        final int x4 = x2 - 2;
        final int y1 = this.getY();
        final int y2 = y1 + this.getHeight();
        final int y3 = y1 + 11;
        final int y4 = y3 + 4;
        final int y5 = y2 - 4;
        final int scroll = this.getParent().isScrollingEnabled() ? this.getParent().getScrollOffset() : 0;
        final boolean hovering = mouseX >= x1 && mouseY >= y1 && mouseX < x2 && mouseY < y2 && mouseY >= -scroll && mouseY < this.getParent().getHeight() - 13 - scroll;
        final boolean hSlider = (hovering && mouseY >= y3) || this.dragging;
        if (hovering && mouseY < y3) {
            gui.setTooltip(this.setting.getDescription());
        }
        GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], opacity);
        GL11.glBegin(7);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y4);
        GL11.glVertex2i(x2, y4);
        GL11.glVertex2i(x2, y1);
        GL11.glVertex2i(x1, y5);
        GL11.glVertex2i(x1, y2);
        GL11.glVertex2i(x2, y2);
        GL11.glVertex2i(x2, y5);
        GL11.glVertex2i(x1, y4);
        GL11.glVertex2i(x1, y5);
        GL11.glVertex2i(x3, y5);
        GL11.glVertex2i(x3, y4);
        GL11.glVertex2i(x4, y4);
        GL11.glVertex2i(x4, y5);
        GL11.glVertex2i(x2, y5);
        GL11.glVertex2i(x2, y4);
        GL11.glEnd();
        GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], hSlider ? (opacity * 1.5f) : opacity);
        GL11.glBegin(7);
        GL11.glVertex2i(x3, y4);
        GL11.glVertex2i(x3, y5);
        GL11.glVertex2i(x4, y5);
        GL11.glVertex2i(x4, y4);
        GL11.glEnd();
        GL11.glColor4f(acColor[0], acColor[1], acColor[2], 0.5f);
        GL11.glBegin(2);
        GL11.glVertex2i(x3, y4);
        GL11.glVertex2i(x3, y5);
        GL11.glVertex2i(x4, y5);
        GL11.glVertex2i(x4, y4);
        GL11.glEnd();
        final double percentage = (this.setting.getValue() - this.setting.getMin()) / (this.setting.getMax() - this.setting.getMin());
        final double xk1 = x1 + (x2 - x1 - 8) * percentage;
        final double xk2 = xk1 + 8.0;
        final double yk1 = y3 + 1.5;
        final double yk2 = y2 - 1.5;
        final float f = (float)(2.0 * percentage);
        GL11.glColor4f(f, 2.0f - f, 0.0f, hSlider ? 1.0f : 0.75f);
        GL11.glBegin(7);
        GL11.glVertex2d(xk1, yk1);
        GL11.glVertex2d(xk1, yk2);
        GL11.glVertex2d(xk2, yk2);
        GL11.glVertex2d(xk2, yk1);
        GL11.glEnd();
        GL11.glColor4f(0.0625f, 0.0625f, 0.0625f, 0.5f);
        GL11.glBegin(2);
        GL11.glVertex2d(xk1, yk1);
        GL11.glVertex2d(xk1, yk2);
        GL11.glVertex2d(xk2, yk2);
        GL11.glVertex2d(xk2, yk1);
        GL11.glEnd();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        final FontRenderer fr = WMinecraft.getFontRenderer();
        fr.drawString(this.setting.getName(), x1, y1 + 2, 15790320);
        fr.drawString(this.setting.getValueString(), x2 - fr.getStringWidth(this.setting.getValueString()), y1 + 2, 15790320);
        GL11.glDisable(3553);
    }
    
    @Override
    public int getDefaultWidth() {
        final FontRenderer fr = WMinecraft.getFontRenderer();
        return fr.getStringWidth(this.setting.getName()) + fr.getStringWidth(this.setting.getValueString()) + 6;
    }
    
    @Override
    public int getDefaultHeight() {
        return 22;
    }
}
