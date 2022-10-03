package zelix.hack.hacks.xray.store;

import zelix.hack.hacks.xray.reference.block.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import zelix.hack.hacks.xray.xray.*;
import java.util.*;

public class GameBlockStore
{
    private ArrayList<BlockItem> store;
    
    public GameBlockStore() {
        this.store = new ArrayList<BlockItem>();
    }
    
    public void populate() {
        if (this.store.size() != 0) {
            return;
        }
        for (final Item item : ForgeRegistries.ITEMS) {
            if (!(item instanceof ItemBlock)) {
                continue;
            }
            final Block block = Block.getBlockFromItem(item);
            if (item == Items.AIR) {
                continue;
            }
            if (block == Blocks.AIR) {
                continue;
            }
            if (item.getHasSubtypes() && item.getCreativeTab() != null) {
                final NonNullList<ItemStack> subItems = NonNullList.create();
                item.getSubItems(item.getCreativeTab(), (NonNullList)subItems);
                for (final ItemStack subItem : subItems) {
                    if (!subItem.equals(ItemStack.EMPTY) && subItem.getItem() != Items.AIR) {
                        if (Controller.blackList.contains(block)) {
                            continue;
                        }
                        this.store.add(new BlockItem(Block.getStateId(Block.getBlockFromItem(subItem.getItem()).getBlockState().getBaseState()), subItem));
                    }
                }
            }
            else {
                if (Controller.blackList.contains(block)) {
                    continue;
                }
                this.store.add(new BlockItem(Block.getStateId(block.getBlockState().getBaseState()), new ItemStack(item)));
            }
        }
    }
    
    public void repopulate() {
        this.store.clear();
        this.populate();
    }
    
    public ArrayList<BlockItem> getStore() {
        return this.store;
    }
}
