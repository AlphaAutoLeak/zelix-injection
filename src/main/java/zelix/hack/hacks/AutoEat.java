package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.settings.*;
import net.minecraft.item.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.block.*;
import zelix.utils.*;

public class AutoEat extends Hack
{
    private int oldSlot;
    private int bestSlot;
    
    public AutoEat() {
        super("AutoEat", HackCategory.PLAYER);
    }
    
    @Override
    public String getDescription() {
        return "Eat food automatically.";
    }
    
    @Override
    public void onEnable() {
        this.oldSlot = -1;
        this.bestSlot = -1;
        super.onEnable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.oldSlot == -1) {
            if (!this.canEat()) {
                return;
            }
            float bestSaturation = 0.0f;
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = Wrapper.INSTANCE.inventory().getStackInSlot(i);
                if (this.isFood(stack)) {
                    final ItemFood food = (ItemFood)stack.getItem();
                    final float saturation = food.getSaturationModifier(stack);
                    if (saturation > bestSaturation) {
                        bestSaturation = saturation;
                        this.bestSlot = i;
                    }
                }
            }
            if (this.bestSlot != -1) {
                this.oldSlot = Wrapper.INSTANCE.inventory().currentItem;
            }
        }
        else {
            if (!this.canEat()) {
                this.stop();
                return;
            }
            if (!this.isFood(Wrapper.INSTANCE.inventory().getStackInSlot(this.bestSlot))) {
                this.stop();
                return;
            }
            Wrapper.INSTANCE.inventory().currentItem = this.bestSlot;
            KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(), true);
        }
        super.onClientTick(event);
    }
    
    boolean canEat() {
        if (!Wrapper.INSTANCE.player().canEat(false)) {
            return false;
        }
        if (Wrapper.INSTANCE.mc().objectMouseOver != null) {
            final Entity entity = Wrapper.INSTANCE.mc().objectMouseOver.entityHit;
            if (entity instanceof EntityVillager || entity instanceof EntityTameable) {
                return false;
            }
            final BlockPos pos = Wrapper.INSTANCE.mc().objectMouseOver.getBlockPos();
            if (pos != null) {
                final Block block = BlockUtils.getBlock(pos);
                if (block instanceof BlockContainer || block instanceof BlockWorkbench) {
                    return false;
                }
            }
        }
        return true;
    }
    
    boolean isFood(final ItemStack stack) {
        return !Utils.isNullOrEmptyStack(stack) && stack.getItem() instanceof ItemFood;
    }
    
    void stop() {
        KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(), false);
        Wrapper.INSTANCE.inventory().currentItem = this.oldSlot;
        this.oldSlot = -1;
    }
}
