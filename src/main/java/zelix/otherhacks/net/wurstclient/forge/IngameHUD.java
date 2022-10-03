package zelix.otherhacks.net.wurstclient.forge;

import net.minecraft.client.*;
import net.minecraftforge.client.event.*;
import org.lwjgl.opengl.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import java.util.function.*;
import zelix.otherhacks.net.wurstclient.forge.clickgui.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;

public final class IngameHUD
{
    private final Minecraft mc;
    private final HackList hackList;
    private final ClickGui clickGui;
    
    public IngameHUD(final HackList hackList, final ClickGui clickGui) {
        this.mc = Minecraft.getMinecraft();
        this.hackList = hackList;
        this.clickGui = clickGui;
    }
    
    @SubscribeEvent
    public void onRenderGUI(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL || this.mc.gameSettings.showDebugInfo) {
            return;
        }
        final boolean blend = GL11.glGetBoolean(3042);
        this.clickGui.updateColors();
        int textColor;
        if (this.hackList.rainbowUiHack.isEnabled()) {
            final float[] acColor = this.clickGui.getAcColor();
            textColor = ((int)(acColor[0] * 256.0f) << 16 | (int)(acColor[1] * 256.0f) << 8 | (int)(acColor[2] * 256.0f));
        }
        else {
            textColor = 16777215;
        }
        GL11.glPushMatrix();
        GL11.glScaled(1.33333333, 1.33333333, 1.0);
        WMinecraft.getFontRenderer().drawStringWithShadow("ForgeWurst v0.11 by H2Eng", 3.0f, 3.0f, textColor);
        GL11.glPopMatrix();
        int y = 19;
        final ArrayList<Hack> hacks = new ArrayList<Hack>();
        hacks.addAll(this.hackList.getValues());
        hacks.sort(Comparator.comparing((Function<? super Hack, ? extends Comparable>)Hack::getName));
        for (final Hack hack : hacks) {
            if (!hack.isEnabled()) {
                continue;
            }
            WMinecraft.getFontRenderer().drawStringWithShadow(hack.getRenderName(), 2.0f, (float)y, textColor);
            y += 9;
        }
        if (!(this.mc.currentScreen instanceof ClickGuiScreen)) {
            this.clickGui.renderPinnedWindows(event.getPartialTicks());
        }
        if (blend) {
            GL11.glEnable(3042);
        }
        else {
            GL11.glDisable(3042);
        }
    }
}
