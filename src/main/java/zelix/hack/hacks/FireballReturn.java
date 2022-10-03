package zelix.hack.hacks;

import net.minecraft.entity.projectile.*;
import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import java.util.*;
import net.minecraft.entity.*;
import zelix.utils.*;

public class FireballReturn extends Hack
{
    public NumberValue yaw;
    public NumberValue pitch;
    public NumberValue range;
    public EntityFireball target;
    public TimerUtils timer;
    
    public FireballReturn() {
        super("FireballReturn", HackCategory.COMBAT);
        this.yaw = new NumberValue("Yaw", 25.0, 0.0, 50.0);
        this.pitch = new NumberValue("Pitch", 25.0, 0.0, 50.0);
        this.range = new NumberValue("Range", 10.0, 0.1, 10.0);
        this.addValue(this.yaw, this.pitch, this.range);
        this.timer = new TimerUtils();
    }
    
    @Override
    public String getDescription() {
        return "Beats fireballs when they fly at you.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        this.updateTarget();
        this.attackTarget();
        super.onClientTick(event);
    }
    
    void updateTarget() {
        for (final Object object : Utils.getEntityList()) {
            if (object instanceof EntityFireball) {
                final EntityFireball entity = (EntityFireball)object;
                if (!this.isInAttackRange(entity) || entity.isDead || entity.onGround || !entity.canBeAttackedWithItem()) {
                    continue;
                }
                this.target = entity;
            }
        }
    }
    
    void attackTarget() {
        if (this.target == null) {
            return;
        }
        Utils.assistFaceEntity((Entity)this.target, (float)(Object)this.yaw.getValue(), (float)(Object)this.pitch.getValue());
        final int currentCPS = Utils.random(4, 7);
        if (this.timer.isDelay(1000 / currentCPS)) {
            RobotUtils.clickMouse(0);
            this.timer.setLastMS();
            this.target = null;
        }
    }
    
    public boolean isInAttackRange(final EntityFireball entity) {
        return entity.getDistanceSq((Entity)Wrapper.INSTANCE.player()) <= (float)(Object)this.range.getValue();
    }
}
