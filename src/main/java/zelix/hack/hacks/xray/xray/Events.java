package zelix.hack.hacks.xray.xray;

import net.minecraft.util.math.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.event.world.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.hack.hacks.xray.etc.*;
import net.minecraft.client.*;
import zelix.utils.hooks.visual.*;
import java.util.*;
import net.minecraftforge.client.event.*;
import zelix.hack.hacks.xray.*;

public class Events
{
    public BlockPos old;
    public int movedblocks;
    
    @SubscribeEvent
    public void pickupItem(final BlockEvent.BreakEvent event) {
        RenderEnqueue.checkBlock(event.getPos(), event.getState(), false);
    }
    
    @SubscribeEvent
    public void placeItem(final BlockEvent.PlaceEvent event) {
        RenderEnqueue.checkBlock(event.getPos(), event.getState(), true);
    }
    
    @SubscribeEvent
    public void chunkLoad(final ChunkEvent.Load event) {
        Controller.requestBlockFinder(true);
    }
    
    @SubscribeEvent
    public void tickEnd(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Controller.requestBlockFinder(false);
        }
        final List<RefreshingJob> nl = new ArrayList<RefreshingJob>();
        final List<RefreshingJob> list = new ArrayList<>();
        AntiAntiXray.jobs.forEach(refreshingJob -> {
            if (!refreshingJob.refresher.done) {
                list.add(refreshingJob);
            }
            return;
        });
        AntiAntiXray.jobs = nl;
        if (Configuration.auto) {
            try {
                assert Minecraft.getMinecraft().player != null;
                final BlockPos pos = Minecraft.getMinecraft().player.getPosition();
                if (pos != this.old) {
                    ++this.movedblocks;
                    if (this.movedblocks > Configuration.movethreshhold && AntiAntiXray.jobs.size() == 0) {
                        AntiAntiXray.revealNewBlocks(Configuration.radius_x, Configuration.radius_y, Configuration.radius_z, Configuration.delay);
                        ChatUtils.message("Scanning new pos: " + pos);
                        this.movedblocks = 0;
                    }
                }
                this.old = pos;
            }
            catch (NullPointerException e) {
                ChatUtils.error("Null Error");
            }
        }
    }
    
    @SubscribeEvent
    public void onWorldRenderLast(final RenderWorldLastEvent event) {
        if (Controller.drawOres()) {
            final float f = event.getPartialTicks();
            Render.drawOres((float)XRay.mc.player.prevPosX + ((float)XRay.mc.player.posX - (float)XRay.mc.player.prevPosX) * f, (float)XRay.mc.player.prevPosY + ((float)XRay.mc.player.posY - (float)XRay.mc.player.prevPosY) * f, (float)XRay.mc.player.prevPosZ + ((float)XRay.mc.player.posZ - (float)XRay.mc.player.prevPosZ) * f);
        }
    }
}
