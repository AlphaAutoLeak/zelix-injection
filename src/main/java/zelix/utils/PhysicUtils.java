package zelix.utils;

import java.util.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.math.*;
import net.minecraft.block.material.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.item.*;

public class PhysicUtils
{
    public static Random random;
    public static Minecraft mc;
    public static RenderItem renderItem;
    public static long tick;
    public static double rotation;
    public static final ResourceLocation RES_ITEM_GLINT;
    
    public static void RenderEntityItem(final Entity par1Entity, final double x, final double y, final double z, final float par8, final float par9) {
        PhysicUtils.rotation = (System.nanoTime() - PhysicUtils.tick) / 3000000.0 * 1.0;
        if (!PhysicUtils.mc.inGameHasFocus) {
            PhysicUtils.rotation = 0.0;
        }
        final EntityItem item;
        final ItemStack itemstack;
        if ((itemstack = (item = (EntityItem)par1Entity).getItem()).getItem() != null) {
            PhysicUtils.random.setSeed(187L);
            boolean flag = false;
            if (TextureMap.LOCATION_BLOCKS_TEXTURE != null) {
                PhysicUtils.mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                PhysicUtils.mc.getRenderManager().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
                flag = true;
            }
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.pushMatrix();
            final IBakedModel ibakedmodel = PhysicUtils.renderItem.getItemModelMesher().getItemModel(itemstack);
            final int i = transformModelCount(item, x, y - 0.10000000149011612, z, par9, ibakedmodel);
            final BlockPos pos = new BlockPos((Entity)item);
            if (item.rotationPitch > 360.0f) {
                item.rotationPitch = 0.0f;
            }
            if (item != null && !Double.isNaN(item.getAge()) && !Double.isNaN(item.getAir()) && !Double.isNaN(item.getEntityId()) && item.getPosition() != null) {
                if (item.onGround) {
                    if (item.rotationPitch != 0.0f && item.rotationPitch != 90.0f && item.rotationPitch != 180.0f && item.rotationPitch != 270.0f) {
                        final double Abstand0 = formPositiv(item.rotationPitch);
                        final double Abstand2 = formPositiv(item.rotationPitch - 90.0f);
                        final double Abstand3 = formPositiv(item.rotationPitch - 180.0f);
                        final double Abstand4 = formPositiv(item.rotationPitch - 270.0f);
                        if (Abstand0 <= Abstand2 && Abstand0 <= Abstand3 && Abstand0 <= Abstand4) {
                            if (item.rotationPitch < 0.0f) {
                                final EntityItem e1 = item;
                                e1.rotationPitch += (float)PhysicUtils.rotation;
                            }
                            else {
                                final EntityItem e2 = item;
                                e2.rotationPitch -= (float)PhysicUtils.rotation;
                            }
                        }
                        if (Abstand2 < Abstand0 && Abstand2 <= Abstand3 && Abstand2 <= Abstand4) {
                            if (item.rotationPitch - 90.0f < 0.0f) {
                                final EntityItem e3 = item;
                                e3.rotationPitch += (float)PhysicUtils.rotation;
                            }
                            else {
                                final EntityItem e4 = item;
                                e4.rotationPitch -= (float)PhysicUtils.rotation;
                            }
                        }
                        if (Abstand3 < Abstand2 && Abstand3 < Abstand0 && Abstand3 <= Abstand4) {
                            if (item.rotationPitch - 180.0f < 0.0f) {
                                final EntityItem e5 = item;
                                e5.rotationPitch += (float)PhysicUtils.rotation;
                            }
                            else {
                                final EntityItem e6 = item;
                                e6.rotationPitch -= (float)PhysicUtils.rotation;
                            }
                        }
                        if (Abstand4 < Abstand2 && Abstand4 < Abstand3 && Abstand4 < Abstand0) {
                            if (item.rotationPitch - 270.0f < 0.0f) {
                                final EntityItem e7 = item;
                                e7.rotationPitch += (float)PhysicUtils.rotation;
                            }
                            else {
                                final EntityItem e8 = item;
                                e8.rotationPitch -= (float)PhysicUtils.rotation;
                            }
                        }
                    }
                }
                else {
                    final BlockPos posUp = new BlockPos((Entity)item);
                    posUp.add(0.0, 0.20000000298023224, 0.0);
                    final Material m1 = BlockUtils.getMaterial(posUp);
                    final Material m2 = BlockUtils.getMaterial(pos);
                    final boolean m3 = item.isInsideOfMaterial(Material.WATER);
                    final boolean m4 = item.isInWater();
                    if (m3 | m1 == Material.WATER | m2 == Material.WATER | m4) {
                        final EntityItem tmp748_746 = item;
                        tmp748_746.rotationPitch += (float)(PhysicUtils.rotation / 4.0);
                    }
                    else {
                        final EntityItem tmp770_768 = item;
                        tmp770_768.rotationPitch += (float)(PhysicUtils.rotation * 2.0);
                    }
                }
            }
            GL11.glRotatef(item.rotationYaw, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(item.rotationPitch + 90.0f, 1.0f, 0.0f, 0.0f);
            for (int j = 0; j < i; ++j) {
                if (ibakedmodel.isAmbientOcclusion()) {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(0.7f, 0.7f, 0.7f);
                    PhysicUtils.renderItem.renderItem(itemstack, ibakedmodel);
                    GlStateManager.popMatrix();
                }
                else {
                    GlStateManager.pushMatrix();
                    if (j > 0 && shouldSpreadItems()) {
                        GlStateManager.translate(0.0f, 0.0f, 0.046875f * j);
                    }
                    PhysicUtils.renderItem.renderItem(itemstack, ibakedmodel);
                    if (!shouldSpreadItems()) {
                        GlStateManager.translate(0.0f, 0.0f, 0.046875f);
                    }
                    GlStateManager.popMatrix();
                }
            }
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            PhysicUtils.mc.getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            if (flag) {
                PhysicUtils.mc.getRenderManager().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
            }
        }
    }
    
    public static int transformModelCount(final EntityItem item, final double x, final double y, final double z, final float p_177077_8_, final IBakedModel p_177077_9_) {
        final ItemStack itemstack = item.getItem();
        final Item item2 = itemstack.getItem();
        if (item2 == null) {
            return 0;
        }
        final boolean flag = p_177077_9_.isAmbientOcclusion();
        final int i = getModelCount(itemstack);
        final float f1 = 0.25f;
        final float f2 = 0.0f;
        GlStateManager.translate((float)x, (float)y + f2 + 0.25f, (float)z);
        float f3 = 0.0f;
        if (flag || (PhysicUtils.mc.getRenderManager().renderEngine != null && PhysicUtils.mc.gameSettings.fancyGraphics)) {
            GlStateManager.rotate(f3, 0.0f, 1.0f, 0.0f);
        }
        if (!flag) {
            f3 = -0.0f * (i - 1) * 0.5f;
            final float f4 = -0.0f * (i - 1) * 0.5f;
            final float f5 = -0.046875f * (i - 1) * 0.5f;
            GlStateManager.translate(f3, f4, f5);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        return i;
    }
    
    public static int getModelCount(final ItemStack stack) {
        byte b0 = 1;
        if (stack.getAnimationsToGo() > 48) {
            b0 = 5;
        }
        else if (stack.getAnimationsToGo() > 32) {
            b0 = 4;
        }
        else if (stack.getAnimationsToGo() > 16) {
            b0 = 3;
        }
        else if (stack.getAnimationsToGo() > 1) {
            b0 = 2;
        }
        return b0;
    }
    
    public static byte getMiniBlockCount(final ItemStack stack, final byte original) {
        return original;
    }
    
    public static byte getMiniItemCount(final ItemStack stack, final byte original) {
        return original;
    }
    
    public static boolean shouldSpreadItems() {
        return true;
    }
    
    public static double formPositiv(final float rotationPitch) {
        if (rotationPitch > 0.0f) {
            return rotationPitch;
        }
        return -rotationPitch;
    }
    
    static {
        PhysicUtils.random = new Random();
        PhysicUtils.mc = Minecraft.getMinecraft();
        PhysicUtils.renderItem = PhysicUtils.mc.getRenderItem();
        RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    }
}
