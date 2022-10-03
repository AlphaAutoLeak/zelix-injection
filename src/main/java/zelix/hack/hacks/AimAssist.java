package zelix.hack.hacks;

import net.minecraft.client.*;
import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import org.lwjgl.input.*;
import net.minecraft.entity.*;
import java.util.concurrent.*;
import net.minecraft.util.math.*;
import net.minecraft.client.entity.*;
import java.util.*;
import net.minecraft.entity.item.*;
import zelix.utils.*;

public class AimAssist extends Hack
{
    public static Minecraft mc;
    public static NumberValue speed;
    public static NumberValue compliment;
    public static NumberValue fov;
    public static NumberValue distance;
    public static NumberValue extradistance;
    public static ModeValue priority;
    public static BooleanValue clickAim;
    public static BooleanValue weaponOnly;
    public static BooleanValue breakBlocks;
    public static BooleanValue blatantMode;
    public static BooleanValue walls;
    public EntityLivingBase target;
    
    public AimAssist() {
        super("AimAssist", HackCategory.COMBAT);
        this.addValue(AimAssist.priority = new ModeValue("Priority", new Mode[] { new Mode("Closest", true), new Mode("Health", false) }));
        this.addValue(AimAssist.speed = new NumberValue("Speed 1", 45.0, 5.0, 100.0));
        this.addValue(AimAssist.compliment = new NumberValue("Speed 2", 15.0, 2.0, 97.0));
        this.addValue(AimAssist.fov = new NumberValue("FOV", 90.0, 15.0, 360.0));
        this.addValue(AimAssist.distance = new NumberValue("Distance", 4.5, 1.0, 10.0));
        this.addValue(AimAssist.extradistance = new NumberValue("Extra-Distance", 0.0, 0.0, 100.0));
        this.addValue(AimAssist.clickAim = new BooleanValue("ClickAim", Boolean.valueOf(true)));
        this.addValue(AimAssist.walls = new BooleanValue("ThroughWalls", Boolean.valueOf(false)));
        this.addValue(AimAssist.breakBlocks = new BooleanValue("Break blocks", Boolean.valueOf(true)));
        this.addValue(AimAssist.weaponOnly = new BooleanValue("Weapon only", Boolean.valueOf(false)));
        this.addValue(AimAssist.blatantMode = new BooleanValue("Blatant mode", Boolean.valueOf(false)));
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        this.getTarget();
        if (AimAssist.breakBlocks.getValue() && AimAssist.mc.objectMouseOver != null) {
            final BlockPos p = AimAssist.mc.objectMouseOver.getBlockPos();
            if (p != null) {
                final Block bl = AimAssist.mc.world.getBlockState(p).getBlock();
                if (bl != Blocks.AIR && !(bl instanceof BlockLiquid) && bl instanceof Block) {
                    return;
                }
            }
        }
        if (!AimAssist.weaponOnly.getValue() || Utils.isPlayerHoldingWeapon()) {
            if (AimAssist.clickAim.getValue()) {
                if (Mouse.isButtonDown(0) && this.target != null) {
                    if (AimAssist.blatantMode.getValue()) {
                        Utils.Player.aim((Entity)this.target, 0.0f, false);
                    }
                    else {
                        final double n = Utils.Player.fovFromEntity((Entity)this.target);
                        if (n > 1.0 || n < -1.0) {
                            final double complimentSpeed = n * (ThreadLocalRandom.current().nextDouble(AimAssist.compliment.getValue() - 1.47328, AimAssist.compliment.getValue() + 2.48293) / 100.0);
                            final double val2 = complimentSpeed + ThreadLocalRandom.current().nextDouble(AimAssist.speed.getValue() - 4.723847, AimAssist.speed.getValue());
                            final float val3 = (float)(-(complimentSpeed + n / (101.0 - (float)ThreadLocalRandom.current().nextDouble(AimAssist.speed.getValue() - 4.723847, AimAssist.speed.getValue()))));
                            final EntityPlayerSP player = AimAssist.mc.player;
                            player.rotationYaw += val3;
                        }
                    }
                }
            }
            else if (this.target != null) {
                if (AimAssist.blatantMode.getValue()) {
                    Utils.Player.aim((Entity)this.target, 0.0f, false);
                }
                else {
                    final double n = Utils.Player.fovFromEntity((Entity)this.target);
                    if (n > 1.0 || n < -1.0) {
                        final double complimentSpeed = n * (ThreadLocalRandom.current().nextDouble(AimAssist.compliment.getValue() - 1.47328, AimAssist.compliment.getValue() + 2.48293) / 100.0);
                        final double val2 = complimentSpeed + ThreadLocalRandom.current().nextDouble(AimAssist.speed.getValue() - 4.723847, AimAssist.speed.getValue());
                        final float val3 = (float)(-(complimentSpeed + n / (101.0 - (float)ThreadLocalRandom.current().nextDouble(AimAssist.speed.getValue() - 4.723847, AimAssist.speed.getValue()))));
                        final EntityPlayerSP player2 = AimAssist.mc.player;
                        player2.rotationYaw += val3;
                    }
                }
            }
        }
        this.target = null;
    }
    
    public void getTarget() {
        for (final Object object : Utils.getEntityList()) {
            if (!(object instanceof EntityLivingBase)) {
                continue;
            }
            final EntityLivingBase entity = (EntityLivingBase)object;
            if (!this.check(entity)) {
                continue;
            }
            this.target = entity;
        }
    }
    
    public boolean check(final EntityLivingBase entity) {
        return !(entity instanceof EntityArmorStand) && !ValidUtils.isValidEntity(entity) && ValidUtils.isNoScreen() && entity != Wrapper.INSTANCE.player() && !entity.isDead && !ValidUtils.isBot(entity) && ValidUtils.isFriendEnemy(entity) && ValidUtils.isInvisible(entity) && ValidUtils.isInAttackFOV(entity, (int)(Object)AimAssist.fov.getValue()) && ValidUtils.isInAttackRange(entity, (float)(Object)AimAssist.distance.getValue() + (float)(Object)AimAssist.extradistance.getValue()) && ValidUtils.isTeam(entity) && ValidUtils.pingCheck(entity) && this.isPriority(entity) && (AimAssist.walls.getValue() || Wrapper.INSTANCE.player().canEntityBeSeen((Entity)entity));
    }
    
    boolean isPriority(final EntityLivingBase entity) {
        return (AimAssist.priority.getMode("Closest").isToggled() && ValidUtils.isClosest(entity, this.target)) || (AimAssist.priority.getMode("Health").isToggled() && ValidUtils.isLowHealth(entity, this.target));
    }
    
    static {
        AimAssist.mc = Minecraft.getMinecraft();
    }
}
