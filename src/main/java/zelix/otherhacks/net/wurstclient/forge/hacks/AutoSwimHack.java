package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraft.client.settings.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.fml.common.eventhandler.*;

public final class AutoSwimHack extends Hack
{
    private final EnumSetting<Mode> mode;
    
    public AutoSwimHack() {
        super("AutoSwim", "Makes you swim automatically.");
        this.mode = new EnumSetting<Mode>("Mode", Mode.values(), Mode.DOLPHIN);
        this.setCategory(Category.MOVEMENT);
        this.addSetting(this.mode);
    }
    
    @Override
    protected void onEnable() {
        AutoSwimHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        final EntityPlayerSP player = event.getPlayer();
        if (player.isInWater() && !player.isSneaking() && !GameSettings.isKeyDown(AutoSwimHack.mc.gameSettings.keyBindJump)) {
            final EntityPlayerSP entityPlayerSP = player;
            entityPlayerSP.motionY += this.mode.getSelected().upwardsMotion;
        }
    }
    
    private enum Mode
    {
        DOLPHIN("Dolphin", 0.04), 
        FISH("Fish", 0.02);
        
        private final String name;
        private final double upwardsMotion;
        
        private Mode(final String name, final double upwardsMotion) {
            this.name = name;
            this.upwardsMotion = upwardsMotion;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
}
