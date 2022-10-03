package zelix.hack.hacks;

import zelix.hack.*;
import zelix.utils.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.math.*;
import net.minecraft.network.*;
import zelix.utils.system.*;
import net.minecraft.network.play.server.*;
import joptsimple.internal.*;
import zelix.utils.hooks.visual.*;
import java.util.*;

public class PluginsGetter extends Hack
{
    public PluginsGetter() {
        super("PluginsGetter", HackCategory.ANOTHER);
    }
    
    @Override
    public String getDescription() {
        return "Show all plugins on current server.";
    }
    
    @Override
    public void onEnable() {
        if (Wrapper.INSTANCE.player() == null) {
            return;
        }
        Wrapper.INSTANCE.sendPacket((Packet)new CPacketTabComplete("/", (BlockPos)null, false));
        super.onEnable();
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (packet instanceof SPacketTabComplete) {
            final SPacketTabComplete s3APacketTabComplete = (SPacketTabComplete)packet;
            final List<String> plugins = new ArrayList<String>();
            final String[] commands = s3APacketTabComplete.getMatches();
            for (int i = 0; i < commands.length; ++i) {
                final String[] command = commands[i].split(":");
                if (command.length > 1) {
                    final String pluginName = command[0].replace("/", "");
                    if (!plugins.contains(pluginName)) {
                        plugins.add(pluginName);
                    }
                }
            }
            Collections.sort(plugins);
            if (!plugins.isEmpty()) {
                ChatUtils.message("Plugins ¡ì7(¡ì8" + plugins.size() + "¡ì7): ¡ì9" + Strings.join((String[])plugins.toArray(new String[0]), "¡ì7, ¡ì9"));
            }
            else {
                ChatUtils.error("No plugins found.");
            }
            this.setToggled(false);
        }
        return true;
    }
}
