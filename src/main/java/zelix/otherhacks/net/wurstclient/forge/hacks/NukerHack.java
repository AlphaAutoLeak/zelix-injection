package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraft.client.entity.*;
import java.util.stream.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraft.entity.player.*;
import net.minecraftforge.client.event.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.tileentity.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import net.minecraft.util.math.*;
import java.util.function.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;

public final class NukerHack extends Hack
{
    private final SliderSetting range;
    private final EnumSetting<Mode> mode;
    private final ArrayDeque<Set<BlockPos>> prevBlocks;
    private BlockPos currentBlock;
    private float progress;
    private float prevProgress;
    private int id;
    
    public NukerHack() {
        super("Nuker", "Automatically breaks blocks around you.");
        this.range = new SliderSetting("Range", 5.0, 1.0, 6.0, 0.05, SliderSetting.ValueDisplay.DECIMAL);
        this.mode = new EnumSetting<Mode>("Mode", Mode.values(), Mode.NORMAL);
        this.prevBlocks = new ArrayDeque<Set<BlockPos>>();
        this.setCategory(Category.BLOCKS);
        this.addSetting(this.range);
        this.addSetting(this.mode);
    }
    
    @Override
    public String getRenderName() {
        return this.mode.getSelected().getRenderName(this);
    }
    
    @Override
    protected void onEnable() {
        NukerHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        if (this.currentBlock != null) {
            try {
                PlayerControllerUtils.setIsHittingBlock(true);
                NukerHack.mc.playerController.resetBlockRemoving();
                this.currentBlock = null;
            }
            catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
        this.prevBlocks.clear();
        this.id = 0;
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        final EntityPlayerSP player = event.getPlayer();
        this.currentBlock = null;
        final Vec3d eyesPos = RotationUtils.getEyesPos().subtract(0.5, 0.5, 0.5);
        final BlockPos eyesBlock = new BlockPos(RotationUtils.getEyesPos());
        final double rangeSq = Math.pow(this.range.getValue(), 2.0);
        final int blockRange = (int)Math.ceil(this.range.getValue());
        final Stream<BlockPos> stream = StreamSupport.stream(BlockPos.getAllInBox(eyesBlock.add(blockRange, blockRange, blockRange), eyesBlock.add(-blockRange, -blockRange, -blockRange)).spliterator(), true);
        final List<BlockPos> blocks = stream.filter(pos -> eyesPos.squareDistanceTo(new Vec3d(pos)) <= rangeSq).filter(pos -> BlockUtils.canBeClicked(pos)).filter(this.mode.getSelected().getValidator(this)).sorted(Comparator.comparingDouble(pos -> eyesPos.squareDistanceTo(new Vec3d(pos)))).collect(Collectors.toList());
        if (player.capabilities.isCreativeMode) {
            Stream<BlockPos> stream2 = blocks.parallelStream();
            for (final Set<BlockPos> set : this.prevBlocks) {
                stream2 = stream2.filter(pos -> !set.contains(pos));
            }
            final List<BlockPos> blocks2 = stream2.collect(Collectors.toList());
            this.prevBlocks.addLast(new HashSet<BlockPos>(blocks2));
            while (this.prevBlocks.size() > 5) {
                this.prevBlocks.removeFirst();
            }
            if (!blocks2.isEmpty()) {
                this.currentBlock = blocks2.get(0);
            }
            NukerHack.mc.playerController.resetBlockRemoving();
            this.progress = 1.0f;
            this.prevProgress = 1.0f;
            BlockUtils.breakBlocksPacketSpam(blocks2);
            return;
        }
        for (final BlockPos pos2 : blocks) {
            if (BlockUtils.breakBlockSimple(pos2)) {
                this.currentBlock = pos2;
                break;
            }
        }
        if (this.currentBlock == null) {
            NukerHack.mc.playerController.resetBlockRemoving();
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
    
    @SubscribeEvent
    public void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
        final EntityPlayer player = event.getEntityPlayer();
        if (!player.world.isRemote) {
            return;
        }
        if (this.mode.getSelected() == Mode.ID) {
            this.id = BlockUtils.getId(event.getPos());
        }
    }
    
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (this.currentBlock == null) {
            return;
        }
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glEnable(2884);
        GL11.glDisable(2929);
        GL11.glPushMatrix();
        GL11.glTranslated(-TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY, -TileEntityRendererDispatcher.staticPlayerZ);
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
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    private enum Mode
    {
        NORMAL("Normal", n -> n.getName(), (n, p) -> true), 
        ID("ID", n -> "IDNuker [" + n.id + "]", (n, p) -> BlockUtils.getId(p) == n.id), 
        FLAT("Flat", n -> "FlatNuker", (n, p) -> p.getY() >= WMinecraft.getPlayer().getPosition().getY()), 
        SMASH("Smash", n -> "SmashNuker", (n, p) -> BlockUtils.getHardness(p) >= 1.0f);
        
        private final String name;
        private final Function<NukerHack, String> renderName;
        private final BiPredicate<NukerHack, BlockPos> validator;
        
        private Mode(final String name, final Function<NukerHack, String> renderName, final BiPredicate<NukerHack, BlockPos> validator) {
            this.name = name;
            this.renderName = renderName;
            this.validator = validator;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public String getRenderName(final NukerHack n) {
            return this.renderName.apply(n);
        }
        
        public Predicate<BlockPos> getValidator(final NukerHack n) {
            return p -> this.validator.test(n, p);
        }
    }
}
