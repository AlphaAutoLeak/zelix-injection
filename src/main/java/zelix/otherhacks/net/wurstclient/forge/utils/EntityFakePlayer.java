package zelix.otherhacks.net.wurstclient.forge.utils;

import net.minecraft.client.entity.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;

public class EntityFakePlayer extends EntityOtherPlayerMP
{
    public EntityFakePlayer() {
        super((World)WMinecraft.getWorld(), WMinecraft.getPlayer().getGameProfile());
        this.copyLocationAndAnglesFrom((Entity)WMinecraft.getPlayer());
        this.inventory.copyInventory(WMinecraft.getPlayer().inventory);
        this.getDataManager().set(EntityPlayer.PLAYER_MODEL_FLAG, WMinecraft.getPlayer().getDataManager().get(EntityPlayer.PLAYER_MODEL_FLAG));
        this.rotationYawHead = WMinecraft.getPlayer().rotationYawHead;
        this.renderYawOffset = WMinecraft.getPlayer().renderYawOffset;
        this.chasingPosX = this.posX;
        this.chasingPosY = this.posY;
        this.chasingPosZ = this.posZ;
        WMinecraft.getWorld().addEntityToWorld(this.getEntityId(), (Entity)this);
    }
    
    public void resetPlayerPosition() {
        WMinecraft.getPlayer().setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
    }
    
    public void despawn() {
        WMinecraft.getWorld().removeEntityFromWorld(this.getEntityId());
    }
}
