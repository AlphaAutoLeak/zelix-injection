package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import zelix.utils.system.*;
import net.minecraft.network.play.client.*;
import zelix.utils.*;
import net.minecraft.network.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraft.client.entity.*;

public class Ghost extends Hack
{
    public BooleanValue noWalls;
    
    public Ghost() {
        super("Ghost", HackCategory.PLAYER);
        this.noWalls = new BooleanValue("NoWalls", Boolean.valueOf(true));
        this.addValue(this.noWalls);
    }
    
    @Override
    public String getDescription() {
        return "Allows you to pass through walls.";
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        boolean skip = true;
        if (this.noWalls.getValue() && side == Connection.Side.OUT && packet instanceof CPacketPlayer) {
            skip = false;
        }
        return skip;
    }
    
    @Override
    public void onDisable() {
        Wrapper.INSTANCE.player().noClip = false;
        if (this.noWalls.getValue()) {
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.PositionRotation(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().getEntityBoundingBox().minY, Wrapper.INSTANCE.player().posZ, Wrapper.INSTANCE.player().cameraYaw, Wrapper.INSTANCE.player().cameraPitch, Wrapper.INSTANCE.player().onGround));
        }
        super.onDisable();
    }
    
    @Override
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        Wrapper.INSTANCE.player().noClip = true;
        Wrapper.INSTANCE.player().fallDistance = 0.0f;
        Wrapper.INSTANCE.player().onGround = true;
        Wrapper.INSTANCE.player().capabilities.isFlying = false;
        Wrapper.INSTANCE.player().motionX = 0.0;
        Wrapper.INSTANCE.player().motionY = 0.0;
        Wrapper.INSTANCE.player().motionZ = 0.0;
        final float speed = 0.2f;
        Wrapper.INSTANCE.player().jumpMovementFactor = speed;
        if (Wrapper.INSTANCE.mcSettings().keyBindJump.isKeyDown()) {
            final EntityPlayerSP player = Wrapper.INSTANCE.player();
            player.motionY += speed;
        }
        if (Wrapper.INSTANCE.mcSettings().keyBindSneak.isKeyDown()) {
            final EntityPlayerSP player2 = Wrapper.INSTANCE.player();
            player2.motionY -= speed;
        }
        super.onLivingUpdate(event);
    }
}
