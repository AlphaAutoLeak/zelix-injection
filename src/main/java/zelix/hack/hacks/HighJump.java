package zelix.hack.hacks;

import net.minecraft.client.*;
import zelix.hack.*;
import zelix.utils.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.hooks.visual.*;
import zelix.managers.*;
import net.minecraft.client.entity.*;

public class HighJump extends Hack
{
    NumberValue High;
    Minecraft mc;
    
    public HighJump() {
        super("HighJump", HackCategory.MOVEMENT);
        this.mc = Wrapper.INSTANCE.mc();
        this.High = new NumberValue("Height", 2.0, 1.1, 10.0);
        this.addValue(this.High);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent e) {
        if (this.mc.player.hurtTime > 0 && this.mc.player.onGround) {
            final EntityPlayerSP player = this.mc.player;
            player.motionY += 0.42f * this.High.value;
            ChatUtils.message("[AutoDisable] HighJump");
            HackManager.getHack("HighJump").toggle();
        }
        super.onPlayerTick(e);
    }
}
