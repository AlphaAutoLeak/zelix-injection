package zelix.hack.hacks;

import net.minecraft.network.play.client.*;
import net.minecraft.client.*;
import zelix.hack.*;
import java.util.*;
import zelix.utils.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.network.*;
import net.minecraft.client.entity.*;
import zelix.utils.system.*;
import zelix.utils.hooks.visual.*;

public class Flight extends Hack
{
    Queue<CPacketPlayer> packets;
    List<CPacketPlayer> p;
    private boolean isfly;
    private int stage;
    private int maxstage;
    private boolean jump;
    Minecraft mc;
    private int timer;
    private double y;
    private NumberValue motionY;
    private NumberValue motionXZ;
    private NumberValue delay;
    private NumberValue flytimer;
    private BooleanValue XYZ;
    boolean send;
    public ModeValue mode;
    int ticks;
    
    public Flight() {
        super("Flight", HackCategory.MOVEMENT);
        this.packets = new LinkedList<CPacketPlayer>();
        this.p = new ArrayList<CPacketPlayer>();
        this.stage = 0;
        this.maxstage = 5;
        this.jump = false;
        this.mc = Wrapper.INSTANCE.mc();
        this.timer = 0;
        this.y = 0.0;
        this.send = false;
        this.ticks = 0;
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("Simple", true), new Mode("Dynamic", false), new Mode("Hypixel", false), new Mode("HYT", false) });
        this.motionY = new NumberValue("MotionY", 0.5, 0.42, 6.0);
        this.motionXZ = new NumberValue("MotionXZ", 1.0, 0.0, 10.0);
        this.delay = new NumberValue("Delay", 2.0, 0.0, 10.0);
        this.flytimer = new NumberValue("FlyTimer", 1.0, 0.1, 10.0);
        this.XYZ = new BooleanValue("AntiKick", Boolean.valueOf(true));
        this.addValue(this.mode, this.motionY, this.motionXZ, this.delay, this.flytimer, this.XYZ);
    }
    
    @Override
    public String getDescription() {
        return "Allows you to you fly.";
    }
    
    @Override
    public void onEnable() {
        this.ticks = 0;
        this.y = Wrapper.INSTANCE.player().posY;
        this.timer = 999;
        super.onEnable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        if (this.mode.getMode("Hypixel").isToggled()) {
            player.motionY = 0.0;
            player.setSprinting(true);
            player.onGround = true;
            ++this.ticks;
            if (this.ticks == 2 || this.ticks == 4 || this.ticks == 6 || this.ticks == 8 || this.ticks == 10 || this.ticks == 12 || this.ticks == 14 || this.ticks == 16 || this.ticks == 18 || this.ticks == 20) {
                player.setPosition(player.posX, player.posY + 1.28E-9, player.posZ);
            }
            if (this.ticks == 20) {
                this.ticks = 0;
            }
        }
        else if (this.mode.getMode("Simple").isToggled()) {
            player.capabilities.isFlying = true;
        }
        else if (this.mode.getMode("Dynamic").isToggled()) {
            if (this.XYZ.getValue() && this.mc.player.ticksExisted % 4 == 0) {
                this.mc.player.motionY = -0.03999999910593033;
            }
            final float flyspeed = (float)(Object)this.flytimer.getValue();
            player.jumpMovementFactor = 0.4f;
            player.motionX = 0.0;
            player.motionY = 0.0;
            player.motionZ = 0.0;
            final EntityPlayerSP entityPlayerSP = player;
            entityPlayerSP.jumpMovementFactor *= flyspeed * 3.0f;
            if (Wrapper.INSTANCE.mcSettings().keyBindJump.isKeyDown()) {
                if (this.XYZ.getValue()) {
                    this.mc.player.motionY = ((this.mc.player.ticksExisted % 20 == 0) ? -0.03999999910593033 : flyspeed);
                }
                else {
                    final EntityPlayerSP entityPlayerSP2 = player;
                    entityPlayerSP2.motionY += flyspeed;
                }
            }
        }
        else if (this.mode.getMode("HYT").isToggled()) {
            if (!this.isfly) {
                ++this.timer;
            }
            if (this.timer > this.delay.getValue()) {
                this.timer = 0;
                this.move();
                this.isfly = true;
                this.stage = 0;
            }
            if (this.stage >= 1) {
                this.isfly = false;
                if (this.packets != null && !this.packets.isEmpty()) {
                    Wrapper.INSTANCE.sendPacket((Packet)this.packets.poll());
                }
            }
        }
        super.onClientTick(event);
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (packet instanceof CPacketPlayer && this.mode.getMode("HYT").isToggled()) {
            ChatUtils.message("Packets:" + String.valueOf(this.packets.size()));
            this.packets.add((CPacketPlayer)packet);
            ++this.stage;
            return !this.isfly;
        }
        return true;
    }
    
    @Override
    public void onDisable() {
        if (this.mode.getMode("Simple").isToggled()) {
            Wrapper.INSTANCE.player().capabilities.isFlying = false;
        }
        if (this.mode.getMode("HYT").isToggled() && this.packets != null && !this.packets.isEmpty()) {
            Wrapper.INSTANCE.sendPacket((Packet)this.packets.poll());
        }
        this.packets.clear();
        super.onDisable();
    }
    
    private void move() {
        if (Wrapper.INSTANCE.player().posY <= this.y) {
            Wrapper.INSTANCE.player().motionY = this.motionY.getValue();
        }
        else {
            final double dir = Wrapper.INSTANCE.player().rotationYaw / 180.0f * 3.141592653589793;
            if (Wrapper.INSTANCE.player().motionY < 0.0) {
                Wrapper.INSTANCE.player().motionY = -0.05;
            }
            Wrapper.INSTANCE.player().motionX = -Math.sin(dir) * this.motionXZ.getValue();
            Wrapper.INSTANCE.player().motionZ = Math.cos(dir) * this.motionXZ.getValue();
        }
    }
}
