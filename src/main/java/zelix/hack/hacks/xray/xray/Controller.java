package zelix.hack.hacks.xray.xray;

import java.util.*;
import net.minecraft.util.math.*;
import zelix.hack.hacks.xray.store.*;
import java.util.concurrent.*;
import zelix.hack.hacks.xray.*;
import net.minecraft.client.resources.*;
import net.minecraft.util.text.*;
import zelix.hack.hacks.xray.utils.*;
import net.minecraft.block.*;
import net.minecraft.init.*;

public class Controller
{
    private static final int[] distanceList;
    public static ArrayList blackList;
    private static Vec3i lastPlayerPos;
    private static BlockStore blockStore;
    private static Future task;
    private static ExecutorService executor;
    private static boolean drawOres;
    
    public static BlockStore getBlockStore() {
        return Controller.blockStore;
    }
    
    public static boolean drawOres() {
        return Controller.drawOres && XRay.mc.world != null && XRay.mc.player != null;
    }
    
    public static void toggleDrawOres() {
        if (!Controller.drawOres) {
            Render.ores.clear();
            Controller.executor = Executors.newSingleThreadExecutor();
            requestBlockFinder(Controller.drawOres = true);
            if (!Configuration.showOverlay) {
                XRay.mc.player.sendMessage((ITextComponent)new TextComponentString(I18n.format("xray.toggle.activated", new Object[0])));
            }
        }
        else {
            if (!Configuration.showOverlay) {
                XRay.mc.player.sendMessage((ITextComponent)new TextComponentString(I18n.format("xray.toggle.deactivated", new Object[0])));
            }
            shutdownExecutor();
        }
    }
    
    public static int getRadius() {
        return Controller.distanceList[Configuration.radius];
    }
    
    public static void incrementCurrentDist() {
        if (Configuration.radius < Controller.distanceList.length - 1) {
            ++Configuration.radius;
        }
        else {
            Configuration.radius = 0;
        }
    }
    
    public static void decrementCurrentDist() {
        if (Configuration.radius > 0) {
            --Configuration.radius;
        }
        else {
            Configuration.radius = Controller.distanceList.length - 1;
        }
    }
    
    private static boolean playerHasMoved() {
        return Controller.lastPlayerPos == null || Controller.lastPlayerPos.getX() != XRay.mc.player.getPosition().getX() || Controller.lastPlayerPos.getZ() != XRay.mc.player.getPosition().getZ();
    }
    
    private static void updatePlayerPosition() {
        Controller.lastPlayerPos = (Vec3i)XRay.mc.player.getPosition();
    }
    
    public static synchronized void requestBlockFinder(final boolean force) {
        if (drawOres() && (Controller.task == null || Controller.task.isDone()) && (force || playerHasMoved())) {
            updatePlayerPosition();
            final WorldRegion region = new WorldRegion(Controller.lastPlayerPos, getRadius());
            Controller.task = Controller.executor.submit(new RenderEnqueue(region));
        }
    }
    
    public static void shutdownExecutor() {
        Controller.drawOres = false;
        try {
            Controller.executor.shutdownNow();
        }
        catch (Throwable t) {}
    }
    
    static {
        distanceList = new int[] { 8, 16, 32, 48, 64, 80, 128, 256 };
        Controller.blackList = new ArrayList<Block>() {
            {
                this.add(Blocks.AIR);
                this.add(Blocks.BEDROCK);
                this.add(Blocks.STONE);
                this.add((Block)Blocks.GRASS);
                this.add(Blocks.DIRT);
            }
        };
        Controller.lastPlayerPos = null;
        Controller.blockStore = new BlockStore();
        Controller.drawOres = false;
    }
}
