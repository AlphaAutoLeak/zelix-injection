package zelix.hack.hacks;

import net.minecraft.util.math.*;
import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.monster.*;
import zelix.utils.*;

public class TpAura extends Hack
{
    private TimerUtils cps;
    public static TimerUtils timer;
    private ArrayList<Vec3d> path;
    private List<Vec3d>[] test;
    public int ticks;
    public NumberValue delay;
    public NumberValue range;
    public BooleanValue players;
    public BooleanValue mobs;
    public BooleanValue atkinv;
    public BooleanValue animals;
    public NumberValue maxtarget;
    public NumberValue dashDistance;
    
    public TpAura() {
        super("TpAura", HackCategory.COMBAT);
        this.cps = new TimerUtils();
        this.path = new ArrayList<Vec3d>();
        this.test = (List<Vec3d>[])new ArrayList[50];
        this.delay = new NumberValue("Delay", 4.0, 0.0, 20.0);
        this.range = new NumberValue("Range", 10.0, 10.0, 100.0);
        this.players = new BooleanValue("Players", Boolean.valueOf(true));
        this.atkinv = new BooleanValue("Invisible", Boolean.valueOf(true));
        this.mobs = new BooleanValue("Mobs", Boolean.valueOf(true));
        this.animals = new BooleanValue("Animals", Boolean.valueOf(false));
        this.maxtarget = new NumberValue("MaxTarget", 3.0, 1.0, 10.0);
        this.dashDistance = new NumberValue("DashDistance", 5.0, 2.0, 10.0);
        this.addValue(this.players, this.animals, this.mobs, this.atkinv, this.delay, this.dashDistance, this.maxtarget, this.range);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        ++this.ticks;
        if (this.cps.hasReached((int)(Object)this.delay.getValue() * 100) && this.getTargets().size() > 0) {
            this.test = (List<Vec3d>[])new ArrayList[50];
            for (int i = 0; i < ((this.getTargets().size() > (int)(Object)this.maxtarget.getValue()) ? ((int)(Object)this.maxtarget.getValue()) : this.getTargets().size()); ++i) {
                final EntityLivingBase T = this.getTargets().get(i);
                final Vec3d topFrom = new Vec3d(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ);
                final Vec3d to = new Vec3d(T.posX, T.posY, T.posZ);
                this.path = this.computePath(topFrom, to);
                this.test[i] = this.path;
                if (ValidUtils.isTeam(T)) {
                    for (final Vec3d pathElm : this.path) {
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(pathElm.x, pathElm.y + 0.1451, pathElm.z, true));
                    }
                    Wrapper.INSTANCE.player().swingArm(EnumHand.MAIN_HAND);
                    Wrapper.INSTANCE.controller().attackEntity((EntityPlayer)Wrapper.INSTANCE.player(), (Entity)T);
                    Collections.reverse(this.path);
                    this.cps.reset();
                    for (final Vec3d pathElm : this.path) {
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(pathElm.x, pathElm.y, pathElm.z + 0.1451, true));
                    }
                }
            }
        }
    }
    
    public List<EntityLivingBase> getTargets() {
        final List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
        for (final Object o3 : Wrapper.INSTANCE.world().getLoadedEntityList()) {
            if (o3 instanceof EntityLivingBase) {
                final EntityLivingBase entity = (EntityLivingBase)o3;
                if (entity == null || entity.isDead || Wrapper.INSTANCE.player().isDead || !(entity instanceof EntityLivingBase) || entity == Wrapper.INSTANCE.player() || Wrapper.INSTANCE.player().getDistance((Entity)entity) > this.range.getValue() || entity.isPlayerSleeping()) {
                    continue;
                }
                if (entity instanceof EntityPlayer && this.players.getValue() && !entity.isInvisible()) {
                    targets.add(entity);
                }
                else if (entity instanceof EntityPlayer && this.players.getValue() && entity.isInvisible() && !this.atkinv.getValue()) {
                    targets.remove(entity);
                }
                else if (entity instanceof EntityPlayer && this.players.getValue() && entity.isInvisible() && this.atkinv.getValue()) {
                    targets.add(entity);
                }
                if (entity instanceof EntityAnimal && this.animals.getValue() && !entity.isInvisible()) {
                    targets.add(entity);
                }
                else if (entity instanceof EntityAnimal && this.animals.getValue() && entity.isInvisible() && !this.atkinv.getValue()) {
                    targets.remove(entity);
                }
                else if (entity instanceof EntityAnimal && this.animals.getValue() && entity.isInvisible() && this.atkinv.getValue()) {
                    targets.add(entity);
                }
                if (entity instanceof EntityMob && this.mobs.getValue() && !entity.isInvisible()) {
                    targets.add(entity);
                }
                else if (entity instanceof EntityMob && this.mobs.getValue() && entity.isInvisible() && !this.atkinv.getValue()) {
                    targets.remove(entity);
                }
                else {
                    if (!(entity instanceof EntityMob) || !this.mobs.getValue() || !entity.isInvisible() || !this.atkinv.getValue()) {
                        continue;
                    }
                    targets.add(entity);
                }
            }
        }
        targets.sort((o1, o2) -> (int)(o1.getDistance((Entity)Wrapper.INSTANCE.player()) * 1000.0f - o2.getDistance((Entity)Wrapper.INSTANCE.player()) * 1000.0f));
        return targets;
    }
    
    private ArrayList<Vec3d> computePath(final Vec3d topFrom, final Vec3d to) {
        final AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
        pathfinder.compute();
        int i = 0;
        Vec3d lastLoc = null;
        Vec3d lastDashLoc = null;
        final ArrayList<Vec3d> path = new ArrayList<Vec3d>();
        final ArrayList<Vec3d> pathFinderPath = pathfinder.getPath();
        for (final Vec3d pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                }
                path.add(pathElm.addVector(0.5, 0.0, 0.5));
                lastDashLoc = pathElm;
            }
            else {
                boolean canContinue = true;
                Label_0348: {
                    if (pathElm.squareDistanceTo(lastDashLoc) > this.dashDistance.getValue() * this.dashDistance.getValue()) {
                        canContinue = false;
                    }
                    else {
                        final double smallX = Math.min(lastDashLoc.x, pathElm.x);
                        final double smallY = Math.min(lastDashLoc.y, pathElm.y);
                        final double smallZ = Math.min(lastDashLoc.z, pathElm.z);
                        final double bigX = Math.max(lastDashLoc.x, pathElm.x);
                        final double bigY = Math.max(lastDashLoc.y, pathElm.y);
                        final double bigZ = Math.max(lastDashLoc.z, pathElm.z);
                        for (int x = (int)smallX; x <= bigX; ++x) {
                            for (int y = (int)smallY; y <= bigY; ++y) {
                                for (int z = (int)smallZ; z <= bigZ; ++z) {
                                    if (!AStarCustomPathFinder.checkPositionValidity(x, y, z, false)) {
                                        canContinue = false;
                                        break Label_0348;
                                    }
                                }
                            }
                        }
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            ++i;
        }
        return path;
    }
    
    static {
        TpAura.timer = new TimerUtils();
    }
}
