package zelix.utils.system;

import zelix.*;
import zelix.utils.hooks.visual.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import zelix.utils.*;
import zelix.eventapi.event.*;
import zelix.eventapi.*;
import zelix.eventapi.events.*;
import io.netty.channel.*;

public class Connection extends ChannelDuplexHandler
{
    private EventsHandler eventHandler;
    
    public Connection(final EventsHandler eventHandler) {
        this.eventHandler = eventHandler;
        try {
            final ChannelPipeline pipeline = Wrapper.INSTANCE.mc().getConnection().getNetworkManager().channel().pipeline();
            pipeline.addBefore("packet_handler", "PacketHandler", (ChannelHandler)this);
        }
        catch (Exception exception) {
            ChatUtils.error("Connection: Error on attaching");
        }
    }
    
    public void channelRead(final ChannelHandlerContext ctx, final Object packet) throws Exception {
        if (((Packet)packet) instanceof CPacketPlayer.PositionRotation) {
            final CPacketPlayer.PositionRotation packetPlayer = (CPacketPlayer.PositionRotation)packet;
            final double x = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "x", "x");
            final double y = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "y", "y");
            final double z = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "z", "z");
            final float yaw = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "yaw", "yaw");
            final float pitch = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "pitch", "pitch");
            final boolean onGroud = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "onGround", "onGround");
            final EventPlayerPre eventPlayerPre = new EventPlayerPre(x, y, z, yaw, pitch, Wrapper.INSTANCE.player().isSneaking(), onGroud);
            EventManager.call(eventPlayerPre);
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Double.valueOf(eventPlayerPre.getX()), "x", "x");
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Double.valueOf(eventPlayerPre.getY()), "y", "y");
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Double.valueOf(eventPlayerPre.getZ()), "z", "z");
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Float.valueOf(eventPlayerPre.getYaw()), "yaw", "yaw");
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Float.valueOf(eventPlayerPre.getPitch()), "pitch", "pitch");
        }
        if (!this.eventHandler.onPacket(packet, Side.IN)) {
            return;
        }
        super.channelRead(ctx, packet);
    }
    
    public void write(final ChannelHandlerContext ctx, final Object packet, final ChannelPromise promise) throws Exception {
        if (((Packet)packet) instanceof CPacketPlayer.PositionRotation) {
            final CPacketPlayer.PositionRotation packetPlayer = (CPacketPlayer.PositionRotation)packet;
            final double x = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "x", "x");
            final double y = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "y", "y");
            final double z = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "z", "z");
            final float yaw = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "yaw", "yaw");
            final float pitch = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "pitch", "pitch");
            final boolean onGroud = ReflectionHelper.getPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, "onGround", "onGround");
            final EventPlayerPre eventPlayerPre = new EventPlayerPre(x, y, z, yaw, pitch, Wrapper.INSTANCE.player().isSneaking(), onGroud);
            EventManager.call(eventPlayerPre);
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Double.valueOf(eventPlayerPre.getX()), "x", "x");
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Double.valueOf(eventPlayerPre.getY()), "y", "y");
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Double.valueOf(eventPlayerPre.getZ()), "z", "z");
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Float.valueOf(eventPlayerPre.getYaw()), "yaw", "yaw");
            ReflectionHelper.setPrivateValue((Class<? super CPacketPlayer.PositionRotation>)CPacketPlayer.class, packetPlayer, Float.valueOf(eventPlayerPre.getPitch()), "pitch", "pitch");
        }
        if (!this.eventHandler.onPacket(packet, Side.OUT)) {
            return;
        }
        super.write(ctx, packet, promise);
    }
    
    public void processPlayerPacket(final CPacketPlayer.PositionRotation packetPlayer) {
    }
    
    public enum Side
    {
        IN, 
        OUT;
    }
}
