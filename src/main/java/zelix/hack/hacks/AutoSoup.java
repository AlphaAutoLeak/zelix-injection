package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.settings.*;
import net.minecraft.item.*;

public class AutoSoup extends Hack
{
    NumberValue health;
    private int oldSlot;
    
    public AutoSoup() {
        super("AutoSoup", HackCategory.COMBAT);
        this.oldSlot = -1;
        this.health = new NumberValue("Health", 5.0, 2.0, 9.5);
        this.addValue(this.health);
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        Utils.nullCheck();
        for (int i = 0; i < 36; ++i) {
            final ItemStack stack = Wrapper.INSTANCE.player().inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() == Items.BOWL) {
                if (i != 9) {
                    final ItemStack emptyBowlStack = Wrapper.INSTANCE.player().inventory.getStackInSlot(9);
                    final boolean swap = !emptyBowlStack.isEmpty() && emptyBowlStack.getItem() != Items.BOWL;
                    Wrapper.INSTANCE.controller().windowClick(0, (i < 9) ? (i + 36) : i, 0, ClickType.PICKUP, (EntityPlayer)Wrapper.INSTANCE.player());
                    Wrapper.INSTANCE.controller().windowClick(0, 9, 0, ClickType.PICKUP, (EntityPlayer)Wrapper.INSTANCE.player());
                    if (swap) {
                        Wrapper.INSTANCE.controller().windowClick(0, (i < 9) ? (i + 36) : i, 0, ClickType.PICKUP, (EntityPlayer)Wrapper.INSTANCE.player());
                    }
                }
            }
        }
        final int soupInHotbar = this.findSoup(0, 9);
        if (soupInHotbar == -1) {
            this.stopIfEating();
            final int soupInInventory = this.findSoup(9, 36);
            if (soupInInventory != -1) {
                Wrapper.INSTANCE.controller().windowClick(0, soupInInventory, 0, ClickType.QUICK_MOVE, (EntityPlayer)Wrapper.INSTANCE.player());
            }
            super.onPlayerTick(event);
            return;
        }
        if (!this.shouldEatSoup()) {
            this.stopIfEating();
            return;
        }
        if (this.oldSlot == -1) {
            this.oldSlot = Wrapper.INSTANCE.player().inventory.currentItem;
        }
        Wrapper.INSTANCE.player().inventory.currentItem = soupInHotbar;
        KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(), true);
    }
    
    private int findSoup(final int startSlot, final int endSlot) {
        for (int i = startSlot; i < endSlot; ++i) {
            final ItemStack stack = Wrapper.INSTANCE.player().inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemSoup) {
                return i;
            }
        }
        return -1;
    }
    
    private boolean shouldEatSoup() {
        return Wrapper.INSTANCE.player().getHealth() <= (float)(Object)this.health.getValue() * 2.0f;
    }
    
    private void stopIfEating() {
        if (this.oldSlot == -1) {
            return;
        }
        KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(), false);
        Wrapper.INSTANCE.player().inventory.currentItem = this.oldSlot;
        this.oldSlot = -1;
    }
}
