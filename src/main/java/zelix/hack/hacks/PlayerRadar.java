package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.gui.*;
import zelix.utils.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import zelix.utils.hooks.visual.*;
import java.util.*;

public class PlayerRadar extends Hack
{
    public PlayerRadar() {
        super("PlayerRadar", HackCategory.VISUAL);
    }
    
    @Override
    public String getDescription() {
        return "Show all players around you.";
    }
    
    @Override
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Text event) {
        int y = 2;
        final ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
        for (final Object o : Utils.getEntityList()) {
            if (o instanceof EntityPlayer) {
                final EntityPlayer e = (EntityPlayer)o;
                final float range = (float) Wrapper.INSTANCE.player().getDistanceSq((Entity)e);
                final float health = e.getHealth();
                String heal = " ¡ì2[" + RenderUtils.DF(health, 0) + "] ";
                if (health >= 12.0) {
                    heal = " ¡ì2[" + RenderUtils.DF(health, 0) + "] ";
                }
                else if (health >= 4.0) {
                    heal = " ¡ì6[" + RenderUtils.DF(health, 0) + "] ";
                }
                else {
                    heal = " ¡ì4[" + RenderUtils.DF(health, 0) + "] ";
                }
                final String name = e.getGameProfile().getName();
                final String str = name + heal + "¡ì7[" + RenderUtils.DF(range, 0) + "]";
                int color;
                if (e.isInvisible()) {
                    color = ColorUtils.color(155, 155, 155, 255);
                }
                else {
                    color = ColorUtils.color(255, 255, 255, 255);
                }
                Wrapper.INSTANCE.fontRenderer().drawString(str, sr.getScaledWidth() - Wrapper.INSTANCE.fontRenderer().getStringWidth(str), y, color);
                y += 12;
            }
        }
        super.onRenderGameOverlay(event);
    }
}
