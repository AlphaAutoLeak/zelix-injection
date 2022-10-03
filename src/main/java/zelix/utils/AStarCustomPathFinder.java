package zelix.utils;

import java.util.*;
import net.minecraft.util.math.*;
import net.minecraft.client.*;
import net.minecraft.block.*;

public class AStarCustomPathFinder
{
    private Vec3d startVec3;
    private Vec3d endVec3;
    private ArrayList<Vec3d> path;
    private ArrayList<Hub> hubs;
    private ArrayList<Hub> hubsToWork;
    private double minDistanceSquared;
    private boolean nearest;
    private static Vec3d[] flatCardinalDirections;
    
    public AStarCustomPathFinder(final Vec3d startVec3, final Vec3d endVec3) {
        this.path = new ArrayList<Vec3d>();
        this.hubs = new ArrayList<Hub>();
        this.hubsToWork = new ArrayList<Hub>();
        this.minDistanceSquared = 9.0;
        this.nearest = true;
        this.startVec3 = startVec3.addVector(0.0, 0.0, 0.0);
        this.endVec3 = endVec3.addVector(0.0, 0.0, 0.0);
    }
    
    public ArrayList<Vec3d> getPath() {
        return this.path;
    }
    
    public void compute() {
        this.compute(1000, 4);
    }
    
    public void compute(final int loops, final int depth) {
        this.path.clear();
        this.hubsToWork.clear();
        final ArrayList<Vec3d> initPath = new ArrayList<Vec3d>();
        initPath.add(this.startVec3);
        this.hubsToWork.add(new Hub(this.startVec3, null, initPath, this.startVec3.squareDistanceTo(this.endVec3), 0.0, 0.0));
    Label_0330:
        for (int i = 0; i < loops; ++i) {
            Collections.sort(this.hubsToWork, new CompareHub());
            int j = 0;
            if (this.hubsToWork.size() == 0) {
                break;
            }
            for (final Hub hub : new ArrayList<Hub>(this.hubsToWork)) {
                if (++j > depth) {
                    break;
                }
                this.hubsToWork.remove(hub);
                this.hubs.add(hub);
                for (final Vec3d direction : AStarCustomPathFinder.flatCardinalDirections) {
                    final Vec3d loc = hub.getLoc().add(direction);
                    if (checkPositionValidity(loc, false) && this.addHub(hub, loc, 0.0)) {
                        break Label_0330;
                    }
                }
                final Vec3d loc2 = hub.getLoc().addVector(0.0, 1.0, 0.0);
                if (checkPositionValidity(loc2, false) && this.addHub(hub, loc2, 0.0)) {
                    break Label_0330;
                }
                final Vec3d loc3 = hub.getLoc().addVector(0.0, -1.0, 0.0);
                if (checkPositionValidity(loc3, false) && this.addHub(hub, loc3, 0.0)) {
                    break Label_0330;
                }
            }
        }
        if (this.nearest) {
            Collections.sort(this.hubs, new CompareHub());
            this.path = this.hubs.get(0).getPath();
        }
    }
    
    public static boolean checkPositionValidity(final Vec3d loc, final boolean checkGround) {
        return checkPositionValidity((int)loc.x, (int)loc.y, (int)loc.z, checkGround);
    }
    
    public static boolean checkPositionValidity(final int x, final int y, final int z, final boolean checkGround) {
        final BlockPos block1 = new BlockPos(x, y, z);
        final BlockPos block2 = new BlockPos(x, y + 1, z);
        final BlockPos block3 = new BlockPos(x, y - 1, z);
        return !isBlockSolid(block1) && !isBlockSolid(block2) && (isBlockSolid(block3) || !checkGround) && isSafeToWalkOn(block3);
    }
    
    private static boolean isBlockSolid(final BlockPos block) {
        return Minecraft.getMinecraft().world.getBlockState(block).isNormalCube() || Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockSlab || Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockStairs || Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockCactus || Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockChest || Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockEnderChest || Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockSkull || Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockPane || Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockFence || Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockWall || Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockGlass || Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockPistonBase || Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockPistonExtension || Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockPistonMoving || Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockStainedGlass || Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockTrapDoor;
    }
    
