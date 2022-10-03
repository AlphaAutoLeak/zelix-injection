package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.client.event.*;
import zelix.utils.*;
import net.minecraft.block.*;
import zelix.utils.hooks.visual.*;
import net.minecraft.util.math.*;

public class BlockOverlay extends Hack
{
    public BlockOverlay() {
        super("BlockOverlay", HackCategory.VISUAL);
    }
    
    @Override
    public String getDescription() {
        return "Show of selected block.";
    }
    
    @Override
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (Wrapper.INSTANCE.mc().objectMouseOver == null) {
            return;
        }
        if (Wrapper.INSTANCE.mc().objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            final Block block = BlockUtils.getBlock(Wrapper.INSTANCE.mc().objectMouseOver.getBlockPos());
            final BlockPos blockPos = Wrapper.INSTANCE.mc().objectMouseOver.getBlockPos();
            if (Block.getIdFromBlock(block) == 0) {
                return;
            }
            RenderUtils.drawBlockESP(blockPos, 1.0f, 1.0f, 1.0f);
        }
        super.onRenderWorldLast(event);
    }
}
