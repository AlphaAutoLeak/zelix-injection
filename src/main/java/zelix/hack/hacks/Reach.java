package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.client.event.*;
import zelix.utils.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import java.util.*;
import net.minecraft.util.math.*;

public class Reach extends Hack
{
    static BooleanValue throughBlocks;
    static NumberValue reachMinVal;
    static NumberValue reachMaxVal;
    static BooleanValue bypassMAC;
    public Random r;
    
    public Reach() {
        super("Reach", HackCategory.COMBAT);
        Reach.reachMinVal = new NumberValue("ReachMin", 3.0, 3.0, 6.0);
        Reach.reachMaxVal = new NumberValue("ReachMax", 3.0, 3.0, 6.0);
        Reach.throughBlocks = new BooleanValue("ThroughBlocks", Boolean.valueOf(false));
        this.r = new Random();
        this.addValue(Reach.throughBlocks, Reach.reachMinVal, Reach.reachMaxVal, Reach.bypassMAC);
    }
    
    @Override
    public void onMouse(final MouseEvent event) {
        if (!Reach.throughBlocks.getValue() && Wrapper.INSTANCE.mc().objectMouseOver != null && Wrapper.INSTANCE.mc().objectMouseOver.typeOfHit != null && Wrapper.INSTANCE.mc().objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            return;
        }
        final double range = Reach.reachMinVal.getValue() + this.r.nextDouble() * (Reach.reachMaxVal.getValue() - Reach.reachMinVal.getValue());
        final Object[] mouseOver = getMouseOver(range, 0.0, 0.0f);
        if (mouseOver == null) {
            return;
        }
        final Vec3d lookVec = Wrapper.INSTANCE.player().getLookVec();
        Wrapper.INSTANCE.mc().objectMouseOver = new RayTraceResult((Entity)mouseOver[0], (Vec3d)mouseOver[1]);
        Wrapper.INSTANCE.mc().pointedEntity = (Entity)mouseOver[0];
        super.onMouse(event);
    }
    
    public static Object[] getMouseOver(final double Range, final double bbExpand, final float f) {
        final Entity renderViewEntity = Wrapper.INSTANCE.mc().getRenderViewEntity();
        Entity entity = null;
        if (renderViewEntity == null || Wrapper.INSTANCE.world() == null) {
            return null;
        }
        Wrapper.INSTANCE.mc().mcProfiler.startSection("pick");
        final Vec3d positionEyes = renderViewEntity.getPositionEyes(0.0f);
        final Vec3d renderViewEntityLook = renderViewEntity.getLook(0.0f);
        final Vec3d vector = positionEyes.addVector(renderViewEntityLook.x * Range, renderViewEntityLook.y * Range, renderViewEntityLook.z * Range);
        Vec3d ve = null;
        final List<Entity> entitiesWithinAABB = Wrapper.INSTANCE.world().getEntitiesWithinAABBExcludingEntity(renderViewEntity, renderViewEntity.getEntityBoundingBox().expand(renderViewEntityLook.x * Range, renderViewEntityLook.y * Range, renderViewEntityLook.z * Range).expand(1.0, 1.0, 1.0));
        double range = Range;
        for (int i = 0; i < entitiesWithinAABB.size(); ++i) {
            final Entity e = entitiesWithinAABB.get(i);
            if (e.canBeCollidedWith()) {
                final float size = e.getCollisionBorderSize();
                AxisAlignedBB bb = e.getEntityBoundingBox().expand((double)size, (double)size, (double)size);
                bb = bb.expand(bbExpand, bbExpand, bbExpand);
                final RayTraceResult objectPosition = bb.calculateIntercept(positionEyes, vector);
                if (bb.contains(positionEyes)) {
                    if (0.0 < range || range == 0.0) {
                        entity = e;
                        ve = ((objectPosition == null) ? positionEyes : objectPosition.hitVec);
                        range = 0.0;
                    }
                }
                else if (objectPosition != null) {
                    final double v;
                    if ((v = positionEyes.distanceTo(objectPosition.hitVec)) < range || range == 0.0) {
                        final boolean b = false;
                        if (e == renderViewEntity.getRidingEntity()) {
                            if (range == 0.0) {
                                entity = e;
                                ve = objectPosition.hitVec;
                            }
                        }
                        else {
                            entity = e;
                            ve = objectPosition.hitVec;
                            range = v;
                        }
                    }
                }
            }
        }
        if (range < Range && !(entity instanceof EntityLivingBase) && !(entity instanceof EntityItemFrame)) {
            entity = null;
        }
        Wrapper.INSTANCE.mc().mcProfiler.endSection();
        if (entity == null || ve == null) {
            return null;
        }
        return new Object[] { entity, ve };
    }
}
