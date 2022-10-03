package zelix.hack.hacks;

import net.minecraft.client.*;
import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.util.math.*;
import zelix.utils.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import zelix.utils.system.*;
import net.minecraft.util.*;
import java.lang.reflect.*;

public class Speed extends Hack
{
    public ModeValue mode;
    public boolean shouldslow;
    double count;
    int jumps;
    private float air;
    private float ground;
    private float aacSlow;
    public static TimerUtils timer;
    boolean collided;
    boolean lessSlow;
    int spoofSlot;
    double less;
    double stair;
    Number ticks;
    private int offGroundTicks;
    private double speed;
    private double speedvalue;
    private double lastDist;
    public static int stage;
    public static int aacCount;
    TimerUtils aac;
    TimerUtils lastFall;
    TimerUtils lastCheck;
    public static Minecraft mc;
    
    public Speed() {
        super("Speed", HackCategory.MOVEMENT);
        this.shouldslow = false;
        this.count = 0.0;
        this.collided = false;
        this.spoofSlot = 0;
        this.ticks = 1.05f;
        this.aac = new TimerUtils();
        this.lastFall = new TimerUtils();
        this.lastCheck = new TimerUtils();
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("Basic", false), new Mode("OldHypixel", false), new Mode("HYT", false), new Mode("AAC4", true), new Mode("AAC", false), new Mode("VulcanLowHop", false) });
        this.addValue(this.mode);
    }
    
    @Override
    public void onDisable() {
        this.setTickLength(50.0f);
        super.onDisable();
    }
    
    @Override
    public void onEnable() {
        this.offGroundTicks = 0;
    }
    
    @Override
    public String getDescription() {
        return "You move faster.";
    }
    
    @Override
    public void onInputUpdate(final InputUpdateEvent event) {
        Utils.nullCheck();
        if (!this.mode.getMode("HYT").isToggled()) {
            return;
        }
        if (MoveUtils.isMoving() && Speed.mc.player.onGround) {
            this.setTickLength(50.0f / this.ticks.floatValue());
            Speed.mc.player.motionY = 0.2;
            Speed.mc.player.jump();
            MoveUtils.strafe(0.3);
        }
        else {
            MoveUtils.strafe();
        }
        super.onInputUpdate(event);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.mode.getMode("Basic").isToggled()) {
            final boolean boost = Math.abs(Wrapper.INSTANCE.player().rotationYawHead - Wrapper.INSTANCE.player().rotationYaw) < 90.0f;
            if (Wrapper.INSTANCE.player().moveForward > 0.0f && Wrapper.INSTANCE.player().hurtTime < 5) {
                if (Wrapper.INSTANCE.player().onGround) {
                    Wrapper.INSTANCE.player().motionY = 0.405;
                    final float f = Utils.getDirection();
                    final EntityPlayerSP player = Wrapper.INSTANCE.player();
                    player.motionX -= MathHelper.sin(f) * 0.2f;
                    final EntityPlayerSP player2 = Wrapper.INSTANCE.player();
                    player2.motionZ += MathHelper.cos(f) * 0.2f;
                }
                else {
                    final double currentSpeed = Math.sqrt(Wrapper.INSTANCE.player().motionX * Wrapper.INSTANCE.player().motionX + Wrapper.INSTANCE.player().motionZ * Wrapper.INSTANCE.player().motionZ);
                    final double speed = boost ? 1.0064 : 1.001;
                    final double direction = Utils.getDirection();
                    Wrapper.INSTANCE.player().motionX = -Math.sin(direction) * speed * currentSpeed;
                    Wrapper.INSTANCE.player().motionZ = Math.cos(direction) * speed * currentSpeed;
                }
            }
        }
        else if (this.mode.getMode("OldHypixel").isToggled()) {
            if (Wrapper.INSTANCE.player().collidedHorizontally) {
                this.collided = true;
            }
            if (this.collided) {
                Speed.stage = -1;
            }
            if (this.stair > 0.0) {
                this.stair -= 0.25;
            }
            this.less -= ((this.less > 1.0) ? 0.12 : 0.11);
            if (this.less < 0.0) {
                this.less = 0.0;
            }
            if (!BlockUtils.isInLiquid() && MoveUtils.isOnGround(0.01) && PlayerControllerUtils.isMoving2()) {
                this.collided = Wrapper.INSTANCE.player().collidedHorizontally;
                if (Speed.stage >= 0 || this.collided) {
                    Speed.stage = 0;
                    final double motY = 0.407 + MoveUtils.getJumpEffect() * 0.1;
                    if (this.stair == 0.0) {
                        Wrapper.INSTANCE.player().jump();
                        Wrapper.INSTANCE.player().motionY = motY;
                    }
                    ++this.less;
                    if (this.less > 1.0 && !this.lessSlow) {
                        this.lessSlow = true;
                    }
                    else {
                        this.lessSlow = false;
                    }
                    if (this.less > 1.12) {
                        this.less = 1.12;
                    }
                }
            }
            this.speed = this.getHypixelSpeed(Speed.stage) + 0.0331;
            this.speed *= 0.91;
            if (this.stair > 0.0) {
                this.speed *= 0.7 - MoveUtils.getSpeedEffect() * 0.1;
            }
            if (Speed.stage < 0) {
                this.speed = MoveUtils.defaultSpeed();
            }
            if (this.lessSlow) {
                this.speed *= 0.95;
            }
            if (BlockUtils.isInLiquid()) {
                this.speed = 0.55;
            }
            if (Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f) {
                this.setMotion(this.speed);
                ++Speed.stage;
            }
        }
        else if (this.mode.getMode("AAC4").isToggled()) {
            final Minecraft mc = Minecraft.getMinecraft();
            if (Wrapper.INSTANCE.mc().player.moveForward > 0.0f) {
                if (Wrapper.INSTANCE.mc().player.onGround) {
                    Wrapper.INSTANCE.mc().player.jump();
                    this.setMotion(1.61);
                    final EntityPlayerSP player = Wrapper.INSTANCE.mc().player;
                    player.motionX *= 1.0708;
                    final EntityPlayerSP player2 = Wrapper.INSTANCE.mc().player;
                    player2.motionZ *= 1.0708;
                }
                else if (Wrapper.INSTANCE.mc().player.fallDistance > 0.0f && Wrapper.INSTANCE.mc().player.fallDistance < 1.0f) {
                    this.setMotion(0.6);
                }
            }
        }
        else if (this.mode.getMode("AAC").isToggled()) {
            if (Wrapper.INSTANCE.player().fallDistance > 1.2) {
                this.lastFall.reset();
            }
            if (!BlockUtils.isInLiquid() && Wrapper.INSTANCE.player().collidedVertically && MoveUtils.isOnGround(0.01) && (Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f)) {
                Speed.stage = 0;
                Wrapper.INSTANCE.player().jump();
                Wrapper.INSTANCE.player().motionY = 0.4199 + MoveUtils.getJumpEffect();
                if (Speed.aacCount < 4) {
                    ++Speed.aacCount;
                }
            }
            this.speed = this.getAACSpeed(Speed.stage, Speed.aacCount);
            if (Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f) {
                if (BlockUtils.isInLiquid()) {
                    this.speed = 0.075;
                }
                this.setMotion(this.speed);
            }
            if (Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f) {
                ++Speed.stage;
            }
        }
        super.onClientTick(event);
    }
    
    private double getHypixelSpeed(final int stage) {
        double value = MoveUtils.defaultSpeed() + 0.028 * MoveUtils.getSpeedEffect() + MoveUtils.getSpeedEffect() / 15.0;
        final double firstvalue = 0.4145 + MoveUtils.getSpeedEffect() / 12.5;
        final double decr = stage / 500.0 * 2.0;
        if (stage == 0) {
            if (Speed.timer.delay(300.0f)) {
                Speed.timer.reset();
            }
            if (!this.lastCheck.delay(500.0f)) {
                if (!this.shouldslow) {
                    this.shouldslow = true;
                }
            }
            else if (this.shouldslow) {
                this.shouldslow = false;
            }
            value = 0.64 + (MoveUtils.getSpeedEffect() + 0.028 * MoveUtils.getSpeedEffect()) * 0.134;
        }
        else if (stage == 1) {
            value = firstvalue;
        }
        else if (stage >= 2) {
            value = firstvalue - decr;
        }
        if (this.shouldslow || !this.lastCheck.delay(500.0f) || this.collided) {
            value = 0.2;
            if (stage == 0) {
                value = 0.0;
            }
        }
        return Math.max(value, this.shouldslow ? value : (MoveUtils.defaultSpeed() + 0.028 * MoveUtils.getSpeedEffect()));
    }
    
    private void setMotion(final double speed) {
        double forward = Wrapper.INSTANCE.player().movementInput.moveForward;
        double strafe = Wrapper.INSTANCE.player().movementInput.moveStrafe;
        float yaw = Wrapper.INSTANCE.player().rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            Wrapper.INSTANCE.player().motionX = 0.0;
            Wrapper.INSTANCE.player().motionZ = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            Wrapper.INSTANCE.player().motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            Wrapper.INSTANCE.player().motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }
    
    private double getAACSpeed(final int stage, final int jumps) {
        double value = 0.29;
        final double firstvalue = 0.3019;
        final double thirdvalue = 0.0286 - stage / 1000.0;
        if (stage == 0) {
            value = 0.497;
            if (jumps >= 2) {
                value += 0.1069;
            }
            if (jumps >= 3) {
                value += 0.046;
            }
            final Block block = MoveUtils.getBlockUnderPlayer((EntityPlayer)Wrapper.INSTANCE.player(), 0.01);
            if (block instanceof BlockIce || block instanceof BlockPackedIce) {
                value = 0.59;
            }
        }
        else if (stage == 1) {
            value = 0.3031;
            if (jumps >= 2) {
                value += 0.0642;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 2) {
            value = 0.302;
            if (jumps >= 2) {
                value += 0.0629;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 3) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0607;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 4) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.04584;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 5) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.04561;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 6) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0539;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 7) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0517;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 8) {
            value = firstvalue;
            if (MoveUtils.isOnGround(0.05)) {
                value -= 0.002;
            }
            if (jumps >= 2) {
                value += 0.0496;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 9) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0475;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 10) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0455;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 11) {
            value = 0.3;
            if (jumps >= 2) {
                value += 0.045;
            }
            if (jumps >= 3) {
                value += 0.018;
            }
        }
        else if (stage == 12) {
            value = 0.301;
            if (jumps <= 2) {
                Speed.aacCount = 0;
            }
            if (jumps >= 2) {
                value += 0.042;
            }
            if (jumps >= 3) {
                value += thirdvalue + 0.001;
            }
        }
        else if (stage == 13) {
            value = 0.298;
            if (jumps >= 2) {
                value += 0.042;
            }
            if (jumps >= 3) {
                value += thirdvalue + 0.001;
            }
        }
        else if (stage == 14) {
            value = 0.297;
            if (jumps >= 2) {
                value += 0.042;
            }
            if (jumps >= 3) {
                value += thirdvalue + 0.001;
            }
        }
        if (Wrapper.INSTANCE.player().moveForward <= 0.0f) {
            value -= 0.06;
        }
        if (Wrapper.INSTANCE.player().collidedHorizontally) {
            value -= 0.1;
            Speed.aacCount = 0;
        }
        return value;
    }
    
    private void setTickLength(final float tickLength) {
        try {
            final Field fTimer = Minecraft.getMinecraft().getClass().getDeclaredField(Mapping.timer);
            fTimer.setAccessible(true);
            final Field fTickLength = Timer.class.getDeclaredField(Mapping.tickLength);
            fTickLength.setAccessible(true);
            fTickLength.setFloat(fTimer.get(Minecraft.getMinecraft()), tickLength);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
    
    static {
        Speed.timer = new TimerUtils();
        Speed.mc = Wrapper.INSTANCE.mc();
    }
}
