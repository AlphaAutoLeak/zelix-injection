package zelix.hack.hacks;

import zelix.hack.*;
import zelix.utils.ReflectionHelper;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.event.entity.player.*;
import zelix.utils.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.client.multiplayer.*;
import zelix.utils.system.*;
import net.minecraftforge.fml.relauncher.*;
import java.lang.reflect.*;

public class FastBreak extends Hack
{
    public BooleanValue Reflection;
    
    public FastBreak() {
        super("FastBreak", HackCategory.PLAYER);
        this.Reflection = new BooleanValue("Reflection", Boolean.valueOf(false));
        this.addValue(this.Reflection);
    }
    
    @Override
    public String getDescription() {
        return "Allows you to break blocks faster.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        PlayerControllerUtils.setBlockHitDelay(0);
        super.onClientTick(event);
    }
    
    @Override
    public void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
        final float progress = PlayerControllerUtils.getCurBlockDamageMP() + BlockUtils.getHardness(event.getPos());
        if (progress >= 1.0f) {
            return;
        }
        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), Wrapper.INSTANCE.mc().objectMouseOver.sideHit));
        super.onLeftClickBlock(event);
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (this.Reflection.getValue() && !Wrapper.INSTANCE.controller().isInCreativeMode()) {
            final Field field = ReflectionHelper.findField((Class)PlayerControllerMP.class, new String[] { Mapping.curBlockDamageMP });
            final Field blockdelay = ReflectionHelper.findField((Class)PlayerControllerMP.class, new String[] { Mapping.blockHitDelay });
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (!blockdelay.isAccessible()) {
                    blockdelay.setAccessible(true);
                }
                blockdelay.setInt(Wrapper.INSTANCE.controller(), 0);
                if (field.getFloat(Wrapper.INSTANCE.controller()) >= 0.7f) {
                    field.setFloat(Wrapper.INSTANCE.controller(), 1.0f);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
