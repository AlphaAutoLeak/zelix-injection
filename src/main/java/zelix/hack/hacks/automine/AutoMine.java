package zelix.hack.hacks.automine;

import net.minecraft.client.*;
import zelix.hack.*;
import zelix.utils.system.*;
import zelix.utils.hooks.visual.*;
import zelix.*;
import zelix.managers.*;
import zelix.hack.hacks.*;
import zelix.utils.*;
import java.util.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.entity.*;
import net.minecraft.block.*;
import net.minecraft.client.multiplayer.*;
import java.lang.reflect.*;
import org.lwjgl.opengl.*;
import net.minecraft.init.*;
import net.minecraftforge.client.event.*;
import net.minecraft.util.math.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import zelix.value.*;

public class AutoMine extends Hack
{
    private Minecraft mc;
    public static BooleanValue showPath;
    public static NumberValue delay;
    public static NumberValue refreshdelay;
    public static NumberValue YCord;
    public static ModeValue mode;
    public BooleanValue dia;
    public BooleanValue gold;
    public BooleanValue iron;
    public BooleanValue lapis;
    public BooleanValue emerald;
    public BooleanValue coal;
    public BooleanValue redstone;
    public BooleanValue quartz;
    private ArrayList<Vec3> path;
    public static LinkedList<BlockPos> blocks;
    public static BlockPos readyBreakBlock;
    public static volatile boolean start;
    public static boolean PathReady;
    public static boolean tryTwo;
    public static boolean shouldPlaceBlock;
    private TimeHelper timeHelper;
    private TimeHelper timeHelper2;
    private TimeHelper timeHelper3;
    private TimeHelper timeHelper4;
    private int count;
    public static String keyDown;
    
    public AutoMine() {
        super("AutoMine", HackCategory.PLAYER);
        this.mc = Minecraft.getMinecraft();
        this.dia = new BooleanValue("Diamond", Boolean.valueOf(true));
        this.gold = new BooleanValue("Gold", Boolean.valueOf(false));
        this.iron = new BooleanValue("Iron", Boolean.valueOf(false));
        this.lapis = new BooleanValue("Lapis", Boolean.valueOf(false));
        this.emerald = new BooleanValue("Emerald", Boolean.valueOf(false));
        this.coal = new BooleanValue("Coal", Boolean.valueOf(false));
        this.redstone = new BooleanValue("Redstone", Boolean.valueOf(false));
        this.quartz = new BooleanValue("Quartz", Boolean.valueOf(false));
        this.path = new ArrayList<Vec3>();
        this.timeHelper = new TimeHelper();
        this.timeHelper2 = new TimeHelper();
        this.timeHelper3 = new TimeHelper();
        this.timeHelper4 = new TimeHelper();
        this.count = 0;
        this.addValue(AutoMine.mode);
        this.addValue(AutoMine.showPath, AutoMine.delay, AutoMine.YCord);
        this.addValue(this.dia);
        this.addValue(this.gold);
        this.addValue(this.iron);
        this.addValue(this.lapis);
        this.addValue(this.emerald);
        this.addValue(this.coal);
        this.addValue(this.redstone);
        this.addValue(this.quartz);
        this.addValue(AutoMine.refreshdelay);
    }
    
    @Override
    public void onEnable() {
        this.onEnableMod();
    }
    
    @Override
    public void onDisable() {
        this.onDisableMod();
    }
    
