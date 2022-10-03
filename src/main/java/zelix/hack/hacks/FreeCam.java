package zelix.hack.hacks;

import zelix.hack.*;
import zelix.utils.system.*;
import net.minecraft.network.play.client.*;
import zelix.utils.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;

public class FreeCam extends Hack
{
    public Entity301 entity301;
    
    public FreeCam() {
        super("FreeCam", HackCategory.VISUAL);
        this.entity301 = null;
    }
    
    @Override
    public String getDescription() {
        return "Allows you to move the camera without moving your character.";
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        return side != Connection.Side.OUT || (!(packet instanceof CPacketPlayer) && !(packet instanceof CPacketPlayer.Position) && !(packet instanceof CPacketPlayer.Rotation) && !(packet instanceof CPacketPlayer.PositionRotation));
    }
    
    @Override
    public void onEnable() {
        if (Wrapper.INSTANCE.player() != null && Wrapper.INSTANCE.world() != null) {
            (this.entity301 = new Entity301((World)Wrapper.INSTANCE.world(), Wrapper.INSTANCE.player().getGameProfile())).setPosition(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ);
            this.entity301.inventory = Wrapper.INSTANCE.inventory();
            this.entity301.rotationPitch = Wrapper.INSTANCE.player().rotationPitch;
            this.entity301.rotationYaw = Wrapper.INSTANCE.player().rotationYaw;
            this.entity301.rotationYawHead = Wrapper.INSTANCE.player().rotationYawHead;
            Wrapper.INSTANCE.world().spawnEntity((Entity)this.entity301);
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        if (this.entity301 != null && Wrapper.INSTANCE.world() != null) {
            Wrapper.INSTANCE.player().setPosition(this.entity301.posX, this.entity301.posY, this.entity301.posZ);
            Wrapper.INSTANCE.player().rotationPitch = this.entity301.rotationPitch;
            Wrapper.INSTANCE.player().rotationYaw = this.entity301.rotationYaw;
            Wrapper.INSTANCE.player().rotationYawHead = this.entity301.rotationYawHead;
            Wrapper.INSTANCE.world().removeEntity((Entity)this.entity301);
            this.entity301 = null;
        }
        super.onDisable();
    }
}
