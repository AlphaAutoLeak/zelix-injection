package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.client.event.*;
import net.minecraft.entity.item.*;
import java.util.*;
import net.minecraft.entity.*;
import zelix.utils.*;
import net.minecraft.entity.player.*;
import zelix.utils.hooks.visual.*;
import zelix.managers.*;

public class Tracers extends Hack
{
    public Tracers() {
        super("Tracers", HackCategory.VISUAL);
    }
    
    @Override
    public String getDescription() {
        return "Traces a line to the players.";
    }
    
    @Override
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        for (final Object object : Utils.getEntityList()) {
            if (object instanceof EntityLivingBase && !(object instanceof EntityArmorStand)) {
                final EntityLivingBase entity = (EntityLivingBase)object;
                this.render(entity, event.getPartialTicks());
            }
        }
        super.onRenderWorldLast(event);
    }
    
    void render(final EntityLivingBase entity, final float ticks) {
        if (ValidUtils.isValidEntity(entity) || entity == Wrapper.INSTANCE.player()) {
            return;
        }
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            final String ID = Utils.getPlayerName(player);
            if (EnemyManager.enemysList.contains(ID)) {
                RenderUtils.drawTracer((Entity)entity, 0.8f, 0.3f, 0.0f, 1.0f, ticks);
                return;
            }
            if (FriendManager.friendsList.contains(ID)) {
                RenderUtils.drawTracer((Entity)entity, 0.0f, 0.7f, 1.0f, 1.0f, ticks);
                return;
            }
        }
        if (HackManager.getHack("Targets").isToggledValue("Murder")) {
            if (Utils.isMurder(entity)) {
                RenderUtils.drawTracer((Entity)entity, 1.0f, 0.0f, 0.8f, 1.0f, ticks);
                return;
            }
            if (Utils.isDetect(entity)) {
                RenderUtils.drawTracer((Entity)entity, 0.0f, 0.0f, 1.0f, 0.5f, ticks);
                return;
            }
        }
        if (entity.isInvisible()) {
            RenderUtils.drawTracer((Entity)entity, 0.0f, 0.0f, 0.0f, 0.5f, ticks);
            return;
        }
        if (entity.hurtTime > 0) {
            RenderUtils.drawTracer((Entity)entity, 1.0f, 0.0f, 0.0f, 1.0f, ticks);
            return;
        }
        RenderUtils.drawTracer((Entity)entity, 1.0f, 1.0f, 1.0f, 0.5f, ticks);
    }
}
