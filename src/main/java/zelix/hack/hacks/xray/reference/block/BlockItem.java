package zelix.hack.hacks.xray.reference.block;

import net.minecraft.item.*;

public class BlockItem
{
    private int stateId;
    private ItemStack item;
    
    public BlockItem(final int stateId, final ItemStack item) {
        this.stateId = stateId;
        this.item = item;
    }
    
    public int getStateId() {
        return this.stateId;
    }
    
    public ItemStack getItemStack() {
        return this.item;
    }
}
