package zelix.utils;

import net.minecraft.client.entity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import com.mojang.authlib.*;
import net.minecraft.entity.*;

public class Entity301 extends EntityOtherPlayerMP
{
    private static MovementInput movementInput;
    
    public Entity301(final World worldIn, final GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }
    
    public void setMovementInput(final MovementInput movementInput) {
        Entity301.movementInput = movementInput;
        if (movementInput.jump && this.onGround) {
            super.jump();
        }
        super.moveRelative(movementInput.moveStrafe, this.moveVertical, movementInput.moveForward, this.movedDistance);
    }
    
    public void move(final MoverType type, final double x, final double y, final double z) {
        this.onGround = true;
        super.move(type, x, y, z);
        this.onGround = true;
    }
    
    public boolean isSneaking() {
        return false;
    }
    
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.noClip = true;
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.noClip = false;
    }
    
    static {
        Entity301.movementInput = null;
    }
}
