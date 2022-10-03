package zelix.otherhacks.net.wurstclient.forge.hacks;

import net.minecraft.util.*;
import com.mojang.realmsclient.gui.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import org.lwjgl.opengl.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import net.minecraft.entity.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraft.client.settings.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.util.math.*;
import net.minecraft.client.*;
import zelix.otherhacks.net.wurstclient.forge.*;
import java.util.function.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import net.minecraft.item.*;
import java.util.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.block.*;

@Hack.DontSaveState
public final class TunnellerHack extends Hack
{
    private final EnumSetting<TunnelSize> size;
    private final SliderSetting limit;
    private final CheckboxSetting torches;
    private BlockPos start;
    private EnumFacing direction;
    private int length;
    private Task[] tasks;
    private int[] displayLists;
    private BlockPos currentBlock;
    private float progress;
    private float prevProgress;
    
    public TunnellerHack() {
        super("Tunneller", "Automatically digs a tunnel.\n\n" + ChatFormatting.RED + ChatFormatting.BOLD + "WARNING:" + ChatFormatting.RESET + " Although this bot will try to avoid\nlava and other dangers, there is no guarantee\nthat it won't die. Only send it out with gear\nthat you don't mind losing.");
        this.size = new EnumSetting<TunnelSize>("Tunnel size", TunnelSize.values(), TunnelSize.SIZE_3X3);
        this.limit = new SliderSetting("Limit", "Automatically stops once the tunnel\nhas reached the given length.\n\n0 = no limit", 0.0, 0.0, 1000.0, 1.0, v -> {
            String string;

            if (v == 0.0) {
                string = "disabled";
            }
            else if (v == 1.0) {
                string = "1 block";
            }
            else {
                string = (int)v + " blocks";
            }
            return string;
        });
        this.torches = new CheckboxSetting("Place torches", "Places just enough torches\nto prevent mobs from\nspawning inside the tunnel.", false);
        this.displayLists = new int[5];
        this.setCategory(Category.BLOCKS);
        this.addSetting(this.size);
        this.addSetting(this.limit);
        this.addSetting(this.torches);
    }
    
    @Override
    public String getRenderName() {
        if (this.limit.getValueI() == 0) {
            return this.getName();
        }
        return this.getName() + " [" + this.length + "/" + this.limit.getValueI() + "]";
    }
    
