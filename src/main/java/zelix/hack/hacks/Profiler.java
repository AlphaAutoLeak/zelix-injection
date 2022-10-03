package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.client.event.*;
import net.minecraft.entity.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.client.*;
import zelix.managers.*;
import org.lwjgl.opengl.*;
import zelix.utils.hooks.visual.*;
import zelix.utils.*;
import java.awt.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.gui.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import java.util.*;
import java.util.List;

import net.minecraft.init.*;
import net.minecraft.client.renderer.*;
import net.minecraft.enchantment.*;

public class Profiler extends Hack
{
    public BooleanValue armor;
    
    public Profiler() {
        super("Profiler", HackCategory.VISUAL);
        this.armor = new BooleanValue("Armor", Boolean.valueOf(true));
        this.addValue(this.armor);
    }
    
    @Override
    public String getDescription() {
        return "Allows you to see armor of player or info of item.";
    }
    
    @Override
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        for (final Object object : Utils.getEntityList()) {
            if (object instanceof EntityLivingBase) {
                final EntityLivingBase entity = (EntityLivingBase)object;
                final RenderManager renderManager = Wrapper.INSTANCE.mc().getRenderManager();
                final double renderPosX = renderManager.viewerPosX;
                final double renderPosY = renderManager.viewerPosY;
                final double renderPosZ = renderManager.viewerPosZ;
                final double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - renderPosX;
                final double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - renderPosY;
                final double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - renderPosZ;
                this.renderNameTag(entity, entity.getName(), xPos, yPos, zPos);
            }
        }
        super.onRenderWorldLast(event);
    }
    
    void renderNameTag(final EntityLivingBase entity, String tag, final double x, double y, final double z) {
        if (entity instanceof EntityArmorStand || ValidUtils.isValidEntity(entity) || entity == Wrapper.INSTANCE.player()) {
            return;
        }
        int color = ColorUtils.color(200, 200, 200, 160);
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        y += (entity.isSneaking() ? 0.5 : 0.7);
        float distance = player.getDistance((Entity)entity) / 4.0f;
        if (distance < 1.6f) {
            distance = 1.6f;
        }
        if (entity instanceof EntityPlayer) {
            final EntityPlayer entityPlayer = (EntityPlayer)entity;
            final String ID = Utils.getPlayerName(entityPlayer);
            if (EnemyManager.enemysList.contains(ID)) {
                tag = "¡ìc" + ID;
                color = ColorUtils.color(179, 20, 20, 160);
            }
            if (FriendManager.friendsList.contains(ID)) {
                tag = "¡ì3" + ID;
                color = ColorUtils.color(66, 147, 179, 160);
            }
            if (ValidUtils.isBot((EntityLivingBase)entityPlayer)) {
                tag = "¡ìe" + ID;
                color = ColorUtils.color(200, 200, 0, 160);
            }
        }
        final int health = (int)entity.getHealth();
        if (health <= entity.getMaxHealth() * 0.25) {
            tag += "¡ì4";
        }
        else if (health <= entity.getMaxHealth() * 0.5) {
            tag += "¡ì6";
        }
        else if (health <= entity.getMaxHealth() * 0.75) {
            tag += "¡ìe";
        }
        else if (health <= entity.getMaxHealth()) {
            tag += "¡ì2";
        }
        tag = String.valueOf(tag) + " " + Math.round(health);
        final RenderManager renderManager = Wrapper.INSTANCE.mc().getRenderManager();
        float scale = distance;
        scale /= 30.0f;
        scale *= 0.3;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y + 1.4f, (float)z);
        GL11.glNormal3f(1.0f, 1.0f, 1.0f);
        GL11.glRotatef(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        final Tessellator var14 = Tessellator.getInstance();
        final BufferBuilder var15 = var14.getBuffer();
        final int width = fontRenderer.getStringWidth(tag) / 2;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        RenderUtils.drawRect(-width - 2, -(fontRenderer.FONT_HEIGHT + 1), width + 2, 2.0f, color);
        fontRenderer.drawString(tag, (float)(MathUtils.getMiddle(-width - 2, width + 2) - width), (float)(-(fontRenderer.FONT_HEIGHT - 1)), Color.WHITE.getRGB(), true);
        if (entity instanceof EntityPlayer && this.armor.getValue()) {
            final EntityPlayer entityPlayer2 = (EntityPlayer)entity;
            GlStateManager.translate(0.0f, 1.0f, 0.0f);
            this.renderArmor(entityPlayer2, 0, -(fontRenderer.FONT_HEIGHT + 1) - 20);
            GlStateManager.translate(0.0f, -1.0f, 0.0f);
        }
        GL11.glPushMatrix();
        GL11.glPopMatrix();
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    public void renderArmor(final EntityPlayer player, int x, final int y) {
        final InventoryPlayer items = player.inventory;
        final ItemStack inHand = player.getHeldItemMainhand();
        final ItemStack boots = items.armorItemInSlot(0);
        final ItemStack leggings = items.armorItemInSlot(1);
        final ItemStack body = items.armorItemInSlot(2);
        final ItemStack helm = items.armorItemInSlot(3);
        ItemStack[] stuff = null;
        if (inHand != null) {
            stuff = new ItemStack[] { inHand, helm, body, leggings, boots };
        }
        else {
            stuff = new ItemStack[] { helm, body, leggings, boots };
        }
        final List<ItemStack> stacks = new ArrayList<ItemStack>();
        ItemStack[] array;
        for (int length = (array = stuff).length, j = 0; j < length; ++j) {
            final ItemStack i = array[j];
            if (i != null && i.getItem() != null) {
                stacks.add(i);
            }
        }
        final int width = 16 * stacks.size() / 2;
        x -= width;
        GlStateManager.disableDepth();
        for (final ItemStack stack : stacks) {
            this.renderItem(stack, x, y);
            x += 16;
        }
        GlStateManager.enableDepth();
    }
    
    public void renderItem(final ItemStack stack, final int x, int y) {
        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        final RenderItem renderItem = Wrapper.INSTANCE.mc().getRenderItem();
        final EnchantEntry[] enchants = { new EnchantEntry(Enchantments.PROTECTION, "Pro"), new EnchantEntry(Enchantments.THORNS, "Th"), new EnchantEntry(Enchantments.SHARPNESS, "Shar"), new EnchantEntry(Enchantments.FIRE_ASPECT, "Fire"), new EnchantEntry(Enchantments.KNOCKBACK, "Kb"), new EnchantEntry(Enchantments.UNBREAKING, "Unb"), new EnchantEntry(Enchantments.POWER, "Pow"), new EnchantEntry(Enchantments.INFINITY, "Inf"), new EnchantEntry(Enchantments.PUNCH, "Punch") };
        GlStateManager.pushMatrix();
        GlStateManager.pushMatrix();
        final float scale1 = 0.3f;
        GlStateManager.translate((float)(x - 3), (float)(y + 10), 0.0f);
        GlStateManager.scale(0.3f, 0.3f, 0.3f);
        GlStateManager.popMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        renderItem.zLevel = -100.0f;
        GlStateManager.disableDepth();
        renderItem.renderItemIntoGUI(stack, x, y);
        renderItem.renderItemOverlayIntoGUI(fontRenderer, stack, x, y, (String)null);
        GlStateManager.enableDepth();
        EnchantEntry[] array;
        for (int length = (array = enchants).length, i = 0; i < length; ++i) {
            final EnchantEntry enchant = array[i];
            final int level = EnchantmentHelper.getEnchantmentLevel(enchant.getEnchant(), stack);
            String levelDisplay = "" + level;
            if (level > 10) {
                levelDisplay = "10+";
            }
            if (level > 0) {
                final float scale2 = 0.32f;
                GlStateManager.translate((float)(x - 2), (float)(y + 1), 0.0f);
                GlStateManager.scale(0.42f, 0.42f, 0.42f);
                GlStateManager.disableDepth();
                GlStateManager.disableLighting();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                fontRenderer.drawString("¡ìf" + enchant.getName() + " " + levelDisplay, (float)(20 - fontRenderer.getStringWidth("¡ìf" + enchant.getName() + " " + levelDisplay) / 2), 0.0f, Color.WHITE.getRGB(), true);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                GlStateManager.scale(2.42f, 2.42f, 2.42f);
                GlStateManager.translate((float)(-x), (float)(-y), 0.0f);
                y += (int)((fontRenderer.FONT_HEIGHT + 3) * 0.28f);
            }
        }
        renderItem.zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
    }
    
    public static class EnchantEntry
    {
        private Enchantment enchant;
        private String name;
        
        public EnchantEntry(final Enchantment enchant, final String name) {
            this.enchant = enchant;
            this.name = name;
        }
        
        public Enchantment getEnchant() {
            return this.enchant;
        }
        
        public String getName() {
            return this.name;
        }
    }
}
