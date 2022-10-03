package netease;

import java.net.*;
import sun.misc.*;
import java.util.*;
import java.lang.reflect.*;
import java.security.*;
import java.io.*;
import java.math.*;

public class Connection extends Thread
{
    private byte[][] classes;
    private String user;
    private String password;
    
    public Connection(final byte[][] classes, final String user, final String password) {
        this.classes = null;
        this.user = null;
        this.password = null;
        this.classes = classes;
        this.user = user;
        this.password = password;
    }
    
    @Override
    public void run() {
        String className = null;
        String Value = null;
        System.out.println("Start Loading");
        try {
            final Socket socket = new Socket("121.62.61.198", 14100);
            String ip = "";
            ip = socket.getInetAddress().getHostName();
            final String Text = "[target][getfield][fieldname][R1122]";
            final String PW = m(ip);
            String newReturn = new StringBuilder(Text).reverse().toString();
            newReturn = PW + newReturn;
            final String Send;
            newReturn = (Send = c(newReturn.getBytes()));
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
                if (Xs.contains("923HD8923EY298IUSNXISNCSICCNCMNCDC09WCJ")) {
                    System.out.println("Verify Error!!!");
                }
                final String Password = String.valueOf(ip);
                final String Text2 = Xs;
                final String PW2 = m(Password);
                String newReturn2 = new String(b(Text2), "utf-8");
                newReturn2 = newReturn2.replace(PW2, "");
                final String Data;
                newReturn2 = (Data = new StringBuilder(newReturn2).reverse().toString());
                Value = x(Data, "[value][", "]");
                final String[] Classes = Value.split("=");
                if (Classes.length != 3) {
                    System.exit(0);
                    return;
                }
                className = Classes[0];
                ClassLoader contextClassLoader = null;
                for (final Thread thread : Thread.getAllStackTraces().keySet()) {
                    if (thread.getName().toLowerCase().equals("client thread")) {
                        contextClassLoader = thread.getContextClassLoader();
                    }
                }
                if (contextClassLoader == null) {
                    return;
                }
                this.setContextClassLoader(contextClassLoader);
                final Method declaredMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
                declaredMethod.setAccessible(true);
                Class clazz = null;
                for (final byte[] array : this.classes) {
                    final Class clazz2 = (Class)declaredMethod.invoke(contextClassLoader, null, array, 0, array.length, contextClassLoader.getClass().getProtectionDomain());
                    if (clazz2 != null && clazz2.getName().contains(className)) {
                        clazz = clazz2;
                    }
                }
                if (clazz == null) {
                    continue;
                }
                clazz.getDeclaredMethod("RLoad", String.class, String.class).invoke(null, Classes[1], Classes[2]);
            }
            socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
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
            catch (NoSuchFieldException eX) {
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
            catch (IllegalAccessException eX2) {
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
        }
    }
    
    public static int a(final byte[][] array, final String s, final String s2) {
        try {
            new Connection(array, s, s2).run();
        }
        catch (Exception ex) {}
        return 0;
    }
    
    public static byte[][] a(final int n) {
        return new byte[n][];
    }
    
    public static String x(final String text, final String left, final String right) {
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        }
        else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            }
            else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }
    
    private static byte[] b(final String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        final byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; ++i) {
            final String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte)Integer.parseInt(subStr, 16);
        }
        return bytes;
    }
    
    private static String m(final String str) {
        byte[] digest = null;
        try {
            final MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes("utf-8"));
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        final String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }
    
    private static String c(final byte[] bytes) {
        final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        final char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for (final byte b : bytes) {
            if (b < 0) {
                a = 256 + b;
            }
            else {
                a = b;
            }
            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }
        return new String(buf);
    }
}
