package zelix.hack.hacks;

import net.minecraft.client.*;
import zelix.hack.*;
import zelix.utils.*;
import zelix.value.*;
import zelix.utils.system.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;

public class Criticals extends Hack
{
    public ModeValue mode;
    NumberValue delayValue;
    NumberValue hurttime;
    TimerUtils timer;
    boolean cancelSomePackets;
    static double attacks;
    Minecraft mc;
    static boolean antiDesync;
    
    public Criticals() {
        super("Criticals", HackCategory.COMBAT);
        this.timer = new TimerUtils();
        this.mc = Wrapper.INSTANCE.mc();
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("Hyt", true), new Mode("Hyt1", false), new Mode("Packet", false), new Mode("Jump", false), new Mode("PJump", false), new Mode("NoGround", false), new Mode("C08P", false), new Mode("FunkNight", false), new Mode("Vulcan", false), new Mode("HytPacket", false), new Mode("HytPit", false) });
        this.delayValue = new NumberValue("Delay", 0.0, 0.0, 1000.0);
        this.hurttime = new NumberValue("HurtTime", 10.0, 0.0, 10.0);
        this.addValue(this.mode, this.delayValue, this.hurttime);
    }
    
    @Override
    public String getDescription() {
        return "Changes all your hits to critical hits.";
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (Wrapper.INSTANCE.player().onGround && side == Connection.Side.OUT) {
            if (packet instanceof CPacketUseEntity) {
                final CPacketUseEntity attack = (CPacketUseEntity)packet;
                if (attack.getAction() == CPacketUseEntity.Action.ATTACK) {
                    if (this.mode.getMode("Packet").isToggled()) {
                        if (Wrapper.INSTANCE.player().collidedVertically && this.timer.isDelay(500L)) {
                            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + 0.0627, Wrapper.INSTANCE.player().posZ, false));
                            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ, false));
                            final Entity entity = attack.getEntityFromWorld((World)Wrapper.INSTANCE.world());
                            if (entity != null) {
                                Wrapper.INSTANCE.player().onCriticalHit(entity);
                            }
                            this.timer.setLastMS();
                            this.cancelSomePackets = true;
                        }
                    }
                    else if (this.mode.getMode("Hyt1").isToggled()) {
                        final Entity entity = attack.getEntityFromWorld((World)Wrapper.INSTANCE.world());
                        if (entity != null && Wrapper.INSTANCE.player().onGround) {
                            Wrapper.INSTANCE.player().onCriticalHit(entity);
                        }
                    }
                    else if (this.mode.getMode("Hyt").isToggled()) {
                        final Entity entity = attack.getEntityFromWorld((World)Wrapper.INSTANCE.world());
                        if (entity != null && Wrapper.INSTANCE.player().onGround) {
                            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + 0.0031311231111, Wrapper.INSTANCE.player().posZ, false));
                            Wrapper.INSTANCE.player().onCriticalHit(entity);
                        }
                    }
                    else if (this.mode.getMode("Jump").isToggled()) {
                        if (this.canJump()) {
                            Wrapper.INSTANCE.player().jump();
                        }
                    }
                    else if (this.mode.getMode("PJump").isToggled()) {
                        if (this.canJump()) {
                            Wrapper.INSTANCE.player().jump();
                            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + 0.0031311231111, Wrapper.INSTANCE.player().posZ, false));
                        }
                    }
                    else if (this.mode.getMode("NoGround").isToggled()) {
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer(false));
                    }
                    else if (this.mode.getMode("C08P").isToggled()) {
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + 0.05250000001304, Wrapper.INSTANCE.player().posZ, true));
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + 0.01400000001304, Wrapper.INSTANCE.player().posZ, false));
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + 0.00150000001304, Wrapper.INSTANCE.player().posZ, false));
                    }
                    else if (this.mode.getMode("FunkNight").isToggled()) {
                        if (Wrapper.INSTANCE.player().hurtTime > this.hurttime.getValue() && this.timer.hasReached((float)(Object)this.delayValue.getValue())) {
                            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + 0.051400040018, Wrapper.INSTANCE.player().posZ, true));
                            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + 0.0015000018, Wrapper.INSTANCE.player().posZ, false));
                            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + 0.01400000018, Wrapper.INSTANCE.player().posZ, false));
                            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + 0.0015300600018, Wrapper.INSTANCE.player().posZ, false));
                            this.timer.reset();
                        }
                    }
                    else if (this.mode.getMode("HytPacket").isToggled()) {
                        final Entity entity = attack.getEntityFromWorld((World)Wrapper.INSTANCE.world());
                        final double x = this.mc.player.posX;
                        final double y = this.mc.player.posY;
                        final double z = this.mc.player.posZ;
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(x, y + 0.11, z, true));
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(x, y + 0.1100013579, z, false));
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(x, y + 1.3579E-6, z, false));
                        this.mc.player.onCriticalHit(entity);
                    }
                    else if (this.mode.getMode("HytPit").isToggled()) {
                        final double x2 = this.mc.player.posX;
                        final double y2 = this.mc.player.posY;
                        final double z2 = this.mc.player.posZ;
                        ++Criticals.attacks;
                        if (Criticals.attacks > 0.0) {
                            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(x2, y2 + 0.2, z2, false));
                            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(x2, y2 + 0.1216, z2, false));
                            Criticals.attacks = 0.0;
                        }
                        else {
                            Criticals.antiDesync = false;
                        }
                    }
                }
            }
            else if (this.mode.getMode("Packet").isToggled() && packet instanceof CPacketPlayer && this.cancelSomePackets) {
                return this.cancelSomePackets = false;
            }
        }
        return true;
    }
    
    boolean canJump() {
        return !Wrapper.INSTANCE.player().isOnLadder() && !Wrapper.INSTANCE.player().isInWater() && !Wrapper.INSTANCE.player().isInLava() && !Wrapper.INSTANCE.player().isSneaking() && !Wrapper.INSTANCE.player().isRiding() && !Wrapper.INSTANCE.player().isPotionActive(MobEffects.BLINDNESS);
    }
    
    static {
        Criticals.attacks = 0.0;
        Criticals.antiDesync = false;
    }
}
