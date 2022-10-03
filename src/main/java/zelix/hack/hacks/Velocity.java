package zelix.hack.hacks;

import net.minecraft.client.*;
import zelix.hack.*;
import zelix.utils.*;
import zelix.utils.ReflectionHelper;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.init.*;
import net.minecraft.entity.player.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.client.entity.*;
import java.lang.reflect.*;
import zelix.utils.system.*;
import net.minecraft.network.play.server.*;

public class Velocity extends Hack
{
    public ModeValue mode;
    private boolean ishurt;
    private TimerUtils velocitytimer;
    private float velocityTick;
    public static Boolean velocityInput;
    Minecraft mc;
    NumberValue velocityTickValue;
    
    public Velocity() {
        super("Velocity", HackCategory.COMBAT);
        this.velocitytimer = new TimerUtils();
        this.velocityTick = 0.0f;
        this.mc = Wrapper.INSTANCE.mc();
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("NewHyt", false), new Mode("Simple", true), new Mode("AAC4", false), new Mode("Tick", false) });
        this.velocityTickValue = new NumberValue("Tick", 10.0, 0.0, 10.0);
        this.addValue(this.mode, this.velocityTickValue);
    }
    
    @Override
    public String getDescription() {
        return "Prevents you from getting pushed by players, mobs and flowing water.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.mode.getMode("NewHyt").isToggled()) {
            final EntityPlayerSP player = Wrapper.INSTANCE.player();
            if (player.hurtTime > 0 && !player.isDead && player.hurtTime <= 5 && !Wrapper.INSTANCE.player().isInWater() && !this.mc.player.isPotionActive(MobEffects.SPEED)) {
                final EntityPlayerSP entityPlayerSP = player;
                entityPlayerSP.motionX *= 0.1599999964237213;
                final EntityPlayerSP entityPlayerSP2 = player;
                entityPlayerSP2.motionZ *= 0.1599999964237213;
                final EntityPlayerSP entityPlayerSP3 = player;
                entityPlayerSP3.motionY /= 1.4500000476837158;
            }
        }
        else if (this.mode.getMode("AAC4").isToggled()) {
            if (Wrapper.INSTANCE.player().isInWater() || Wrapper.INSTANCE.player().isInLava()) {
                return;
            }
            if (!Wrapper.INSTANCE.player().onGround) {
                if (this.ishurt) {
                    final Field field = ReflectionHelper.findField((Class)EntityPlayer.class, new String[] { "speedInAir", "speedInAir" });
                    try {
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        field.setFloat(Wrapper.INSTANCE.player(), 0.02f);
                    }
                    catch (Exception ex) {}
                    final EntityPlayerSP player2 = Wrapper.INSTANCE.player();
                    player2.motionX *= 0.6;
                    final EntityPlayerSP player3 = Wrapper.INSTANCE.player();
                    player3.motionZ *= 0.6;
                }
            }
            else if (this.velocitytimer.hasReached(80.0f)) {
                this.ishurt = false;
                final Field field = ReflectionHelper.findField((Class)EntityPlayer.class, new String[] { "speedInAir", "speedInAir" });
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    field.setFloat(Wrapper.INSTANCE.player(), 0.02f);
                }
                catch (Exception ex2) {}
            }
        }
        else if (this.mode.getMode("Tick").isToggled()) {
            if (this.velocityTick > (float)(Object)this.velocityTickValue.getValue()) {
                if (this.mc.player.motionY > 0.0) {
                    this.mc.player.motionY = 0.0;
                }
                this.mc.player.motionX = 0.0;
                this.mc.player.motionZ = 0.0;
                this.mc.player.jumpMovementFactor = -1.0E-5f;
                Velocity.velocityInput = false;
            }
            if (this.mc.player.onGround && this.velocityTick > 1.0f) {
                Velocity.velocityInput = false;
            }
        }
        super.onClientTick(event);
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (packet instanceof SPacketEntityVelocity && this.mode.getMode("Simple").isToggled() && Wrapper.INSTANCE.player().hurtTime >= 0) {
            final SPacketEntityVelocity p = (SPacketEntityVelocity)packet;
            if (p.getEntityID() == Wrapper.INSTANCE.player().getEntityId()) {
                return false;
            }
        }
        else if (packet instanceof SPacketEntityVelocity && this.mode.getMode("AAC4").isToggled() && Wrapper.INSTANCE.player().hurtTime >= 0) {
            final SPacketEntityVelocity p = (SPacketEntityVelocity)packet;
            if (p.getEntityID() == Wrapper.INSTANCE.player().getEntityId()) {
                this.ishurt = true;
                this.velocitytimer.reset();
            }
        }
        return true;
    }
    
    static {
        Velocity.velocityInput = false;
    }
}