    private static boolean isSafeToWalkOn(final BlockPos block) {
        return !(Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockFence) && !(Minecraft.getMinecraft().world.getBlockState(block) instanceof BlockWall);
    }
    
    public Hub isHubExisting(final Vec3d loc) {
        for (final Hub hub : this.hubs) {
            if (hub.getLoc().x == loc.x && hub.getLoc().y == loc.y && hub.getLoc().z == loc.z) {
                return hub;
            }
        }
        for (final Hub hub : this.hubsToWork) {
            if (hub.getLoc().x == loc.x && hub.getLoc().y == loc.y && hub.getLoc().z == loc.z) {
                return hub;
            }
        }
        return null;
    }
    
    public boolean addHub(final Hub parent, final Vec3d loc, final double cost) {
        final Hub existingHub = this.isHubExisting(loc);
        double totalCost = cost;
        if (parent != null) {
            totalCost += parent.getTotalCost();
        }
        if (existingHub == null) {
            if ((loc.x == this.endVec3.x && loc.y == this.endVec3.y && loc.z == this.endVec3.z) || (this.minDistanceSquared != 0.0 && loc.squareDistanceTo(this.endVec3) <= this.minDistanceSquared)) {
                this.path.clear();
                (this.path = parent.getPath()).add(loc);
                return true;
            }
            final ArrayList<Vec3d> path = new ArrayList<Vec3d>(parent.getPath());
            path.add(loc);
            this.hubsToWork.add(new Hub(loc, parent, path, loc.squareDistanceTo(this.endVec3), cost, totalCost));
        }
        else if (existingHub.getCost() > cost) {
            final ArrayList<Vec3d> path = new ArrayList<Vec3d>(parent.getPath());
            path.add(loc);
            existingHub.setLoc(loc);
            existingHub.setParent(parent);
            existingHub.setPath(path);
            existingHub.setSquareDistanceToFromTarget(loc.squareDistanceTo(this.endVec3));
            existingHub.setCost(cost);
            existingHub.setTotalCost(totalCost);
        }
        return false;
    }
    
    static {
        AStarCustomPathFinder.flatCardinalDirections = new Vec3d[] { new Vec3d(1.0, 0.0, 0.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, 0.0, -1.0) };
    }
    
    private class Hub
    {
        private Vec3d loc;
        private Hub parent;
        private ArrayList<Vec3d> path;
        private double squareDistanceToFromTarget;
        private double cost;
        private double totalCost;
        
        public Hub(final Vec3d loc, final Hub parent, final ArrayList<Vec3d> path, final double squareDistanceToFromTarget, final double cost, final double totalCost) {
            this.loc = null;
            this.parent = null;
            this.loc = loc;
            this.parent = parent;
            this.path = path;
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
            this.cost = cost;
            this.totalCost = totalCost;
        }
        
        public Vec3d getLoc() {
            return this.loc;
        }
        
        public Hub getParent() {
            return this.parent;
        }
        
        public ArrayList<Vec3d> getPath() {
            return this.path;
        }
        
        public double getSquareDistanceToFromTarget() {
            return this.squareDistanceToFromTarget;
        }
        
        public double getCost() {
            return this.cost;
        }
        
        public void setLoc(final Vec3d loc) {
            this.loc = loc;
        }
        
        public void setParent(final Hub parent) {
            this.parent = parent;
        }
        
        public void setPath(final ArrayList<Vec3d> path) {
            this.path = path;
        }
        
        public void setSquareDistanceToFromTarget(final double squareDistanceToFromTarget) {
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
        }
        
        public void setCost(final double cost) {
            this.cost = cost;
        }
        
        public double getTotalCost() {
            return this.totalCost;
        }
        
        public void setTotalCost(final double totalCost) {
            this.totalCost = totalCost;
        }
    }
    
    public class CompareHub implements Comparator<Hub>
    {
        @Override
        public int compare(final Hub o1, final Hub o2) {
            return (int)(o1.getSquareDistanceToFromTarget() + o1.getTotalCost() - (o2.getSquareDistanceToFromTarget() + o2.getTotalCost()));
        }
    }
}
