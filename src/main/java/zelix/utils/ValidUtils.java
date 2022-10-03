package zelix.utils;

import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import zelix.hack.*;
import zelix.hack.hacks.*;
import zelix.managers.*;

public class ValidUtils
{
    public static boolean isLowHealth(final EntityLivingBase entity, final EntityLivingBase entityPriority) {
        return entityPriority == null || entity.getHealth() < entityPriority.getHealth();
    }
    
    public static boolean isClosest(final EntityLivingBase entity, final EntityLivingBase entityPriority) {
        return entityPriority == null || Wrapper.INSTANCE.player().getDistance((Entity)entity) < Wrapper.INSTANCE.player().getDistance((Entity)entityPriority);
    }
    
    public static boolean isInAttackFOV(final EntityLivingBase entity, final int fov) {
        return Utils.getDistanceFromMouse(entity) <= fov;
    }
    
    public static boolean isInAttackRange(final EntityLivingBase entity, final float range) {
        return entity.getDistance((Entity)Wrapper.INSTANCE.player()) <= range;
    }
    
    public static boolean isValidEntity(final EntityLivingBase e) {
        final Hack targets = HackManager.getHack("Targets");
        if (targets.isToggled()) {
            if (targets.isToggledValue("Players") && e instanceof EntityPlayer) {
                if (!targets.isToggledValue("Sleeping")) {
                    if (e.isPlayerSleeping()) {
                        return true;
                    }
                }
                return false;
            }
            if (targets.isToggledValue("Mobs") && e instanceof EntityLiving) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean pingCheck(final EntityLivingBase entity) {
        final Hack hack = HackManager.getHack("AntiBot");
        return !hack.isToggled() || !hack.isToggledValue("PingCheck") || !(entity instanceof EntityPlayer) || (Wrapper.INSTANCE.mc().getConnection().getPlayerInfo(entity.getUniqueID()) != null && Wrapper.INSTANCE.mc().getConnection().getPlayerInfo(entity.getUniqueID()).getResponseTime() > 5);
    }
    
    public static boolean isBot(final EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            final Hack hack = HackManager.getHack("AntiBot");
            return hack.isToggled() && AntiBot.isBot((Entity)player);
        }
        return false;
    }
    
    public static boolean isFriendEnemy(final EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            final String ID = Utils.getPlayerName(player);
            if (FriendManager.friendsList.contains(ID)) {
                return false;
            }
            if (HackManager.getHack("Enemys").isToggled() && !EnemyManager.enemysList.contains(ID)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isTeam(final EntityLivingBase entity) {
        final Hack teams = HackManager.getHack("Teams");
        if (teams.isToggled() && entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            if (teams.isToggledMode("Base") && player.getTeam() != null && Wrapper.INSTANCE.player().getTeam() != null && player.getTeam().isSameTeam(Wrapper.INSTANCE.player().getTeam())) {
                return false;
            }
            if (teams.isToggledMode("ArmorColor") && !Utils.checkEnemyColor(player)) {
                return false;
            }
            if (teams.isToggledMode("NameColor") && !Utils.checkEnemyNameColor((EntityLivingBase)player)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isInvisible(final EntityLivingBase entity) {
        final Hack targets = HackManager.getHack("Targets");
        return targets.isToggledValue("Invisibles") || !entity.isInvisible();
    }
    
    public static boolean isNoScreen() {
        return Utils.screenCheck();
    }
}
