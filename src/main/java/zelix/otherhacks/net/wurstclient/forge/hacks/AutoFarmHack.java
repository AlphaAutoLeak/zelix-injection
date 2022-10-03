package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import org.lwjgl.opengl.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import java.util.function.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.tileentity.*;
import java.util.stream.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import net.minecraft.client.entity.*;
import net.minecraft.item.*;
import java.util.*;
import net.minecraft.util.math.*;

public final class AutoFarmHack extends Hack
{
    private final SliderSetting range;
    private final HashMap<BlockPos, Item> plants;
    private final ArrayDeque<Set<BlockPos>> prevBlocks;
    private BlockPos currentBlock;
    private float progress;
    private float prevProgress;
    private int displayList;
    private int box;
    private int node;
    
    public AutoFarmHack() {
        super("AutoFarm", "Harvests and re-plants crops automatically.\nWorks with wheat, carrots, potatoes, beetroots,\npumpkins, melons, cacti, sugar canes and\nnether warts.");
        this.range = new SliderSetting("Range", 5.0, 1.0, 6.0, 0.05, SliderSetting.ValueDisplay.DECIMAL);
        this.plants = new HashMap<BlockPos, Item>();
        this.prevBlocks = new ArrayDeque<Set<BlockPos>>();
        this.setCategory(Category.BLOCKS);
        this.addSetting(this.range);
    }
    
