package zelix.otherhacks.net.wurstclient.forge.clickgui;

import zelix.otherhacks.net.wurstclient.forge.settings.*;
import zelix.otherhacks.net.wurstclient.forge.*;
import org.lwjgl.opengl.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import net.minecraft.client.gui.*;

public final class Checkbox extends Component
{
    private final CheckboxSetting setting;
    
    public Checkbox(final CheckboxSetting setting) {
        this.setting = setting;
        this.setWidth(this.getDefaultWidth());
        this.setHeight(this.getDefaultHeight());
    }
    
    @Override
    public void handleMouseClick(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0) {
            this.setting.setChecked(!this.setting.isChecked());
        }
        else if (mouseButton == 1) {
            this.setting.setChecked(this.setting.isCheckedByDefault());
        }
    }
    
    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        final ClickGui gui = ForgeWurst.getForgeWurst().getGui();
        final float[] bgColor = gui.getBgColor();
        final float[] acColor = gui.getAcColor();
        final float opacity = gui.getOpacity();
        final int x1 = this.getX();
        final int x2 = x1 + this.getWidth();
        final int x3 = x1 + 11;
        final int y1 = this.getY();
        final int y2 = y1 + this.getHeight();
        final int scroll = this.getParent().isScrollingEnabled() ? this.getParent().getScrollOffset() : 0;
        final boolean hovering = mouseX >= x1 && mouseY >= y1 && mouseX < x2 && mouseY < y2 && mouseY >= -scroll && mouseY < this.getParent().getHeight() - 13 - scroll;
        if (hovering && mouseX >= x3) {
            gui.setTooltip(this.setting.getDescription());
        }
        GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], opacity);
        GL11.glBegin(7);
        GL11.glVertex2i(x3, y1);
        GL11.glVertex2i(x3, y2);
        GL11.glVertex2i(x2, y2);
        GL11.glVertex2i(x2, y1);
        GL11.glEnd();
        GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], hovering ? (opacity * 1.5f) : opacity);
        GL11.glBegin(7);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y2);
        GL11.glVertex2i(x3, y2);
        GL11.glVertex2i(x3, y1);
        GL11.glEnd();
        GL11.glColor4f(acColor[0], acColor[1], acColor[2], 0.5f);
        GL11.glBegin(2);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y2);
        GL11.glVertex2i(x3, y2);
        GL11.glVertex2i(x3, y1);
        GL11.glEnd();
        if (this.setting.isChecked()) {
            final double xc1 = x1 + 2.5;
            final double xc2 = x1 + 3.5;
            final double xc3 = x1 + 4.5;
            final double xc4 = x1 + 7.5;
            final double xc5 = x1 + 8.5;
            final double yc1 = y1 + 2.5;
            final double yc2 = y1 + 3.5;
            final double yc3 = y1 + 5.5;
            final double yc4 = y1 + 6.5;
            final double yc5 = y1 + 8.5;
            GL11.glColor4f(0.0f, hovering ? 1.0f : 0.85f, 0.0f, 1.0f);
            GL11.glBegin(7);
            GL11.glVertex2d(xc2, yc3);
            GL11.glVertex2d(xc3, yc4);
            GL11.glVertex2d(xc3, yc5);
            GL11.glVertex2d(xc1, yc4);
            GL11.glVertex2d(xc4, yc1);
            GL11.glVertex2d(xc5, yc2);
            GL11.glVertex2d(xc3, yc5);
            GL11.glVertex2d(xc3, yc4);
            GL11.glEnd();
            GL11.glColor4f(0.0625f, 0.0625f, 0.0625f, 0.5f);
            GL11.glBegin(2);
            GL11.glVertex2d(xc2, yc3);
            GL11.glVertex2d(xc3, yc4);
            GL11.glVertex2d(xc4, yc1);
            GL11.glVertex2d(xc5, yc2);
            GL11.glVertex2d(xc3, yc5);
            GL11.glVertex2d(xc1, yc4);
            GL11.glEnd();
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        final FontRenderer fr = WMinecraft.getFontRenderer();
        fr.drawString(this.setting.getName(), x3 + 2, y1 + 2, 15790320);
        GL11.glDisable(3553);
    }
    
    @Override
    public int getDefaultWidth() {
        return WMinecraft.getFontRenderer().getStringWidth(this.setting.getName()) + 13;
    }
    
    @Override
    public int getDefaultHeight() {
        return 11;
    }
}
