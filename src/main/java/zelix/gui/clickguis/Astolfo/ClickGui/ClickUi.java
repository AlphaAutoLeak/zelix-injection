package zelix.gui.clickguis.Astolfo.ClickGui;

import java.util.*;
import zelix.hack.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.*;
import org.lwjgl.input.*;
import java.io.*;
import com.google.common.collect.*;

public class ClickUi extends GuiScreen
{
    public static ArrayList<Window> windows;
    public double opacity;
    public int scrollVelocity;
    public static boolean binding;
    private float animpos;
    
    public ClickUi() {
        this.opacity = 0.0;
        this.animpos = 75.0f;
        if (ClickUi.windows.isEmpty()) {
            int x = 5;
            for (final HackCategory c : HackCategory.values()) {
                ClickUi.windows.add(new Window(c, x, 5));
                x += 105;
            }
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.opacity = ((this.opacity + 10.0 < 200.0) ? (this.opacity += 10.0) : 200.0);
        GlStateManager.pushMatrix();
        final ScaledResolution scaledRes = new ScaledResolution(this.mc);
        final float scale = scaledRes.getScaleFactor() / (float)Math.pow(scaledRes.getScaleFactor(), 2.0);
        ClickUi.windows.forEach(w -> w.render(mouseX, mouseY));
        GlStateManager.popMatrix();
        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            this.scrollVelocity = ((wheel < 0) ? -120 : ((wheel > 0) ? 120 : 0));
        }
        ClickUi.windows.forEach(w -> w.mouseScroll(mouseX, mouseY, this.scrollVelocity));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        ClickUi.windows.forEach(w -> w.click(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1 && !ClickUi.binding) {
            this.mc.displayGuiScreen((GuiScreen)null);
            return;
        }
        ClickUi.windows.forEach(w -> w.key(typedChar, keyCode));
    }
    
    public void initGui() {
        super.initGui();
    }
    
    public void onGuiClosed() {
        this.mc.entityRenderer.stopUseShader();
    }
    
    public synchronized void sendToFront(final Window window) {
        int panelIndex = 0;
        for (int i = 0; i < ClickUi.windows.size(); ++i) {
            if (ClickUi.windows.get(i) == window) {
                panelIndex = i;
                break;
            }
        }
        final Window t = ClickUi.windows.get(ClickUi.windows.size() - 1);
        ClickUi.windows.set(ClickUi.windows.size() - 1, ClickUi.windows.get(panelIndex));
        ClickUi.windows.set(panelIndex, t);
    }
    
    static {
        ClickUi.windows = Lists.newArrayList();
        ClickUi.binding = false;
    }
}
