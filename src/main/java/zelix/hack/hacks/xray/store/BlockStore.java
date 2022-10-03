package zelix.hack.hacks.xray.store;

import zelix.hack.hacks.xray.reference.block.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.block.state.*;
import java.util.*;

public class BlockStore
{
    static final List<SimpleBlockData> DEFAULT_BLOCKS;
    private HashMap<String, BlockData> store;
    
    public BlockStore() {
        this.store = new HashMap<String, BlockData>();
    }
    
    public void put(final String key, final BlockData data) {
        if (this.store.containsKey(key)) {
            return;
        }
        this.store.put(key, data);
    }
    
    public HashMap<String, BlockData> getStore() {
        return this.store;
    }
    
    public void setStore(final HashMap<String, BlockData> store) {
        this.store = store;
    }
    
    public void toggleDrawing(final String key) {
        if (!this.store.containsKey(key)) {
            return;
        }
        final BlockData data = this.store.get(key);
        data.setDrawing(!data.isDrawing());
    }
    
    public static HashMap<String, BlockData> getFromSimpleBlockList(final List<SimpleBlockData> simpleList) {
        final HashMap<String, BlockData> blockData = new HashMap<String, BlockData>();
        for (final SimpleBlockData e : simpleList) {
            final IBlockState state = Block.getStateById(e.getStateId());
            blockData.put(e.getStateString(), new BlockData(e.getStateString(), e.getName(), e.getStateId(), e.getColor(), new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)), e.isDrawing(), e.getOrder()));
        }
        return blockData;
    }
    
    static {
        DEFAULT_BLOCKS = new ArrayList<SimpleBlockData>() {
            {
                this.add(SimpleBlockData.firstOreInDictionary("oreIron", "Iron Ore", new int[] { 170, 117, 37 }, false));
                this.add(SimpleBlockData.firstOreInDictionary("oreCoal", "Coal Ore", new int[] { 0, 0, 0 }, false));
                this.add(SimpleBlockData.firstOreInDictionary("oreGold", "Gold Ore", new int[] { 255, 255, 0 }, false));
                this.add(SimpleBlockData.firstOreInDictionary("oreRedstone", "Redstone Ore", new int[] { 255, 0, 0 }, false));
                this.add(SimpleBlockData.firstOreInDictionary("oreDiamond", "Diamond Ore", new int[] { 136, 136, 255 }, true));
                this.add(SimpleBlockData.firstOreInDictionary("oreEmerald", "Emerald Ore", new int[] { 0, 136, 10 }, true));
                this.add(SimpleBlockData.firstOreInDictionary("oreQuartz", "Nether Quart", new int[] { 30, 74, 0 }, false));
                this.add(SimpleBlockData.firstOreInDictionary("oreLapis", "Lapis", new int[] { 0, 0, 255 }, false));
            }
        };
    }
}