    @Override
    protected void onEnable() {
        this.plants.clear();
        this.displayList = GL11.glGenLists(1);
        this.box = GL11.glGenLists(1);
        this.node = GL11.glGenLists(1);
        GL11.glNewList(this.box, 4864);
        final AxisAlignedBB box = new AxisAlignedBB(0.0625, 0.0625, 0.0625, 0.9375, 0.9375, 0.9375);
        GL11.glBegin(1);
        RenderUtils.drawOutlinedBox(box);
        GL11.glEnd();
        GL11.glEndList();
        GL11.glNewList(this.node, 4864);
        final AxisAlignedBB node = new AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
        GL11.glBegin(1);
        RenderUtils.drawNode(node);
        GL11.glEnd();
        GL11.glEndList();
        AutoFarmHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        if (this.currentBlock != null) {
            try {
                PlayerControllerUtils.setIsHittingBlock(true);
                AutoFarmHack.mc.playerController.resetBlockRemoving();
                this.currentBlock = null;
            }
            catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
        this.prevBlocks.clear();
        GL11.glDeleteLists(this.displayList, 1);
        GL11.glDeleteLists(this.box, 1);
        GL11.glDeleteLists(this.node, 1);
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        this.currentBlock = null;
        final Vec3d eyesVec = RotationUtils.getEyesPos().subtract(0.5, 0.5, 0.5);
        final BlockPos eyesBlock = new BlockPos(RotationUtils.getEyesPos());
        final double rangeSq = Math.pow(this.range.getValue(), 2.0);
        final int blockRange = (int)Math.ceil(this.range.getValue());
        final List<BlockPos> blocks = this.getBlockStream(eyesBlock, blockRange).filter(pos -> eyesVec.squareDistanceTo(new Vec3d(pos)) <= rangeSq).filter(pos -> BlockUtils.canBeClicked(pos)).collect(Collectors.toList());
        this.registerPlants(blocks);
        final List<BlockPos> blocksToHarvest = blocks.parallelStream().filter(this::shouldBeHarvested).sorted(Comparator.comparingDouble(pos -> eyesVec.squareDistanceTo(new Vec3d(pos)))).collect(Collectors.toList());
        final List<BlockPos> blocksToReplant = this.getBlockStream(eyesBlock, blockRange).filter(pos -> eyesVec.squareDistanceTo(new Vec3d(pos)) <= rangeSq).filter(pos -> BlockUtils.getMaterial(pos).isReplaceable()).filter(pos -> this.plants.containsKey(pos)).filter(this::canBeReplanted).sorted(Comparator.comparingDouble(pos -> eyesVec.squareDistanceTo(new Vec3d(pos)))).collect(Collectors.toList());
        while (!blocksToReplant.isEmpty()) {
            final BlockPos pos2 = blocksToReplant.get(0);
            final Item neededItem = this.plants.get(pos2);
            if (this.tryToReplant(pos2, neededItem)) {
                break;
            }
            blocksToReplant.removeIf(p -> this.plants.get(p) == neededItem);
        }
        if (blocksToReplant.isEmpty()) {
            this.harvest(blocksToHarvest);
        }
        this.updateDisplayList(blocksToHarvest, blocksToReplant);
    }
    
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glEnable(2884);
        GL11.glDisable(2929);
        GL11.glPushMatrix();
        GL11.glTranslated(-TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY, -TileEntityRendererDispatcher.staticPlayerZ);
        GL11.glCallList(this.displayList);
        if (this.currentBlock != null) {
            GL11.glPushMatrix();
            final AxisAlignedBB box = new AxisAlignedBB(BlockPos.ORIGIN);
            final float p = this.prevProgress + (this.progress - this.prevProgress) * event.getPartialTicks();
            final float red = p * 2.0f;
            final float green = 2.0f - red;
            GL11.glTranslated((double)this.currentBlock.getX(), (double)this.currentBlock.getY(), (double)this.currentBlock.getZ());
            if (p < 1.0f) {
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glScaled((double)p, (double)p, (double)p);
                GL11.glTranslated(-0.5, -0.5, -0.5);
            }
            GL11.glColor4f(red, green, 0.0f, 0.25f);
            GL11.glBegin(7);
            RenderUtils.drawSolidBox(box);
            GL11.glEnd();
            GL11.glColor4f(red, green, 0.0f, 0.5f);
            GL11.glBegin(1);
            RenderUtils.drawOutlinedBox(box);
            GL11.glEnd();
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    private Stream<BlockPos> getBlockStream(final BlockPos center, final int range) {
        return StreamSupport.stream(BlockPos.getAllInBox(center.add(range, range, range), center.add(-range, -range, -range)).spliterator(), true);
    }
    
    private boolean shouldBeHarvested(final BlockPos pos) {
        final Block block = BlockUtils.getBlock(pos);
        final IBlockState state = BlockUtils.getState(pos);
        if (block instanceof BlockCrops) {
            return ((BlockCrops)block).isMaxAge(state);
        }
        if (block instanceof BlockPumpkin || block instanceof BlockMelon) {
            return true;
        }
        if (block instanceof BlockReed) {
            return BlockUtils.getBlock(pos.down()) instanceof BlockReed && !(BlockUtils.getBlock(pos.down(2)) instanceof BlockReed);
        }
        if (block instanceof BlockCactus) {
            return BlockUtils.getBlock(pos.down()) instanceof BlockCactus && !(BlockUtils.getBlock(pos.down(2)) instanceof BlockCactus);
        }
        return block instanceof BlockNetherWart && (int)state.getValue((IProperty)BlockNetherWart.AGE) >= 3;
    }
    
    private void registerPlants(final List<BlockPos> blocks) {
        final HashMap<Block, Item> seeds = new HashMap<Block, Item>();
        seeds.put(Blocks.WHEAT, Items.WHEAT_SEEDS);
        seeds.put(Blocks.CARROTS, Items.CARROT);
        seeds.put(Blocks.POTATOES, Items.POTATO);
        seeds.put(Blocks.BEETROOTS, Items.BEETROOT_SEEDS);
        seeds.put(Blocks.PUMPKIN_STEM, Items.PUMPKIN_SEEDS);
        seeds.put(Blocks.MELON_STEM, Items.MELON_SEEDS);
        seeds.put(Blocks.NETHER_WART, Items.NETHER_WART);
        this.plants.putAll(blocks.parallelStream().filter(pos -> seeds.containsKey(BlockUtils.getBlock(pos))).collect(Collectors.toMap(pos -> pos, pos -> seeds.get(BlockUtils.getBlock(pos)))));
    }
    
    private boolean canBeReplanted(final BlockPos pos) {
        final Item item = this.plants.get(pos);
        if (item == Items.WHEAT_SEEDS || item == Items.CARROT || item == Items.POTATO || item == Items.BEETROOT_SEEDS || item == Items.PUMPKIN_SEEDS || item == Items.MELON_SEEDS) {
            return BlockUtils.getBlock(pos.down()) instanceof BlockFarmland;
        }
        return item == Items.NETHER_WART && BlockUtils.getBlock(pos.down()) instanceof BlockSoulSand;
    }
    
    private boolean tryToReplant(final BlockPos pos, final Item neededItem) {
        final EntityPlayerSP player = WMinecraft.getPlayer();
        final ItemStack heldItem = player.getHeldItemMainhand();
        if (heldItem != null && !heldItem.isEmpty() && heldItem.getItem() == neededItem) {
            BlockUtils.placeBlockSimple(pos);
            return true;
        }
        for (int slot = 0; slot < 36; ++slot) {
            if (slot != player.inventory.currentItem) {
                final ItemStack stack = player.inventory.getStackInSlot(slot);
                if (stack != null && !stack.isEmpty()) {
                    if (stack.getItem() == neededItem) {
                        if (slot < 9) {
                            player.inventory.currentItem = slot;
                        }
                        else if (player.inventory.getFirstEmptyStack() < 9) {
                            PlayerControllerUtils.windowClick_QUICK_MOVE(slot);
                        }
                        else if (player.inventory.getFirstEmptyStack() != -1) {
                            PlayerControllerUtils.windowClick_QUICK_MOVE(player.inventory.currentItem + 36);
                            PlayerControllerUtils.windowClick_QUICK_MOVE(slot);
                        }
                        else {
                            PlayerControllerUtils.windowClick_PICKUP(player.inventory.currentItem + 36);
                            PlayerControllerUtils.windowClick_PICKUP(slot);
                            PlayerControllerUtils.windowClick_PICKUP(player.inventory.currentItem + 36);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void harvest(final List<BlockPos> blocksToHarvest) {
        if (WMinecraft.getPlayer().capabilities.isCreativeMode) {
            Stream<BlockPos> stream3 = blocksToHarvest.parallelStream();
            for (final Set<BlockPos> set : this.prevBlocks) {
                stream3 = stream3.filter(pos -> !set.contains(pos));
            }
            final List<BlockPos> blocksToHarvest2 = stream3.collect(Collectors.toList());
            this.prevBlocks.addLast(new HashSet<BlockPos>(blocksToHarvest2));
            while (this.prevBlocks.size() > 5) {
                this.prevBlocks.removeFirst();
            }
            if (!blocksToHarvest2.isEmpty()) {
                this.currentBlock = blocksToHarvest2.get(0);
            }
            AutoFarmHack.mc.playerController.resetBlockRemoving();
            this.progress = 1.0f;
            this.prevProgress = 1.0f;
            BlockUtils.breakBlocksPacketSpam(blocksToHarvest2);
            return;
        }
        for (final BlockPos pos2 : blocksToHarvest) {
            if (BlockUtils.breakBlockSimple(pos2)) {
                this.currentBlock = pos2;
                break;
            }
        }
        if (this.currentBlock == null) {
            AutoFarmHack.mc.playerController.resetBlockRemoving();
        }
        if (this.currentBlock != null && BlockUtils.getHardness(this.currentBlock) < 1.0f) {
            try {
                this.prevProgress = this.progress;
                this.progress = PlayerControllerUtils.getCurBlockDamageMP();
                if (this.progress < this.prevProgress) {
                    this.prevProgress = this.progress;
                }
                return;
            }
            catch (ReflectiveOperationException e) {
                this.setEnabled(false);
                throw new RuntimeException(e);
            }
        }
        this.progress = 1.0f;
        this.prevProgress = 1.0f;
    }
    
    private void updateDisplayList(final List<BlockPos> blocksToHarvest, final List<BlockPos> blocksToReplant) {
        GL11.glNewList(this.displayList, 4864);
        GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.5f);
        for (final BlockPos pos : blocksToHarvest) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
            GL11.glCallList(this.box);
            GL11.glPopMatrix();
        }
        GL11.glColor4f(0.0f, 1.0f, 1.0f, 0.5f);
        for (final BlockPos pos : this.plants.keySet()) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
            GL11.glCallList(this.node);
            GL11.glPopMatrix();
        }
        GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.5f);
        for (final BlockPos pos : blocksToReplant) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
            GL11.glCallList(this.box);
            GL11.glPopMatrix();
        }
        GL11.glEndList();
    }
}
