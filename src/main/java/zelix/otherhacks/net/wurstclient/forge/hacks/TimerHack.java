package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.util.*;
import java.lang.reflect.*;

public final class TimerHack extends Hack
{
    private final SliderSetting speed;
    
    public TimerHack() {
        super("Timer", "Changes the speed of almost everything.");
        this.speed = new SliderSetting("Speed", 2.0, 0.1, 20.0, 0.1, SliderSetting.ValueDisplay.DECIMAL);
        this.setCategory(Category.OTHER);
        this.addSetting(this.speed);
    }
    
    @Override
    public String getRenderName() {
        return this.getName() + " [" + this.speed.getValueString() + "]";
    }
    
    @Override
    protected void onEnable() {
        TimerHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        this.setTickLength(50.0f);
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        this.setTickLength(50.0f / this.speed.getValueF());
    }
    
    private void setTickLength(final float tickLength) {
        try {
            final Field fTimer = TimerHack.mc.getClass().getDeclaredField(TimerHack.wurst.isObfuscated() ? "timer" : "timer");
            fTimer.setAccessible(true);
            if ("1.12.2".equals("1.10.2")) {
                final Field fTimerSpeed = Timer.class.getDeclaredField(TimerHack.wurst.isObfuscated() ? "field_74278_d" : "timerSpeed");
                fTimerSpeed.setAccessible(true);
                fTimerSpeed.setFloat(fTimer.get(TimerHack.mc), 50.0f / tickLength);
            }
            else {
                final Field fTickLength = Timer.class.getDeclaredField(TimerHack.wurst.isObfuscated() ? "tickLength" : "tickLength");
                fTickLength.setAccessible(true);
                fTickLength.setFloat(fTimer.get(TimerHack.mc), tickLength);
            }
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
