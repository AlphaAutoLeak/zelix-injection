package zelix.hack.hacks;

import zelix.hack.*;
import java.util.*;
import net.minecraftforge.client.event.*;
import zelix.utils.*;
import zelix.managers.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.projectile.*;

public class Glowing extends Hack
{
    public Glowing() {
        super("Glowing", HackCategory.VISUAL);
    }
    
    @Override
    public String getDescription() {
        return "Glows all entities around you.";
    }
    
    @Override
    public void onDisable() {
        for (final Object object : Utils.getEntityList()) {
            final Entity entity = (Entity)object;
            if (entity.isGlowing()) {
                entity.setGlowing(false);
            }
        }
        super.onDisable();
    }
    
    @Override
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        for (final Object object : Utils.getEntityList()) {
            final Entity entity = (Entity)object;
            if (this.checkEntity(entity) != null && entity != Wrapper.INSTANCE.player() && !entity.isGlowing()) {
                entity.setGlowing(true);
            }
        }
        super.onRenderWorldLast(event);
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
