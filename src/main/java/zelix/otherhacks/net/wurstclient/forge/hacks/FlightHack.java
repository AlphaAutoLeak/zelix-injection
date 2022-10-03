package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.fml.common.eventhandler.*;

public final class FlightHack extends Hack
{
    private final SliderSetting speed;
    
    public FlightHack() {
        super("Flight", "Allows you to fly.\n\n¡ìc¡ìlWARNING:¡ìr You will take fall damage\nif you don't use NoFall.");
        this.speed = new SliderSetting("Speed", 1.0, 0.05, 5.0, 0.05, SliderSetting.ValueDisplay.DECIMAL);
        this.setCategory(Category.MOVEMENT);
        this.addSetting(this.speed);
    }
    
    @Override
    public String getRenderName() {
        return this.getName() + " [" + this.speed.getValueString() + "]";
    }
    
    @Override
    protected void onEnable() {
        FlightHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        final EntityPlayerSP player = event.getPlayer();
        player.capabilities.isFlying = false;
        player.motionX = 0.0;
        player.motionY = 0.0;
        player.motionZ = 0.0;
        player.jumpMovementFactor = this.speed.getValueF();
        if (FlightHack.mc.gameSettings.keyBindJump.isKeyDown()) {
            final EntityPlayerSP entityPlayerSP = player;
            entityPlayerSP.motionY += this.speed.getValue();
        }
        if (FlightHack.mc.gameSettings.keyBindSneak.isKeyDown()) {
            final EntityPlayerSP entityPlayerSP2 = player;
            entityPlayerSP2.motionY -= this.speed.getValue();
        }
    }
}
