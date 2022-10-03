package zelix.hack.hacks;

import zelix.hack.*;
import java.net.*;
import zelix.gui.clickguis.caesium.components.*;
import zelix.utils.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;
import java.io.*;

public class CloudConfig extends Hack
{
    public CloudConfig() {
        super("CloudConfig", HackCategory.ANOTHER);
    }
    
    @Override
    public void onEnable() {
        final String IP = "121.62.61.198";
        final int Port = 9990;
        final String Message = zelix.gui.cloudconfig.CloudConfig.verify + "[TARGET][GETCFGLIST]";
        try {
            final Socket socket = new Socket(IP, Port);
            final OutputStream ops = socket.getOutputStream();
            final OutputStreamWriter opsw = new OutputStreamWriter(ops);
            final BufferedWriter bw = new BufferedWriter(opsw);
            bw.write(Message);
            bw.flush();
            final InputStream ips = socket.getInputStream();
            final InputStreamReader ipsr = new InputStreamReader(ips);
            final BufferedReader br = new BufferedReader(ipsr);
            String s = null;
            while ((s = br.readLine()) != null) {
                final String[] var1 = s.split("@-@-@");
                zelix.gui.cloudconfig.CloudConfig.NEW = new ConfigFrame[var1.length];
                for (int i = 0; i < var1.length; ++i) {
                    final String[] var2 = var1[i].split("=");
                    if (var2.length == 3) {
                        try {
                            zelix.gui.cloudconfig.CloudConfig.NEW[i] = new ConfigFrame(var2[0], var2[1], var2[2]);
                        }
                        catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                    final int page = i / 6;
                    zelix.gui.cloudconfig.CloudConfig.pages = page + 1;
                }
                Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)new zelix.gui.cloudconfig.CloudConfig());
                this.setToggled(false);
            }
            socket.close();
        }
        catch (Exception e2) {
            Display.setTitle("Failed Connect to The Server(0x66FF)");
            e2.printStackTrace();
        }
    }
}
