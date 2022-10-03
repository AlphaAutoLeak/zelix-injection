package zelix.hack.hacks.HytUtils;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.item.*;
import zelix.hack.hacks.*;
import net.minecraft.client.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;

public class AutoPlatform extends Hack
{
    NumberValue criticalSpeed;
    
    public AutoPlatform() {
        super("AutoPlatform", HackCategory.HYT_UTILS);
        this.criticalSpeed = new NumberValue("CriticalSpeed", -1.0, -2.5, -0.5);
        this.addValue(this.criticalSpeed);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        if (player.motionY < this.criticalSpeed.getValue() && this.isBlockBelowAllVoid(Wrapper.INSTANCE) && this.getItemIndexInHotBar(new ItemStack(Items.BLAZE_ROD), player) != -1) {
            final int prevIndex = player.inventory.currentItem;
            player.inventory.currentItem = this.getItemIndexInHotBar(new ItemStack(Items.BLAZE_ROD), player);
            AutoClicker.rightClickMouse();
            player.inventory.currentItem = prevIndex;
        }
        super.onClientTick(event);
    }
    
    private int getItemIndexInHotBar(final ItemStack itemStack, final EntityPlayerSP player) {
        for (int i = 0; i < 9; ++i) {
            if (player.inventory.getStackInSlot(i).isItemEqual(itemStack)) {
                return i;
            }
        }
        return -1;
    }
    
    private boolean isBlockBelowAllVoid(final Wrapper wrapper) {
        for (int i = (int)wrapper.player().posY; i > 0; --i) {
            if (wrapper.world().getBlockState(new BlockPos(wrapper.player().posX, (double)i, wrapper.player().posZ)).getBlock() != Blocks.AIR) {
                return false;
            }
        }
        return true;
    }
}
