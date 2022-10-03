package zelix.otherhacks.net.wurstclient.forge.clickgui;

import zelix.otherhacks.net.wurstclient.forge.hacks.*;
import zelix.otherhacks.net.wurstclient.forge.*;
import org.lwjgl.opengl.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.client.entity.*;
import java.util.*;

public final class Radar extends Component
{
    private final RadarHack hack;
    
    public Radar(final RadarHack hack) {
        this.hack = hack;
        this.setWidth(this.getDefaultWidth());
        this.setHeight(this.getDefaultHeight());
    }
    
    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        final ClickGui gui = ForgeWurst.getForgeWurst().getGui();
        final float[] bgColor = gui.getBgColor();
        final float[] acColor = gui.getAcColor();
        final float opacity = gui.getOpacity();
        final int x1 = this.getX();
        final int x2 = x1 + this.getWidth();
        final int y1 = this.getY();
        final int y2 = y1 + this.getHeight();
        final int scroll = this.getParent().isScrollingEnabled() ? this.getParent().getScrollOffset() : 0;
        final boolean hovering = mouseX >= x1 && mouseY >= y1 && mouseX < x2 && mouseY < y2 && mouseY >= -scroll && mouseY < this.getParent().getHeight() - 13 - scroll;
        if (hovering) {
            gui.setTooltip(null);
        }
        GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], opacity);
        GL11.glBegin(7);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y2);
        GL11.glVertex2i(x2, y2);
        GL11.glVertex2i(x2, y1);
        GL11.glEnd();
        final double middleX = (x1 + x2) / 2.0;
        final double middleY = (y1 + y2) / 2.0;
        GL11.glPushMatrix();
        GL11.glTranslated(middleX, middleY, 0.0);
        final EntityPlayerSP player = WMinecraft.getPlayer();
        if (!this.hack.isRotateEnabled()) {
            GL11.glRotated((double)(180.0f + player.rotationYaw), 0.0, 0.0, 1.0);
        }
        final double xa1 = 0.0;
        final double xa2 = 2.0;
        final double xa3 = -2.0;
        final double ya1 = -2.0;
        final double ya2 = 2.0;
        final double ya3 = 1.0;
        GL11.glColor4f(acColor[0], acColor[1], acColor[2], opacity);
        GL11.glBegin(9);
        GL11.glVertex2d(xa1, ya1);
        GL11.glVertex2d(xa2, ya2);
        GL11.glVertex2d(xa1, ya3);
        GL11.glVertex2d(xa3, ya2);
        GL11.glEnd();
        GL11.glColor4f(0.0625f, 0.0625f, 0.0625f, 0.5f);
        GL11.glBegin(2);
        GL11.glVertex2d(xa1, ya1);
        GL11.glVertex2d(xa2, ya2);
        GL11.glVertex2d(xa1, ya3);
        GL11.glVertex2d(xa3, ya2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(2832);
        GL11.glPointSize(2.0f);
        GL11.glBegin(0);
        for (final Entity e : this.hack.getEntities()) {
            final double diffX = e.prevPosX + (e.posX - e.prevPosX) * partialTicks - (player.prevPosX + (player.posX - player.prevPosX) * partialTicks);
            final double diffZ = e.prevPosZ + (e.posZ - e.prevPosZ) * partialTicks - (player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks);
            final double distance = Math.sqrt(diffX * diffX + diffZ * diffZ) * (this.getWidth() * 0.5 / this.hack.getRadius());
            final double neededRotation = Math.toDegrees(Math.atan2(diffZ, diffX));
            double angle;
            if (this.hack.isRotateEnabled()) {
                angle = Math.toRadians(player.rotationYaw - neededRotation - 90.0);
            }
            else {
                angle = Math.toRadians(180.0 - neededRotation - 90.0);
            }
            final double renderX = Math.sin(angle) * distance;
            final double renderY = Math.cos(angle) * distance;
            if (Math.abs(renderX) <= this.getWidth() / 2.0) {
                if (Math.abs(renderY) > this.getHeight() / 2.0) {
                    continue;
                }
                int color;
                if (e instanceof EntityPlayer) {
                    color = 16711680;
                }
                else if (e instanceof IMob) {
                    color = 16744448;
                }
                else if (e instanceof EntityAnimal || e instanceof EntityAmbientCreature || e instanceof EntityWaterMob) {
                    color = 65280;
                }
                else {
                    color = 8421504;
                }
                GL11.glColor4f((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, 1.0f);
                GL11.glVertex2d(middleX + renderX, middleY + renderY);
            }
        }
        GL11.glEnd();
    }
    
    @Override
    public int getDefaultWidth() {
        return 96;
    }
    
    @Override
    public int getDefaultHeight() {
        return 96;
    }
}