    @Override
    protected void onEnable() {
        TunnellerHack.wurst.register(this);
        for (int i = 0; i < this.displayLists.length; ++i) {
            this.displayLists[i] = GL11.glGenLists(1);
        }
        final EntityPlayerSP player = WMinecraft.getPlayer();
        this.start = new BlockPos((Entity)player);
        this.direction = player.getHorizontalFacing();
        this.length = 0;
        this.tasks = new Task[] { new DodgeLiquidTask(), new FillInFloorTask(), new PlaceTorchTask(), new DigTunnelTask(), new WalkForwardTask() };
        this.updateCyanList();
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        if (this.currentBlock != null) {
            try {
                PlayerControllerUtils.setIsHittingBlock(true);
                TunnellerHack.mc.playerController.resetBlockRemoving();
                this.currentBlock = null;
            }
            catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
        for (final int displayList : this.displayLists) {
            GL11.glDeleteLists(displayList, 1);
        }
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        final HackList hax = TunnellerHack.wurst.getHax();
        final Hack[] array;
        final Hack[] incompatibleHax = array = new Hack[] { hax.autoToolHack, hax.autoWalkHack, hax.flightHack, hax.nukerHack };
        for (final Hack hack : array) {
            hack.setEnabled(false);
        }
        final GameSettings gs = TunnellerHack.mc.gameSettings;
        final KeyBinding[] array2;
        final KeyBinding[] bindings = array2 = new KeyBinding[] { gs.keyBindForward, gs.keyBindBack, gs.keyBindLeft, gs.keyBindRight, gs.keyBindJump, gs.keyBindSneak };
        for (final KeyBinding binding : array2) {
            KeyBindingUtils.setPressed(binding, false);
        }
        for (final Task task : this.tasks) {
            if (task.canRun()) {
                task.run();
                break;
            }
        }
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
        for (final int displayList : this.displayLists) {
            GL11.glCallList(displayList);
        }
        if (this.currentBlock != null) {
            final float p = this.prevProgress + (this.progress - this.prevProgress) * event.getPartialTicks();
            final float red = p * 2.0f;
            final float green = 2.0f - red;
            GL11.glTranslated((double)this.currentBlock.getX(), (double)this.currentBlock.getY(), (double)this.currentBlock.getZ());
            if (p < 1.0f) {
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glScaled((double)p, (double)p, (double)p);
                GL11.glTranslated(-0.5, -0.5, -0.5);
            }
            final AxisAlignedBB box2 = new AxisAlignedBB(BlockPos.ORIGIN);
            GL11.glColor4f(red, green, 0.0f, 0.25f);
            GL11.glBegin(7);
            RenderUtils.drawSolidBox(box2);
            GL11.glEnd();
            GL11.glColor4f(red, green, 0.0f, 0.5f);
            GL11.glBegin(1);
            RenderUtils.drawOutlinedBox(box2);
            GL11.glEnd();
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    private void updateCyanList() {
        GL11.glNewList(this.displayLists[0], 4864);
        GL11.glPushMatrix();
        GL11.glTranslated((double)this.start.getX(), (double)this.start.getY(), (double)this.start.getZ());
        GL11.glTranslated(0.5, 0.5, 0.5);
        GL11.glColor4f(0.0f, 1.0f, 1.0f, 0.5f);
        GL11.glBegin(1);
        RenderUtils.drawNode(new AxisAlignedBB(-0.25, -0.25, -0.25, 0.25, 0.25, 0.25));
        GL11.glEnd();
        RenderUtils.drawArrow(new Vec3d(this.direction.getDirectionVec()).scale(0.25), new Vec3d(this.direction.getDirectionVec()).scale(Math.max(0.5, this.length)));
        GL11.glPopMatrix();
        GL11.glEndList();
    }
    
    private BlockPos offset(final BlockPos pos, final Vec3i vec) {
        return pos.offset(this.direction.rotateYCCW(), vec.getX()).up(vec.getY());
    }
    
    private int getDistance(final BlockPos pos1, final BlockPos pos2) {
        return Math.abs(pos1.getX() - pos2.getX()) + Math.abs(pos1.getY() - pos2.getY()) + Math.abs(pos1.getZ() - pos2.getZ());
    }
    
    private abstract static class Task
    {
        public abstract boolean canRun();
        
        public abstract void run();
    }
    
    private class DigTunnelTask extends Task
    {
        private int requiredDistance;
        
        @Override
        public boolean canRun() {
            final BlockPos player = new BlockPos((Entity)WMinecraft.getPlayer());
            final BlockPos base = TunnellerHack.this.start.offset(TunnellerHack.this.direction, TunnellerHack.this.length);
            final int distance = TunnellerHack.this.getDistance(player, base);
            if (distance <= 1) {
                this.requiredDistance = TunnellerHack.this.size.getSelected().maxRange;
            }
            else if (distance > TunnellerHack.this.size.getSelected().maxRange) {
                this.requiredDistance = 1;
            }
            return distance <= this.requiredDistance;
        }
        
        @Override
        public void run() {
            final BlockPos base = TunnellerHack.this.start.offset(TunnellerHack.this.direction, TunnellerHack.this.length);
            final BlockPos from = TunnellerHack.this.offset(base, TunnellerHack.this.size.getSelected().from);
            final BlockPos to = TunnellerHack.this.offset(base, TunnellerHack.this.size.getSelected().to);
            final ArrayList<BlockPos> blocks = new ArrayList<BlockPos>();
            BlockPos.getAllInBox(from, to).forEach(blocks::add);
            Collections.reverse(blocks);
            GL11.glNewList(TunnellerHack.this.displayLists[1], 4864);
            final AxisAlignedBB box = new AxisAlignedBB(0.1, 0.1, 0.1, 0.9, 0.9, 0.9);
            GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.5f);
            for (final BlockPos pos : blocks) {
                GL11.glPushMatrix();
                GL11.glTranslated((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
                GL11.glBegin(1);
                RenderUtils.drawOutlinedBox(box);
                GL11.glEnd();
                GL11.glPopMatrix();
            }
            GL11.glEndList();
            TunnellerHack.this.currentBlock = null;
            for (final BlockPos pos : blocks) {
                if (!BlockUtils.canBeClicked(pos)) {
                    continue;
                }
                TunnellerHack.this.currentBlock = pos;
                break;
            }
            if (TunnellerHack.this.currentBlock == null) {
                TunnellerHack.mc.playerController.resetBlockRemoving();
                TunnellerHack.this.progress = 1.0f;
                TunnellerHack.this.prevProgress = 1.0f;
                TunnellerHack.this.length++;
                if (TunnellerHack.this.limit.getValueI() == 0 || TunnellerHack.this.length < TunnellerHack.this.limit.getValueI()) {
                    TunnellerHack.this.updateCyanList();
                }
                else {
                    ChatUtils.message("Tunnel completed.");
                    TunnellerHack.this.setEnabled(false);
                }
                return;
            }
            TunnellerHack.wurst.getHax().autoToolHack.equipBestTool(TunnellerHack.this.currentBlock, false, true, false);
            BlockUtils.breakBlockSimple(TunnellerHack.this.currentBlock);
            if (WMinecraft.getPlayer().capabilities.isCreativeMode || BlockUtils.getHardness(TunnellerHack.this.currentBlock) >= 1.0f) {
                TunnellerHack.this.progress = 1.0f;
                TunnellerHack.this.prevProgress = 1.0f;
                return;
            }
            try {
                TunnellerHack.this.prevProgress = TunnellerHack.this.progress;
                TunnellerHack.this.progress = PlayerControllerUtils.getCurBlockDamageMP();
                if (TunnellerHack.this.progress < TunnellerHack.this.prevProgress) {
                    TunnellerHack.this.prevProgress = TunnellerHack.this.progress;
                }
            }
            catch (ReflectiveOperationException e) {
                TunnellerHack.this.setEnabled(false);
                throw new RuntimeException(e);
            }
        }
    }
    
    private class WalkForwardTask extends Task
    {
        @Override
        public boolean canRun() {
            final BlockPos player = new BlockPos((Entity)WMinecraft.getPlayer());
            final BlockPos base = TunnellerHack.this.start.offset(TunnellerHack.this.direction, TunnellerHack.this.length);
            return TunnellerHack.this.getDistance(player, base) > 1;
        }
        
        @Override
        public void run() {
            final BlockPos base = TunnellerHack.this.start.offset(TunnellerHack.this.direction, TunnellerHack.this.length);
            final Vec3d vec = new Vec3d((Vec3i)base).addVector(0.5, 0.5, 0.5);
            RotationUtils.faceVectorForWalking(vec);
            KeyBindingUtils.setPressed(TunnellerHack.mc.gameSettings.keyBindForward, true);
        }
    }
    
    private class FillInFloorTask extends Task
    {
        private final ArrayList<BlockPos> blocks;
        
        private FillInFloorTask() {
            this.blocks = new ArrayList<BlockPos>();
        }
        
        @Override
        public boolean canRun() {
            final BlockPos player = new BlockPos((Entity)WMinecraft.getPlayer());
            final BlockPos from = this.offsetFloor(player, TunnellerHack.this.size.getSelected().from);
            final BlockPos to = this.offsetFloor(player, TunnellerHack.this.size.getSelected().to);
            this.blocks.clear();
            for (final BlockPos pos : BlockPos.getAllInBox(from, to)) {
                if (!BlockUtils.getState(pos).isFullBlock()) {
                    this.blocks.add(pos);
                }
            }
            GL11.glNewList(TunnellerHack.this.displayLists[2], 4864);
            final AxisAlignedBB box = new AxisAlignedBB(0.1, 0.1, 0.1, 0.9, 0.9, 0.9);
            GL11.glColor4f(1.0f, 1.0f, 0.0f, 0.5f);
            for (final BlockPos pos2 : this.blocks) {
                GL11.glPushMatrix();
                GL11.glTranslated((double)pos2.getX(), (double)pos2.getY(), (double)pos2.getZ());
                GL11.glBegin(1);
                RenderUtils.drawOutlinedBox(box);
                GL11.glEnd();
                GL11.glPopMatrix();
            }
            GL11.glEndList();
            return !this.blocks.isEmpty();
        }
        
        private BlockPos offsetFloor(final BlockPos pos, final Vec3i vec) {
            return pos.offset(TunnellerHack.this.direction.rotateYCCW(), vec.getX()).down();
        }
        
        @Override
        public void run() {
            KeyBindingUtils.setPressed(TunnellerHack.mc.gameSettings.keyBindSneak, true);
            WMinecraft.getPlayer().motionX = 0.0;
            WMinecraft.getPlayer().motionZ = 0.0;
            final Vec3d eyes = RotationUtils.getEyesPos().addVector(-0.5, -0.5, -0.5);
            final Comparator<BlockPos> comparator = Comparator.comparingDouble(p -> eyes.squareDistanceTo(new Vec3d(p)));
            final BlockPos pos = this.blocks.stream().max(comparator).get();
            if (!this.equipSolidBlock(pos)) {
                ChatUtils.error("Found a hole in the tunnel's floor but don't have any blocks to fill it with.");
                TunnellerHack.this.setEnabled(false);
                return;
            }
            if (BlockUtils.getMaterial(pos).isReplaceable()) {
                BlockUtils.placeBlockSimple(pos);
            }
            else {
                TunnellerHack.wurst.getHax().autoToolHack.equipBestTool(pos, false, true, false);
                BlockUtils.breakBlockSimple(pos);
            }
        }
        
        private boolean equipSolidBlock(final BlockPos pos) {
            for (int slot = 0; slot < 9; ++slot) {
                final ItemStack stack = WMinecraft.getPlayer().inventory.getStackInSlot(slot);
                if (stack != null && !stack.isEmpty()) {
                    if (stack.getItem() instanceof ItemBlock) {
                        final Block block = Block.getBlockFromItem(stack.getItem());
                        if (block.getDefaultState().isFullBlock()) {
                            if (!(block instanceof BlockFalling) || !BlockFalling.canFallThrough(BlockUtils.getState(pos.down()))) {
                                WMinecraft.getPlayer().inventory.currentItem = slot;
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
    }
    
    private class DodgeLiquidTask extends Task
    {
        private final HashSet<BlockPos> liquids;
        private int disableTimer;
        
        private DodgeLiquidTask() {
            this.liquids = new HashSet<BlockPos>();
            this.disableTimer = 60;
        }
        
        @Override
        public boolean canRun() {
            if (!this.liquids.isEmpty()) {
                return true;
            }
            final BlockPos base = TunnellerHack.this.start.offset(TunnellerHack.this.direction, TunnellerHack.this.length);
            final BlockPos from = TunnellerHack.this.offset(base, TunnellerHack.this.size.getSelected().from);
            final BlockPos to = TunnellerHack.this.offset(base, TunnellerHack.this.size.getSelected().to);
            final int maxY = Math.max(from.getY(), to.getY());
            for (final BlockPos pos : BlockPos.getAllInBox(from, to)) {
                for (int maxOffset = Math.min(TunnellerHack.this.size.getSelected().maxRange, TunnellerHack.this.length), i = 0; i <= maxOffset; ++i) {
                    final BlockPos pos2 = pos.offset(TunnellerHack.this.direction.getOpposite(), i);
                    if (BlockUtils.getBlock(pos2) instanceof BlockLiquid) {
                        this.liquids.add(pos2);
                    }
                }
                if (BlockUtils.getState(pos).isFullBlock()) {
                    continue;
                }
                final BlockPos pos3 = pos.offset(TunnellerHack.this.direction);
                if (BlockUtils.getBlock(pos3) instanceof BlockLiquid) {
                    this.liquids.add(pos3);
                }
                if (pos.getY() != maxY) {
                    continue;
                }
                final BlockPos pos4 = pos.up();
                if (!(BlockUtils.getBlock(pos4) instanceof BlockLiquid)) {
                    continue;
                }
                this.liquids.add(pos4);
            }
            if (this.liquids.isEmpty()) {
                return false;
            }
            ChatUtils.error("The tunnel is flooded, cannot continue.");
            GL11.glNewList(TunnellerHack.this.displayLists[3], 4864);
            final AxisAlignedBB box = new AxisAlignedBB(0.1, 0.1, 0.1, 0.9, 0.9, 0.9);
            GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.5f);
            for (final BlockPos pos5 : this.liquids) {
                GL11.glPushMatrix();
                GL11.glTranslated((double)pos5.getX(), (double)pos5.getY(), (double)pos5.getZ());
                GL11.glBegin(1);
                RenderUtils.drawOutlinedBox(box);
                GL11.glEnd();
                GL11.glPopMatrix();
            }
            GL11.glEndList();
            return true;
        }
        
        @Override
        public void run() {
            final BlockPos player = new BlockPos((Entity)WMinecraft.getPlayer());
            final KeyBinding forward = TunnellerHack.mc.gameSettings.keyBindForward;
            final Vec3d diffVec = new Vec3d((Vec3i)player.subtract((Vec3i)TunnellerHack.this.start));
            final Vec3d dirVec = new Vec3d(TunnellerHack.this.direction.getDirectionVec());
            final double dotProduct = diffVec.dotProduct(dirVec);
            final BlockPos pos1 = TunnellerHack.this.start.offset(TunnellerHack.this.direction, (int)dotProduct);
            if (!player.equals((Object)pos1)) {
                RotationUtils.faceVectorForWalking(this.toVec3d(pos1));
                KeyBindingUtils.setPressed(forward, true);
                return;
            }
            final BlockPos pos2 = TunnellerHack.this.start.offset(TunnellerHack.this.direction, Math.max(0, TunnellerHack.this.length - 10));
            if (!player.equals((Object)pos2)) {
                RotationUtils.faceVectorForWalking(this.toVec3d(pos2));
                KeyBindingUtils.setPressed(forward, true);
                WMinecraft.getPlayer().setSprinting(true);
                return;
            }
            final BlockPos pos3 = TunnellerHack.this.start.offset(TunnellerHack.this.direction, TunnellerHack.this.length + 1);
            RotationUtils.faceVectorForWalking(this.toVec3d(pos3));
            KeyBindingUtils.setPressed(forward, false);
            WMinecraft.getPlayer().setSprinting(false);
            if (this.disableTimer > 0) {
                --this.disableTimer;
                return;
            }
            TunnellerHack.this.setEnabled(false);
        }
        
        private Vec3d toVec3d(final BlockPos pos) {
            return new Vec3d((Vec3i)pos).addVector(0.5, 0.5, 0.5);
        }
    }
    
    private class PlaceTorchTask extends Task
    {
        private BlockPos lastTorch;
        private BlockPos nextTorch;
        
        private PlaceTorchTask() {
            this.nextTorch = TunnellerHack.this.start;
        }
        
        @Override
        public boolean canRun() {
            if (!TunnellerHack.this.torches.isChecked()) {
                this.lastTorch = null;
                this.nextTorch = new BlockPos((Entity)WMinecraft.getPlayer());
                GL11.glNewList(TunnellerHack.this.displayLists[4], 4864);
                GL11.glEndList();
                return false;
            }
            if (this.lastTorch != null) {
                this.nextTorch = this.lastTorch.offset(TunnellerHack.this.direction, TunnellerHack.this.size.getSelected().torchDistance);
            }
            GL11.glNewList(TunnellerHack.this.displayLists[4], 4864);
            GL11.glColor4f(1.0f, 1.0f, 0.0f, 0.5f);
            final Vec3d torchVec = new Vec3d((Vec3i)this.nextTorch).addVector(0.5, 0.0, 0.5);
            RenderUtils.drawArrow(torchVec, torchVec.addVector(0.0, 0.5, 0.0));
            GL11.glEndList();
            final BlockPos base = TunnellerHack.this.start.offset(TunnellerHack.this.direction, TunnellerHack.this.length);
            return TunnellerHack.this.getDistance(TunnellerHack.this.start, base) > TunnellerHack.this.getDistance(TunnellerHack.this.start, this.nextTorch) && Blocks.TORCH.canPlaceBlockAt((World)WMinecraft.getWorld(), this.nextTorch);
        }
        
        @Override
        public void run() {
            if (!this.equipTorch()) {
                ChatUtils.error("Out of torches.");
                TunnellerHack.this.setEnabled(false);
                return;
            }
            KeyBindingUtils.setPressed(TunnellerHack.mc.gameSettings.keyBindSneak, true);
            BlockUtils.placeBlockSimple(this.nextTorch);
            if (BlockUtils.getBlock(this.nextTorch) instanceof BlockTorch) {
                this.lastTorch = this.nextTorch;
            }
        }
        
        private boolean equipTorch() {
            for (int slot = 0; slot < 9; ++slot) {
                final ItemStack stack = WMinecraft.getPlayer().inventory.getStackInSlot(slot);
                if (stack != null && !stack.isEmpty()) {
                    if (stack.getItem() instanceof ItemBlock) {
                        final Block block = Block.getBlockFromItem(stack.getItem());
                        if (block instanceof BlockTorch) {
                            WMinecraft.getPlayer().inventory.currentItem = slot;
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }
    
    private enum TunnelSize
    {
        SIZE_1X2("1x2", new Vec3i(0, 1, 0), new Vec3i(0, 0, 0), 4, 13), 
        SIZE_3X3("3x3", new Vec3i(1, 2, 0), new Vec3i(-1, 0, 0), 4, 11);
        
        private final String name;
        private final Vec3i from;
        private final Vec3i to;
        private final int maxRange;
        private final int torchDistance;
        
        private TunnelSize(final String name, final Vec3i from, final Vec3i to, final int maxRange, final int torchDistance) {
            this.name = name;
            this.from = from;
            this.to = to;
            this.maxRange = maxRange;
            this.torchDistance = torchDistance;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
}
