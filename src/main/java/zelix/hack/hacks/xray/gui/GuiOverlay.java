package zelix.hack.hacks.xray.gui;

import net.minecraft.util.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.toasts.*;
import net.minecraft.client.resources.*;
import zelix.hack.hacks.xray.etc.*;
import zelix.hack.hacks.xray.xray.*;
import zelix.hack.hacks.xray.*;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;

public class GuiOverlay
{
    public static String XrayStr;
    private static final ResourceLocation circle;
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void RenderGameOverlayEvent(final RenderGameOverlayEvent.Post event) {
        final int width = event.getResolution().getScaledWidth();
        for (int i = 0; i < AntiAntiXray.jobs.size(); ++i) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(width - 160), (float)(i * 32), 0.0f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(IToast.TEXTURE_TOASTS);
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(0, 0, 0, 96, 160, 32);
            XRay.mc.fontRenderer.drawStringWithShadow(I18n.format("xray.toast.title", new Object[0]), 6.0f, 6.0f, 0);
            XRay.mc.fontRenderer.drawStringWithShadow(AntiAntiXray.jobs.get(i).refresher.getProcessText(), 6.0f, 18.0f, 0);
            GlStateManager.popMatrix();
        }
        if (!Controller.drawOres() || !Configuration.showOverlay) {
            GuiOverlay.XrayStr = "";
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.color(0.0f, 255.0f, 0.0f, 30.0f);
        XRay.mc.renderEngine.bindTexture(GuiOverlay.circle);
        Gui.drawModalRectWithCustomSizedTexture(5, 5, 0.0f, 0.0f, 5, 5, 5.0f, 5.0f);
        GlStateManager.disableBlend();
        GuiOverlay.XrayStr = "Rendering";
        if (Configuration.freeze) {
            GlStateManager.enableBlend();
            GlStateManager.color(0.0f, 255.0f, 0.0f, 30.0f);
            XRay.mc.renderEngine.bindTexture(GuiOverlay.circle);
            Gui.drawModalRectWithCustomSizedTexture(5, 17, 0.0f, 0.0f, 5, 5, 5.0f, 5.0f);
            GlStateManager.disableBlend();
            GuiOverlay.XrayStr = "Rendering Freeze";
        }
        GlStateManager.popMatrix();
    }
    
    public void drawTexturedModalRect(final int x, final int y, final int textureX, final int textureY, final int width, final int height) {
        final double zLevel = 0.0;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)x, (double)(y + height), zLevel).tex((double)(textureX * 0.00390625f), (double)((textureY + height) * 0.00390625f)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), zLevel).tex((double)((textureX + width) * 0.00390625f), (double)((textureY + height) * 0.00390625f)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)y, zLevel).tex((double)((textureX + width) * 0.00390625f), (double)(textureY * 0.00390625f)).endVertex();
        bufferbuilder.pos((double)x, (double)y, zLevel).tex((double)(textureX * 0.00390625f), (double)(textureY * 0.00390625f)).endVertex();
        tessellator.draw();
    }
    
    static {
        GuiOverlay.XrayStr = "";
        circle = new ResourceLocation("xray:textures/gui/circle.png");
    }
}