    public void onEnableMod() {
        if (!this.hasBlock()) {
            this.onDisableMod();
            ChatUtils.message(EnumChatFormatting.RED + "\u8bf7\u653e\u4e9b\u65b9\u5757\u5728\u7269\u54c1\u680f\uff0c\u4ee5\u4fbf\u4e0a\u5347\u65f6\u57ab\u811a\uff01");
            return;
        }
        this.resetAllOptions();
        final HackManager hackManager = Core.hackManager;
        if (HackManager.getHack("Search").isToggled()) {
            for (final BlockPos pos2 : Search.toRender) {
                final Block bId = BlockUtils.getBlock(pos2);
                if (!(bId instanceof BlockAir) && BlockUtils.getBlockMeta(pos2) == 0 && this.isTarget(pos2)) {
                    final int x = pos2.getX();
                    final int y = pos2.getY();
                    final int z = pos2.getZ();
                    boolean canBreak = true;
                    boolean isLiquid = false;
                    if (BlockUtils.getBlock(new BlockPos(x, y + 1, z)) instanceof BlockGravel) {
                        canBreak = false;
                    }
                    if (!AutoMinePathFinder.isSafeToWalkOn(new BlockPos(x, y - 1, z)) && BlockUtils.getBlock(new BlockPos(x, y - 3, z)) instanceof BlockAir) {
                        canBreak = false;
                    }
                    for (int blockPosY = 1; blockPosY <= 3; ++blockPosY) {
                        if (BlockUtils.getBlock(new BlockPos(x, y - blockPosY, z)) instanceof BlockLiquid) {
                            canBreak = false;
                            isLiquid = true;
                        }
                    }
                    if (!isLiquid) {
                        for (int blockPosY = 1; blockPosY <= 3; ++blockPosY) {
                            if (!(BlockUtils.getBlock(new BlockPos(x, y - blockPosY, z)) instanceof BlockLiquid) && AutoMinePathFinder.isSafeToWalkOn(new BlockPos(x, y - blockPosY, z))) {
                                canBreak = true;
                            }
                        }
                    }
                Label_0414:
                    for (int x2 = x - 2; x2 <= x + 2; ++x2) {
                        for (int y2 = y; y2 <= y + 2; ++y2) {
                            for (int z2 = z - 2; z2 <= z + 2; ++z2) {
                                if (BlockUtils.getBlock(new BlockPos(x2, y2, z2)) instanceof BlockLiquid) {
                                    canBreak = false;
                                    break Label_0414;
                                }
                            }
                        }
                    }
                    if (!canBreak) {
                        continue;
                    }
                    AutoMine.blocks.add(pos2);
                }
            }
        }
        else {
            for (int i = (int)(Wrapper.INSTANCE.mc().player.posX - 50.0); i <= (int)(Wrapper.INSTANCE.mc().player.posX + 50.0); ++i) {
                for (int j = (int)(Wrapper.INSTANCE.mc().player.posZ - 50.0); j <= (int)(Wrapper.INSTANCE.mc().player.posZ + 50.0); ++j) {
                    for (int k = 5; k <= Wrapper.INSTANCE.mc().world.getHeight(); ++k) {
                        final BlockPos pos3 = new BlockPos(i, k, j);
                        final Block bId2 = BlockUtils.getBlock(pos3);
                        if (!(bId2 instanceof BlockAir) && BlockUtils.getBlockMeta(pos3) == 0 && this.isTarget(pos3)) {
                            final int x3 = pos3.getX();
                            final int y3 = pos3.getY();
                            final int z3 = pos3.getZ();
                            boolean canBreak2 = true;
                            boolean isLiquid2 = false;
                            if (BlockUtils.getBlock(new BlockPos(x3, y3 + 1, z3)) instanceof BlockGravel) {
                                canBreak2 = false;
                            }
                            if (!AutoMinePathFinder.isSafeToWalkOn(new BlockPos(x3, y3 - 1, z3)) && BlockUtils.getBlock(new BlockPos(x3, y3 - 3, z3)) instanceof BlockAir) {
                                canBreak2 = false;
                            }
                            for (int blockPosY2 = 1; blockPosY2 <= 3; ++blockPosY2) {
                                if (BlockUtils.getBlock(new BlockPos(x3, y3 - blockPosY2, z3)) instanceof BlockLiquid) {
                                    canBreak2 = false;
                                    isLiquid2 = true;
                                }
                            }
                            if (!isLiquid2) {
                                for (int blockPosY2 = 1; blockPosY2 <= 3; ++blockPosY2) {
                                    if (!(BlockUtils.getBlock(new BlockPos(x3, y3 - blockPosY2, z3)) instanceof BlockLiquid) && AutoMinePathFinder.isSafeToWalkOn(new BlockPos(x3, y3 - blockPosY2, z3))) {
                                        canBreak2 = true;
                                    }
                                }
                            }
                        Label_0880:
                            for (int x4 = x3 - 2; x4 <= x3 + 2; ++x4) {
                                for (int y4 = y3; y4 <= y3 + 2; ++y4) {
                                    for (int z4 = z3 - 2; z4 <= z3 + 2; ++z4) {
                                        if (BlockUtils.getBlock(new BlockPos(x4, y4, z4)) instanceof BlockLiquid) {
                                            canBreak2 = false;
                                            break Label_0880;
                                        }
                                    }
                                }
                            }
                            if (canBreak2) {
                                AutoMine.blocks.add(pos3);
                            }
                        }
                    }
                }
            }
        }
        if (!AutoMine.blocks.isEmpty()) {
            AutoMine.blocks.sort(Comparator.comparingDouble(pos -> BlockUtils.getBlockDistance((float)(Wrapper.INSTANCE.mc().player.posX - pos.getX()), (float)(Wrapper.INSTANCE.mc().player.posY - pos.getY()), (float)(Wrapper.INSTANCE.mc().player.posZ - pos.getZ()))));
            AutoMine.start = true;
        }
        new Thread() {
            @Override
            public void run() {
                while (!AutoMine.start) {
                    if (Wrapper.INSTANCE.mc().world == null) {
                        return;
                    }
                }
                if (Wrapper.INSTANCE.mc().world == null) {
                    return;
                }
                AutoMine.readyBreakBlock = AutoMine.blocks.get(0);
                if (BlockUtils.getNeaestPlayerBlockDistance(AutoMine.readyBreakBlock.getX(), AutoMine.readyBreakBlock.getY(), AutoMine.readyBreakBlock.getZ()) > 2.0f) {
                    final Vec3 topFrom = new Vec3(Wrapper.INSTANCE.mc().player.posX, Wrapper.INSTANCE.mc().player.posY, Wrapper.INSTANCE.mc().player.posZ);
                    final Vec3 to = new Vec3(AutoMine.readyBreakBlock.getX(), AutoMine.readyBreakBlock.getY(), AutoMine.readyBreakBlock.getZ());
                    AutoMine.this.path = AutoMine.this.computePath(topFrom, to, 8);
                    if (AutoMine.this.path.get(AutoMine.this.path.size() - 1).squareDistanceTo(to) > 0.25) {
                        AutoMine.tryTwo = true;
                        final Vec3 twice = AutoMine.this.path.get(AutoMine.this.path.size() - 1);
                        for (final Vec3 vec3 : AutoMine.this.computePath(twice, to, 0)) {
                            AutoMine.this.path.add(vec3);
                        }
                    }
                    AutoMine.tryTwo = false;
                }
                AutoMine.PathReady = true;
            }
        }.start();
    }
    
