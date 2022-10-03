package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import zelix.utils.system.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.gameevent.*;
import org.lwjgl.input.*;
import zelix.utils.*;
import net.minecraft.network.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.settings.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.client.event.*;
import zelix.utils.hooks.visual.*;
import net.minecraft.world.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.block.*;

public class Teleport extends Hack
{
    public ModeValue mode;
    public BooleanValue math;
    public boolean passPacket;
    private BlockPos teleportPosition;
    private boolean canDraw;
    private int delay;
    float reach;
    
    public Teleport() {
        super("Teleport", HackCategory.PLAYER);
        this.passPacket = false;
        this.teleportPosition = null;
        this.reach = 0.0f;
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("Reach", true), new Mode("Flight", false) });
        this.math = new BooleanValue("Math", Boolean.valueOf(false));
        this.addValue(this.mode, this.math);
    }
    
    @Override
    public String getDescription() {
        return "Teleports you on selected block.";
    }
    
    @Override
    public void onEnable() {
        if (this.mode.getMode("Reach").isToggled()) {
//            this.reach = (float)Wrapper.INSTANCE.player().getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
            this.reach = 3f;
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        if (this.mode.getMode("Flight").isToggled()) {
            Wrapper.INSTANCE.player().noClip = false;
            this.passPacket = false;
            this.teleportPosition = null;
            return;
        }
        this.canDraw = false;
        PlayerControllerUtils.setReach((Entity)Wrapper.INSTANCE.player(), this.reach);
        super.onDisable();
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        return side != Connection.Side.OUT || !this.mode.getMode("Flight").isToggled() || (!(packet instanceof CPacketPlayer) && !(packet instanceof CPacketPlayer.Position) && !(packet instanceof CPacketPlayer.Rotation) && !(packet instanceof CPacketPlayer.PositionRotation)) || this.passPacket;
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (!this.mode.getMode("Flight").isToggled()) {
            if (((!Mouse.isButtonDown(0) && Wrapper.INSTANCE.mc().inGameHasFocus) || !Wrapper.INSTANCE.mc().inGameHasFocus) && Wrapper.INSTANCE.player().getItemInUseCount() == 0) {
                PlayerControllerUtils.setReach((Entity)Wrapper.INSTANCE.player(), 100.0);
                this.canDraw = true;
            }
            else {
                this.canDraw = false;
                PlayerControllerUtils.setReach((Entity)Wrapper.INSTANCE.player(), this.reach);
            }
            if (this.teleportPosition != null && this.delay == 0 && Mouse.isButtonDown(1)) {
                if (this.math.getValue()) {
                    final double[] playerPosition = { Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ };
                    final double[] blockPosition = { this.teleportPosition.getX() + 0.5f, this.teleportPosition.getY() + this.getOffset(BlockUtils.getBlock(this.teleportPosition), this.teleportPosition) + 1.0, this.teleportPosition.getZ() + 0.5f };
                    Utils.teleportToPosition(playerPosition, blockPosition, 0.25, 0.0, true, true);
                    Wrapper.INSTANCE.player().setPosition(blockPosition[0], blockPosition[1], blockPosition[2]);
                    this.teleportPosition = null;
                }
                else {
                    final double x = this.teleportPosition.getX();
                    final double y = this.teleportPosition.getY() + 1;
                    final double z = this.teleportPosition.getZ();
                    Wrapper.INSTANCE.player().setPosition(x, y, z);
                    for (int i = 0; i < 1; ++i) {
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(x, y, z, Wrapper.INSTANCE.player().onGround));
                    }
                }
                this.delay = 5;
            }
            if (this.delay > 0) {
                --this.delay;
            }
            super.onClientTick(event);
            return;
        }
        final RayTraceResult object = Wrapper.INSTANCE.mc().objectMouseOver;
        if (object == null) {
            return;
        }
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        final GameSettings settings = Wrapper.INSTANCE.mcSettings();
        if (!this.passPacket) {
            if (settings.keyBindAttack.isKeyDown() && object.typeOfHit == RayTraceResult.Type.BLOCK) {
                if (BlockUtils.isBlockMaterial(object.getBlockPos(), Blocks.AIR)) {
                    return;
                }
                this.teleportPosition = object.getBlockPos();
                this.passPacket = true;
            }
            return;
        }
        player.noClip = false;
        if (settings.keyBindSneak.isKeyDown() && player.onGround) {
            if (this.math.getValue()) {
                final double[] playerPosition2 = { Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ };
                final double[] blockPosition2 = { this.teleportPosition.getX() + 0.5f, this.teleportPosition.getY() + this.getOffset(BlockUtils.getBlock(this.teleportPosition), this.teleportPosition) + 1.0, this.teleportPosition.getZ() + 0.5f };
                Utils.teleportToPosition(playerPosition2, blockPosition2, 0.25, 0.0, true, true);
                Wrapper.INSTANCE.player().setPosition(blockPosition2[0], blockPosition2[1], blockPosition2[2]);
                this.teleportPosition = null;
            }
            else {
                final double x2 = this.teleportPosition.getX();
                final double y2 = this.teleportPosition.getY() + 1;
                final double z2 = this.teleportPosition.getZ();
                Wrapper.INSTANCE.player().setPosition(x2, y2, z2);
                for (int j = 0; j < 1; ++j) {
                    Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(x2, y2, z2, Wrapper.INSTANCE.player().onGround));
                }
            }
        }
    }
    
    @Override
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (!this.mode.getMode("Flight").isToggled()) {
            return;
        }
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        final GameSettings settings = Wrapper.INSTANCE.mcSettings();
        if (!this.passPacket) {
            player.noClip = true;
            player.fallDistance = 0.0f;
            player.onGround = true;
            player.capabilities.isFlying = false;
            player.motionX = 0.0;
            player.motionY = 0.0;
            player.motionZ = 0.0;
            final float speed = 0.5f;
            if (settings.keyBindJump.isKeyDown()) {
                final EntityPlayerSP entityPlayerSP = player;
                entityPlayerSP.motionY += speed;
            }
            if (settings.keyBindSneak.isKeyDown()) {
                final EntityPlayerSP entityPlayerSP2 = player;
                entityPlayerSP2.motionY -= speed;
            }
            final double d5 = player.rotationPitch + 90.0f;
            double d6 = player.rotationYaw + 90.0f;
            final boolean flag4 = settings.keyBindForward.isKeyDown();
            final boolean flag5 = settings.keyBindBack.isKeyDown();
            final boolean flag6 = settings.keyBindLeft.isKeyDown();
            final boolean flag7 = settings.keyBindRight.isKeyDown();
            if (flag4) {
                if (flag6) {
                    d6 -= 45.0;
                }
                else if (flag7) {
                    d6 += 45.0;
                }
            }
            else if (flag5) {
                d6 += 180.0;
                if (flag6) {
                    d6 += 45.0;
                }
                else if (flag7) {
                    d6 -= 45.0;
                }
            }
            else if (flag6) {
                d6 -= 90.0;
            }
            else if (flag7) {
                d6 += 90.0;
            }
            if (flag4 || flag6 || flag5 || flag7) {
                player.motionX = Math.cos(Math.toRadians(d6));
                player.motionZ = Math.sin(Math.toRadians(d6));
            }
        }
        super.onLivingUpdate(event);
    }
    
    @Override
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (this.mode.getMode("Flight").isToggled()) {
            if (this.teleportPosition == null) {
                return;
            }
            if (this.teleportPosition.getY() == new BlockPos((Entity)Wrapper.INSTANCE.player()).down().getY()) {
                RenderUtils.drawBlockESP(this.teleportPosition, 1.0f, 0.0f, 1.0f);
                return;
            }
            RenderUtils.drawBlockESP(this.teleportPosition, 1.0f, 0.0f, 0.0f);
        }
        else {
            final RayTraceResult object = Wrapper.INSTANCE.mc().objectMouseOver;
            if (object == null) {
                return;
            }
            if (object.getBlockPos() != null && this.canDraw) {
                for (float offset = -2.0f; offset < 18.0f; ++offset) {
                    final double[] mouseOverPos = { object.getBlockPos().getX(), object.getBlockPos().getY() + offset, object.getBlockPos().getZ() };
                    final BlockPos blockBelowPos = new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]);
                    final Block blockBelow = BlockUtils.getBlock(blockBelowPos);
                    if (this.canRenderBox(mouseOverPos)) {
                        RenderUtils.drawBlockESP(new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]), 1.0f, 0.0f, 1.0f);
                        if (Wrapper.INSTANCE.mc().inGameHasFocus) {
                            this.teleportPosition = blockBelowPos;
                            break;
                        }
                        this.teleportPosition = null;
                    }
                }
            }
            else if (object.entityHit != null) {
                for (float offset = -2.0f; offset < 18.0f; ++offset) {
                    final double[] mouseOverPos = { object.entityHit.posX, object.entityHit.posY + offset, object.entityHit.posZ };
                    final BlockPos blockBelowPos = new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]);
                    final Block blockBelow = BlockUtils.getBlock(blockBelowPos);
                    if (this.canRenderBox(mouseOverPos)) {
                        RenderUtils.drawBlockESP(new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]), 1.0f, 0.0f, 1.0f);
                        if (Wrapper.INSTANCE.mc().inGameHasFocus) {
                            this.teleportPosition = blockBelowPos;
                            break;
                        }
                        this.teleportPosition = null;
                    }
                }
            }
            else {
                this.teleportPosition = null;
            }
            super.onRenderWorldLast(event);
        }
    }
    
    public boolean canRenderBox(final double[] mouseOverPos) {
        boolean canTeleport = false;
        final Block blockBelowPos = BlockUtils.getBlock(new BlockPos(mouseOverPos[0], mouseOverPos[1] - 1.0, mouseOverPos[2]));
        final Block blockPos = BlockUtils.getBlock(new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]));
        final Block blockAbovePos = BlockUtils.getBlock(new BlockPos(mouseOverPos[0], mouseOverPos[1] + 1.0, mouseOverPos[2]));
        final boolean validBlockBelow = blockBelowPos.getCollisionBoundingBox(BlockUtils.getState(new BlockPos(mouseOverPos[0], mouseOverPos[1] - 1.0, mouseOverPos[2])), (IBlockAccess)Wrapper.INSTANCE.world(), new BlockPos(mouseOverPos[0], mouseOverPos[1] - 1.0, mouseOverPos[2])) != null;
        final boolean validBlock = this.isValidBlock(blockPos);
        final boolean validBlockAbove = this.isValidBlock(blockAbovePos);
        if (validBlockBelow && validBlock && validBlockAbove) {
            canTeleport = true;
        }
        return canTeleport;
    }
    
    public double getOffset(final Block block, final BlockPos pos) {
        final IBlockState state = BlockUtils.getState(pos);
        double offset = 0.0;
        if (block instanceof BlockSlab && !((BlockSlab)block).isDouble()) {
            offset -= 0.5;
        }
        else if (block instanceof BlockEndPortalFrame) {
            offset -= 0.20000000298023224;
        }
        else if (block instanceof BlockBed) {
            offset -= 0.4399999976158142;
        }
        else if (block instanceof BlockCake) {
            offset -= 0.5;
        }
        else if (block instanceof BlockDaylightDetector) {
            offset -= 0.625;
        }
        else if (block instanceof BlockRedstoneComparator || block instanceof BlockRedstoneRepeater) {
            offset -= 0.875;
        }
        else if (block instanceof BlockChest || block == Blocks.ENDER_CHEST) {
            offset -= 0.125;
        }
        else if (block instanceof BlockLilyPad) {
            offset -= 0.949999988079071;
        }
        else if (block == Blocks.SNOW_LAYER) {
            offset -= 0.875;
            offset += 0.125f * ((int)state.getValue((IProperty)BlockSnow.LAYERS) - 1);
        }
        else if (this.isValidBlock(block)) {
            --offset;
        }
        return offset;
    }
    
    public boolean isValidBlock(final Block block) {
        return block == Blocks.PORTAL || block == Blocks.SNOW_LAYER || block instanceof BlockTripWireHook || block instanceof BlockTripWire || block instanceof BlockDaylightDetector || block instanceof BlockRedstoneComparator || block instanceof BlockRedstoneRepeater || block instanceof BlockSign || block instanceof BlockAir || block instanceof BlockPressurePlate || block instanceof BlockTallGrass || block instanceof BlockFlower || block instanceof BlockMushroom || block instanceof BlockDoublePlant || block instanceof BlockReed || block instanceof BlockSapling || block == Blocks.CARROTS || block == Blocks.WHEAT || block == Blocks.NETHER_WART || block == Blocks.POTATOES || block == Blocks.PUMPKIN_STEM || block == Blocks.MELON_STEM || block == Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE || block == Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE || block == Blocks.REDSTONE_WIRE || block instanceof BlockTorch || block instanceof BlockRedstoneTorch || block == Blocks.LEVER || block instanceof BlockButton;
    }
}
