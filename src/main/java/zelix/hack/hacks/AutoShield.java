package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.item.*;
import zelix.utils.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.client.settings.*;
import net.minecraft.entity.player.*;
import zelix.managers.*;

public class AutoShield extends Hack
{
    BooleanValue INTERACT;
    
    public AutoShield() {
        super("AutoShield", HackCategory.COMBAT);
        this.INTERACT = new BooleanValue("INTERACT", Boolean.valueOf(false));
        this.addValue(this.INTERACT);
    }
    
    @Override
    public String getDescription() {
        return "Manages your shield automatically.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (!Utils.screenCheck()) {
            this.blockByShield(false);
        }
        super.onClientTick(event);
    }
    
    @Override
    public void onDisable() {
        this.blockByShield(false);
        super.onDisable();
    }
    
    public void blockByShield(final boolean state) {
        if (!(Wrapper.INSTANCE.player().getHeldItemMainhand().getItem() instanceof ItemSword)) {
            return;
        }
        if (this.INTERACT.getValue() && KillAura.getTarget() != null) {
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketUseEntity((Entity)KillAura.getTarget(), EnumHand.MAIN_HAND, new Vec3d(randomNumber(-50, 50) / 100.0, randomNumber(0, 200) / 100.0, randomNumber(-50, 50) / 100.0)));
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketUseEntity((Entity)KillAura.getTarget(), EnumHand.MAIN_HAND));
        }
        if (KillAura.getTarget() != null) {
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            Wrapper.INSTANCE.player().setActiveHand(EnumHand.OFF_HAND);
        }
        else {
            KeyBinding.setKeyBindState(Wrapper.INSTANCE.mc().gameSettings.keyBindUseItem.getKeyCode(), false);
            Wrapper.INSTANCE.controller().onStoppedUsingItem((EntityPlayer)Wrapper.INSTANCE.player());
        }
        KeyBinding.setKeyBindState(Wrapper.INSTANCE.mc().gameSettings.keyBindUseItem.getKeyCode(), false);
    }
    
    public static void block(final boolean state) {
        final AutoShield hack = (AutoShield)HackManager.getHack("AutoShield");
        if (hack.isToggled()) {
            hack.blockByShield(state);
        }
    }
    
    public static int randomNumber(final int max, final int min) {
        return Math.round(min + (float)Math.random() * (max - min));
    }
}
