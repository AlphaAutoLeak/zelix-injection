package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import java.util.function.*;
import net.minecraft.client.entity.*;
import net.minecraft.world.*;
import net.minecraft.util.math.*;
import java.util.stream.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.block.*;

public final class GlideHack extends Hack
{
    private final SliderSetting fallSpeed;
    private final SliderSetting moveSpeed;
    private final SliderSetting minHeight;
    
    public GlideHack() {
        super("Glide", "Makes you glide down slowly when falling.");
        this.fallSpeed = new SliderSetting("Fall speed", 0.125, 0.005, 0.25, 0.005, SliderSetting.ValueDisplay.DECIMAL);
        this.moveSpeed = new SliderSetting("Move speed", "Horizontal movement factor.", 1.2, 1.0, 5.0, 0.05, SliderSetting.ValueDisplay.PERCENTAGE);
        this.minHeight = new SliderSetting("Min height", "Won't glide when you are\ntoo close to the ground.", 0.0, 0.0, 2.0, 0.01, v -> (v == 0.0) ? "disabled" : SliderSetting.ValueDisplay.DECIMAL.getValueString(v));
        this.setCategory(Category.MOVEMENT);
        this.addSetting(this.fallSpeed);
        this.addSetting(this.moveSpeed);
        this.addSetting(this.minHeight);
    }
    
    @Override
    protected void onEnable() {
        GlideHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        final EntityPlayerSP player = event.getPlayer();
        final World world = player.world;
        if (!player.isAirBorne || player.isInWater() || player.isInLava() || player.isOnLadder() || player.motionY >= 0.0) {
            return;
        }
        if (this.minHeight.getValue() > 0.0) {
            AxisAlignedBB box = player.getEntityBoundingBox();
            box = box.union(box.offset(0.0, -this.minHeight.getValue(), 0.0));
            if (world.collidesWithAnyBlock(box)) {
                return;
            }
            final BlockPos min = new BlockPos(new Vec3d(box.minX, box.minY, box.minZ));
            final BlockPos max = new BlockPos(new Vec3d(box.maxX, box.maxY, box.maxZ));
            final Stream<BlockPos> stream = StreamSupport.stream(BlockPos.getAllInBox(min, max).spliterator(), true);
            if (stream.map((Function<? super BlockPos, ?>)BlockUtils::getBlock).anyMatch(b -> b instanceof BlockLiquid)) {
                return;
            }
        }
        player.motionY = Math.max(player.motionY, -this.fallSpeed.getValue());
        final EntityPlayerSP entityPlayerSP = player;
        entityPlayerSP.jumpMovementFactor *= this.moveSpeed.getValueF();
    }
}
