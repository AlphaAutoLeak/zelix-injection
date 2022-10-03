package zelix.hack.hacks.xray.reference.block;

import zelix.hack.hacks.xray.utils.*;
import net.minecraft.item.*;

public class BlockData
{
    private String entryKey;
    private String entryName;
    private int stateId;
    private OutlineColor color;
    private ItemStack itemStack;
    private boolean drawing;
    private int order;
    
    public BlockData(final String entryKey, final String entryName, final int stateId, final OutlineColor color, final ItemStack itemStack, final boolean drawing, final int order) {
        this.entryKey = entryKey;
        this.entryName = entryName;
        this.stateId = stateId;
        this.color = color;
        this.itemStack = itemStack;
        this.drawing = drawing;
        this.order = order;
    }
    
    public String getEntryKey() {
        return this.entryKey;
    }
    
    public String getEntryName() {
        return this.entryName;
    }
    
    public int getStateId() {
        return this.stateId;
    }
    
    public OutlineColor getColor() {
        return this.color;
    }
    
    public ItemStack getItemStack() {
        return this.itemStack;
    }
    
    public boolean isDrawing() {
        return this.drawing;
    }
    
    public void setDrawing(final boolean drawing) {
        this.drawing = drawing;
    }
    
    public void setColor(final OutlineColor color) {
        this.color = color;
    }
    
    public int getOrder() {
        return this.order;
    }
}
