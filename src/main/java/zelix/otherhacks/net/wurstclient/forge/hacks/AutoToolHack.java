package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.util.math.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import net.minecraft.block.state.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.enchantment.*;

public final class AutoToolHack extends Hack
{
    private final CheckboxSetting useSwords;
    private final CheckboxSetting useHands;
    private final CheckboxSetting repairMode;
    
    public AutoToolHack() {
        super("AutoTool", "Automatically equips the fastest\napplicable tool in your hotbar\nwhen you try to break a block.");
        this.useSwords = new CheckboxSetting("Use swords", "Uses swords to break\nleaves, cobwebs, etc.", false);
        this.useHands = new CheckboxSetting("Use hands", "Uses an empty hand or a\nnon-damageable item when\nno applicable tool is found.", true);
        this.repairMode = new CheckboxSetting("Repair mode", "Won't use tools that are about to break.", false);
        this.setCategory(Category.BLOCKS);
        this.addSetting(this.useSwords);
        this.addSetting(this.useHands);
        this.addSetting(this.repairMode);
    }
    
    @Override
    protected void onEnable() {
        AutoToolHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onPlayerDamageBlock(final PlayerInteractEvent.LeftClickBlock event) {
        this.equipBestTool(event.getPos(), this.useSwords.isChecked(), this.useHands.isChecked(), this.repairMode.isChecked());
    }
    
    public void equipBestTool(final BlockPos pos, final boolean useSwords, final boolean useHands, final boolean repairMode) {
        final EntityPlayer player = (EntityPlayer)WMinecraft.getPlayer();
        if (player.capabilities.isCreativeMode) {
            return;
        }
        final IBlockState state = BlockUtils.getState(pos);
        final ItemStack heldItem = player.getHeldItemMainhand();
        float bestSpeed = this.getDestroySpeed(heldItem, state);
        int bestSlot = -1;
        int fallbackSlot = -1;
        final InventoryPlayer inventory = player.inventory;
        for (int slot = 0; slot < 9; ++slot) {
            if (slot != inventory.currentItem) {
                final ItemStack stack = inventory.getStackInSlot(slot);
                if (fallbackSlot == -1 && !this.isDamageable(stack)) {
                    fallbackSlot = slot;
                }
                final float speed = this.getDestroySpeed(stack, state);
                if (speed > bestSpeed) {
                    if (useSwords || !(stack.getItem() instanceof ItemSword)) {
                        if (!this.isTooDamaged(stack, repairMode)) {
                            bestSpeed = speed;
                            bestSlot = slot;
                        }
                    }
                }
            }
        }
        final boolean useFallback = this.isDamageable(heldItem) && (this.isTooDamaged(heldItem, repairMode) || (useHands && this.getDestroySpeed(heldItem, state) <= 1.0f));
        if (bestSlot != -1) {
            inventory.currentItem = bestSlot;
            return;
        }
        if (!useFallback) {
            return;
        }
        if (fallbackSlot != -1) {
            inventory.currentItem = fallbackSlot;
            return;
        }
        if (this.isTooDamaged(heldItem, repairMode)) {
            if (inventory.currentItem == 8) {
                inventory.currentItem = 0;
            }
            else {
                final InventoryPlayer inventoryPlayer = inventory;
                ++inventoryPlayer.currentItem;
            }
        }
    }
    
    private float getDestroySpeed(final ItemStack stack, final IBlockState state) {
        float speed = (stack == null || stack.isEmpty()) ? 1.0f : stack.getDestroySpeed(state);
        if (speed > 1.0f) {
            final int efficiency = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
            if (efficiency > 0 && stack != null && !stack.isEmpty()) {
                speed += efficiency * efficiency + 1;
            }
        }
        return speed;
    }
    
    private boolean isDamageable(final ItemStack stack) {
        return stack != null && !stack.isEmpty() && stack.getItem().isDamageable();
    }
    
    private boolean isTooDamaged(final ItemStack stack, final boolean repairMode) {
        return repairMode && stack.getMaxDamage() - stack.getItemDamage() <= 4;
    }
}
