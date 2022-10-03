package zelix.hack.hacks;

import java.util.*;
import zelix.hack.*;
import net.minecraftforge.client.event.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import zelix.utils.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import zelix.utils.hooks.visual.*;

public class ChestESP extends Hack
{
    private int maxChests;
    public boolean shouldInform;
    private TileEntityChest openChest;
    private ArrayDeque<TileEntityChest> emptyChests;
    private ArrayDeque<TileEntityChest> nonEmptyChests;
    private String[] chestClasses;
    
    public ChestESP() {
        super("ChestESP", HackCategory.VISUAL);
        this.maxChests = 1000;
        this.shouldInform = true;
        this.emptyChests = new ArrayDeque<TileEntityChest>();
        this.nonEmptyChests = new ArrayDeque<TileEntityChest>();
        this.chestClasses = new String[] { "TileEntityIronChest", "TileEntityGoldChest", "TileEntityDiamondChest", "TileEntityCopperChest", "TileEntitySilverChest", "TileEntityCrystalChest", "TileEntityObsidianChest", "TileEntityDirtChest" };
    }
    
    @Override
    public String getDescription() {
        return "Allows you to see all of the chests around you.";
    }
    
    @Override
    public void onEnable() {
        this.shouldInform = true;
        this.emptyChests.clear();
        this.nonEmptyChests.clear();
        super.onEnable();
    }
    
    @Override
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        int chests = 0;
        for (int i = 0; i < Wrapper.INSTANCE.world().loadedTileEntityList.size(); ++i) {
            final TileEntity tileEntity = Wrapper.INSTANCE.world().loadedTileEntityList.get(i);
            if (chests >= this.maxChests) {
                break;
            }
            if (tileEntity instanceof TileEntityChest) {
                ++chests;
                final TileEntityChest chest = (TileEntityChest)tileEntity;
                final boolean trapped = chest.getChestType() == BlockChest.Type.TRAP;
                if (this.emptyChests.contains(tileEntity)) {
                    RenderUtils.drawBlockESP(chest.getPos(), 0.25f, 0.25f, 0.25f);
                }
                else if (this.nonEmptyChests.contains(tileEntity)) {
                    if (trapped) {
                        RenderUtils.drawBlockESP(chest.getPos(), 0.0f, 1.0f, 0.0f);
                    }
                    else {
                        RenderUtils.drawBlockESP(chest.getPos(), 1.0f, 0.0f, 0.0f);
                    }
                }
                else if (trapped) {
                    RenderUtils.drawBlockESP(chest.getPos(), 0.0f, 1.0f, 0.0f);
                }
                else {
                    RenderUtils.drawBlockESP(chest.getPos(), 1.0f, 0.0f, 0.0f);
                }
                if (trapped) {
                    RenderUtils.drawBlockESP(chest.getPos(), 0.0f, 1.0f, 0.0f);
                }
                else {
                    RenderUtils.drawBlockESP(chest.getPos(), 1.0f, 0.0f, 0.0f);
                }
            }
            else if (tileEntity instanceof TileEntityEnderChest) {
                ++chests;
                RenderUtils.drawBlockESP(((TileEntityEnderChest)tileEntity).getPos(), 1.0f, 0.0f, 1.0f);
            }
            else {
                try {
                    for (final String chestClass : this.chestClasses) {
                        final Class clazz = Class.forName("cpw.mods.ironchest.common.tileentity.chest." + chestClass);
                        if (clazz != null && clazz.isInstance(tileEntity)) {
                            RenderUtils.drawBlockESP(tileEntity.getPos(), 0.7f, 0.7f, 0.7f);
                        }
                    }
                }
                catch (ClassNotFoundException ex) {}
            }
        }
        for (int i = 0; i < Utils.getEntityList().size(); ++i) {
            final Entity entity = Utils.getEntityList().get(i);
            if (chests >= this.maxChests) {
                break;
            }
            if (entity instanceof EntityMinecartChest) {
                ++chests;
                RenderUtils.drawBlockESP(((EntityMinecartChest)entity).getPosition(), 1.0f, 1.0f, 1.0f);
            }
        }
        if (chests >= this.maxChests && this.shouldInform) {
            ChatUtils.warning("To prevent lag, it will only show the first " + this.maxChests + " chests.");
            this.shouldInform = false;
        }
        else if (chests < this.maxChests) {
            this.shouldInform = true;
        }
        super.onRenderWorldLast(event);
    }
}
