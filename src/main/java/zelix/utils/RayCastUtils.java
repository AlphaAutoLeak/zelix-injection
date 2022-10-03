package zelix.utils;

import net.minecraft.entity.*;
import net.minecraft.util.*;
import com.google.common.base.*;
import java.util.*;
import net.minecraft.util.math.*;

public class RayCastUtils
{
    public static Entity rayCast(final double range, final float yaw, final float pitch) {
        final Vec3d vec3 = Wrapper.INSTANCE.player().getPositionEyes(1.0f);
        boolean flag = false;
        final boolean flag2 = true;
        if (range > 3.0) {
            flag = true;
        }
        final Vec3d vec4 = getVectorForRotation(pitch, yaw);
        final Vec3d vec5 = vec3.addVector(vec4.x * range, vec4.y * range, vec4.z * range);
        Entity pointedEntity = null;
        Vec3d vec6 = null;
        final float f = 1.0f;
        final List<Entity> list = Wrapper.INSTANCE.world().getEntitiesInAABBexcluding(Wrapper.INSTANCE.mc().getRenderViewEntity(), Wrapper.INSTANCE.mc().getRenderViewEntity().getEntityBoundingBox().offset(vec4.x * range, vec4.y * range, vec4.z * range).expand((double)f, (double)f, (double)f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
        double d2 = range;
        for (int i = 0; i < list.size(); ++i) {
            final Entity entity1 = list.get(i);
            final float f2 = entity1.getCollisionBorderSize();
            final AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)f2, (double)f2, (double)f2);
            final RayTraceResult movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec5);
            if (axisalignedbb.contains(vec3)) {
                if (d2 >= 0.0) {
                    pointedEntity = entity1;
                    vec6 = ((movingobjectposition == null) ? vec3 : movingobjectposition.hitVec);
                    d2 = 0.0;
                }
            }
            else if (movingobjectposition != null) {
                final double d3 = vec3.distanceTo(movingobjectposition.hitVec);
                if (d3 < d2 || d2 == 0.0) {
                    final boolean flag3 = false;
                    if (entity1 == Wrapper.INSTANCE.mc().getRenderViewEntity().getRidingEntity() && !flag3) {
                        if (d2 == 0.0) {
                            pointedEntity = entity1;
                            vec6 = movingobjectposition.hitVec;
                        }
                    }
                    else {
                        pointedEntity = entity1;
                        vec6 = movingobjectposition.hitVec;
                        d2 = d3;
                    }
                }
            }
        }
        return pointedEntity;
    }
    
    public static Vec3d getVectorForRotation(final float pitch, final float yaw) {
        final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
        final float f4 = MathHelper.sin(-pitch * 0.017453292f);
        return new Vec3d((double)(f2 * f3), (double)f4, (double)(f * f3));
    }
}
