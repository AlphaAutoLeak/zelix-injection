package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import zelix.managers.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.client.entity.*;

public class Glide extends Hack
{
    public BooleanValue damage;
    public ModeValue mode;
    static int tick;
    static boolean fall;
    static int times;
    TimerUtils timer;
    
    public Glide() {
        super("Glide", HackCategory.MOVEMENT);
        this.damage = new BooleanValue("SelfDamage", Boolean.valueOf(false));
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("Falling", true), new Mode("Flat", false) });
        this.addValue(this.mode, this.damage);
        this.timer = new TimerUtils();
    }
    
    @Override
    public String getDescription() {
        return "Makes you glide down slowly when falling.";
    }
    
    @Override
    public void onEnable() {
        if (this.damage.getValue()) {
            HackManager.getHack("SelfDamage").toggle();
        }
        super.onEnable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        if (this.mode.getMode("Flat").isToggled()) {
            if (!player.capabilities.isFlying && player.fallDistance > 0.0f && !player.isSneaking()) {
                player.motionY = 0.0;
            }
            if (Wrapper.INSTANCE.mcSettings().keyBindSneak.isKeyDown()) {
                player.motionY = -0.11;
            }
            if (Wrapper.INSTANCE.mcSettings().keyBindJump.isKeyDown()) {
                player.motionY = 0.11;
            }
            if (this.timer.delay(50.0f)) {
                player.onGround = false;
                this.timer.setLastMS();
            }
        }
        else if (this.mode.getMode("Falling").isToggled()) {
            if (player.onGround) {
                Glide.times = 0;
            }
            if (player.fallDistance > 0.0f && Glide.times <= 1) {
                if (Glide.tick > 0 && Glide.fall) {
                    player.motionY = 0.0;
                    Glide.tick = 0;
                }
                else {
                    ++Glide.tick;
                }
                if (player.fallDistance >= 0.1) {
                    Glide.fall = false;
                }
                if (player.fallDistance >= 0.4) {
                    Glide.fall = true;
                    player.fallDistance = 0.0f;
                }
            }
        }
        super.onClientTick(event);
    }
}
