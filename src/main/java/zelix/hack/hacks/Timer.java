package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.*;
import zelix.utils.system.*;
import java.lang.reflect.*;

public class Timer extends Hack
{
    NumberValue ticks;
    
    public Timer() {
        super("Timer", HackCategory.PLAYER);
        this.ticks = new NumberValue("Ticks", 2.0, 1.0, 10.0);
        this.addValue(this.ticks);
    }
    
    @Override
    public void onEnable() {
        this.setTickLength(50.0f / (float)(Object)this.ticks.getValue());
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.setTickLength(50.0f);
        super.onEnable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        this.setTickLength(50.0f / (float)(Object)this.ticks.getValue());
        super.onClientTick(event);
    }
    
    private void setTickLength(final float tickLength) {
        try {
            final Field fTimer = Minecraft.getMinecraft().getClass().getDeclaredField(Mapping.timer);
            fTimer.setAccessible(true);
            final Field fTickLength = net.minecraft.util.Timer.class.getDeclaredField(Mapping.tickLength);
            fTickLength.setAccessible(true);
            fTickLength.setFloat(fTimer.get(Minecraft.getMinecraft()), tickLength);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
