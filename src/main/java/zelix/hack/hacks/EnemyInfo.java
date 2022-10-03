package zelix.hack.hacks;

import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import zelix.hack.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.util.math.*;
import net.minecraftforge.client.event.*;
import java.text.*;
import net.minecraft.client.*;
import org.lwjgl.opengl.*;
import net.minecraft.entity.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;

public class EnemyInfo extends Hack
{
    private ResourceLocation hpbar;
    private boolean show;
    private String enemyNickname;
    private double enemyHp;
    private double enemyDistance;
    private Entity entity;
    private EntityPlayer entityPlayer;
    private int hpbarwidth;
    
    public EnemyInfo() {
        super("EnemyInfo", HackCategory.VISUAL);
        this.hpbar = new ResourceLocation("healthbar.png");
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent e) {
        final RayTraceResult objectMouseOver = Wrapper.INSTANCE.mc().objectMouseOver;
        if (objectMouseOver != null) {
            if (objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
                this.entity = objectMouseOver.entityHit;
                if (this.entity instanceof EntityPlayer) {
                    this.entityPlayer = (EntityPlayer)this.entity;
                    this.enemyNickname = this.entityPlayer.getName();
                    this.enemyHp = this.entityPlayer.getHealth();
                    this.enemyDistance = this.entityPlayer.getDistanceSq((Entity)Wrapper.INSTANCE.mc().player);
                    this.show = true;
                }
            }
            else {
                this.show = false;
            }
        }
        else {
            this.show = false;
        }
    }
    
    @Override
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Text event) {
        final RenderGameOverlayEvent.ElementType type = event.getType();
        event.getType();
        if (!type.equals((Object)RenderGameOverlayEvent.ElementType.TEXT)) {
            return;
        }
        if (this.show && Wrapper.INSTANCE.mc().world != null && Wrapper.INSTANCE.mc().player != null) {
            final DecimalFormat decimalFormat = new DecimalFormat("###.#");
            final FontRenderer fr = Wrapper.INSTANCE.mc().fontRenderer;
            final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            Gui.drawRect(sr.getScaledWidth() / 2 + 120, sr.getScaledHeight() / 2 + 25, sr.getScaledWidth() / 2 + 20, sr.getScaledHeight() / 2 + 90, 1325400064);
            fr.drawString(this.enemyNickname, sr.getScaledWidth() / 2 + 60, sr.getScaledHeight() / 2 + 30, -1);
            GL11.glPushMatrix();
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            fr.drawString("HP: " + Math.round(this.enemyHp), (sr.getScaledWidth() / 2 + 60) * 2, (sr.getScaledHeight() / 2 + 40) * 2, -1);
            fr.drawString("Distanse: " + decimalFormat.format(this.enemyDistance), (sr.getScaledWidth() / 2 + 60) * 2, (sr.getScaledHeight() / 2 + 45) * 2, -1);
            GL11.glPopMatrix();
            drawEntityOnScreen(sr.getScaledWidth() / 2 + 40, sr.getScaledHeight() / 2 + 80, 25, 0.0f, 0.0f, (EntityLivingBase)this.entityPlayer);
            Wrapper.INSTANCE.mc().renderEngine.bindTexture(this.hpbar);
            Gui.drawScaledCustomSizeModalRect(sr.getScaledWidth() / 2 + 20, sr.getScaledHeight() / 2 + 89, 0.0f, 0.0f, 256, 256, sr.getScaledWidth() / 2 - sr.getScaledWidth() / 2 + 100, 1, 256.0f, 256.0f);
        }
    }
    
    public static void drawEntityOnScreen(final int posX, final int posY, final int scale, final float mouseX, final float mouseY, final EntityLivingBase ent) {
        if (Wrapper.INSTANCE.mc().world != null && Wrapper.INSTANCE.mc().player != null) {
            GlStateManager.enableColorMaterial();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)posX, (float)posY, 50.0f);
            GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
            GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
            final float f = ent.renderYawOffset;
            final float f2 = ent.rotationYaw;
            final float f3 = ent.rotationPitch;
            final float f4 = ent.prevRotationYawHead;
            final float f5 = ent.rotationYawHead;
            GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
            RenderHelper.enableStandardItemLighting();
            GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-(float)Math.atan(mouseY / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
            ent.renderYawOffset = (float)Math.atan(mouseX / 40.0f) * 20.0f;
            ent.rotationYaw = (float)Math.atan(mouseX / 40.0f) * 40.0f;
            ent.rotationPitch = -(float)Math.atan(mouseY / 40.0f) * 20.0f;
            ent.rotationYawHead = ent.rotationYaw;
            ent.prevRotationYawHead = ent.rotationYaw;
            GlStateManager.translate(0.0f, 0.0f, 0.0f);
            final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.setPlayerViewY(180.0f);
            rendermanager.setRenderShadow(false);
            rendermanager.renderEntity((Entity)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
            rendermanager.setRenderShadow(true);
            ent.renderYawOffset = f;
            ent.rotationYaw = f2;
            ent.rotationPitch = f3;
            ent.prevRotationYawHead = f4;
            ent.rotationYawHead = f5;
            GlStateManager.popMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        }
    }
}
