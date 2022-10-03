package zelix.hack.hacks.xray.etc;

import java.text.*;
import net.minecraft.client.*;
import net.minecraft.util.math.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import net.minecraft.util.text.*;
import zelix.hack.hacks.xray.*;
import zelix.hack.hacks.xray.xray.*;
import net.minecraft.client.network.*;

public class Runner implements Runnable
{
    boolean isRunning;
    public boolean done;
    long delay;
    int current;
    int max;
    int radX;
    int radY;
    int radZ;
    
    public Runner(final int radX, final int radY, final int radZ, final long delay) {
        this.isRunning = true;
        this.done = false;
        this.max = (radX + radX + 1) * (radY + radY + 1) * (radZ + radZ + 1);
        this.radX = radX;
        this.radY = radY;
        this.radZ = radZ;
        this.delay = delay;
    }
    
    public double getProcess() {
        return this.current / this.max * 100.0;
    }
    
    public String getProcessText() {
        return new DecimalFormat("0.00").format(this.getProcess());
    }
    
    @Override
    public void run() {
        final NetHandlerPlayClient conn = Minecraft.getMinecraft().getConnection();
        if (conn == null) {
            return;
        }
        assert Minecraft.getMinecraft().player != null;
        final BlockPos pos = Minecraft.getMinecraft().player.getPosition();
        for (int cx = -this.radX; cx <= this.radX; ++cx) {
            for (int cy = -this.radY; cy <= this.radY; ++cy) {
                for (int cz = -this.radZ; cz <= this.radZ && this.isRunning; ++cz) {
                    ++this.current;
                    final BlockPos currblock = new BlockPos(pos.getX() + cx, pos.getY() + cy, pos.getZ() + cz);
                    CPacketPlayerDigging packet = new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, currblock, EnumFacing.UP);
                    conn.sendPacket((Packet)packet);
                    packet = new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, currblock, EnumFacing.UP);
                    conn.sendPacket((Packet)packet);
                    try {
                        Thread.sleep(this.delay);
                    }
                    catch (InterruptedException ex) {}
                }
            }
        }
        Minecraft.getMinecraft().player.sendStatusMessage((ITextComponent)new TextComponentString("¡ì6[ ¡ìa\uff01 ¡ì6] ¡ìfRefresh done."), true);
        Configuration.freeze = false;
        Controller.requestBlockFinder(true);
        this.done = true;
    }
}
