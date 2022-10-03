package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import zelix.utils.system.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.inventory.*;
import zelix.utils.*;

public class AutoTotem extends Hack
{
    public BooleanValue swapWhileMoving;
    public NumberValue delay;
    private int timer;
    
    public AutoTotem() {
        super("AutoTotem", HackCategory.COMBAT);
        this.swapWhileMoving = new BooleanValue("SwapWhileMoving", Boolean.valueOf(false));
        this.delay = new NumberValue("SwipeDelay", 2.0, 0.0, 20.0);
        this.addValue(this.swapWhileMoving, this.delay);
    }
    
    @Override
    public String getDescription() {
        return "Automatically takes a totem in offhand.";
    }
    
    @Override
    public void onEnable() {
        this.timer = 0;
        super.onEnable();
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (side == Connection.Side.OUT && packet instanceof CPacketClickWindow) {
            this.timer = (int)(Object)this.delay.getValue();
        }
        return true;
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.timer > 0) {
            --this.timer;
            return;
        }
        if (!this.swapWhileMoving.getValue() && (Wrapper.INSTANCE.player().movementInput.moveForward != 0.0f || Wrapper.INSTANCE.player().movementInput.moveStrafe != 0.0f)) {
            return;
        }
        final ItemStack offhand = Wrapper.INSTANCE.player().getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
        final NonNullList<ItemStack> inv = (NonNullList<ItemStack>)Wrapper.INSTANCE.inventory().mainInventory;
        for (int inventoryIndex = 0; inventoryIndex < inv.size(); ++inventoryIndex) {
            if (inv.get(inventoryIndex) != ItemStack.EMPTY && (offhand == null || offhand.getItem() != Items.TOTEM_OF_UNDYING) && ((ItemStack)inv.get(inventoryIndex)).getItem() == Items.TOTEM_OF_UNDYING) {
                this.replace(inventoryIndex);
                break;
            }
        }
        super.onClientTick(event);
    }
    
    public void replace(final int inventoryIndex) {
        if (Wrapper.INSTANCE.player().openContainer instanceof ContainerPlayer) {
            Utils.windowClick(0, (inventoryIndex < 9) ? (inventoryIndex + 36) : inventoryIndex, 0, ClickType.PICKUP);
            Utils.windowClick(0, 45, 0, ClickType.PICKUP);
            Utils.windowClick(0, (inventoryIndex < 9) ? (inventoryIndex + 36) : inventoryIndex, 0, ClickType.PICKUP);
        }
    }
}
