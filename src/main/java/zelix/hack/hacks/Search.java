package zelix.hack.hacks;

import net.minecraft.client.*;
import zelix.hack.*;
import zelix.value.*;
import zelix.utils.*;
import net.minecraft.init.*;
import java.util.function.*;
import java.util.stream.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.entity.*;
import net.minecraft.world.chunk.*;
import net.minecraft.block.state.*;
import net.minecraft.block.*;
import net.minecraftforge.client.event.*;
import java.util.*;
import zelix.utils.hooks.visual.*;
import org.lwjgl.opengl.*;
import net.minecraft.util.math.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;

public class Search extends Hack
{
    public static List<BlockPos> toRender;
    public EBlockPos pos;
    public BooleanValue dia;
    public BooleanValue gold;
    public BooleanValue iron;
    public BooleanValue lapis;
    public BooleanValue emerald;
    public BooleanValue coal;
    public BooleanValue redstone;
    public BooleanValue bypass;
    public BooleanValue limitEnabled;
    public BooleanValue radiusOn;
    public NumberValue depth;
    public NumberValue limit;
    public NumberValue refresh_timer;
    public static NumberValue alpha;
    public static NumberValue width;
    public NumberValue radius;
    private final Minecraft mc;
    private final TimerUtils refresh;
    
    public Search() {
        super("Search", HackCategory.VISUAL);
        this.pos = new EBlockPos();
        this.dia = new BooleanValue("Diamond", Boolean.valueOf(true));
        this.gold = new BooleanValue("Gold", Boolean.valueOf(true));
        this.iron = new BooleanValue("Iron", Boolean.valueOf(true));
        this.lapis = new BooleanValue("Lapis", Boolean.valueOf(true));
        this.emerald = new BooleanValue("Emerald", Boolean.valueOf(true));
        this.coal = new BooleanValue("Coal", Boolean.valueOf(true));
        this.redstone = new BooleanValue("Redstone", Boolean.valueOf(true));
        this.bypass = new BooleanValue("Bypass", Boolean.valueOf(true));
        this.limitEnabled = new BooleanValue("RenderLimitEnabled", Boolean.valueOf(true));
        this.radiusOn = new BooleanValue("RadiusEnabled", Boolean.valueOf(true));
        this.depth = new NumberValue("Depth", 2.0, 1.0, 5.0);
        this.limit = new NumberValue("RenderLimit", 10.0, 5.0, 100.0);
        this.refresh_timer = new NumberValue("RefreshDelay", 5.0, 0.0, 50.0);
        this.radius = new NumberValue("Radius", 10.0, 5.0, 100.0);
        this.mc = Minecraft.getMinecraft();
        this.refresh = new TimerUtils();
        this.addValue(this.dia);
        this.addValue(this.gold);
        this.addValue(this.iron);
        this.addValue(this.lapis);
        this.addValue(this.emerald);
        this.addValue(this.coal);
        this.addValue(this.redstone);
        this.addValue(this.bypass);
        this.addValue(this.depth);
        this.addValue(this.radiusOn);
        this.addValue(this.radius);
        this.addValue(this.limitEnabled);
        this.addValue(this.limit);
        this.addValue(this.refresh_timer);
        this.addValue(Search.alpha);
        this.addValue(Search.width);
    }
    
    @Override
    public void onEnable() {
        Search.toRender.clear();
        this.refresh.reset();
        Wrapper.INSTANCE.mc().renderGlobal.loadRenderers();
    }
    
