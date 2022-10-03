package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import java.util.stream.*;
import java.util.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraft.block.state.*;
import net.minecraftforge.client.event.*;
import zelix.utils.hooks.visual.*;
import net.minecraft.util.math.*;

public class Nuker extends Hack
{
    public ModeValue mode;
    public NumberValue distance;
    public BooleanValue packet;
    public BooleanValue nofalling;
    public final ArrayDeque<Set<BlockPos>> prevBlocks;
    public BlockPos currentBlock;
    public float progress;
    public float prevProgress;
    public int id;
    
    public Nuker() {
        super("Nuker", HackCategory.PLAYER);
        this.prevBlocks = new ArrayDeque<Set<BlockPos>>();
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("ID", true), new Mode("All", false) });
        this.distance = new NumberValue("Distance", 6.0, 0.1, 6.0);
        this.packet = new BooleanValue("Creative", Boolean.valueOf(false));
        this.nofalling = new BooleanValue("NoFallingBlocks", Boolean.valueOf(true));
        this.addValue(this.mode, this.distance, this.packet, this.nofalling);
    }
    
    @Override
    public String getDescription() {
        return "Automatically breaks blocks around you.";
    }
    
    @Override
    public void onDisable() {
        if (this.currentBlock != null) {
            PlayerControllerUtils.setIsHittingBlock(true);
            Wrapper.INSTANCE.controller().resetBlockRemoving();
            this.currentBlock = null;
        }
        this.prevBlocks.clear();
        this.id = 0;
        super.onDisable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        this.currentBlock = null;
        final Vec3d eyesPos = Utils.getEyesPos().subtract(0.5, 0.5, 0.5);
        final BlockPos eyesBlock = new BlockPos(Utils.getEyesPos());
        final double rangeSq = Math.pow(this.distance.getValue(), 2.0);
        final int blockRange = (int)Math.ceil(this.distance.getValue());
        Stream<BlockPos> stream = StreamSupport.stream(BlockPos.getAllInBox(eyesBlock.add(blockRange, blockRange, blockRange), eyesBlock.add(-blockRange, -blockRange, -blockRange)).spliterator(), true);
        stream = stream.filter(pos -> eyesPos.squareDistanceTo(new Vec3d(pos)) <= rangeSq).filter(pos -> BlockUtils.canBeClicked(pos)).sorted(Comparator.comparingDouble(pos -> eyesPos.squareDistanceTo(new Vec3d(pos))));
        if (this.mode.getMode("ID").isToggled()) {
            stream = stream.filter(pos -> Block.getIdFromBlock(BlockUtils.getBlock(pos)) == this.id);
        }
        else if (this.mode.getMode("All").isToggled()) {}
        final List<BlockPos> blocks = stream.collect(Collectors.toList());
        if (Wrapper.INSTANCE.player().capabilities.isCreativeMode) {
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
            Wrapper.INSTANCE.controller().resetBlockRemoving();
            this.progress = 1.0f;
            this.prevProgress = 1.0f;
            BlockUtils.breakBlocksPacketSpam(blocks2);
            return;
        }
        for (final BlockPos pos2 : blocks) {
            if (!(BlockUtils.getBlock(pos2) instanceof Block) && this.nofalling.getValue()) {
                continue;
            }
            if (this.packet.getValue()) {
                Minecraft.getMinecraft().player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos2, EnumFacing.UP));
            }
            else {
                if (BlockUtils.breakBlockSimple(pos2)) {
                    this.currentBlock = pos2;
                    break;
                }
                continue;
            }
        }
        if (this.currentBlock == null) {
            Wrapper.INSTANCE.controller().resetBlockRemoving();
        }
        if (this.currentBlock != null && BlockUtils.getHardness(this.currentBlock) < 1.0f) {
            this.prevProgress = this.progress;
        }
        this.progress = PlayerControllerUtils.getCurBlockDamageMP();
        if (this.progress < this.prevProgress) {
            this.prevProgress = this.progress;
        }
        else {
            this.progress = 1.0f;
            this.prevProgress = 1.0f;
        }
        super.onClientTick(event);
    }
    
    @Override
    public void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
        if (this.mode.getMode("ID").isToggled() && Wrapper.INSTANCE.world().isRemote) {
            final IBlockState blockState = BlockUtils.getState(event.getPos());
            this.id = Block.getIdFromBlock(blockState.getBlock());
        }
        super.onLeftClickBlock(event);
    }
    
    @Override
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (this.currentBlock == null) {
            return;
        }
        if (this.mode.getMode("All").isToggled()) {
            RenderUtils.drawBlockESP(this.currentBlock, 1.0f, 0.0f, 0.0f);
        }
        else if (this.mode.getMode("ID").isToggled()) {
            RenderUtils.drawBlockESP(this.currentBlock, 0.0f, 0.0f, 1.0f);
        }
        super.onRenderWorldLast(event);
    }
}
