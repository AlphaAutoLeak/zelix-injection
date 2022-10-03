package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.entity.*;
import java.util.*;
import zelix.managers.*;
import org.lwjgl.input.*;
import net.minecraft.client.*;
import net.minecraft.entity.item.*;
import java.awt.*;
import java.util.List;

import net.minecraft.util.math.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import net.minecraftforge.client.event.*;
import net.minecraft.entity.boss.*;
import zelix.utils.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;

public class Hitbox extends Hack
{
    public NumberValue width;
    public NumberValue height;
    public BooleanValue walls;
    public static NumberValue expand;
    public static BooleanValue extra;
    public static BooleanValue nofire;
    public static NumberValue extraV;
    public ModeValue mode;
    private static RayTraceResult mv;
    NumberValue multiplier;
    NumberValue FOV;
    BooleanValue showbox;
    int getInput;
    
    public Hitbox() {
        super("HitBox", HackCategory.COMBAT);
        this.getInput = 2;
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("Basic", true), new Mode("Box", false), new Mode("Raven", false) });
        this.width = new NumberValue("Width", 1.0, 0.6, 5.0);
        this.height = new NumberValue("Height", 2.2, 1.8, 5.0);
        Hitbox.expand = new NumberValue("Expand", 0.1, 1.0, 2.0);
        Hitbox.extra = new BooleanValue("Extra", Boolean.valueOf(false));
        Hitbox.nofire = new BooleanValue("NoFire", Boolean.valueOf(false));
        Hitbox.extraV = new NumberValue("ExtraExpand", 0.0, 0.0, 15.0);
        this.FOV = new NumberValue("FOV", 90.0, 1.0, 180.0);
        this.multiplier = new NumberValue("Multiplier", 1.2, 1.0, 5.0);
        this.showbox = new BooleanValue("ShowBox", Boolean.valueOf(true));
        this.walls = new BooleanValue("ThroughWalls", Boolean.valueOf(false));
        this.addValue(Hitbox.extra, this.width, this.height, Hitbox.expand, Hitbox.extraV, Hitbox.nofire, this.multiplier, this.showbox, this.walls, this.mode);
    }
    
    @Override
    public String getDescription() {
        return "Change size hit box of entity.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.mode.getMode("Basic").isToggled()) {
            for (final Object object : Utils.getEntityList()) {
                if (!(object instanceof EntityLivingBase)) {
                    continue;
                }
                final EntityLivingBase entity = (EntityLivingBase)object;
                if (!this.check(entity)) {
                    continue;
                }
                if (Hitbox.nofire.getValue()) {
                    entity.setFire(0);
                }
                Utils.setEntityBoundingBoxSize((Entity)entity, (float)(Object)this.width.getValue(), (float)(Object)this.height.getValue());
            }
        }
        else if (this.mode.getMode("Box").isToggled()) {
            final List<Entity> loadedEntityList = Wrapper.INSTANCE.world().loadedEntityList;
            for (int i = 0; i < loadedEntityList.size(); ++i) {
                final Entity e = loadedEntityList.get(i);
                if (this.isValidEntity(e)) {
                    if (Hitbox.nofire.getValue()) {
                        e.setFire(0);
                    }
                    e.width = (float)(Hitbox.extra.getValue() ? (0.6 + Hitbox.expand.getValue() + Hitbox.extraV.getValue()) : (0.6 + Hitbox.expand.getValue()));
                }
            }
        }
        else if (this.mode.getMode("Raven").isToggled()) {
            final List<Entity> loadedEntityList = Wrapper.INSTANCE.world().loadedEntityList;
            for (int i = 0; i < loadedEntityList.size(); ++i) {
                final Entity e = loadedEntityList.get(i);
                if (this.isValidEntity(e)) {
                    e.height = (float)(Object)this.multiplier.getValue();
                    e.width = (float)(Object)this.multiplier.getValue();
                }
            }
        }
        super.onClientTick(event);
    }
    
    @Override
    public void onDisable() {
        for (final Object object : Utils.getEntityList()) {
            if (!(object instanceof EntityLivingBase)) {
                continue;
            }
            final EntityLivingBase entity = (EntityLivingBase)object;
            final EntitySize entitySize = this.getEntitySize((Entity)entity);
            Utils.setEntityBoundingBoxSize((Entity)entity, entitySize.width, entitySize.height);
        }
        super.onDisable();
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (this.mode.getMode("Raven").isToggled()) {
            Utils.nullCheck();
            final Hack click = HackManager.getHack("AutoClicker");
            if (click != null && !click.isToggled()) {
                return;
            }
            if (click != null && click.isToggled() && Mouse.isButtonDown(0) && Hitbox.mv != null) {
                Minecraft.getMinecraft().objectMouseOver = Hitbox.mv;
            }
        }
        super.onPlayerTick(event);
    }
    
    @Override
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (this.mode.getMode("Raven").isToggled() && this.showbox.getValue()) {
            for (final Entity en : Wrapper.INSTANCE.world().loadedEntityList) {
                if (en != Wrapper.INSTANCE.player() && en instanceof EntityLivingBase && ((EntityLivingBase)en).deathTime == 0 && !(en instanceof EntityArmorStand) && !en.isInvisible()) {
                    this.rh(en, Color.WHITE, event.getPartialTicks());
                }
            }
        }
        super.onRenderWorldLast(event);
    }
    
    private void rh(final Entity e, final Color c, final float partialTicks) {
        if (e instanceof EntityLivingBase) {
            final double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * partialTicks - Wrapper.INSTANCE.mc().getRenderManager().viewerPosX;
            final double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * partialTicks - Wrapper.INSTANCE.mc().getRenderManager().viewerPosY;
            final double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * partialTicks - Wrapper.INSTANCE.mc().getRenderManager().viewerPosZ;
            final float ex = e.getCollisionBorderSize() * this.getInput;
            final AxisAlignedBB bbox = e.getEntityBoundingBox().expand((double)ex, (double)ex, (double)ex);
            final AxisAlignedBB axis = new AxisAlignedBB(bbox.minX - e.posX + x, bbox.minY - e.posY + y, bbox.minZ - e.posZ + z, bbox.maxX - e.posX + x, bbox.maxY - e.posY + y, bbox.maxZ - e.posZ + z);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glLineWidth(2.0f);
            GL11.glColor3d((double)c.getRed(), (double)c.getGreen(), (double)c.getBlue());
            RenderGlobal.drawSelectionBoundingBox(axis, (float)c.getRed(), (float)c.getGreen(), (float)c.getBlue(), (float)c.getAlpha());
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
        }
    }
    
    @Override
    public void onMouse(final MouseEvent event) {
        Utils.nullCheck();
        if (event.getButton() == 0 && event.isButtonstate() && Hitbox.mv != null) {
            Wrapper.INSTANCE.mc().objectMouseOver = Hitbox.mv;
        }
        super.onMouse(event);
    }
    
    public EntitySize getEntitySize(final Entity entity) {
        EntitySize entitySize = new EntitySize(0.6f, 1.8f);
        if (entity instanceof EntitySpider) {
            entitySize = new EntitySize(1.4f, 0.9f);
        }
        if (entity instanceof EntityBat) {
            entitySize = new EntitySize(0.5f, 0.9f);
        }
        if (entity instanceof EntityChicken) {
            entitySize = new EntitySize(0.5f, 0.9f);
        }
        if (entity instanceof EntityCow) {
            entitySize = new EntitySize(0.9f, 1.4f);
        }
        if (entity instanceof EntitySheep) {
            entitySize = new EntitySize(0.9f, 1.4f);
        }
        if (entity instanceof EntityEnderman) {
            entitySize = new EntitySize(0.6f, 2.9f);
        }
        if (entity instanceof EntityGhast) {
            entitySize = new EntitySize(4.0f, 4.0f);
        }
        if (entity instanceof EntityEndermite) {
            entitySize = new EntitySize(0.4f, 0.3f);
        }
        if (entity instanceof EntityGiantZombie) {
            entitySize = new EntitySize(3.6000001f, 10.799999f);
        }
        if (entity instanceof EntityWolf) {
            entitySize = new EntitySize(0.6f, 0.85f);
        }
        if (entity instanceof EntityGuardian) {
            entitySize = new EntitySize(0.85f, 0.85f);
        }
        if (entity instanceof EntitySquid) {
            entitySize = new EntitySize(0.8f, 0.8f);
        }
        if (entity instanceof EntityDragon) {
            entitySize = new EntitySize(16.0f, 8.0f);
        }
        if (entity instanceof EntityRabbit) {
            entitySize = new EntitySize(0.4f, 0.5f);
        }
        return entitySize;
    }
    
    public Entity entity() {
        Entity e = null;
        if (Wrapper.INSTANCE.mc().player.world != null) {
            for (final Object o : Wrapper.INSTANCE.world().loadedEntityList) {
                e = (Entity)o;
            }
        }
        return e;
    }
    
    public boolean check(final EntityLivingBase entity) {
        return !(entity instanceof EntityArmorStand) && !ValidUtils.isValidEntity(entity) && entity != Wrapper.INSTANCE.player() && !entity.isDead && ValidUtils.isFriendEnemy(entity) && ValidUtils.isTeam(entity) && ValidUtils.isBot(entity) && (!this.walls.getValue() || !Wrapper.INSTANCE.player().canEntityBeSeen((Entity)entity)) && entity.canBeCollidedWith();
    }
    
    private boolean isValidEntity(final Entity entity) {
        if (entity instanceof EntityLivingBase) {
            if (entity.isDead || ((EntityLivingBase)entity).getHealth() <= 0.0f || !entity.canBeCollidedWith()) {
                return false;
            }
            final Hack targets = HackManager.getHack("Targets");
            if (entity != Wrapper.INSTANCE.player() && !Wrapper.INSTANCE.player().isDead && !(entity instanceof EntityArmorStand) && !(entity instanceof EntitySnowman)) {
                if (targets.isToggled() && entity instanceof EntityPlayer && targets.isToggledValue("Players")) {
                    return (Wrapper.INSTANCE.player().canEntityBeSeen(entity) || this.walls.getValue()) && (!targets.isToggled() || !entity.isInvisible() || targets.isToggledValue("Invisibles")) && !AntiBot.isBot(entity) && ValidUtils.isFriendEnemy((EntityLivingBase)entity);
                }
                if ((entity instanceof EntityMob || entity instanceof EntitySlime) && targets.isToggledValue("Mobs") && targets.isToggled()) {
                    return (Wrapper.INSTANCE.player().canEntityBeSeen(entity) || this.walls.getValue()) && !AntiBot.isBot(entity);
                }
                if ((entity instanceof EntityAnimal || entity instanceof EntityVillager) && targets.isToggledValue("Mobs") && targets.isToggled()) {
                    return (Wrapper.INSTANCE.player().canEntityBeSeen(entity) || this.walls.getValue()) && !AntiBot.isBot(entity);
                }
            }
        }
        return false;
    }
    
    class EntitySize
    {
        public float width;
        public float height;
        
        public EntitySize(final float width, final float height) {
            this.width = width;
            this.height = height;
        }
    }
}
