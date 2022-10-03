package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import zelix.utils.system.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.entity.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.settings.*;

public class AntiSneak extends Hack
{
    public BooleanValue fullSprint;
    
    public AntiSneak() {
        super("AntiSneak", HackCategory.MOVEMENT);
        this.fullSprint = new BooleanValue("FullSprint", Boolean.valueOf(true));
        this.addValue(this.fullSprint);
    }
    
    @Override
    public String getDescription() {
        return "Does not change walking speed when sneak.";
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (side == Connection.Side.OUT && packet instanceof CPacketEntityAction) {
            final CPacketEntityAction p = (CPacketEntityAction)packet;
            if (p.getAction() == CPacketEntityAction.Action.START_SNEAKING) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        final GameSettings settings = Wrapper.INSTANCE.mcSettings();
        if (player.onGround && settings.keyBindSneak.isKeyDown()) {
            if (!this.fullSprint.getValue() && settings.keyBindForward.isKeyDown()) {
                player.setSprinting(Utils.isMoving((Entity)player));
            }
            else if (this.fullSprint.getValue()) {
                player.setSprinting(Utils.isMoving((Entity)player));
            }
            if (settings.keyBindRight.isKeyDown() || settings.keyBindLeft.isKeyDown() || settings.keyBindBack.isKeyDown()) {
                if (settings.keyBindBack.isKeyDown()) {
                    final EntityPlayerSP entityPlayerSP = player;
                    entityPlayerSP.motionX *= 1.268;
                    final EntityPlayerSP entityPlayerSP2 = player;
                    entityPlayerSP2.motionZ *= 1.268;
                }
                else {
                    final EntityPlayerSP entityPlayerSP3 = player;
                    entityPlayerSP3.motionX *= 1.252;
                    final EntityPlayerSP entityPlayerSP4 = player;
                    entityPlayerSP4.motionZ *= 1.252;
                }
            }
            else {
                final EntityPlayerSP entityPlayerSP5 = player;
                entityPlayerSP5.motionX *= 1.2848;
                final EntityPlayerSP entityPlayerSP6 = player;
                entityPlayerSP6.motionZ *= 1.2848;
            }
        }
        super.onClientTick(event);
    }
}
