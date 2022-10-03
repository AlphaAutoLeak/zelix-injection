package zelix;

import zelix.gui.Notification.*;
import zelix.utils.hooks.visual.font.*;
import zelix.managers.*;
import zelix.utils.*;
import java.security.*;
import java.util.*;
import java.net.*;
import zelix.gui.cloudconfig.*;
import net.minecraftforge.common.*;
import zelix.utils.system.*;
import net.minecraftforge.fml.common.*;
import sun.misc.*;
import zelix.utils.resourceloader.*;
import java.io.*;
import java.lang.reflect.*;

public class Core
{
    public static final String MODID = "rsaaaaaa";
    public static final String NAME = "Zelix";
    public static final String VERSION = "1.1.6";
    public static final String MCVERSION = "1.12.2";
    public static int initCount;
    public static HackManager hackManager;
    public static FileManager fileManager;
    public static EventsHandler eventsHandler;
    public static NotificationManager notificationManager;
    public static CapeManager capeManager;
    public static FontLoaders fontLoaders;
    public static FontManager fontManager;
    public static String UN;
    public static String UP;
    public static String Love = "External";
    
    public Core() {
        this.fakeVerify();
//        this.Sort_Verify();
    }
    
    public static void main(final String[] args) {
    }

    public void fakeVerify(){
        Core.hackManager = new HackManager();
        Core.fileManager = new FileManager();
        Core.eventsHandler = new EventsHandler();
        Core.capeManager = new CapeManager();
        Core.notificationManager = new NotificationManager();
        Nan0EventRegister.register(MinecraftForge.EVENT_BUS, Core.eventsHandler);
        Nan0EventRegister.register(FMLCommonHandler.instance().bus(), Core.eventsHandler);
    }

