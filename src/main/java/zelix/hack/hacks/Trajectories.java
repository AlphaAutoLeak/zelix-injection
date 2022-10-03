package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.client.event.*;
import zelix.utils.*;
import org.lwjgl.opengl.*;
import net.minecraft.util.math.*;
import zelix.utils.hooks.visual.*;
import net.minecraft.client.entity.*;
import net.minecraft.item.*;
import net.minecraft.client.renderer.entity.*;

public class Trajectories extends Hack
{
    public Trajectories() {
        super("Trajectories", HackCategory.VISUAL);
    }
    
    @Override
    public String getDescription() {
        return "Predicts the flight path of arrows and throwable items.";
    }
    
    @Override
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        final float yawAim = Wrapper.INSTANCE.player().rotationYaw;
        final float pitchAim = Wrapper.INSTANCE.player().rotationPitch;
        final ItemStack stack = player.inventory.getCurrentItem();
        if (stack == null) {
            return;
        }
        final Item item = stack.getItem();
        if (!(item instanceof ItemBow) && !(item instanceof ItemSnowball) && !(item instanceof ItemEgg) && !(item instanceof ItemEnderPearl) && !(item instanceof ItemSplashPotion) && !(item instanceof ItemLingeringPotion) && !(item instanceof ItemFishingRod)) {
            return;
        }
        final boolean usingBow = player.inventory.getCurrentItem().getItem() instanceof ItemBow;
        double arrowPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks() - MathHelper.cos((float)Math.toRadians(yawAim)) * 0.16f;
        double arrowPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks() + player.getEyeHeight() - 0.1;
        double arrowPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks() - MathHelper.sin((float)Math.toRadians(yawAim)) * 0.16f;
        final float arrowMotionFactor = usingBow ? 1.0f : 0.4f;
        final float yaw = (float)Math.toRadians(yawAim);
        final float pitch = (float)Math.toRadians(pitchAim);
        float arrowMotionX = -MathHelper.sin(yaw) * MathHelper.cos(pitch) * arrowMotionFactor;
        float arrowMotionY = -MathHelper.sin(pitch) * arrowMotionFactor;
        float arrowMotionZ = MathHelper.cos(yaw) * MathHelper.cos(pitch) * arrowMotionFactor;
        final double arrowMotion = Math.sqrt(arrowMotionX * arrowMotionX + arrowMotionY * arrowMotionY + arrowMotionZ * arrowMotionZ);
        arrowMotionX /= (float)arrowMotion;
        arrowMotionY /= (float)arrowMotion;
        arrowMotionZ /= (float)arrowMotion;
        if (usingBow) {
            float bowPower = (72000 - player.getItemInUseCount()) / 20.0f;
            bowPower = (bowPower * bowPower + bowPower * 2.0f) / 3.0f;
            if (bowPower > 1.0f) {
                bowPower = 1.0f;
            }
            if (bowPower <= 0.1f) {
                bowPower = 1.0f;
            }
            bowPower *= 3.0f;
            arrowMotionX *= bowPower;
            arrowMotionY *= bowPower;
            arrowMotionZ *= bowPower;
        }
        else {
            arrowMotionX *= 1.5;
            arrowMotionY *= 1.5;
            arrowMotionZ *= 1.5;
        }
        GL11.glPushMatrix();
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glEnable(32925);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.8f);
        final RenderManager renderManager = Wrapper.INSTANCE.mc().getRenderManager();
        final double gravity = usingBow ? 0.05 : ((item instanceof ItemPotion) ? 0.4 : ((item instanceof ItemFishingRod) ? 0.15 : 0.03));
        final Vec3d playerVector = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        GL11.glColor3d(1.0, 1.0, 1.0);
        GL11.glBegin(3);
        for (int i = 0; i < 1000; ++i) {
            GL11.glVertex3d(arrowPosX - renderManager.viewerPosX, arrowPosY - renderManager.viewerPosY, arrowPosZ - renderManager.viewerPosZ);
            arrowPosX += arrowMotionX * 0.1;
            arrowPosY += arrowMotionY * 0.1;
            arrowPosZ += arrowMotionZ * 0.1;
            arrowMotionX *= 0.999;
            arrowMotionY *= 0.999;
            arrowMotionZ *= 0.999;
            arrowMotionY -= (float)(gravity * 0.1);
            if (Wrapper.INSTANCE.world().rayTraceBlocks(playerVector, new Vec3d(arrowPosX, arrowPosY, arrowPosZ)) != null) {
                break;
            }
        }
        GL11.glEnd();
        final double renderX = arrowPosX - renderManager.viewerPosX;
        final double renderY = arrowPosY - renderManager.viewerPosY;
        final double renderZ = arrowPosZ - renderManager.viewerPosZ;
        final AxisAlignedBB bb = new AxisAlignedBB(renderX - 0.5, renderY - 0.5, renderZ - 0.5, renderX + 0.5, renderY + 0.5, renderZ + 0.5);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.15f);
        RenderUtils.drawColorBox(bb, 1.0f, 1.0f, 1.0f, 0.15f);
        GL11.glColor4d(1.0, 1.0, 1.0, 0.5);
        RenderUtils.drawSelectionBoundingBox(bb);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(32925);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
        super.onRenderWorldLast(event);
    }
}
