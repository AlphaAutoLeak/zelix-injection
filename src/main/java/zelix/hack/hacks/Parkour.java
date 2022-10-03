package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.entity.*;

public class Parkour extends Hack
{
    public Parkour() {
        super("Parkour", HackCategory.PLAYER);
    }
    
    @Override
    public String getDescription() {
        return "Jump when reaching a block's edge.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Utils.isBlockEdge((EntityLivingBase)Wrapper.INSTANCE.player()) && !Wrapper.INSTANCE.player().isSneaking()) {
            Wrapper.INSTANCE.player().jump();
        }
        super.onClientTick(event);
    }
}
