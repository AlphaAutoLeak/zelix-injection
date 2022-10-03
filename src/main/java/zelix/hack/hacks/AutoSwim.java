package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.client.entity.*;

public class AutoSwim extends Hack
{
    public ModeValue mode;
    
    public AutoSwim() {
        super("AutoSwim", HackCategory.PLAYER);
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("Jump", true), new Mode("Dolphin", false), new Mode("Fish", false) });
        this.addValue(this.mode);
    }
    
    @Override
    public String getDescription() {
        return "Jumps automatically when you in water.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (!Wrapper.INSTANCE.player().isInWater() && !Wrapper.INSTANCE.player().isInLava()) {
            return;
        }
        if (Wrapper.INSTANCE.player().isSneaking() || Wrapper.INSTANCE.mcSettings().keyBindJump.isKeyDown()) {
            return;
        }
        if (this.mode.getMode("Jump").isToggled()) {
            Wrapper.INSTANCE.player().jump();
        }
        else if (this.mode.getMode("Dolphin").isToggled()) {
            final EntityPlayerSP player = Wrapper.INSTANCE.player();
            player.motionY += 0.03999999910593033;
        }
        else if (this.mode.getMode("Fish").isToggled()) {
            final EntityPlayerSP player2 = Wrapper.INSTANCE.player();
            player2.motionY += 0.019999999552965164;
        }
        super.onClientTick(event);
    }
}
