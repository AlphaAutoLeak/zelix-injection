package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.event.entity.player.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public final class FastBreakHack extends Hack
{
    public FastBreakHack() {
        super("FastBreak", "Allows you to break blocks faster.");
        this.setCategory(Category.BLOCKS);
    }
    
    @Override
    protected void onEnable() {
        FastBreakHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        try {
            PlayerControllerUtils.setBlockHitDelay(0);
        }
        catch (ReflectiveOperationException e) {
            this.setEnabled(false);
            throw new RuntimeException(e);
        }
    }
    
    @SubscribeEvent
    public void onPlayerDamageBlock(final PlayerInteractEvent.LeftClickBlock event) {
        try {
            final float progress = PlayerControllerUtils.getCurBlockDamageMP() + BlockUtils.getHardness(event.getPos());
            if (progress >= 1.0f) {
                return;
            }
        }
        catch (ReflectiveOperationException e) {
            this.setEnabled(false);
            throw new RuntimeException(e);
        }
        WMinecraft.getPlayer().connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), FastBreakHack.mc.objectMouseOver.sideHit));
    }
}
