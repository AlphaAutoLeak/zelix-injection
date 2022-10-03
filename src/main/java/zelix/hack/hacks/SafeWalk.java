package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.settings.*;
import net.minecraft.client.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.block.*;

public class SafeWalk extends Hack
{
    BooleanValue sneak;
    public NumberValue edgeSpeed;
    
    public SafeWalk() {
        super("SafeWalk", HackCategory.MOVEMENT);
        this.sneak = new BooleanValue("Eagle", Boolean.valueOf(false));
        this.edgeSpeed = new NumberValue("EdgeSpeed", 0.0, 0.0, 2.0);
        this.addValue(this.sneak, this.edgeSpeed);
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        if (getBlockUnderPlayer((EntityPlayer)player) instanceof BlockAir && player.onGround && player.getLookVec().y < -0.6660000085830688) {
            if (this.sneak.getValue()) {
                KeyBinding.setKeyBindState(Wrapper.INSTANCE.mc().gameSettings.keyBindSneak.getKeyCode(), true);
            }
            else {
                player.motionX = ((player.motionX > 0.0) ? 1 : -1) * this.edgeSpeed.getValue();
                player.motionZ = ((player.motionZ > 0.0) ? 1 : -1) * this.edgeSpeed.getValue();
            }
        }
        else if (this.sneak.getValue()) {
            KeyBinding.setKeyBindState(Wrapper.INSTANCE.mc().gameSettings.keyBindSneak.getKeyCode(), false);
        }
        super.onPlayerTick(event);
    }
    
    public static Block getBlock(final BlockPos pos) {
        return Wrapper.INSTANCE.world().getBlockState(pos).getBlock();
    }
    
    public static Block getBlockUnderPlayer(final EntityPlayer player) {
        return getBlock(new BlockPos(player.posX, player.posY - 1.0, player.posZ));
    }
}
