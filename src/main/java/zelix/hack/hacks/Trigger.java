package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.enchantment.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.item.*;
import java.util.*;
import zelix.utils.*;

public class Trigger extends Hack
{
    public BooleanValue autoDelay;
    public BooleanValue advanced;
    public ModeValue mode;
    public NumberValue minCPS;
    public NumberValue maxCPS;
    public EntityLivingBase target;
    public TimerUtils timer;
    
    public Trigger() {
        super("Trigger", HackCategory.COMBAT);
        this.autoDelay = new BooleanValue("AutoDelay", Boolean.valueOf(true));
        this.advanced = new BooleanValue("Advanced", Boolean.valueOf(false));
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("Click", true), new Mode("Attack", false) });
        this.minCPS = new NumberValue("MinCPS", 4.0, 1.0, 20.0);
        this.maxCPS = new NumberValue("MaxCPS", 8.0, 1.0, 20.0);
        this.addValue(this.mode, this.advanced, this.autoDelay, this.minCPS, this.maxCPS);
        this.timer = new TimerUtils();
    }
    
    @Override
    public String getDescription() {
        return "Automatically attacks the entity you're looking at.";
    }
    
    @Override
    public void onDisable() {
        this.target = null;
        AutoShield.block(false);
        super.onDisable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        this.updateTarget();
        this.attackTarget(this.target);
        super.onClientTick(event);
    }
    
    void attackTarget(final EntityLivingBase target) {
        if (this.check(target)) {
            if (this.autoDelay.getValue()) {
                if (Wrapper.INSTANCE.player().getCooledAttackStrength(0.0f) == 1.0f) {
                    this.processAttack(target, false);
                }
            }
            else {
                final int currentCPS = Utils.random((int)(Object)this.minCPS.getValue(), (int)(Object)this.maxCPS.getValue());
                if (this.timer.isDelay(1000 / currentCPS)) {
                    this.processAttack(target, true);
                    this.timer.setLastMS();
                }
            }
            return;
        }
        AutoShield.block(false);
    }
    
    public void processAttack(final EntityLivingBase entity, final boolean packet) {
        AutoShield.block(false);
        final float sharpLevel = EnchantmentHelper.getModifierForCreature(Wrapper.INSTANCE.player().getHeldItemMainhand(), this.target.getCreatureAttribute());
        if (this.mode.getMode("Click").isToggled()) {
            RobotUtils.clickMouse(0);
        }
        else {
            if (packet) {
                Wrapper.INSTANCE.sendPacket((Packet)new CPacketUseEntity((Entity)this.target));
            }
            else {
                Utils.attack((Entity)this.target);
            }
            Utils.swingMainHand();
            if (sharpLevel > 0.0f) {
                Wrapper.INSTANCE.player().onEnchantmentCritical((Entity)this.target);
            }
        }
        AutoShield.block(true);
    }
    
    void updateTarget() {
        final RayTraceResult object = Wrapper.INSTANCE.mc().objectMouseOver;
        if (object == null) {
            return;
        }
        EntityLivingBase entity = null;
        if (this.target != entity) {
            this.target = null;
        }
        if (object.typeOfHit == RayTraceResult.Type.ENTITY) {
            if (object.entityHit instanceof EntityLivingBase) {
                entity = (EntityLivingBase)object.entityHit;
                this.target = entity;
            }
        }
        else if (object.typeOfHit != RayTraceResult.Type.ENTITY && this.advanced.getValue()) {
            entity = this.getClosestEntity();
        }
        if (entity != null) {
            this.target = entity;
        }
    }
    
    EntityLivingBase getClosestEntity() {
        EntityLivingBase closestEntity = null;
        for (final Object o : Utils.getEntityList()) {
            if (o instanceof EntityLivingBase && !(o instanceof EntityArmorStand)) {
                final EntityLivingBase entity = (EntityLivingBase)o;
                if (!this.check(entity) || (closestEntity != null && Wrapper.INSTANCE.player().getDistance((Entity)entity) >= Wrapper.INSTANCE.player().getDistance((Entity)closestEntity))) {
                    continue;
                }
                closestEntity = entity;
            }
        }
        return closestEntity;
    }
    
    public boolean check(final EntityLivingBase entity) {
        if (entity instanceof EntityArmorStand) {
            return false;
        }
        if (ValidUtils.isValidEntity(entity)) {
            return false;
        }
        if (!ValidUtils.isNoScreen()) {
            return false;
        }
        if (entity == Wrapper.INSTANCE.player()) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        if (ValidUtils.isBot(entity)) {
            return false;
        }
        if (!ValidUtils.isFriendEnemy(entity)) {
            return false;
        }
        if (!ValidUtils.isInvisible(entity)) {
            return false;
        }
        if (this.advanced.getValue()) {
            if (!ValidUtils.isInAttackFOV(entity, 50)) {
                return false;
            }
            if (!ValidUtils.isInAttackRange(entity, 4.7f)) {
                return false;
            }
        }
        return ValidUtils.isTeam(entity) && ValidUtils.pingCheck(entity) && Wrapper.INSTANCE.player().canEntityBeSeen((Entity)entity);
    }
}
