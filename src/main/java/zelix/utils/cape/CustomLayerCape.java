package zelix.utils.cape;

import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.entity.*;
import net.minecraft.util.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.player.*;
import zelix.*;
import net.minecraft.inventory.*;
import net.minecraft.init.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.math.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;

public class CustomLayerCape implements LayerRenderer<AbstractClientPlayer>
{
    private static ResourceLocation cape;
    private final RenderPlayer playerRenderer;
    
    public CustomLayerCape(final RenderPlayer playerRenderer) {
        this.playerRenderer = playerRenderer;
    }
    
    public void doRenderLayer(final AbstractClientPlayer entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (entitylivingbaseIn.hasPlayerInfo() && !entitylivingbaseIn.isInvisible() && entitylivingbaseIn.isWearing(EnumPlayerModelParts.CAPE) && Core.capeManager.getCapeByName(entitylivingbaseIn.getName()) != null) {
            final ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (itemstack.getItem() != Items.ELYTRA) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.playerRenderer.bindTexture(Core.capeManager.getCapeByName(entitylivingbaseIn.getName()));
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0f, 0.0f, 0.125f);
                final double d0 = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * partialTicks);
                final double d2 = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * partialTicks);
                final double d3 = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * partialTicks);
                final float f = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
                final double d4 = MathHelper.sin(f * 0.017453292f);
                final double d5 = -MathHelper.cos(f * 0.017453292f);
                float f2 = (float)d2 * 10.0f;
                f2 = MathHelper.clamp(f2, -6.0f, 32.0f);
                float f3 = (float)(d0 * d4 + d3 * d5) * 100.0f;
                final float f4 = (float)(d0 * d5 - d3 * d4) * 100.0f;
                if (f3 < 0.0f) {
                    f3 = 0.0f;
                }
                final float f5 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
                f2 += MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0f) * 32.0f * f5;
                if (entitylivingbaseIn.isSneaking()) {
                    f2 += 25.0f;
                }
                GlStateManager.rotate(6.0f + f3 / 2.0f + f2, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(f4 / 2.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(-f4 / 2.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                this.playerRenderer.getMainModel().renderCape(0.0625f);
                GlStateManager.popMatrix();
            }
        }
    }
    
    public boolean shouldCombineTextures() {
        return false;
    }
}
