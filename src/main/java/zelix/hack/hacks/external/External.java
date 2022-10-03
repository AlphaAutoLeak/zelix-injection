package zelix.hack.hacks.external;

import java.net.*;
import zelix.hack.*;
import zelix.utils.*;
import java.io.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.ReflectionHelper;
import zelix.utils.system.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.relauncher.*;
import java.nio.charset.*;
import java.lang.reflect.*;
import zelix.utils.hooks.visual.*;
import zelix.*;
import java.util.*;
import zelix.value.*;
import zelix.managers.*;

public class External extends Hack
{
    public static boolean isdev;
    public static OutputStream out;
    public BufferedReader reader;
    public static Socket socket;
    public static PrintWriter pw;
    static InputStream in;
    private static boolean connect;
    private static String prefix;
    public static List<String> banned;
    public static String FGF;
    private static int sec;
    
    public External() {
        super("External", HackCategory.ANOTHER);
    }
    
    @Override
    public void onEnable() {
        if (Utils.nullCheck()) {
            return;
        }
        new EXTTheard().start();
        super.onEnable();
    }
    
    public static void send(final String TEXT) {
        try {
            External.out.write(TEXT.getBytes("UTF-8"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        ++External.sec;
        if (External.sec > 10000) {
            this.onDisable();
            this.onEnable();
            External.sec = 0;
        }
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
                        External.out.write(("[TARGET][MESSAGE][MSG][" + p.getMessage().substring(1, p.getMessage().length()) + "]").getBytes(StandardCharsets.UTF_8));
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
    
    @Override
    public void onDisable() {
        try {
            External.connect = false;
            if (External.in == null || External.out == null || External.socket == null) {
                return;
            }
            External.in.close();
            External.out.close();
            External.socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        super.onDisable();
    }
    
    public static void connect() {
        try {
            External.socket = new Socket("127.0.0.1", 38912);
            External.in = External.socket.getInputStream();
            External.out = External.socket.getOutputStream();
            External.pw = new PrintWriter(External.socket.getOutputStream(), true);
            External.out.write(getHackList().getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e) {
            ChatUtils.error("Failed to Connect!");
            HackManager.getHack("External").setToggled(false);
            HackManager.getHack("External").onDisable();
            e.printStackTrace();
        }
    }
    
    public static String getHackList() {
        String Info = "";
        final HackManager hackManager = Core.hackManager;
        for (final Hack m : HackManager.getHacks()) {
            Info = Info + "=" + m.getName() + "(" + m.getCategory().name() + ")[";
            for (final Value v : m.getValues()) {
                if (v instanceof NumberValue) {
                    Info = Info + "{NumberValue:" + v.getName() + "|" + ((NumberValue)v).getMax().toString() + "|" + ((NumberValue)v).getMin().toString() + "|" + ((NumberValue)v).getDefaultValue().toString() + "}";
                }
                if (v instanceof ModeValue) {
                    Info = Info + "{ModeValue:" + v.getName();
                    for (final Mode z : ((ModeValue)v).getModes()) {
                        Info = Info + "|" + z.getName();
                    }
                    Info += "}";
                }
                if (v instanceof BooleanValue) {
                    Info = Info + "{BooleanValue:" + v.getName() + "}";
                }
            }
            Info += "]";
        }
        return Info.substring(1);
    }
    
    public static String getNowSetting() {
        String Info = "";
        final HackManager hackManager = Core.hackManager;
        for (final Hack m : HackManager.getHacks()) {
            Info = Info + "=" + m.getName() + "(" + (m.isToggled() ? "true" : "false") + ")[";
            for (final Value v : m.getValues()) {
                if (v instanceof NumberValue) {
                    Info = Info + "{NumberValue:" + v.getName() + "|" + v.getValue().toString() + "}";
                }
                if (v instanceof ModeValue && ((ModeValue)v).getSelectMode() != null) {
                    Info = Info + "{ModeValue:" + v.getName() + "|" + ((ModeValue)v).getSelectMode().getName() + "}";
                }
                if (v instanceof BooleanValue) {
                    if (((BooleanValue)v).getValue()) {
                        Info = Info + "{BooleanValue:" + v.getName() + "|true}";
                    }
                    else {
                        Info = Info + "{BooleanValue:" + v.getName() + "|false}";
                    }
                }
            }
            Info += "]";
        }
        return Info.substring(1);
    }
    
    public static void handleInput() {
        final byte[] data = new byte[1024];
        try {
            final int len = External.in.read(data);
            String ircmessage = new String(data, 0, len);
            ircmessage = ircmessage.replaceAll("\n", "");
            ircmessage = ircmessage.replaceAll("\r", "");
            ircmessage = ircmessage.replaceAll("\t", "");
            if (!External.connect) {
                if (ircmessage.equals("OK CONNECTION")) {
                    External.connect = true;
                    External.out.write(getNowSetting().getBytes(StandardCharsets.UTF_8));
                }
            }
            else {
                CommandManager.getInstance().runCommands("." + ircmessage);
            }
        }
        catch (Throwable t) {}
    }
    
    public static void write(final String str) {
        try {
            External.out.write(str.getBytes("UTF-8"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static {
        External.isdev = false;
        External.connect = false;
        External.prefix = "";
        External.FGF = "r@safucku@uuense@";
        External.sec = 1;
    }
}
