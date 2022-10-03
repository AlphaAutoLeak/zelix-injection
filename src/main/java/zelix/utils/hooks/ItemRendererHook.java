package zelix.utils.hooks;

import net.minecraft.client.renderer.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.entity.*;
import net.minecraft.util.*;

public class ItemRendererHook extends ItemRenderer
{
    public ItemRendererHook(final Minecraft mcIn) {
        super(mcIn);
    }
    
    public void renderItem(final EntityLivingBase entityIn, final ItemStack heldStack, final ItemCameraTransforms.TransformType transform) {
        super.renderItem(entityIn, heldStack, transform);
    }
    
    public void renderItemSide(final EntityLivingBase entitylivingbaseIn, final ItemStack heldStack, final ItemCameraTransforms.TransformType transform, final boolean leftHanded) {
        super.renderItemSide(entitylivingbaseIn, heldStack, transform, leftHanded);
    }
    
    public void renderItemInFirstPerson(final float partialTicks) {
        super.renderItemInFirstPerson(partialTicks);
    }
    
    public void renderItemInFirstPerson(final AbstractClientPlayer player, final float p_187457_2_, final float p_187457_3_, final EnumHand hand, final float p_187457_5_, final ItemStack stack, final float p_187457_7_) {
        super.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_, stack, p_187457_7_);
    }
    
    public void renderOverlays(final float partialTicks) {
        super.renderOverlays(partialTicks);
    }
    
    public void updateEquippedItem() {
        super.updateEquippedItem();
    }
    
    public void resetEquippedProgress(final EnumHand hand) {
        super.resetEquippedProgress(hand);
    }
}
