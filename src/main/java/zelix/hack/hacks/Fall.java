package zelix.hack.hacks;

import zelix.hack.*;
import zelix.managers.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.util.math.*;
import net.minecraft.block.*;

public class Fall extends Hack
{
    Hack h;
    TimerUtils timer;
    NumberValue height;
    
    public Fall() {
        super("AntiFall", HackCategory.PLAYER);
        this.h = HackManager.getHack("Blink");
        this.timer = new TimerUtils();
        this.height = new NumberValue("Height", 2.0, 1.0, 10.0);
        this.addValue(this.height);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        super.onClientTick(event);
    }
    
    @Override
    public void onDisable() {
        this.h.onDisable();
        this.h.setToggled(false);
        super.onDisable();
    }
    
    @Override
    public void onEnable() {
        Utils.nullCheck();
        this.h.onEnable();
        this.h.setToggled(true);
        this.Jump();
        super.onEnable();
    }
    
    private Boolean isBlockBelow() {
        for (int i = (int)(Wrapper.INSTANCE.player().posY - 1.0); i > 0; --i) {
            final BlockPos pos = new BlockPos(Wrapper.INSTANCE.player().posX, (double)i, Wrapper.INSTANCE.player().posZ);
            if (!(Wrapper.INSTANCE.world().getBlockState(pos).getBlock() instanceof BlockAir)) {
                return true;
            }
        }
        return false;
    }
    
    void Jump() {
        Utils.nullCheck();
        Wrapper.INSTANCE.player().motionY = this.height.getValue();
    }
}
