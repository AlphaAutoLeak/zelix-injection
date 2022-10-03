package zelix.hack.hacks;

import net.minecraftforge.common.config.*;
import zelix.hack.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;
import net.minecraft.client.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import zelix.utils.*;
import net.minecraft.client.multiplayer.*;
import org.apache.logging.log4j.*;
import zelix.utils.hooks.visual.*;
import java.util.*;
import java.lang.reflect.*;
import net.minecraftforge.fml.common.*;

public class AntiItemLag extends Hack
{
    public static final Logger logger;
    public int INT_ITEM_LAG_MAX;
    public int INT_ITEM_LAG_DOWN_TO;
    public Configuration configuration;
    public boolean antiItemLag;
    
    public AntiItemLag() {
        super("AntiItemLag", HackCategory.ANOTHER);
        this.INT_ITEM_LAG_MAX = 128;
        this.INT_ITEM_LAG_DOWN_TO = 64;
        this.antiItemLag = true;
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        this.updateAntiItemLag();
        super.onPlayerTick(event);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        this.updateAntiItemLag();
        super.onClientTick(event);
    }
    
    private void updateAntiItemLag() {
        if (!this.antiItemLag) {
            return;
        }
        final HashMap<Item, List<List<Entity>>> map = new HashMap<Item, List<List<Entity>>>();
        final WorldClient worldClient = Minecraft.getMinecraft().world;
        if (worldClient == null) {
            return;
        }
        final List<Entity> list = (List<Entity>)Minecraft.getMinecraft().world.getLoadedEntityList();
        if (list == null) {
            return;
        }
        for (int j = 0; j < list.size(); ++j) {
            final Entity entity = list.get(j);
            final Item item = (entity instanceof EntityItem) ? ((EntityItem)entity).getItem().getItem() : null;
            if (item != null) {
                List<List<Entity>> list2 = map.get(item);
                if (list2 == null) {
                    list2 = new ArrayList<List<Entity>>();
                    map.put(item, list2);
                }
                List<Entity> targetList = null;
                for (int i = 0; i < list2.size(); ++i) {
                    targetList = list2.get(i);
                    final Entity entity2 = targetList.get(0);
                    if (entity.getDistanceSq(entity2) <= 1.0) {
                        break;
                    }
                    targetList = null;
                }
                if (targetList == null) {
                    targetList = new ArrayList<Entity>();
                    list2.add(targetList);
                }
                targetList.add(entity);
            }
        }
        int removeCount = 0;
        for (final Map.Entry<Item, List<List<Entity>>> entry : map.entrySet()) {
            final List<List<Entity>> list2 = entry.getValue();
            List<Entity> targetList = null;
            for (int i = 0; i < list2.size(); ++i) {
                targetList = list2.get(i);
                this.INT_ITEM_LAG_MAX = this.configuration.get("general", "INT_ITEM_LAG_MAX", this.INT_ITEM_LAG_MAX).getInt();
                this.INT_ITEM_LAG_DOWN_TO = this.configuration.get("general", "INT_ITEM_LAG_DOWN_TO", this.INT_ITEM_LAG_DOWN_TO).getInt();
                if (targetList.size() > this.INT_ITEM_LAG_MAX) {
                    for (int k = this.INT_ITEM_LAG_DOWN_TO - 1; k < targetList.size(); ++k) {
                        ++removeCount;
                        final Entity entityIn = targetList.get(k);
                        if (entityIn instanceof EntityPlayer) {
                            worldClient.playerEntities.remove(entityIn);
                            worldClient.updateAllPlayersSleepingFlag();
                        }
                        final int x = entityIn.chunkCoordX;
                        final int z = entityIn.chunkCoordZ;
                        final Method method = ReflectionHelper.findMethod((Class<? super WorldClient>)World.class, worldClient, new String[] { "isChunkLoaded", "isChunkLoaded" }, Integer.TYPE, Integer.TYPE, Boolean.TYPE);
                        method.setAccessible(true);
                        boolean isLoad = false;
                        try {
                            isLoad = (boolean)method.invoke(worldClient, new Integer(x), new Integer(z), new Boolean(true));
                        }
                        catch (IllegalAccessException e) {
                            AntiItemLag.logger.log(Level.INFO, e.getMessage());
                        }
                        catch (IllegalArgumentException e2) {
                            AntiItemLag.logger.log(Level.INFO, e2.getMessage());
                        }
                        catch (InvocationTargetException e3) {
                            AntiItemLag.logger.log(Level.INFO, e3.getMessage());
                        }
                        catch (Exception e4) {
                            AntiItemLag.logger.log(Level.INFO, e4.getMessage());
                        }
                        if (entityIn.addedToChunk && isLoad) {
                            worldClient.getChunkFromChunkCoords(x, z).removeEntity(entityIn);
                        }
                        worldClient.loadedEntityList.remove(entityIn);
                    }
                }
            }
        }
        if (removeCount > 0) {
            ChatUtils.message("antiItemLag: " + removeCount);
        }
    }
    
    static {
        logger = FMLCommonHandler.instance().getFMLLogger();
    }
}
