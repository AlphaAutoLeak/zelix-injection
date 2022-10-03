package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.event.entity.player.*;
import zelix.managers.*;
import net.minecraft.item.*;
import java.util.*;

public class PickupFilter extends Hack
{
    public BooleanValue all;
    
    public PickupFilter() {
        super("PickupFilter", HackCategory.ANOTHER);
        this.all = new BooleanValue("IgnoreAll", Boolean.valueOf(false));
        this.addValue(this.all);
    }
    
    @Override
    public String getDescription() {
        return "Filters item picking.";
    }
    
    @Override
    public void onItemPickup(final EntityItemPickupEvent event) {
        if (this.all.getValue()) {
            event.setCanceled(true);
            return;
        }
        for (final int itemId : PickupFilterManager.items) {
            final Item item = Item.getItemById(itemId);
            if (item == null) {
                continue;
            }
            if (event.getItem().getItem().getItem() != item) {
                continue;
            }
            event.setCanceled(true);
        }
        super.onItemPickup(event);
    }
}
