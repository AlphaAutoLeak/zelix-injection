package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.*;
import java.util.*;
import zelix.utils.*;
import zelix.managers.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.projectile.*;

public class WallHack extends Hack
{
    public WallHack() {
        super("WallHack", HackCategory.VISUAL);
    }
    
    @Override
    public String getDescription() {
        return "The skin of the entities around you glows.";
    }
    
    @Override
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        for (final Object object : Utils.getEntityList()) {
            final Entity entity = (Entity)object;
            this.render(entity, event.getPartialTicks());
        }
        super.onRenderWorldLast(event);
    }
    
    void render(final Entity entity, final float ticks) {
        final Entity ent = this.checkEntity(entity);
        if (ent == null || ent == Wrapper.INSTANCE.player()) {
            return;
        }
        if (ent == Wrapper.INSTANCE.mc().getRenderViewEntity() && Wrapper.INSTANCE.mcSettings().thirdPersonView == 0) {
            return;
        }
        Wrapper.INSTANCE.mc().entityRenderer.disableLightmap();
        Wrapper.INSTANCE.mc().getRenderManager().renderEntityStatic(ent, ticks, false);
        Wrapper.INSTANCE.mc().entityRenderer.enableLightmap();
    }
    
    Entity checkEntity(final Entity e) {
        Entity entity = null;
        final Hack targets = HackManager.getHack("Targets");
        if (targets.isToggledValue("Players") && e instanceof EntityPlayer) {
            entity = e;
        }
        else if (targets.isToggledValue("Mobs") && e instanceof EntityLiving) {
            entity = e;
        }
        else if (e instanceof EntityItem) {
            entity = e;
        }
        else if (e instanceof EntityArrow) {
            entity = e;
        }
        return entity;
    }
}
