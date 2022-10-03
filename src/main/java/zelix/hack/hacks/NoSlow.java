package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.client.event.*;
import org.lwjgl.input.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.client.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;

public class NoSlow extends Hack
{
    TimerUtils timer;
    ModeValue mode;
    ModeValue action;
    BooleanValue sendpacket;
    
    public NoSlow() {
        super("NoSlow", HackCategory.MOVEMENT);
        this.timer = new TimerUtils();
        this.sendpacket = new BooleanValue("SendPacket", Boolean.valueOf(true));
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("AAC1", false), new Mode("AAC2", true), new Mode("HytPit", false), new Mode("HYT 4V4", false) });
        this.action = new ModeValue("ActionMode", new Mode[] { new Mode("ABORT", false), new Mode("START", true), new Mode("STOP", false), new Mode("RELEASE", false) });
        this.addValue(this.sendpacket, this.mode, this.action);
    }
    
    @Override
    public void onInputUpdate(final InputUpdateEvent event) {
        Utils.nullCheck();
        if (Wrapper.INSTANCE.player().isHandActive() && !Wrapper.INSTANCE.player().isRiding()) {
            final MovementInput movementInput = event.getMovementInput();
            movementInput.moveStrafe *= 5.0f;
            final MovementInput movementInput2 = event.getMovementInput();
            movementInput2.moveForward *= 5.0f;
        }
        if (this.mode.getMode("HYT 4V4").isToggled() && Mouse.isButtonDown(1) && AimAssist.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword) {
            AimAssist.mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(new BlockPos(-1, -1, -1), Minecraft.getMinecraft().objectMouseOver.sideHit, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        }
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (!this.sendpacket.getValue()) {
            return;
        }
        if (this.mode.getMode("AAC1").isToggled()) {
            if (Wrapper.INSTANCE.player().isHandActive() && !Wrapper.INSTANCE.player().isRiding() && MoveUtils.isMoving()) {
                if (event.phase == TickEvent.Phase.START) {
                    if (Wrapper.INSTANCE.player().onGround || MoveUtils.isOnGround(0.5)) {
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                    }
                }
                else if (event.phase == TickEvent.Phase.END && this.timer.delay(65.0f)) {
                    Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(new BlockPos(-1, -1, -1), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                    this.timer.reset();
                }
            }
        }
        else if (this.mode.getMode("AAC2").isToggled()) {
            if (Wrapper.INSTANCE.player().isHandActive() && !Wrapper.INSTANCE.player().isRiding() && MoveUtils.isMoving()) {
                if (event.phase == TickEvent.Phase.START) {
                    if (this.action.getMode("ABORT").isToggled()) {
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                    }
                    else if (this.action.getMode("START").isToggled()) {
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                    }
                    else if (this.action.getMode("STOP").isToggled()) {
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                    }
                    else if (this.action.getMode("RELEASE").isToggled()) {
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                    }
                }
                else if (event.phase == TickEvent.Phase.END) {
                    Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(new BlockPos(-1, -1, -1), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                }
            }
        }
        else if (this.mode.getMode("HytPit").isToggled()) {}
        super.onPlayerTick(event);
    }
}