    public void onDisableMod() {
        this.resetAllOptions();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (AutoMine.start && AutoMine.PathReady) {
            if (AutoMine.shouldPlaceBlock) {
                Wrapper.INSTANCE.mc().player.rotationPitch = 90.0f;
                this.placeBlock();
            }
            if ((AutoMine.readyBreakBlock != null && BlockUtils.getBlock(AutoMine.readyBreakBlock) == Block.getBlockById(0)) || Wrapper.INSTANCE.mc().player.isDead) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            AutoMine.this.onDisableMod();
                            Thread.sleep(500L);
                            AutoMine.this.onEnableMod();
                        }
                        catch (Exception ex) {}
                    }
                }.start();
                return;
            }
            if (Wrapper.INSTANCE.mc().player.motionX == 0.0 && (Wrapper.INSTANCE.mc().player.motionY == -0.1552320045166016 || Wrapper.INSTANCE.mc().player.motionY == -0.0784000015258789) && Wrapper.INSTANCE.mc().player.motionZ == 0.0) {
                if (this.timeHelper2.hasReach(10000.0)) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                AutoMine.this.onDisableMod();
                                Thread.sleep(1000L);
                                AutoMine.this.onEnableMod();
                                AutoMine.keyDown = "W";
                                Thread.sleep(1000L);
                                AutoMine.keyDown = "";
                            }
                            catch (Exception ex) {}
                        }
                    }.start();
                }
            }
            else {
                this.timeHelper2.reset();
            }
            if (!this.timeHelper4.hasReach(AutoMine.delay.getValue())) {
                return;
            }
            final int posX = (int)Wrapper.INSTANCE.mc().player.posX;
            final int posY = (int)Wrapper.INSTANCE.mc().player.posY;
            final int posZ = (int)Wrapper.INSTANCE.mc().player.posZ;
            final BlockPos pos = Wrapper.INSTANCE.mc().objectMouseOver.getBlockPos();
            if (this.path.size() < 1 || this.path.get(this.path.size() - 1).squareDistanceTo(new Vec3(posX, posY, posZ)) < 1.51) {
                for (int x1 = posX - 2; x1 <= posX + 2; ++x1) {
                    for (int y1 = posY - 1; y1 <= posY + 2; ++y1) {
                        for (int z1 = posZ - 2; z1 <= posZ + 2; ++z1) {
                            if (BlockUtils.getBlock(AutoMine.readyBreakBlock) == BlockUtils.getBlock(new BlockPos(x1, y1, z1))) {
                                BlockUtils.faceBlockClient(AutoMine.readyBreakBlock);
                                final EntityPlayerSP player = Wrapper.INSTANCE.mc().player;
                                player.rotationYaw += 0.5f;
                                this.breakBlockWithHand(pos);
                                return;
                            }
                        }
                    }
                }
                if (BlockUtils.getNeaestPlayerBlockDistance(AutoMine.readyBreakBlock.getX(), AutoMine.readyBreakBlock.getY(), AutoMine.readyBreakBlock.getZ()) < 2.0f) {
                    BlockUtils.faceBlockClient(AutoMine.readyBreakBlock);
                    final EntityPlayerSP player2 = Wrapper.INSTANCE.mc().player;
                    player2.rotationYaw += 0.5f;
                    this.breakBlockWithHand(pos);
                    return;
                }
            }
            if (BlockUtils.getBlock(new BlockPos((Entity)Wrapper.INSTANCE.mc().player)) instanceof BlockGravel) {
                this.breakBlockWithHand(pos);
                return;
            }
            final Vec3 vec3 = this.path.get((this.count < this.path.size() - 1) ? this.count : (this.path.size() - 1));
            final Vec3 player = new Vec3(Wrapper.INSTANCE.mc().player.posX, Wrapper.INSTANCE.mc().player.posY + 0.5, Wrapper.INSTANCE.mc().player.posZ);
            if (vec3.squareDistanceTo(player) < 0.4) {
                AutoMine.keyDown = "";
                ++this.count;
            }
            else if (Wrapper.INSTANCE.mc().player.posY == vec3.getY()) {
                faceEntity(vec3.getX(), vec3.getY(), vec3.getZ());
                if (BlockUtils.getNeaestPlayerBlockDistance(pos.getX(), pos.getY(), pos.getZ()) < 1.0f && ((Wrapper.INSTANCE.mc().objectMouseOver != null && BlockUtils.getBlock(Wrapper.INSTANCE.mc().objectMouseOver.getBlockPos()) instanceof BlockSand) || BlockUtils.getBlock(Wrapper.INSTANCE.mc().objectMouseOver.getBlockPos()) instanceof BlockGravel)) {
                    AutoMine.keyDown = "";
                    this.timeHelper.reset();
                }
                else if (this.timeHelper.hasReach(800.0)) {
                    AutoMine.keyDown = "W";
                }
                if (BlockUtils.getNeaestPlayerBlockDistance(pos.getX(), pos.getY(), pos.getZ()) < 1.25) {
                    AutoMine.keyDown = "";
                    this.breakBlockWithHand(pos);
                }
                else {
                    AutoMine.keyDown = "W";
                }
            }
            else if (vec3.getY() > Wrapper.INSTANCE.mc().player.posY) {
                AutoMine.keyDown = "W";
                if (BlockUtils.getBlock(new BlockPos((Entity)Wrapper.INSTANCE.mc().player).up(2)) instanceof BlockAir || BlockUtils.getBlock(new BlockPos((Entity)Wrapper.INSTANCE.mc().player).down()) instanceof BlockAir) {
                    if (!this.hasBlock()) {
                        this.onDisableMod();
                        ChatUtils.message("¡ìc\u7269\u54c1\u680f\u7684\u8e2e\u811a\u65b9\u5757\u6ca1\u4e86\uff01\uff01\uff01\u8d76\u7d27\u8865\u4e0a\uff01");
                        return;
                    }
                    AutoMine.shouldPlaceBlock = true;
                    AutoMine.keyDown = "SPACE";
                }
                else if (!(BlockUtils.getBlock(new BlockPos((Entity)Wrapper.INSTANCE.mc().player).up(2)) instanceof BlockAir) && !(BlockUtils.getBlock(new BlockPos((Entity)Wrapper.INSTANCE.mc().player).down()) instanceof BlockAir)) {
                    faceEntity(vec3.getX(), vec3.getY() + 1.0, vec3.getZ());
                    if (BlockUtils.getNeaestPlayerBlockDistance(pos.getX(), pos.getY(), pos.getZ()) < 2.0f) {
                        this.breakBlockWithHand(pos);
                    }
                    AutoMine.keyDown = "";
                }
            }
            else if (vec3.getY() < Wrapper.INSTANCE.mc().player.posY) {
                AutoMine.keyDown = "";
                faceEntity(vec3.getX(), vec3.getY() - 1.0, vec3.getZ());
                if (BlockUtils.getNeaestPlayerBlockDistance(pos.getX(), pos.getY(), pos.getZ()) > 2.0f) {
                    if (this.timeHelper3.hasReach(1000.0)) {
                        AutoMine.keyDown = "W";
                    }
                }
                else {
                    this.timeHelper3.reset();
                }
                if (BlockUtils.getNeaestPlayerBlockDistance(pos.getX(), pos.getY(), pos.getZ()) < 2.0f) {
                    this.timeHelper3.reset();
                    this.breakBlockWithHand(pos);
                }
            }
            if (AutoMine.shouldPlaceBlock) {
                Wrapper.INSTANCE.mc().player.rotationPitch = 90.0f;
                this.placeBlock();
            }
        }
        else {
            this.count = 0;
        }
    }
    
    @Override
    public void onInputUpdate(final InputUpdateEvent event) {
        if (AutoMine.keyDown.equalsIgnoreCase("")) {
            return;
        }
        final MovementInput input = event.getMovementInput();
        input.moveStrafe = 0.0f;
        input.moveForward = 0.0f;
        if (AutoMine.keyDown.equalsIgnoreCase("W")) {
            final MovementInput movementInput = input;
            ++movementInput.moveForward;
        }
        if (AutoMine.keyDown.equalsIgnoreCase("S")) {
            final MovementInput movementInput2 = input;
            --movementInput2.moveForward;
        }
        if (AutoMine.keyDown.equalsIgnoreCase("A")) {
            final MovementInput movementInput3 = input;
            ++movementInput3.moveStrafe;
        }
        if (AutoMine.keyDown.equalsIgnoreCase("D")) {
            final MovementInput movementInput4 = input;
            --movementInput4.moveStrafe;
        }
        input.jump = AutoMine.keyDown.equalsIgnoreCase("SPACE");
        input.sneak = AutoMine.keyDown.equalsIgnoreCase("SHIFT");
        if (input.sneak) {
            input.moveStrafe *= 0.3;
            input.moveForward *= 0.3;
        }
        final MovementInput movementInput5 = input;
        movementInput5.moveStrafe *= 5.0f;
        final MovementInput movementInput6 = input;
        movementInput6.moveForward *= 5.0f;
    }
    
    public static void faceEntity(final double xp, final double yp, final double zp) {
        final Minecraft mc = Minecraft.getMinecraft();
        final double x = xp - Wrapper.INSTANCE.mc().player.posX;
        final double y = yp - Wrapper.INSTANCE.mc().player.posY;
        final double z = zp - Wrapper.INSTANCE.mc().player.posZ;
        final double d1 = Wrapper.INSTANCE.mc().player.posY + Wrapper.INSTANCE.mc().player.getEyeHeight() - yp - 0.8;
        final double d2 = Math.sqrt(x * x + z * z);
        final float f = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float f2 = (float)(Math.atan2(d1, d2) * 180.0 / 3.141592653589793);
        Wrapper.INSTANCE.mc().player.rotationYaw = f;
        Wrapper.INSTANCE.mc().player.rotationPitch = f2;
    }
    
    public void placeBlock() {
        final BlockPos blockDown = new BlockPos((Entity)Wrapper.INSTANCE.mc().player).down();
        if (!BlockUtils.getBlock(blockDown).getMaterial(zelix.utils.BlockUtils.getState(blockDown)).isReplaceable()) {
            return;
        }
        int newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Wrapper.INSTANCE.mc().player.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ItemBlock && !(((ItemBlock)stack.getItem()).getBlock() instanceof BlockOre)) {
                newSlot = i;
            }
        }
        if (newSlot == -1) {
            return;
        }
        final int oldSlot = Wrapper.INSTANCE.mc().player.inventory.currentItem;
        Wrapper.INSTANCE.mc().player.inventory.currentItem = newSlot;
        AutoMine.shouldPlaceBlock = true;
        final Vec3d eyesPos = new Vec3d(Wrapper.INSTANCE.mc().player.posX, Wrapper.INSTANCE.mc().player.posY + Wrapper.INSTANCE.mc().player.getEyeHeight(), Wrapper.INSTANCE.mc().player.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, j = 0; j < length; ++j) {
            final EnumFacing side = values[j];
            final BlockPos neighbor = blockDown.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3d((Vec3i)blockDown).addVector(0.5, 0.5, 0.5)) < eyesPos.squareDistanceTo(new Vec3d((Vec3i)neighbor).addVector(0.5, 0.5, 0.5)) && BlockUtils.getBlock(neighbor).canCollideCheck(Wrapper.INSTANCE.mc().world.getBlockState(neighbor), false)) {
                final Vec3d hitVec = new Vec3d((Vec3i)neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
                if (eyesPos.squareDistanceTo(hitVec) <= 18.0625) {
                    Wrapper.INSTANCE.mc().player.swingArm(EnumHand.MAIN_HAND);
                    Wrapper.INSTANCE.controller().processRightClickBlock(Wrapper.INSTANCE.mc().player, Wrapper.INSTANCE.mc().world, neighbor, side2, new Vec3d(hitVec.x, hitVec.y, hitVec.z), EnumHand.MAIN_HAND);
                    for (final String fieldName : new String[] { "blockHitDelay", "blockHitDelay" }) {
                        try {
                            final Field f = PlayerControllerMP.class.getDeclaredField(fieldName);
                            f.setAccessible(true);
                            f.set(Wrapper.INSTANCE.controller(), 4);
                        }
                        catch (Exception ex) {}
                    }
                    AutoMine.shouldPlaceBlock = false;
                }
            }
        }
        Wrapper.INSTANCE.mc().player.inventory.currentItem = oldSlot;
    }
    
    public static void glColor(final int color) {
        GL11.glColor4f((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, (color >> 24 & 0xFF) / 255.0f);
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
        if (Blocks.REDSTONE_TORCH.equals(block) || Blocks.LIT_REDSTONE_ORE.equals(block)) {
            return this.redstone.getValue();
        }
        return Blocks.QUARTZ_ORE.equals(block) && this.quartz.getValue();
    }
    
    @Override
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (!this.path.isEmpty() && AutoMine.start) {
            try {
                if (AutoMine.showPath.getValue()) {
                    Search.renderBlock(AutoMine.readyBreakBlock, event);
                    GL11.glPushMatrix();
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    GL11.glShadeModel(7425);
                    GL11.glDisable(3553);
                    GL11.glEnable(2848);
                    GL11.glDisable(2929);
                    GL11.glDisable(2896);
                    GL11.glDepthMask(false);
                    GL11.glHint(3154, 4354);
                    glColor(-131);
                    GL11.glPushMatrix();
                    GL11.glLineWidth(2.5f);
                    GL11.glDisable(3553);
                    GL11.glDisable(2896);
                    GL11.glBlendFunc(770, 771);
                    GL11.glEnable(2848);
                    GL11.glEnable(3042);
                    GL11.glDisable(2929);
                    GL11.glBegin(3);
                    for (final Vec3 pos : this.path) {
                        GL11.glVertex3d(pos.getX() - Wrapper.INSTANCE.mc().player.posX, pos.getY() - Wrapper.INSTANCE.mc().player.posY, pos.getZ() - Wrapper.INSTANCE.mc().player.posZ);
                    }
                    GL11.glEnd();
                    GL11.glEnable(2929);
                    GL11.glDisable(2848);
                    GL11.glDisable(3042);
                    GL11.glEnable(3553);
                    GL11.glEnable(2896);
                    GL11.glPopMatrix();
                    GL11.glDepthMask(true);
                    GL11.glEnable(2929);
                    GL11.glDisable(2848);
                    GL11.glEnable(3553);
                    GL11.glDisable(3042);
                    GL11.glPopMatrix();
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
            catch (Exception ex) {}
        }
    }
    
    private ArrayList<Vec3> computePath(final Vec3 topFrom, final Vec3 to, final int everyDistance) {
        final AutoMinePathFinder pathfinder = new AutoMinePathFinder(topFrom, to);
        pathfinder.compute();
        int i = 0;
        Vec3 lastLoc = null;
        Vec3 lastDashLoc = null;
        final ArrayList<Vec3> path = new ArrayList<Vec3>();
        final ArrayList<Vec3> pathFinderPath = pathfinder.getPath();
        for (final Vec3 pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                }
                path.add(pathElm.addVector(0.5, 0.0, 0.5));
                lastDashLoc = pathElm;
            }
            else {
                boolean canContinue = true;
                Label_0328: {
                    if (pathElm.squareDistanceTo(lastDashLoc) > everyDistance * everyDistance) {
                        canContinue = false;
                    }
                    else {
                        final double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
                        final double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
                        final double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
                        final double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
                        final double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
                        final double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
                        for (int x = (int)smallX; x <= bigX; ++x) {
                            for (int y = (int)smallY; y <= bigY; ++y) {
                                for (int z = (int)smallZ; z <= bigZ; ++z) {
                                    if (!AutoMinePathFinder.checkPositionValidity(x, y, z, true)) {
                                        canContinue = false;
                                        break Label_0328;
                                    }
                                }
                            }
                        }
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            ++i;
        }
        return path;
    }
    
    private void breakBlockWithHand(final BlockPos pos) {
        if (Wrapper.INSTANCE.mc().objectMouseOver != null) {
            if (Wrapper.INSTANCE.mc().objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                final Block block = BlockUtils.getBlock(Wrapper.INSTANCE.mc().objectMouseOver.getBlockPos());
                if (block != null && block != Block.getBlockById(0)) {
                    float ChangSpeed = 1.0f;
                    int Item = -1;
                    for (int SelectItem = 0; SelectItem < 9; ++SelectItem) {
                        final ItemStack stack = Wrapper.INSTANCE.mc().player.inventory.getStackInSlot(SelectItem);
                        if (stack != null) {
                            final float speed = stack.getItem().getDestroySpeed(stack, Block.getStateById(Block.getIdFromBlock(block)));
                            if (speed > ChangSpeed) {
                                ChangSpeed = speed;
                                Item = SelectItem;
                            }
                        }
                    }
                    if (Item != -1) {
                        Wrapper.INSTANCE.mc().player.inventory.currentItem = Item;
                    }
                }
            }
            else if (Wrapper.INSTANCE.mc().objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
                final NonNullList<ItemStack> inventory = (NonNullList<ItemStack>)Wrapper.INSTANCE.mc().player.inventory.mainInventory;
                int currentBest = 0;
                int diamond = -1;
                int iron = -1;
                int stone = -1;
                int wood = -1;
                while (currentBest < 9) {
                    if (inventory.get(currentBest) == null) {
                        ++currentBest;
                    }
                    else {
                        final Item item = ((ItemStack)inventory.get(currentBest)).getItem();
                        if (item instanceof ItemSword) {
                            final int id = Item.getIdFromItem(item);
                            if (id == 276) {
                                diamond = currentBest;
                            }
                            if (id == 267) {
                                iron = currentBest;
                            }
                            if (id == 272) {
                                stone = currentBest;
                            }
                            if (id == 268) {
                                wood = currentBest;
                            }
                        }
                        ++currentBest;
                    }
                }
                final int itemIndex = (diamond >= 0) ? diamond : ((iron >= 0) ? iron : ((stone >= 0) ? stone : ((wood >= 0) ? wood : -1)));
                if (itemIndex != -1) {
                    Wrapper.INSTANCE.mc().player.inventory.currentItem = itemIndex;
                }
            }
        }
        Wrapper.INSTANCE.mc().player.swingArm(EnumHand.MAIN_HAND);
        Wrapper.INSTANCE.controller().onPlayerDamageBlock(pos, getDirectionFromEntityLiving(pos, (EntityLivingBase)Wrapper.INSTANCE.mc().player));
    }
    
    private boolean hasBlock() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Wrapper.INSTANCE.mc().player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBlock && !(Block.getBlockFromItem(stack.getItem()) instanceof BlockOre)) {
                return true;
            }
        }
        return false;
    }
    
    private void resetAllOptions() {
        this.path.clear();
        AutoMine.blocks.clear();
        AutoMine.readyBreakBlock = null;
        AutoMine.start = false;
        AutoMine.PathReady = false;
        AutoMine.shouldPlaceBlock = false;
        this.count = 0;
        AutoMine.tryTwo = false;
        this.timeHelper.reset();
        this.timeHelper2.reset();
        this.timeHelper3.reset();
        this.timeHelper4.reset();
        AutoMine.keyDown = "";
    }
    
    public static EnumFacing getDirectionFromEntityLiving(final BlockPos pos, final EntityLivingBase placer) {
        if (Math.abs(placer.posX - (pos.getX() + 0.5f)) < 2.0 && Math.abs(placer.posZ - (pos.getZ() + 0.5f)) < 2.0) {
            final double d0 = placer.posY + placer.getEyeHeight();
            if (d0 - pos.getY() > 2.0) {
                return EnumFacing.UP;
            }
            if (pos.getY() - d0 > 0.0) {
                return EnumFacing.DOWN;
            }
        }
        return placer.getHorizontalFacing().getOpposite();
    }
    
    static {
        AutoMine.showPath = new BooleanValue("ShowPath", Boolean.valueOf(true));
        AutoMine.delay = new NumberValue("DigDelay", 10.0, 0.0, 1000.0);
        AutoMine.refreshdelay = new NumberValue("RefreshTime(Sec)", 1.0, 0.0, 60.0);
        AutoMine.YCord = new NumberValue("YCord", 5.0, 3.0, 100.0);
        AutoMine.mode = new ModeValue("Mode", new Mode[] { new Mode("Simple", true), new Mode("Stone", false) });
        AutoMine.blocks = new LinkedList<BlockPos>();
        AutoMine.readyBreakBlock = null;
        AutoMine.start = false;
        AutoMine.PathReady = false;
        AutoMine.tryTwo = false;
        AutoMine.shouldPlaceBlock = false;
        AutoMine.keyDown = "";
    }
}
