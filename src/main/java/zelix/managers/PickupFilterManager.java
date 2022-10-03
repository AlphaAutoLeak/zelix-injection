package zelix.managers;

import net.minecraft.item.*;
import zelix.utils.hooks.visual.*;
import java.util.*;

public class PickupFilterManager
{
    public static LinkedList<Integer> items;
    
    public static void addItem(final int id) {
        try {
            if (Item.getItemById(id) == null) {
                ChatUtils.error("Item is null.");
                return;
            }
            for (final int itemId : PickupFilterManager.items) {
                if (itemId == id) {
                    ChatUtils.error("Item already added.");
                    return;
                }
            }
            PickupFilterManager.items.add(id);
            FileManager.savePickupFilter();
            ChatUtils.message(String.format("��7ID: ��3%s ��7NAME: ��3%s ��7- ADDED.", id, Item.getItemById(id).getUnlocalizedName()));
        }
        catch (Exception ex) {
            ChatUtils.error(ex.getMessage());
        }
    }
    
    public static void removeItem(final int id) {
        for (final int itemId : PickupFilterManager.items) {
            if (itemId == id) {
                PickupFilterManager.items.remove((Object)id);
                ChatUtils.message(String.format("��7ID: ��3%s ��7- REMOVED.", id));
                FileManager.savePickupFilter();
                return;
            }
        }
        ChatUtils.error("Item not found.");
    }
    
    public static void clear() {
        if (PickupFilterManager.items.isEmpty()) {
            return;
        }
        PickupFilterManager.items.clear();
        FileManager.savePickupFilter();
        ChatUtils.message("��dPickupFilter ��7list clear.");
    }
    
    static {
        PickupFilterManager.items = new LinkedList<Integer>();
    }
}
