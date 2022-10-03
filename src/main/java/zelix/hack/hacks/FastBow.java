package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.hack.hacks.hud.*;
import net.minecraft.util.*;
import org.lwjgl.input.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;

public class FastBow extends Hack
{
    public FastBow() {
        super("FastBow", HackCategory.COMBAT);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (HUD.mc.player == null || HUD.mc.player.getHeldItem(EnumHand.MAIN_HAND) == null || HUD.mc.player.getHeldItem(EnumHand.OFF_HAND) == null) {
            return;
        }
        if (Mouse.isButtonDown(1) && (HUD.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow || HUD.mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemBow) && HUD.mc.player.onGround) {
            HUD.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem().onPlayerStoppedUsing(HUD.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem().getDefaultInstance(), (World)HUD.mc.world, (EntityLivingBase)HUD.mc.player, 10);
        }
    }
    
    protected ItemStack findAmmo(final EntityPlayer player) {
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            final ItemStack itemstack = player.inventory.getStackInSlot(i);
            if (this.isArrow(itemstack)) {
                return itemstack;
            }
        }
        return ItemStack.EMPTY;
    }
    
    protected boolean isArrow(final ItemStack stack) {
        return stack.getItem() instanceof ItemArrow;
    }
}
