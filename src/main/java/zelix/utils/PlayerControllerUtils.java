package zelix.utils;

import net.minecraft.entity.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.*;
import net.minecraft.client.network.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import zelix.utils.system.*;
import net.minecraftforge.fml.relauncher.*;
import java.lang.reflect.*;

public class PlayerControllerUtils
{
    public static void setReach(final Entity entity, final double range) {
        final Minecraft mc = Wrapper.INSTANCE.mc();
        final EntityPlayer player = (EntityPlayer)Wrapper.INSTANCE.player();
        if (player == entity) {
            class RangePlayerController extends PlayerControllerMP
            {
                private float range;
                
                public RangePlayerController(final Minecraft mcIn, final NetHandlerPlayClient netHandler) {
                    super(mcIn, netHandler);
                    this.range = 3f;
//                    this.range = (float)Wrapper.INSTANCE.player().getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
                }
                
                public float getBlockReachDistance() {
                    return this.range;
                }
                
                public void setBlockReachDistance(final float range) {
                    this.range = range;
                }
            }
            if (!(Wrapper.INSTANCE.mc().playerController instanceof RangePlayerController)) {
                final GameType gameType = (GameType)ReflectionHelper.getPrivateValue(PlayerControllerMP.class, Wrapper.INSTANCE.mc().playerController, new String[] { Mapping.currentGameType });
                final NetHandlerPlayClient netClient = (NetHandlerPlayClient)ReflectionHelper.getPrivateValue(PlayerControllerMP.class,Wrapper.INSTANCE.mc().playerController, new String[] { Mapping.connection });
                final RangePlayerController controller = new RangePlayerController(mc, netClient);
                final boolean isFlying = player.capabilities.isFlying;
                final boolean allowFlying = player.capabilities.allowFlying;
                controller.setGameType(gameType);
                player.capabilities.isFlying = isFlying;
                player.capabilities.allowFlying = allowFlying;
                Wrapper.INSTANCE.mc().playerController = controller;
            }
            ((RangePlayerController)Wrapper.INSTANCE.mc().playerController).setBlockReachDistance((float)range);
        }
    }
    
    public static void setIsHittingBlock(final boolean isHittingBlock) {
        try {
            final Field field = PlayerControllerMP.class.getDeclaredField(Mapping.isHittingBlock);
            field.setAccessible(true);
            field.setBoolean(Wrapper.INSTANCE.controller(), isHittingBlock);
        }
        catch (Exception ex) {}
    }
    
    public static void setBlockHitDelay(final int blockHitDelay) {
        try {
            final Field field = PlayerControllerMP.class.getDeclaredField(Mapping.blockHitDelay);
            field.setAccessible(true);
            field.setInt(Wrapper.INSTANCE.controller(), blockHitDelay);
        }
        catch (Exception ex) {}
    }
    
    public static float getCurBlockDamageMP() {
        float getCurBlockDamageMP = 0.0f;
        try {
            final Field field = PlayerControllerMP.class.getDeclaredField(Mapping.curBlockDamageMP);
            field.setAccessible(true);
            getCurBlockDamageMP = field.getFloat(Wrapper.INSTANCE.controller());
        }
        catch (Exception ex) {}
        return getCurBlockDamageMP;
    }
    
    public static boolean isMoving2() {
        return Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f;
    }
    
    public static boolean isMoving() {
        return !Wrapper.INSTANCE.player().collidedHorizontally && !Wrapper.INSTANCE.player().isSneaking() && (Wrapper.INSTANCE.player().movementInput.moveForward != 0.0f || Wrapper.INSTANCE.player().movementInput.moveStrafe != 0.0f);
    }
}
