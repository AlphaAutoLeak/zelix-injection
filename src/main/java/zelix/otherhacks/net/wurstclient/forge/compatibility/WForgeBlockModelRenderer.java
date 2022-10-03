package zelix.otherhacks.net.wurstclient.forge.compatibility;

import net.minecraftforge.client.model.pipeline.*;
import net.minecraft.client.renderer.color.*;
import net.minecraft.world.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.block.state.*;
import net.minecraft.util.math.*;
import net.minecraft.client.renderer.*;
import zelix.otherhacks.net.wurstclient.forge.*;
import net.minecraft.block.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import zelix.otherhacks.net.wurstclient.forge.hacks.*;
import java.util.*;

public class WForgeBlockModelRenderer extends ForgeBlockModelRenderer
{
    public WForgeBlockModelRenderer(final BlockColors colors) {
        super(colors);
    }
    
    public boolean renderModel(final IBlockAccess blockAccessIn, final IBakedModel modelIn, final IBlockState blockStateIn, final BlockPos blockPosIn, final BufferBuilder buffer, boolean checkSides) {
        if (ForgeWurst.getForgeWurst().getHax().xRayHack.isEnabled()) {
            if (!this.isVisible(blockStateIn.getBlock())) {
                return false;
            }
            blockStateIn.getBlock().setLightLevel(100.0f);
            checkSides = false;
        }
        return super.renderModel(blockAccessIn, modelIn, blockStateIn, blockPosIn, buffer, checkSides);
    }
    
    private boolean isVisible(final Block block) {
        final String name = BlockUtils.getName(block);
        final int index = Collections.binarySearch(XRayHack.blocks.getBlockNames(), name);
        return index >= 0;
    }
}
