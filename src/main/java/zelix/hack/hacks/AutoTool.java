package zelix.hack.hacks;

import zelix.hack.*;
import zelix.utils.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;

public class AutoTool extends Hack
{
    public AutoTool() {
        super("AutoTool", HackCategory.PLAYER);
    }
    
    @Override
    public String getDescription() {
        return "Automatically switch to the best tools when mining or attacking";
    }
    
    @Override
    public void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
        Utils.nullCheck();
        this.equipBestTool(Wrapper.INSTANCE.world().getBlockState(event.getPos()));
    }
    
    @Override
    public void onAttackEntity(final AttackEntityEvent event) {
        Utils.nullCheck();
        equipBestWeapon();
    }
    
    private void equipBestTool(final IBlockState blockState) {
        int bestSlot = -1;
        double max = 0.0;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Wrapper.INSTANCE.player().inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                float speed = stack.getDestroySpeed(blockState);
                if (speed > 1.0f) {
                    final int eff;
                    speed += (float)(((eff = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)) > 0) ? (Math.pow(eff, 2.0) + 1.0) : 0.0);
                    if (speed > max) {
                        max = speed;
                        bestSlot = i;
                    }
                }
            }
        }
        if (bestSlot != -1) {
            equip(bestSlot);
        }
    }
    
    public static void equipBestWeapon() {
        int bestSlot = -1;
        double maxDamage = 0.0;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Wrapper.INSTANCE.player().inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ItemSword) {
                    final double damage = ((ItemSword)stack.getItem()).getAttackDamage() + EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED) + 1.0 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack) * 0.6000000238418579;
                    if (damage > maxDamage) {
                        maxDamage = damage;
                        bestSlot = i;
                    }
                }
            }
        }
        if (bestSlot != -1) {
            equip(bestSlot);
        }
    }
    
    private static void equip(final int slot) {
        Wrapper.INSTANCE.player().inventory.currentItem = slot;
    }
}
