package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.client.event.*;
import zelix.utils.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.*;
import zelix.utils.hooks.visual.*;
import java.util.*;

public class ItemESP extends Hack
{
    public ItemESP() {
        super("ItemESP", HackCategory.VISUAL);
    }
    
    @Override
    public String getDescription() {
        return "Highlights nearby items.";
    }
    
    @Override
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        for (final Object object : Utils.getEntityList()) {
            if (object instanceof EntityItem || object instanceof EntityArrow) {
                final Entity item = (Entity)object;
                RenderUtils.drawESP(item, 1.0f, 1.0f, 1.0f, 1.0f, event.getPartialTicks());
            }
        }
        super.onRenderWorldLast(event);
    }
}
