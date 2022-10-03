package zelix.hack.hacks;

import net.minecraft.client.*;
import net.minecraft.util.math.*;
import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import org.lwjgl.input.*;
import net.minecraft.block.material.*;
import zelix.utils.hooks.visual.*;
import org.lwjgl.util.vector.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import zelix.utils.*;
import net.minecraft.block.state.*;
import net.minecraft.client.entity.*;
import java.util.*;

public class ItemTeleport extends Hack
{
    public Minecraft mc;
    private final ModeValue mode;
    private final BooleanValue resetAfterTp;
    private final ModeValue button;
    private int delay;
    private BlockPos endPos;
    private RayTraceResult objectPosition;
    
    public ItemTeleport() {
        super("ItemTeleport", HackCategory.PLAYER);
        this.mc = Minecraft.getMinecraft();
        this.resetAfterTp = new BooleanValue("ResetAfterTP", Boolean.valueOf(true));
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("New", true), new Mode("Old", false) });
        this.button = new ModeValue("Button", new Mode[] { new Mode("Left", false), new Mode("Right", false), new Mode("Middle", true) });
        this.addValue(this.mode, this.button, this.resetAfterTp);
    }
    
    @Override
    public void onDisable() {
        this.delay = 0;
        this.endPos = null;
        super.onDisable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Mouse.isButtonDown(Arrays.asList(this.button.getModes()).indexOf(this.button.getSelectMode())) && this.delay <= 0) {
            this.objectPosition = this.mc.objectMouseOver;
            this.endPos = this.objectPosition.getBlockPos();
            final IBlockState state = BlockUtils.getState(this.endPos);
            if (state.getBlock().getMaterial(state) == Material.AIR) {
                this.endPos = null;
                return;
            }
            ChatUtils.message("¡ì7[¡ì8¡ìlItemTeleport¡ì7] ¡ì3Position was set to ¡ì8" + this.endPos.getX() + "¡ì3, ¡ì8" + this.endPos.getY() + "¡ì3, ¡ì8" + this.endPos.getZ());
            this.delay = 6;
        }
        if (this.delay > 0) {
            --this.delay;
        }
        final EntityPlayerSP thePlayer = this.mc.player;
        if (thePlayer == null) {
            return;
        }
        if (this.endPos != null && thePlayer.isSneaking()) {
            if (!thePlayer.onGround) {
                final double endX = this.endPos.getX() + 0.5;
                final double endY = this.endPos.getY() + 1.0;
                final double endZ = this.endPos.getZ() + 0.5;
                final String lowerCase = this.mode.getSelectMode().getName().toLowerCase();
                switch (lowerCase) {
                    case "old": {
                        for (final Vector3f vector3f : this.vanillaTeleportPositions(endX, endY, endZ, 4.0)) {
                            this.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position((double)vector3f.getX(), (double)vector3f.getY(), (double)vector3f.getZ(), false));
                        }
                        break;
                    }
                    case "new": {
                        for (final Vector3f vector3f : this.vanillaTeleportPositions(endX, endY, endZ, 5.0)) {
                            this.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(thePlayer.posX, thePlayer.posY, thePlayer.posZ, true));
                            this.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position((double)vector3f.x, (double)vector3f.y, (double)vector3f.z, true));
                            this.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(thePlayer.posX, thePlayer.posY, thePlayer.posZ, true));
                            this.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(thePlayer.posX, thePlayer.posY + 4.0, thePlayer.posZ, true));
                            this.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position((double)vector3f.x, (double)vector3f.y, (double)vector3f.z, true));
                            PlayerUtil.forward(0.04);
                        }
                        break;
                    }
                }
                if (this.resetAfterTp.getValue()) {
                    this.endPos = null;
                }
                ChatUtils.message("¡ì7[¡ì8¡ìlItemTeleport¡ì7] ¡ì3Tried to collect items");
            }
            else {
                thePlayer.jump();
            }
        }
    }
    
    private List<Vector3f> vanillaTeleportPositions(final double tpX, final double tpY, final double tpZ, final double speed) {
        final List<Vector3f> positions = new ArrayList<Vector3f>();
        final double posX = tpX - this.mc.player.posX;
        final double posZ = tpZ - this.mc.player.posZ;
        final float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / 3.141592653589793 - 90.0);
        double tmpY = this.mc.player.posY;
        double steps = 1.0;
        for (double d = speed; d < this.getDistance(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ, tpX, tpY, tpZ); d += speed) {
            ++steps;
        }
        for (double d = speed; d < this.getDistance(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ, tpX, tpY, tpZ); d += speed) {
            final double tmpX = this.mc.player.posX - Math.sin(Math.toRadians(yaw)) * d;
            final double tmpZ = this.mc.player.posZ + Math.cos(Math.toRadians(yaw)) * d;
            tmpY -= (this.mc.player.posY - tpY) / steps;
            positions.add(new Vector3f((float)tmpX, (float)tmpY, (float)tmpZ));
        }
        positions.add(new Vector3f((float)tpX, (float)tpY, (float)tpZ));
        return positions;
    }
    
    private double getDistance(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        final double d0 = x1 - x2;
        final double d2 = y1 - y2;
        final double d3 = z1 - z2;
        return Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
    }
}
