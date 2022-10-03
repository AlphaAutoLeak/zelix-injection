package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.renderer.*;
import zelix.utils.*;
import net.minecraft.item.*;
import java.util.*;
import zelix.utils.system.*;
import net.minecraft.network.play.client.*;
import net.minecraft.inventory.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.enchantment.*;
import net.minecraft.client.entity.*;

public class AutoArmor extends Hack
{
    public BooleanValue useEnchantments;
    public BooleanValue swapWhileMoving;
    public NumberValue delay;
    private int timer;
    
    public AutoArmor() {
        super("AutoArmor", HackCategory.PLAYER);
        this.useEnchantments = new BooleanValue("Enchantments", Boolean.valueOf(true));
        this.swapWhileMoving = new BooleanValue("SwapWhileMoving", Boolean.valueOf(false));
        this.delay = new NumberValue("Delay", 2.0, 0.0, 20.0);
        this.addValue(this.useEnchantments, this.swapWhileMoving, this.delay);
    }
    
    @Override
    public String getDescription() {
        return "Manages your armor automatically.";
    }
    
    @Override
    public void onEnable() {
        this.timer = 0;
        super.onEnable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.timer > 0) {
            --this.timer;
            return;
        }
        if (Wrapper.INSTANCE.mc().currentScreen instanceof GuiContainer && !(Wrapper.INSTANCE.mc().currentScreen instanceof InventoryEffectRenderer)) {
            return;
        }
        final InventoryPlayer inventory = Wrapper.INSTANCE.inventory();
        if (!this.swapWhileMoving.getValue() && (Wrapper.INSTANCE.player().movementInput.moveForward != 0.0f || Wrapper.INSTANCE.player().movementInput.moveStrafe != 0.0f)) {
            return;
        }
        final int[] bestArmorSlots = new int[4];
        final int[] bestArmorValues = new int[4];
        for (int type = 0; type < 4; ++type) {
            bestArmorSlots[type] = -1;
            final ItemStack stack = inventory.armorItemInSlot(type);
            if (!Utils.isNullOrEmptyStack(stack)) {
                if (stack.getItem() instanceof ItemArmor) {
                    final ItemArmor item = (ItemArmor)stack.getItem();
                    bestArmorValues[type] = this.getArmorValue(item, stack);
                }
            }
        }
        for (int slot = 0; slot < 36; ++slot) {
            final ItemStack stack = inventory.getStackInSlot(slot);
            if (!Utils.isNullOrEmptyStack(stack)) {
                if (stack.getItem() instanceof ItemArmor) {
                    final ItemArmor item = (ItemArmor)stack.getItem();
                    final int armorType = item.armorType.getIndex();
                    final int armorValue = this.getArmorValue(item, stack);
                    if (armorValue > bestArmorValues[armorType]) {
                        bestArmorSlots[armorType] = slot;
                        bestArmorValues[armorType] = armorValue;
                    }
                }
            }
        }
        final ArrayList<Integer> types = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(types);
        for (final int type2 : types) {
            int slot2 = bestArmorSlots[type2];
            if (slot2 == -1) {
                continue;
            }
            final ItemStack oldArmor = inventory.armorItemInSlot(type2);
            if (!Utils.isNullOrEmptyStack(oldArmor) && inventory.getFirstEmptyStack() == -1) {
                continue;
            }
            if (slot2 < 9) {
                slot2 += 36;
            }
            if (!Utils.isNullOrEmptyStack(oldArmor)) {
                Utils.windowClick(0, 8 - type2, 0, ClickType.QUICK_MOVE);
            }
            Utils.windowClick(0, slot2, 0, ClickType.QUICK_MOVE);
            break;
        }
        super.onClientTick(event);
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (side == Connection.Side.OUT && packet instanceof CPacketClickWindow) {
            this.timer = (int)(Object)this.delay.getValue();
        }
        return true;
    }
    
    int getArmorValue(final ItemArmor item, final ItemStack stack) {
        final int armorPoints = item.damageReduceAmount;
        int prtPoints = 0;
        final int armorToughness = (int)item.toughness;
        final int armorType = item.getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);
        if (this.useEnchantments.getValue()) {
            final Enchantment protection = Enchantments.PROTECTION;
            final int prtLvl = EnchantmentHelper.getEnchantmentLevel(protection, stack);
            final EntityPlayerSP player = Wrapper.INSTANCE.player();
            final DamageSource dmgSource = DamageSource.causePlayerDamage((EntityPlayer)player);
            prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);
        }
        return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
    }
}
