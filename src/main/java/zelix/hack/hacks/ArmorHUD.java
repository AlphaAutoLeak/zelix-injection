package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.client.event.*;
import zelix.utils.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import java.util.*;
import net.minecraft.item.*;
import java.util.function.*;

public class ArmorHUD extends Hack
{
    public BooleanValue damage;
    public BooleanValue extraInfo;
    private int offHandHeldItemCount;
    private int armourCompress;
    private int armourSpacing;
    
    public ArmorHUD() {
        super("ArmorHUD", HackCategory.VISUAL);
        this.damage = new BooleanValue("Damage", Boolean.valueOf(true));
        this.extraInfo = new BooleanValue("ExtraInfo", Boolean.valueOf(false));
        this.addValue(this.damage, this.extraInfo);
    }
    
    @Override
    public String getDescription() {
        return "Show armor in game overlay.";
    }
    
    @Override
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Text event) {
        final ScaledResolution resolution = new ScaledResolution(Wrapper.INSTANCE.mc());
        final RenderItem itemRender = Wrapper.INSTANCE.mc().getRenderItem();
        GlStateManager.enableTexture2D();
        final int i = resolution.getScaledWidth() / 2;
        int iteration = 0;
        final int y = resolution.getScaledHeight() - 55 - (Wrapper.INSTANCE.player().isInWater() ? 10 : 0);
        for (final ItemStack is : Wrapper.INSTANCE.inventory().armorInventory) {
            ++iteration;
            if (is.isEmpty()) {
                continue;
            }
            final int x = i - 90 + (9 - iteration) * this.armourSpacing + this.armourCompress;
            GlStateManager.enableDepth();
            itemRender.zLevel = 200.0f;
            itemRender.renderItemAndEffectIntoGUI(is, x, y);
            itemRender.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, is, x, y, "");
            itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            final String s = (is.getCount() > 1) ? (is.getCount() + "") : "";
            Wrapper.INSTANCE.fontRenderer().drawStringWithShadow(s, (float)(x + 19 - 2 - Wrapper.INSTANCE.fontRenderer().getStringWidth(s)), (float)(y + 9), 16777215);
            if (this.damage.getValue()) {
                final float green = (is.getMaxDamage() - is.getItemDamage()) / is.getMaxDamage();
                final float red = 1.0f - green;
                final int dmg = 100 - (int)(red * 100.0f);
                Wrapper.INSTANCE.fontRenderer().drawStringWithShadow(dmg + "", (float)(x + 8 - Wrapper.INSTANCE.fontRenderer().getStringWidth(dmg + "") / 2), (float)(y - 11), 16777215);
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
        if (this.extraInfo.getValue()) {
            for (final ItemStack is : Wrapper.INSTANCE.inventory().offHandInventory) {
                final Item helfInOffHand = Wrapper.INSTANCE.player().getHeldItemOffhand().getItem();
                this.offHandHeldItemCount = this.getItemsOffHand(helfInOffHand);
                GlStateManager.pushMatrix();
                GlStateManager.disableAlpha();
                GlStateManager.clear(256);
                GlStateManager.enableBlend();
                GlStateManager.pushAttrib();
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.disableDepth();
                Wrapper.INSTANCE.mc().getRenderItem().renderItemAndEffectIntoGUI(is, 572, y);
                itemRender.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, is, 572, y, String.valueOf(this.offHandHeldItemCount));
                GlStateManager.enableDepth();
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popAttrib();
                GlStateManager.disableBlend();
                GlStateManager.disableDepth();
                GlStateManager.disableLighting();
                GlStateManager.enableDepth();
                GlStateManager.enableAlpha();
                GlStateManager.popMatrix();
            }
        }
        if (this.extraInfo.getValue()) {
            final Item currentHeldItem = Wrapper.INSTANCE.inventory().getCurrentItem().getItem();
            final int currentHeldItemCount = Wrapper.INSTANCE.inventory().getCurrentItem().getCount();
            final ItemStack stackHeld = new ItemStack(currentHeldItem, 1);
            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            GlStateManager.clear(256);
            GlStateManager.enableBlend();
            GlStateManager.pushAttrib();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableDepth();
            Wrapper.INSTANCE.mc().getRenderItem().renderItemAndEffectIntoGUI(stackHeld, 556, y);
            itemRender.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, stackHeld, 556, y, String.valueOf(currentHeldItemCount));
            GlStateManager.enableDepth();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();
            GlStateManager.disableBlend();
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        if (this.extraInfo.getValue()) {
            this.armourCompress = 14;
            this.armourSpacing = 17;
        }
        else {
            this.armourCompress = 2;
            this.armourSpacing = 20;
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }
    
    int getItemsOffHand(final Item i) {
        return Wrapper.INSTANCE.inventory().offHandInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::getCount).sum();
    }
}
