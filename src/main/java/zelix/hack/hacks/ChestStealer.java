package zelix.hack.hacks;

import net.minecraft.network.play.server.*;
import zelix.hack.*;
import zelix.value.*;
import zelix.utils.system.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.entity.*;
import net.minecraft.inventory.*;

public class ChestStealer extends Hack
{
    public NumberValue delay;
    public SPacketWindowItems packet;
    public int ticks;
    
    public ChestStealer() {
        super("ChestStealer", HackCategory.PLAYER);
        this.delay = new NumberValue("Delay", 4.0, 0.0, 20.0);
        this.addValue(this.delay);
        this.ticks = 0;
    }
    
    @Override
    public String getDescription() {
        return "Steals all stuff from chest.";
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (side == Connection.Side.IN && packet instanceof SPacketWindowItems) {
            this.packet = (SPacketWindowItems)packet;
        }
        return true;
    }
    
    boolean isContainerEmpty(final Container container) {
        boolean temp = true;
        for (int i = 0, slotAmount = (container.inventorySlots.size() == 90) ? 54 : 35; i < slotAmount; ++i) {
            if (container.getSlot(i).getHasStack()) {
                temp = false;
            }
        }
        return temp;
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        if (!Wrapper.INSTANCE.mc().inGameHasFocus && this.packet != null && player.openContainer.windowId == this.packet.getWindowId() && Wrapper.INSTANCE.mc().currentScreen instanceof GuiChest) {
            if (!this.isContainerEmpty(player.openContainer)) {
                for (int i = 0; i < player.openContainer.inventorySlots.size() - 36; ++i) {
                    final Slot slot = player.openContainer.getSlot(i);
                    if (slot.getHasStack() && slot.getStack() != null && this.ticks >= (int)(Object)this.delay.getValue()) {
                        Wrapper.INSTANCE.controller().windowClick(player.openContainer.windowId, i, 1, ClickType.QUICK_MOVE, (EntityPlayer)player);
                        this.ticks = 0;
                    }
                }
                ++this.ticks;
            }
            else {
                player.closeScreen();
                this.packet = null;
            }
        }
    }
}
