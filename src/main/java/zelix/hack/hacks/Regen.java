package zelix.hack.hacks;

import net.minecraft.client.*;
import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.client.event.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import zelix.utils.*;
import zelix.utils.system.*;
import net.minecraft.util.*;
import java.lang.reflect.*;

public class Regen extends Hack
{
    Float ticks;
    Minecraft mc;
    ModeValue mode;
    
    public Regen() {
        super("Regen", HackCategory.PLAYER);
        this.ticks = 0.98f;
        this.mc = Wrapper.INSTANCE.mc();
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("New", true), new Mode("Old", false) });
    }
    
    @Override
    public void onDisable() {
        this.setTickLength(50.0f);
        super.onEnable();
    }
    
    @Override
    public void onInputUpdate(final InputUpdateEvent event) {
        Utils.nullCheck();
        if (this.mode.getMode("New").isToggled()) {
            if (this.mc.player.ticksExisted % 5 == 0) {
                this.setTickLength(50.0f / this.ticks);
                for (int i = 0; i < 10; ++i) {
                    Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer(true));
                }
            }
            else if (MoveUtils.isMoving()) {
                Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer(this.mc.player.onGround));
            }
        }
        else if (this.mode.getMode("Old").isToggled()) {
            if (MoveUtils.isMoving() || !this.mc.player.onGround) {
                return;
            }
            for (int i = 0; i < 9; ++i) {
                Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer(this.mc.player.onGround));
            }
            this.setTickLength(111.111115f);
        }
    }
    
    private void setTickLength(final float tickLength) {
        try {
            final Field fTimer = Minecraft.getMinecraft().getClass().getDeclaredField(Mapping.timer);
            fTimer.setAccessible(true);
            final Field fTickLength = Timer.class.getDeclaredField(Mapping.tickLength);
            fTickLength.setAccessible(true);
            fTickLength.setFloat(fTimer.get(Minecraft.getMinecraft()), tickLength);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
