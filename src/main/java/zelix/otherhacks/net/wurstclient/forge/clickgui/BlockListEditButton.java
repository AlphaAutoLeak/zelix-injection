package zelix.otherhacks.net.wurstclient.forge.clickgui;

import zelix.otherhacks.net.wurstclient.forge.settings.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import zelix.otherhacks.net.wurstclient.forge.*;
import org.lwjgl.opengl.*;

public final class BlockListEditButton extends Component
{
    private final BlockListSetting setting;
    private int buttonWidth;
    
    public BlockListEditButton(final BlockListSetting setting) {
        this.setting = setting;
        final FontRenderer fr = WMinecraft.getFontRenderer();
        this.buttonWidth = fr.getStringWidth("Edit...");
        this.setWidth(this.getDefaultWidth());
        this.setHeight(this.getDefaultHeight());
    }
    
    @Override
    public void handleMouseClick(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton != 0) {
            return;
        }
        if (mouseX < this.getX() + this.getWidth() - this.buttonWidth - 4) {
            return;
        }
        Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new EditBlockListScreen(Minecraft.getMinecraft().currentScreen, this.setting));
    }
    
    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        final ClickGui gui = ForgeWurst.getForgeWurst().getGui();
        final float[] bgColor = gui.getBgColor();
        final float[] acColor = gui.getAcColor();
        final float opacity = gui.getOpacity();
        final int x1 = this.getX();
        final int x2 = x1 + this.getWidth();
        final int x3 = x2 - this.buttonWidth - 4;
        final int y1 = this.getY();
        final int y2 = y1 + this.getHeight();
        final int scroll = this.getParent().isScrollingEnabled() ? this.getParent().getScrollOffset() : 0;
        final boolean hovering = mouseX >= x1 && mouseY >= y1 && mouseX < x2 && mouseY < y2 && mouseY >= -scroll && mouseY < this.getParent().getHeight() - 13 - scroll;
        final boolean hText = hovering && mouseX < x3;
        final boolean hBox = hovering && mouseX >= x3;
        if (hText) {
            gui.setTooltip(this.setting.getDescription());
        }
        GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], opacity);
        GL11.glBegin(7);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y2);
        GL11.glVertex2i(x3, y2);
        GL11.glVertex2i(x3, y1);
        GL11.glEnd();
        GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], hBox ? (opacity * 1.5f) : opacity);
        GL11.glBegin(7);
        GL11.glVertex2i(x3, y1);
        GL11.glVertex2i(x3, y2);
        GL11.glVertex2i(x2, y2);
        GL11.glVertex2i(x2, y1);
        GL11.glEnd();
        GL11.glColor4f(acColor[0], acColor[1], acColor[2], 0.5f);
        GL11.glBegin(2);
        GL11.glVertex2i(x3, y1);
        GL11.glVertex2i(x3, y2);
        GL11.glVertex2i(x2, y2);
        GL11.glVertex2i(x2, y1);
        GL11.glEnd();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        final FontRenderer fr = WMinecraft.getFontRenderer();
        final String text = this.setting.getName() + ": " + this.setting.getBlockNames().size();
        fr.drawString(text, x1, y1 + 2, 15790320);
        fr.drawString("Edit...", x3 + 2, y1 + 2, 15790320);
        GL11.glDisable(3553);
    }
    
    @Override
    public int getDefaultWidth() {
        final FontRenderer fr = WMinecraft.getFontRenderer();
        final String text = this.setting.getName() + ": " + this.setting.getBlockNames().size();
        return fr.getStringWidth(text) + this.buttonWidth + 6;
    }
    
    @Override
    public int getDefaultHeight() {
        return 11;
    }
}
