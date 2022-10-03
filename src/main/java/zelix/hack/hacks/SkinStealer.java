package zelix.hack.hacks;

import net.minecraft.entity.player.*;
import zelix.hack.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.item.*;
import org.lwjgl.input.*;
import com.mojang.authlib.minecraft.*;
import zelix.managers.*;
import net.minecraft.entity.*;

public class SkinStealer extends Hack
{
    public EntityPlayer currentPlayer;
    
    public SkinStealer() {
        super("SkinStealer", HackCategory.ANOTHER);
    }
    
    @Override
    public String getDescription() {
        return "Left click on player - steal skin.";
    }
    
    @Override
    public void onDisable() {
        this.currentPlayer = null;
        super.onDisable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        final RayTraceResult object = Wrapper.INSTANCE.mc().objectMouseOver;
        if (object == null) {
            return;
        }
        if (object.typeOfHit == RayTraceResult.Type.ENTITY) {
            final Entity entity = object.entityHit;
            if (entity instanceof EntityPlayer && !(entity instanceof EntityArmorStand) && !Wrapper.INSTANCE.player().isDead && Wrapper.INSTANCE.player().canEntityBeSeen(entity)) {
                final EntityPlayer player = (EntityPlayer)entity;
                if (Mouse.isButtonDown(0) && Wrapper.INSTANCE.mc().currentScreen == null && player != this.currentPlayer && player.getGameProfile() != null) {
                    SkinChangerManager.addTexture(MinecraftProfileTexture.Type.SKIN, player.getGameProfile().getName());
                    this.currentPlayer = player;
                }
            }
        }
        super.onClientTick(event);
    }
}
