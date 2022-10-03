package zelix.hack.hacks.xray.utils;

import net.minecraft.client.entity.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.block.state.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import net.minecraft.client.renderer.*;
import zelix.hack.hacks.xray.reference.block.*;

public class Utils
{
    public static void sendMessage(final EntityPlayerSP player, final String message) {
        player.sendMessage((ITextComponent)new TextComponentString(message));
    }
    
    public static IBlockState getStateFromPlacement(final World world, final EntityLivingBase player, final ItemStack stack) {
        return Block.getBlockFromItem(stack.getItem()).getStateForPlacement(world, player.getPosition(), EnumFacing.NORTH, 0.1f, 0.1f, 0.1f, stack.getMetadata(), player, player.getActiveHand());
    }
    
    public static int clampColor(final int c) {
        return (c < 0) ? 0 : ((c > 255) ? 255 : c);
    }
    
    public static void renderBlockBounding(final BufferBuilder buffer, final BlockInfo b, final int opacity) {
        if (b == null) {
            return;
        }
        final float size = 1.0f;
        final int red = b.color[0];
        final int green = b.color[1];
        final int blue = b.color[2];
        final int x = b.getX();
        final int y = b.getY();
        final int z = b.getZ();
        buffer.pos((double)x, (double)(y + 1.0f), (double)z).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)(x + 1.0f), (double)(y + 1.0f), (double)z).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)(x + 1.0f), (double)(y + 1.0f), (double)z).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)(x + 1.0f), (double)(y + 1.0f), (double)(z + 1.0f)).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)(x + 1.0f), (double)(y + 1.0f), (double)(z + 1.0f)).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)x, (double)(y + 1.0f), (double)(z + 1.0f)).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)x, (double)(y + 1.0f), (double)(z + 1.0f)).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)x, (double)(y + 1.0f), (double)z).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)(x + 1.0f), (double)y, (double)z).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)(x + 1.0f), (double)y, (double)(z + 1.0f)).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)(x + 1.0f), (double)y, (double)(z + 1.0f)).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)x, (double)y, (double)(z + 1.0f)).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)x, (double)y, (double)(z + 1.0f)).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)x, (double)y, (double)z).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)x, (double)y, (double)z).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)(x + 1.0f), (double)y, (double)z).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)(x + 1.0f), (double)y, (double)(z + 1.0f)).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)(x + 1.0f), (double)(y + 1.0f), (double)(z + 1.0f)).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)(x + 1.0f), (double)y, (double)z).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)(x + 1.0f), (double)(y + 1.0f), (double)z).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)x, (double)y, (double)(z + 1.0f)).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)x, (double)(y + 1.0f), (double)(z + 1.0f)).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)x, (double)y, (double)z).color(red, green, blue, opacity).endVertex();
        buffer.pos((double)x, (double)(y + 1.0f), (double)z).color(red, green, blue, opacity).endVertex();
    }
}