    public void Sort_Verify() {
        if (Core.initCount > 0) {
            return;
        }
        if (!LoadClient.isCheck) {
            new Cr4sh();
            return;
        }
        ++Core.initCount;
        try {
            final String serial;
            final String cpuSerialNumber = serial = Utils.getCPUSerialNumber();
            MessageDigest messageDigest = null;
            messageDigest = MessageDigest.getInstance("MD5");
            final byte[] ciphertext = messageDigest.digest(serial.getBytes());
            final String s = Utils.encodeHexString(ciphertext);
            final Random r = new Random();
            final int VerifyCode = r.nextInt(99999999) + 10000000;
            final Socket socket = new Socket("121.62.61.198", 14100);
            String ip = "";
            ip = socket.getInetAddress().getHostName();
            final String UserName = Utils.readFile("C:\\Zelix\\username.exs");
            final String VPassword = Utils.readFile("C:\\Zelix\\password.exs");
            Core.UN = UserName;
            Core.UP = VPassword;
            final String Text = "[target][verify][hwid][" + s.toString() + "][code][" + String.valueOf(VerifyCode) + "][username][" + UserName + "][password][" + VPassword + "]";
            CloudConfig.verify = "[username][" + UserName + "][password][" + VPassword + "]";
            final String PW = Utils.getMD5Str(ip);
            String newReturn = new StringBuilder(Text).reverse().toString();
            newReturn = PW + newReturn;
            final String Send;
            newReturn = (Send = Utils.bytesToHex(newReturn.getBytes()));
            final OutputStream ops = socket.getOutputStream();
            final OutputStreamWriter opsw = new OutputStreamWriter(ops);
            final BufferedWriter bw = new BufferedWriter(opsw);
            bw.write(Send);
            bw.flush();
            final InputStream ips = socket.getInputStream();
            final InputStreamReader ipsr = new InputStreamReader(ips, "UTF-8");
            final BufferedReader br = new BufferedReader(ipsr);
            String Xs = null;
            while ((Xs = br.readLine()) != null) {
                final String Password = String.valueOf(VerifyCode);
                final String Text2 = Xs;
                final String PW2 = Utils.getMD5Str(Password);
                String newReturn2 = new String(Utils.toBytes(Text2), "utf-8");
                newReturn2 = newReturn2.replace(PW2, "");
                final String Data;
                newReturn2 = (Data = new StringBuilder(newReturn2).reverse().toString());
                final String result = Utils.getSubString(Data, "[result][", "][hwid][");
                final String hwid = Utils.getSubString(Data, "][hwid][", "]");
                Core.Love = Utils.getSubString(Data, "[project][", "]");
                String Notice = null;
                switch (result.toLowerCase().hashCode()) {
                    case -779418060: {
                        Notice = "Succeed To Verify";
                        if (hwid.hashCode() == s.toString().hashCode()) {
                            Core.hackManager = new HackManager();
                            Core.fileManager = new FileManager();
                            Core.eventsHandler = new EventsHandler();
                            Core.capeManager = new CapeManager();
                            Core.notificationManager = new NotificationManager();
                            Nan0EventRegister.register(MinecraftForge.EVENT_BUS, Core.eventsHandler);
                            Nan0EventRegister.register(FMLCommonHandler.instance().bus(), Core.eventsHandler);
                            continue;
                        }
                        continue;
                    }
                    case -1364519915: {
                        Notice = "Failed To Verify";
                        try {
                            final Field F = Unsafe.class.getDeclaredField("theUnsafe");
                            F.setAccessible(true);
                            ((Unsafe)F.get(null)).putAddress(114514L, 1919810L);
                            ((Unsafe)F.get(null)).putAddress(114514L, 19810L);
                            ((Unsafe)F.get(null)).putAddress(114514L, 1919810L);
                            ((Unsafe)F.get(null)).putAddress(1114L, 191810L);
                            ((Unsafe)F.get(null)).putAddress(114514L, 1919810L);
                            ((Unsafe)F.get(null)).putAddress(11414L, 1919810L);
                            ((Unsafe)F.get(null)).putAddress(114514L, 119810L);
                            ((Unsafe)F.get(null)).putAddress(11414L, 1919810L);
                            ((Unsafe)F.get(null)).putAddress(114514L, 19810L);
                            ((Unsafe)F.get(null)).putAddress(114514L, 191810L);
                            ((Unsafe)F.get(null)).putAddress(11454L, 1919810L);
                            ((Unsafe)F.get(null)).putAddress(1114L, 191810L);
                        }
                        catch (NoSuchFieldException e2) {
                            try {
                                new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                            }
                            catch (NoSuchFieldException nosadtion) {
                                try {
                                    final Field F2 = Unsafe.class.getDeclaredField("theUnsafe");
                                    F2.setAccessible(true);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 19810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 19810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 19810L);
                                    ((Unsafe)F2.get(null)).putAddress(1514L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 19110L);
                                    ((Unsafe)F2.get(null)).putAddress(1114L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 19110L);
                                    ((Unsafe)F2.get(null)).putAddress(1144L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 91810L);
                                    ((Unsafe)F2.get(null)).putAddress(1114L, 19810L);
                                    ((Unsafe)F2.get(null)).putAddress(1514L, 1919810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(11414L, 191810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(11414L, 191810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(11414L, 191810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                }
                                catch (NoSuchFieldException se) {
                                    try {
                                        new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                                    }
                                    catch (NoSuchFieldException ex) {}
                                }
                                catch (IllegalAccessException sse) {
                                    try {
                                        new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                                    }
                                    catch (NoSuchFieldException ex2) {}
                                }
                            }
                        }
                        catch (IllegalAccessException e3) {
                            try {
                                new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                            }
                            catch (NoSuchFieldException nosadtion) {
                                try {
                                    final Field F2 = Unsafe.class.getDeclaredField("theUnsafe");
                                    F2.setAccessible(true);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(1114L, 19810L);
                                    ((Unsafe)F2.get(null)).putAddress(1114L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 19110L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(1514L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 19810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 119810L);
                                    ((Unsafe)F2.get(null)).putAddress(14514L, 1919810L);
                                }
                                catch (NoSuchFieldException ffe) {
                                    try {
                                        new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                                    }
                                    catch (NoSuchFieldException ex3) {}
                                }
                                catch (IllegalAccessException ef) {
                                    try {
                                        new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                                    }
                                    catch (NoSuchFieldException ex4) {}
                                }
                            }
                        }
                        continue;
                    }
                    default: {
                        Notice = "Are You Cracking?";
                        try {
                            final Field F = Unsafe.class.getDeclaredField("theUnsafe");
                            F.setAccessible(true);
                            ((Unsafe)F.get(null)).putAddress(114514L, 1919810L);
                            ((Unsafe)F.get(null)).putAddress(114514L, 19810L);
                            ((Unsafe)F.get(null)).putAddress(114514L, 1919810L);
                            ((Unsafe)F.get(null)).putAddress(1114L, 191810L);
                            ((Unsafe)F.get(null)).putAddress(114514L, 1919810L);
                            ((Unsafe)F.get(null)).putAddress(11414L, 1919810L);
                            ((Unsafe)F.get(null)).putAddress(114514L, 119810L);
                            ((Unsafe)F.get(null)).putAddress(11414L, 1919810L);
                            ((Unsafe)F.get(null)).putAddress(114514L, 19810L);
                            ((Unsafe)F.get(null)).putAddress(114514L, 191810L);
                            ((Unsafe)F.get(null)).putAddress(11454L, 1919810L);
                            ((Unsafe)F.get(null)).putAddress(1114L, 191810L);
                        }
                        catch (NoSuchFieldException e2) {
                            try {
                                new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                            }
                            catch (NoSuchFieldException nosadtion) {
                                try {
                                    final Field F2 = Unsafe.class.getDeclaredField("theUnsafe");
                                    F2.setAccessible(true);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 19810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 19810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 19810L);
                                    ((Unsafe)F2.get(null)).putAddress(1514L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 19110L);
                                    ((Unsafe)F2.get(null)).putAddress(1114L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 19110L);
                                    ((Unsafe)F2.get(null)).putAddress(1144L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 91810L);
                                    ((Unsafe)F2.get(null)).putAddress(1114L, 19810L);
                                    ((Unsafe)F2.get(null)).putAddress(1514L, 1919810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(11414L, 191810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(11414L, 191810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(11414L, 191810L);
                                    ((Unsafe)F2.get(null)).setMemory(14514L, 191810L, new Byte(null));
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                }
                                catch (NoSuchFieldException se) {
                                    try {
                                        new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                                    }
                                    catch (NoSuchFieldException ex5) {}
                                }
                                catch (IllegalAccessException sse) {
                                    try {
                                        new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                                    }
                                    catch (NoSuchFieldException ex6) {}
                                }
                            }
                        }
                        catch (IllegalAccessException e3) {
                            try {
                                new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                            }
                            catch (NoSuchFieldException nosadtion) {
                                try {
                                    final Field F2 = Unsafe.class.getDeclaredField("theUnsafe");
                                    F2.setAccessible(true);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(1114L, 19810L);
                                    ((Unsafe)F2.get(null)).putAddress(1114L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 19110L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(1514L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 19810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 1919810L);
                                    ((Unsafe)F2.get(null)).putAddress(114514L, 119810L);
                                    ((Unsafe)F2.get(null)).putAddress(14514L, 1919810L);
                                }
                                catch (NoSuchFieldException ffe) {
                                    try {
                                        new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                                    }
                                    catch (NoSuchFieldException ex7) {}
                                }
                                catch (IllegalAccessException ef) {
                                    try {
                                        new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                                    }
                                    catch (NoSuchFieldException ex8) {}
                                }
                            }
                        }
                        continue;
                    }
                }
            }
            socket.close();
        }
        catch (Exception eee) {
            eee.printStackTrace();
            try {
                final Field F3 = Unsafe.class.getDeclaredField("theUnsafe");
                F3.setAccessible(true);
                ((Unsafe)F3.get(null)).putAddress(114514L, 1919810L);
                ((Unsafe)F3.get(null)).putAddress(114514L, 19810L);
                ((Unsafe)F3.get(null)).putAddress(114514L, 1919810L);
                ((Unsafe)F3.get(null)).putAddress(1114L, 191810L);
                ((Unsafe)F3.get(null)).putAddress(114514L, 1919810L);
                ((Unsafe)F3.get(null)).putAddress(11414L, 1919810L);
                ((Unsafe)F3.get(null)).putAddress(114514L, 119810L);
                ((Unsafe)F3.get(null)).putAddress(11414L, 1919810L);
                ((Unsafe)F3.get(null)).putAddress(114514L, 19810L);
                ((Unsafe)F3.get(null)).putAddress(114514L, 191810L);
                ((Unsafe)F3.get(null)).putAddress(11454L, 1919810L);
                ((Unsafe)F3.get(null)).putAddress(1114L, 191810L);
            }
            catch (NoSuchFieldException e4) {
                try {
                    new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                }
                catch (NoSuchFieldException nosadtion2) {
                    try {
                        final Field F4 = Unsafe.class.getDeclaredField("theUnsafe");
                        F4.setAccessible(true);
                        ((Unsafe)F4.get(null)).putAddress(114514L, 19810L);
                        ((Unsafe)F4.get(null)).putAddress(114514L, 1919810L);
                        ((Unsafe)F4.get(null)).putAddress(114514L, 19810L);
                        ((Unsafe)F4.get(null)).putAddress(114514L, 19810L);
                        ((Unsafe)F4.get(null)).putAddress(1514L, 1919810L);
                        ((Unsafe)F4.get(null)).putAddress(114514L, 19110L);
                        ((Unsafe)F4.get(null)).putAddress(1114L, 1919810L);
                        ((Unsafe)F4.get(null)).putAddress(114514L, 19110L);
                        ((Unsafe)F4.get(null)).putAddress(1144L, 1919810L);
                        ((Unsafe)F4.get(null)).putAddress(114514L, 91810L);
                        ((Unsafe)F4.get(null)).putAddress(1114L, 19810L);
                        ((Unsafe)F4.get(null)).putAddress(1514L, 1919810L);
                        ((Unsafe)F4.get(null)).setMemory(14514L, 191810L, new Byte(null));
                        ((Unsafe)F4.get(null)).setMemory(14514L, 191810L, new Byte(null));
                        ((Unsafe)F4.get(null)).putAddress(114514L, 1919810L);
                        ((Unsafe)F4.get(null)).setMemory(14514L, 191810L, new Byte(null));
                        ((Unsafe)F4.get(null)).putAddress(11414L, 191810L);
                        ((Unsafe)F4.get(null)).setMemory(14514L, 191810L, new Byte(null));
                        ((Unsafe)F4.get(null)).putAddress(114514L, 1919810L);
                        ((Unsafe)F4.get(null)).setMemory(14514L, 191810L, new Byte(null));
                        ((Unsafe)F4.get(null)).setMemory(14514L, 191810L, new Byte(null));
                        ((Unsafe)F4.get(null)).putAddress(114514L, 1919810L);
                        ((Unsafe)F4.get(null)).setMemory(14514L, 191810L, new Byte(null));
                        ((Unsafe)F4.get(null)).putAddress(11414L, 191810L);
                        ((Unsafe)F4.get(null)).setMemory(14514L, 191810L, new Byte(null));
                        ((Unsafe)F4.get(null)).putAddress(114514L, 1919810L);
                        ((Unsafe)F4.get(null)).setMemory(14514L, 191810L, new Byte(null));
                        ((Unsafe)F4.get(null)).setMemory(14514L, 191810L, new Byte(null));
                        ((Unsafe)F4.get(null)).putAddress(114514L, 1919810L);
                        ((Unsafe)F4.get(null)).setMemory(14514L, 191810L, new Byte(null));
                        ((Unsafe)F4.get(null)).putAddress(11414L, 191810L);
                        ((Unsafe)F4.get(null)).setMemory(14514L, 191810L, new Byte(null));
                        ((Unsafe)F4.get(null)).putAddress(114514L, 1919810L);
                    }
                    catch (NoSuchFieldException se2) {
                        try {
                            new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                        }
                        catch (NoSuchFieldException ex9) {}
                    }
                    catch (IllegalAccessException sse2) {
                        try {
                            new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                        }
                        catch (NoSuchFieldException ex10) {}
                    }
                }
            }
            catch (IllegalAccessException e5) {
                try {
                    new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                }
                catch (NoSuchFieldException nosadtion2) {
                    try {
                        final Field F4 = Unsafe.class.getDeclaredField("theUnsafe");
                        F4.setAccessible(true);
                        ((Unsafe)F4.get(null)).putAddress(114514L, 1919810L);
                        ((Unsafe)F4.get(null)).putAddress(114514L, 1919810L);
                        ((Unsafe)F4.get(null)).putAddress(1114L, 19810L);
                        ((Unsafe)F4.get(null)).putAddress(1114L, 1919810L);
                        ((Unsafe)F4.get(null)).putAddress(114514L, 19110L);
                        ((Unsafe)F4.get(null)).putAddress(114514L, 1919810L);
                        ((Unsafe)F4.get(null)).putAddress(1514L, 1919810L);
                        ((Unsafe)F4.get(null)).putAddress(114514L, 19810L);
                        ((Unsafe)F4.get(null)).putAddress(114514L, 1919810L);
                        ((Unsafe)F4.get(null)).putAddress(114514L, 1919810L);
                        ((Unsafe)F4.get(null)).putAddress(114514L, 119810L);
                        ((Unsafe)F4.get(null)).putAddress(14514L, 1919810L);
                    }
                    catch (NoSuchFieldException ffe2) {
                        try {
                            new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                        }
                        catch (NoSuchFieldException ex11) {}
                    }
                    catch (IllegalAccessException ef2) {
                        try {
                            new Object().getClass().getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass().getDeclaredField(null).getClass();
                        }
                        catch (NoSuchFieldException ex12) {}
                    }
                }
            }
        }
        try {
            Strings.loadTranslation();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    static {
        Core.initCount = 0;
    }
}