    @Override
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        for (final BlockPos blockPos : Search.toRender) {
            renderBlock(blockPos, event);
        }
        if (this.refresh.isDelay((long)(Object)this.refresh_timer.getValue())) {
            final WorldClient world = Wrapper.INSTANCE.mc().world;
            final EntityPlayerSP player = Wrapper.INSTANCE.mc().player;
            if (world != null && player != null) {
                final int sx = (int)player.posX - (int)(Object)this.radius.getValue();
                final int sz = (int)player.posZ - (int)(Object)this.radius.getValue();
                final int endX = (int)player.posX + (int)(Object)this.radius.getValue();
                final int endZ = (int)player.posZ + (int)(Object)this.radius.getValue();
                for (int x = sx; x <= endX; ++x) {
                    this.pos.setX(x);
                    for (int z = sz; z <= endZ; ++z) {
                        final Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
                        if (chunk.isLoaded()) {
                            this.pos.setZ(z);
                            for (int y = 0; y <= 255; ++y) {
                                this.pos.setY(y);
                                final IBlockState blockState = chunk.getBlockState((BlockPos)this.pos);
                                final Block block = blockState.getBlock();
                                if (block != Blocks.AIR) {
                                    final BlockPos poss = new BlockPos(x, y, z);
                                    if (!Search.toRender.contains(poss) && this.test(poss) && (Search.toRender.size() <= this.limit.getValue() || !this.limitEnabled.getValue())) {
                                        Search.toRender.add(poss);
                                    }
                                }
                            }
                        }
                    }
                }
                List<BlockPos> list = Search.toRender;
                list = (Search.toRender = list.stream().filter(this::test).collect(Collectors.toList()));
                this.refresh.reset();
            }
        }
    }
    
    @Override
    public void onRender3D(final RenderBlockOverlayEvent event) {
        if (!Search.toRender.contains(this.pos) && this.test(this.pos) && (Search.toRender.size() <= this.limit.getValue() || !this.limitEnabled.getValue())) {
            Search.toRender.add(this.pos);
        }
    }
    
    public boolean isTarget(final BlockPos pos) {
        final Block block = Wrapper.INSTANCE.mc().world.getBlockState(pos).getBlock();
        if (Blocks.DIAMOND_ORE.equals(block)) {
            return this.dia.getValue();
        }
        if (Blocks.LAPIS_ORE.equals(block)) {
            return this.lapis.getValue();
        }
        if (Blocks.IRON_ORE.equals(block)) {
            return this.iron.getValue();
        }
        if (Blocks.GOLD_ORE.equals(block)) {
            return this.gold.getValue();
        }
        if (Blocks.COAL_ORE.equals(block)) {
            return this.coal.getValue();
        }
        if (Blocks.EMERALD_ORE.equals(block)) {
            return this.emerald.getValue();
        }
        return (Blocks.REDSTONE_TORCH.equals(block) || Blocks.LIT_REDSTONE_ORE.equals(block)) && this.redstone.getValue();
    }
    
    private Boolean oreTest(final BlockPos origPos, final Double depth) {
        Collection<BlockPos> posesNew = new ArrayList<BlockPos>();
        Collection<BlockPos> posesLast = new ArrayList<BlockPos>(Collections.singletonList(origPos));
        final Collection<BlockPos> finalList = new ArrayList<BlockPos>();
        for (int i = 0; i < depth; ++i) {
            for (final BlockPos blockPos2 : posesLast) {
                posesNew.add(blockPos2.up());
                posesNew.add(blockPos2.down());
                posesNew.add(blockPos2.north());
                posesNew.add(blockPos2.south());
                posesNew.add(blockPos2.west());
                posesNew.add(blockPos2.east());
            }
            for (final BlockPos pos : posesNew) {
                if (posesLast.contains(pos)) {
                    posesNew.remove(pos);
                }
            }
            posesLast = posesNew;
            finalList.addAll(posesNew);
            posesNew = new ArrayList<BlockPos>();
        }
        final List<Block> legitBlocks = Arrays.asList(Blocks.WATER, Blocks.LAVA, Blocks.FLOWING_LAVA, Blocks.AIR, Blocks.FLOWING_WATER, Blocks.FIRE);
        return finalList.stream().anyMatch(blockPos -> legitBlocks.contains(Wrapper.INSTANCE.mc().world.getBlockState(blockPos).getBlock()));
    }
    
    public boolean test(final BlockPos pos1) {
        return this.isTarget(pos1) && (!this.bypass.getValue() || this.oreTest(pos1, (double)this.depth.getValue())) && (!this.radiusOn.getValue() || Wrapper.INSTANCE.mc().player.getDistance((double)pos1.getX(), (double)pos1.getY(), (double)pos1.getZ()) < this.radius.getValue());
    }
    
    public static void renderBlock(final BlockPos pos, final RenderWorldLastEvent event) {
        final double x = pos.getX() - Wrapper.getRenderPosX();
        final double y = pos.getY() - Wrapper.getRenderPosY();
        final double z = pos.getZ() - Wrapper.getRenderPosZ();
        final float[] color = getColor(pos);
        RenderUtils.drawTracer(pos, color[0], color[1], color[2], (float)(Object)Search.alpha.getValue(), event.getPartialTicks());
        drawOutlinedBlockESP(x, y, z, color[0], color[1], color[2], (float)(Object)Search.alpha.getValue(), (float)(Object)Search.width.getValue());
    }
    
    public static float[] getColor(final BlockPos pos) {
        final Block block = Minecraft.getMinecraft().world.getBlockState(pos).getBlock();
        if (Blocks.DIAMOND_ORE.equals(block)) {
            return new float[] { 0.0f, 1.0f, 1.0f };
        }
        if (Blocks.LAPIS_ORE.equals(block)) {
            return new float[] { 0.0f, 0.0f, 1.0f };
        }
        if (Blocks.IRON_ORE.equals(block)) {
            return new float[] { 1.0f, 1.0f, 1.0f };
        }
        if (Blocks.GOLD_ORE.equals(block)) {
            return new float[] { 1.0f, 1.0f, 0.0f };
        }
        if (Blocks.COAL_ORE.equals(block)) {
            return new float[] { 0.0f, 0.0f, 0.0f };
        }
        if (Blocks.EMERALD_BLOCK.equals(block)) {
            return new float[] { 0.0f, 1.0f, 0.0f };
        }
        if (Blocks.REDSTONE_ORE.equals(block) || Blocks.LIT_REDSTONE_ORE.equals(block)) {
            return new float[] { 1.0f, 0.0f, 0.0f };
        }
        return new float[] { 0.0f, 0.0f, 0.0f };
    }
    
    public static void drawOutlinedBlockESP(final double x, final double y, final double z, final float red, final float green, final float blue, final float alpha, final float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawOutlinedBoundingBox(final AxisAlignedBB aa) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }
    
    static {
        Search.toRender = new ArrayList<BlockPos>();
        Search.alpha = new NumberValue("Alpha", 0.25, 0.0, 1.0);
        Search.width = new NumberValue("LineWidth", 2.5, 1.0, 10.0);
    }
}
