package zelix.hack.hacks.automine;

import java.util.*;
import net.minecraft.util.math.*;
import net.minecraft.block.*;

public class AutoMinePathFinder
{
    private static Vec3 startVec3;
    private static Vec3 endVec3;
    private ArrayList<Vec3> path;
    private ArrayList<Hub> hubs;
    private ArrayList<Hub> hubsToWork;
    private double minDistanceSquared;
    private boolean nearest;
    private static boolean doIt;
    private static Vec3[] flatCardinalDirections;
    
    public AutoMinePathFinder(final Vec3 startVec3, final Vec3 endVec3) {
        this.path = new ArrayList<Vec3>();
        this.hubs = new ArrayList<Hub>();
        this.hubsToWork = new ArrayList<Hub>();
        this.minDistanceSquared = 1.0E-4;
        this.nearest = true;
        AutoMinePathFinder.startVec3 = startVec3.addVector(0.0, 0.0, 0.0).floor();
        AutoMinePathFinder.endVec3 = endVec3.addVector(0.0, 0.0, 0.0).floor();
    }
    
    public ArrayList<Vec3> getPath() {
        return this.path;
    }
    
    public void compute() {
        this.compute(1000, 13);
    }
    
    public void compute(final int loops, final int depth) {
        this.path.clear();
        this.hubsToWork.clear();
        final ArrayList<Vec3> initPath = new ArrayList<Vec3>();
        initPath.add(AutoMinePathFinder.startVec3);
        this.hubsToWork.add(new Hub(AutoMinePathFinder.startVec3, null, initPath, AutoMinePathFinder.startVec3.squareDistanceTo(AutoMinePathFinder.endVec3), 0.0, 0.0));
    Label_0335:
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
                for (final Vec3 direction : AutoMinePathFinder.flatCardinalDirections) {
                    final Vec3 loc = hub.getLoc().add(direction).floor();
                    if (checkPositionValidity(loc, true) && this.addHub(hub, loc, 0.0)) {
                        break Label_0335;
                    }
                }
                final Vec3 loc2 = hub.getLoc().addVector(0.0, 1.0, 0.0).floor();
                if (checkPositionValidity(loc2, true) && this.addHub(hub, loc2, 0.0)) {
                    break Label_0335;
                }
                final Vec3 loc3 = hub.getLoc().addVector(0.0, -1.0, 0.0).floor();
                if (checkPositionValidity(loc3, true) && this.addHub(hub, loc3, 0.0)) {
                    break Label_0335;
                }
            }
        }
        if (this.nearest) {
            Collections.sort(this.hubs, new CompareHub());
            this.path = this.hubs.get(0).getPath();
        }
    }
    
    public static boolean checkPositionValidity(final Vec3 loc, final boolean checkGround) {
        return checkPositionValidity((int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), checkGround);
    }
    
    public static boolean checkPositionValidity(final int x, final int y, final int z, final boolean checkGround) {
        final BlockPos block1 = new BlockPos(x, y, z);
        final BlockPos block2 = new BlockPos(x, y + 1, z);
        final BlockPos block3 = new BlockPos(x, y - 1, z);
        for (int x2 = x - 1; x2 <= x + 1; ++x2) {
            for (int y2 = y; y2 <= y + 2; ++y2) {
                for (int z2 = z - 1; z2 <= z + 1; ++z2) {
                    if (BlockUtils.getBlock(new BlockPos(x2, y2, z2)) instanceof BlockLiquid) {
                        return false;
                    }
                }
            }
        }
        final Block blockUp = BlockUtils.getBlock(new BlockPos(x, y + 2, z));
        if (blockUp instanceof BlockGravel || blockUp instanceof BlockSand) {
            return false;
        }
        if (AutoMine.tryTwo) {
            return (BlockUtils.getBlock(block1) instanceof BlockAir || isBlockSolid(block1)) && BlockUtils.getBlock(block1) != Block.getBlockById(7) && (BlockUtils.getBlock(block2) instanceof BlockAir || isBlockSolid(block2)) && BlockUtils.getBlock(block2) != Block.getBlockById(7) && (isBlockSolid(block3) || !checkGround) && isSafeToThrough(block3) && isSafeToThrough(block2) && isSafeToThrough(block1);
        }
        return !isBlockSolid(block1) && BlockUtils.getBlock(block1) != Block.getBlockById(7) && !isBlockSolid(block2) && BlockUtils.getBlock(block2) != Block.getBlockById(7) && (isBlockSolid(block3) || !checkGround) && isSafeToThrough(block3) && isSafeToThrough(block2) && isSafeToThrough(block1);
    }
    
    public static boolean isBlockSolid(final BlockPos block) {
        return BlockUtils.getBlock(block).isBlockNormalCube(zelix.utils.BlockUtils.getState(block)) || BlockUtils.getBlock(block) instanceof BlockSlab || BlockUtils.getBlock(block) instanceof BlockStairs || BlockUtils.getBlock(block) instanceof BlockCactus || BlockUtils.getBlock(block) instanceof BlockChest || BlockUtils.getBlock(block) instanceof BlockEnderChest || BlockUtils.getBlock(block) instanceof BlockSkull || BlockUtils.getBlock(block) instanceof BlockPane || BlockUtils.getBlock(block) instanceof BlockFence || BlockUtils.getBlock(block) instanceof BlockWall || BlockUtils.getBlock(block) instanceof BlockGlass || BlockUtils.getBlock(block) instanceof BlockPistonBase || BlockUtils.getBlock(block) instanceof BlockPistonExtension || BlockUtils.getBlock(block) instanceof BlockPistonMoving || BlockUtils.getBlock(block) instanceof BlockStainedGlass || BlockUtils.getBlock(block) instanceof BlockTrapDoor;
    }
    
    public static boolean isSafeToThrough(final BlockPos block) {
        return !(BlockUtils.getBlock(block) instanceof BlockFence) && !(BlockUtils.getBlock(block) instanceof BlockWall) && !(BlockUtils.getBlock(block) instanceof BlockLiquid);
    }
    
    public static boolean isSafeToWalkOn(final BlockPos block) {
        return !(BlockUtils.getBlock(block) instanceof BlockLiquid) && !(BlockUtils.getBlock(block) instanceof BlockAir);
    }
    
    public Hub isHubExisting(final Vec3 loc) {
        for (final Hub hub : this.hubs) {
            if (hub.getLoc().getX() == loc.getX() && hub.getLoc().getY() == loc.getY() && hub.getLoc().getZ() == loc.getZ()) {
                return hub;
            }
        }
        for (final Hub hub : this.hubsToWork) {
            if (hub.getLoc().getX() == loc.getX() && hub.getLoc().getY() == loc.getY() && hub.getLoc().getZ() == loc.getZ()) {
                return hub;
            }
        }
        return null;
    }
    
    public boolean addHub(final Hub parent, final Vec3 loc, final double cost) {
        final Hub existingHub = this.isHubExisting(loc);
        double totalCost = cost;
        if (parent != null) {
            totalCost += parent.getTotalCost();
        }
        if (existingHub == null) {
            if (loc.getX() == AutoMinePathFinder.endVec3.getX() && loc.getY() == AutoMinePathFinder.endVec3.getY() && loc.getZ() == AutoMinePathFinder.endVec3.getZ()) {
                this.path.clear();
                (this.path = parent.getPath()).add(loc);
                return true;
            }
            final ArrayList<Vec3> path = new ArrayList<Vec3>(parent.getPath());
            path.add(loc);
            this.hubsToWork.add(new Hub(loc, parent, path, loc.squareDistanceTo(AutoMinePathFinder.endVec3), cost, totalCost));
        }
        else if (existingHub.getCost() > cost) {
            final ArrayList<Vec3> path = new ArrayList<Vec3>(parent.getPath());
            path.add(loc);
            existingHub.setLoc(loc);
            existingHub.setParent(parent);
            existingHub.setPath(path);
            existingHub.setSquareDistanceToFromTarget(loc.squareDistanceTo(AutoMinePathFinder.endVec3));
            existingHub.setCost(cost);
            existingHub.setTotalCost(totalCost);
        }
        return false;
    }
    
    static {
        AutoMinePathFinder.doIt = false;
        AutoMinePathFinder.flatCardinalDirections = new Vec3[] { new Vec3(1.0, 0.0, 0.0), new Vec3(-1.0, 0.0, 0.0), new Vec3(0.0, 0.0, 1.0), new Vec3(0.0, 0.0, -1.0) };
    }
    
    private class Hub
    {
        private Vec3 loc;
        private Hub parent;
        private ArrayList<Vec3> path;
        private double squareDistanceToFromTarget;
        private double cost;
        private double totalCost;
        
        public Hub(final Vec3 loc, final Hub parent, final ArrayList<Vec3> path, final double squareDistanceToFromTarget, final double cost, final double totalCost) {
            this.loc = null;
            this.parent = null;
            this.loc = loc;
            this.parent = parent;
            this.path = path;
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
            this.cost = cost;
            this.totalCost = totalCost;
        }
        
        public Vec3 getLoc() {
            return this.loc;
        }
        
        public Hub getParent() {
            return this.parent;
        }
        
        public ArrayList<Vec3> getPath() {
            return this.path;
        }
        
        public double getSquareDistanceToFromTarget() {
            return this.squareDistanceToFromTarget;
        }
        
        public double getCost() {
            return this.cost;
        }
        
        public void setLoc(final Vec3 loc) {
            this.loc = loc;
        }
        
        public void setParent(final Hub parent) {
            this.parent = parent;
        }
        
        public void setPath(final ArrayList<Vec3> path) {
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
