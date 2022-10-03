package zelix.hack.hacks.xray.xray;

import zelix.hack.hacks.xray.utils.*;
import zelix.hack.hacks.xray.*;
import net.minecraft.client.*;
import zelix.hack.hacks.xray.reference.block.*;
import net.minecraft.world.*;
import java.util.*;
import net.minecraft.block.state.*;
import net.minecraft.util.math.*;
import net.minecraft.world.chunk.*;
import net.minecraft.world.chunk.storage.*;

public class RenderEnqueue implements Runnable
{
    private final WorldRegion box;
    
    public RenderEnqueue(final WorldRegion region) {
        this.box = region;
    }
    
    @Override
    public void run() {
        if (!Configuration.freeze) {
            this.blockFinder2();
        }
    }
    
    private void blockFinder2() {
        final HashMap<String, BlockData> blocks = Controller.getBlockStore().getStore();
        if (blocks.isEmpty()) {
            if (!Render.ores.isEmpty()) {
                Render.ores.clear();
            }
            return;
        }
        final World world = (World)XRay.mc.world;
        final List<BlockInfo> renderQueue = new ArrayList<BlockInfo>();
        final BlockPos pos = XRay.mc.player.getPosition();
        final int radX = Configuration.radius_x;
        final int radY = Configuration.radius_y;
        final int radZ = Configuration.radius_z;
        for (int cx = -radX; cx <= radX; ++cx) {
            for (int cy = -radY; cy <= radY; ++cy) {
                for (int cz = -radZ; cz <= radZ; ++cz) {
                    final BlockPos currblock = new BlockPos(pos.getX() + cx, pos.getY() + cy, pos.getZ() + cz);
                    final IBlockState currentState = Minecraft.getMinecraft().player.world.getBlockState(currblock);
                    if (!Controller.blackList.contains(currentState.getBlock())) {
                        final IBlockState defaultState = currentState.getBlock().getDefaultState();
                        final boolean defaultExists = blocks.containsKey(defaultState.toString());
                        final boolean currentExists = blocks.containsKey(currentState.toString());
                        if (defaultExists || currentExists) {
                            final BlockData blockData = blocks.get(currentExists ? currentState.toString() : defaultState.toString());
                            if (blockData != null) {
                                if (blockData.isDrawing()) {
                                    final double alpha = Configuration.shouldFade ? Math.max(0.0, (Controller.getRadius() - XRay.mc.player.getDistance((double)currblock.getX(), (double)currblock.getY(), (double)currblock.getZ())) / Controller.getRadius() * 255.0) : 255.0;
                                    renderQueue.add(new BlockInfo(currblock.getX(), currblock.getY(), currblock.getZ(), blockData.getColor().getColor(), alpha));
                                }
                            }
                        }
                    }
                }
            }
        }
        renderQueue.sort((t, t1) -> Double.compare((double)t1.distanceSq((Vec3i)((Object)((Object)((Object)pos)))), (double)t.distanceSq((Vec3i)((Object)((Object)((Object)pos))))));
        Render.ores.clear();
        Render.ores.addAll(renderQueue);
    }
    
    private void blockFinder() {
        final HashMap<String, BlockData> blocks = Controller.getBlockStore().getStore();
        if (blocks.isEmpty()) {
            if (!Render.ores.isEmpty()) {
                Render.ores.clear();
            }
            return;
        }
        final World world = (World)XRay.mc.world;
        final List<BlockInfo> renderQueue = new ArrayList<BlockInfo>();
        for (int chunkX = this.box.minChunkX; chunkX <= this.box.maxChunkX; ++chunkX) {
            final int x = chunkX << 4;
            final int lowBoundX = (x < this.box.minX) ? (this.box.minX - x) : 0;
            final int highBoundX = (x + 15 > this.box.maxX) ? (this.box.maxX - x) : 15;
            for (int chunkZ = this.box.minChunkZ; chunkZ <= this.box.maxChunkZ; ++chunkZ) {
                final Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
                if (chunk.isLoaded()) {
                    final ExtendedBlockStorage[] extendsList = chunk.getBlockStorageArray();
                    final int z = chunkZ << 4;
                    final int lowBoundZ = (z < this.box.minZ) ? (this.box.minZ - z) : 0;
                    final int highBoundZ = (z + 15 > this.box.maxZ) ? (this.box.maxZ - z) : 15;
                    for (int curExtend = this.box.minChunkY; curExtend <= this.box.maxChunkY; ++curExtend) {
                        final ExtendedBlockStorage ebs = extendsList[curExtend];
                        if (ebs != null) {
                            final int y = curExtend << 4;
                            final int lowBoundY = (y < this.box.minY) ? (this.box.minY - y) : 0;
                            final int highBoundY = (y + 15 > this.box.maxY) ? (this.box.maxY - y) : 15;
                            for (int i = lowBoundX; i <= highBoundX; ++i) {
                                for (int j = lowBoundY; j <= highBoundY; ++j) {
                                    for (int k = lowBoundZ; k <= highBoundZ; ++k) {
                                        final IBlockState currentState = ebs.get(i, j, k);
                                        if (!Controller.blackList.contains(currentState.getBlock())) {
                                            final IBlockState defaultState = currentState.getBlock().getDefaultState();
                                            final boolean defaultExists = blocks.containsKey(defaultState.toString());
                                            final boolean currentExists = blocks.containsKey(currentState.toString());
                                            if (defaultExists || currentExists) {
                                                final BlockData blockData = blocks.get(currentExists ? currentState.toString() : defaultState.toString());
                                                if (blockData != null) {
                                                    if (blockData.isDrawing()) {
                                                        final double alpha = Configuration.shouldFade ? Math.max(0.0, (Controller.getRadius() - XRay.mc.player.getDistance((double)(x + i), (double)(y + j), (double)(z + k))) / Controller.getRadius() * 255.0) : 255.0;
                                                        renderQueue.add(new BlockInfo(x + i, y + j, z + k, blockData.getColor().getColor(), alpha));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        final BlockPos playerPos = XRay.mc.player.getPosition();
        renderQueue.sort((t, t1) -> Double.compare((double)t1.distanceSq((Vec3i)((Object)((Object)((Object)playerPos)))), (double)t.distanceSq((Vec3i)((Object)((Object)((Object)playerPos))))));
        Render.ores.clear();
        Render.ores.addAll(renderQueue);
    }
    
    public static void checkBlock(final BlockPos pos, final IBlockState state, final boolean add) {
        if (!Controller.drawOres() || Controller.getBlockStore().getStore().isEmpty()) {
            return;
        }
        final String defaultState = state.getBlock().getDefaultState().toString();
        if (Controller.getBlockStore().getStore().containsKey(defaultState)) {
            if (!add) {
                Render.ores.remove(new BlockInfo((Vec3i)pos, null, 0.0));
                return;
            }
            BlockData data = null;
            if (Controller.getBlockStore().getStore().containsKey(defaultState)) {
                data = Controller.getBlockStore().getStore().get(defaultState);
            }
            if (data == null) {
                return;
            }
            final double alpha = Configuration.shouldFade ? Math.max(0.0, (Controller.getRadius() - XRay.mc.player.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ())) / Controller.getRadius() * 255.0) : 255.0;
            Render.ores.add(new BlockInfo((Vec3i)pos, data.getColor().getColor(), alpha));
        }
    }
}
