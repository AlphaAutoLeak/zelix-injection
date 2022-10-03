package zelix.hack.hacks.xray.reference.block;

import zelix.hack.hacks.xray.utils.*;
import net.minecraftforge.oredict.*;
import net.minecraft.item.*;
import net.minecraft.block.*;
import net.minecraft.util.*;

public class SimpleBlockData
{
    private String name;
    private String stateString;
    private int stateId;
    private int order;
    private OutlineColor color;
    private boolean drawing;
    
    public SimpleBlockData(final String name, final String stateString, final int stateId, final OutlineColor color, final boolean drawing, final int order) {
        this.name = name;
        this.stateString = stateString;
        this.stateId = stateId;
        this.color = color;
        this.drawing = drawing;
        this.order = order;
    }
    
    public static SimpleBlockData firstOreInDictionary(final String name, final String entryName, final int[] color, final boolean draw) {
        final NonNullList<ItemStack> ores = (NonNullList<ItemStack>)OreDictionary.getOres(name);
        if (ores.isEmpty() || ((ItemStack)ores.get(0)).isEmpty()) {
            return null;
        }
        final ItemStack stack = (ItemStack)ores.get(0);
        return new SimpleBlockData(entryName, Block.getBlockFromItem(stack.getItem()).getDefaultState().toString(), Block.getStateId(Block.getBlockFromItem(stack.getItem()).getDefaultState()), new OutlineColor(color[0], color[1], color[2]), draw, 0);
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getStateString() {
        return this.stateString;
    }
    
    public OutlineColor getColor() {
        return this.color;
    }
    
    public boolean isDrawing() {
        return this.drawing;
    }
    
    public int getStateId() {
        return this.stateId;
    }
    
    public int getOrder() {
        return this.order;
    }
    
    public void setOrder(final int order) {
        this.order = order;
    }
}
