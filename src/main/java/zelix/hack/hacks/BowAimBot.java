package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.entity.item.*;
import zelix.utils.*;
import java.util.*;

public class BowAimBot extends Hack
{
    public BooleanValue walls;
    public NumberValue yaw;
    public NumberValue FOV;
    public EntityLivingBase target;
    public float rangeAimVelocity;
    
    public BowAimBot() {
        super("BowAimBot", HackCategory.COMBAT);
        this.rangeAimVelocity = 0.0f;
        this.walls = new BooleanValue("ThroughWalls", Boolean.valueOf(false));
        this.yaw = new NumberValue("Yaw", 22.0, 0.0, 50.0);
        this.FOV = new NumberValue("FOV", 90.0, 1.0, 180.0);
        this.addValue(this.walls, this.yaw, this.FOV);
    }
    
    @Override
    public String getDescription() {
        return "Automatically aims your bow at the closest entity.";
    }
    
    @Override
    public void onDisable() {
        this.target = null;
        super.onDisable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        final ItemStack itemStack = Wrapper.INSTANCE.inventory().getCurrentItem();
        if (itemStack == null || !(itemStack.getItem() instanceof ItemBow)) {
            return;
        }
        if (!Wrapper.INSTANCE.mcSettings().keyBindUseItem.isKeyDown()) {
            return;
        }
        this.target = this.getClosestEntity();
        if (this.target == null) {
            return;
        }
        final int rangeCharge = Wrapper.INSTANCE.player().getItemInUseCount();
        this.rangeAimVelocity = rangeCharge / 20;
        this.rangeAimVelocity = (this.rangeAimVelocity * this.rangeAimVelocity + this.rangeAimVelocity * 2.0f) / 3.0f;
        this.rangeAimVelocity = 1.0f;
        if (this.rangeAimVelocity > 1.0f) {
            this.rangeAimVelocity = 1.0f;
        }
        final double posX = this.target.posX - Wrapper.INSTANCE.player().posX;
        final double posY = this.target.posY + this.target.getEyeHeight() - 0.15 - Wrapper.INSTANCE.player().posY - Wrapper.INSTANCE.player().getEyeHeight();
        final double posZ = this.target.posZ - Wrapper.INSTANCE.player().posZ;
        final double y2 = Math.sqrt(posX * posX + posZ * posZ);
        final float g = 0.006f;
        final float tmp = (float)(this.rangeAimVelocity * this.rangeAimVelocity * this.rangeAimVelocity * this.rangeAimVelocity - g * (g * (y2 * y2) + 2.0 * posY * (this.rangeAimVelocity * this.rangeAimVelocity)));
        final float pitch = (float)(-Math.toDegrees(Math.atan((this.rangeAimVelocity * this.rangeAimVelocity - Math.sqrt(tmp)) / (g * y2))));
        Utils.assistFaceEntity((Entity)this.target, (float)(Object)this.yaw.getValue(), 0.0f);
        Wrapper.INSTANCE.player().rotationPitch = pitch;
        super.onClientTick(event);
    }
    
    public boolean check(final EntityLivingBase entity) {
        return !(entity instanceof EntityArmorStand) && !ValidUtils.isValidEntity(entity) && ValidUtils.isNoScreen() && entity != Wrapper.INSTANCE.player() && !entity.isDead && !ValidUtils.isBot(entity) && ValidUtils.isFriendEnemy(entity) && ValidUtils.isInvisible(entity) && ValidUtils.isInAttackFOV(entity, (int)(Object)this.FOV.getValue()) && ValidUtils.isTeam(entity) && ValidUtils.pingCheck(entity) && (this.walls.getValue() || Wrapper.INSTANCE.player().canEntityBeSeen((Entity)entity));
    }
    
    EntityLivingBase getClosestEntity() {
        EntityLivingBase closestEntity = null;
        for (final Object o : Utils.getEntityList()) {
            if (o instanceof EntityLivingBase && !(o instanceof EntityArmorStand)) {
                final EntityLivingBase entity = (EntityLivingBase)o;
                if (!this.check(entity) || (closestEntity != null && Wrapper.INSTANCE.player().getDistanceSq((Entity)entity) >= Wrapper.INSTANCE.player().getDistanceSq((Entity)closestEntity))) {
                    continue;
                }
                closestEntity = entity;
            }
        }
        return closestEntity;
    }
}
