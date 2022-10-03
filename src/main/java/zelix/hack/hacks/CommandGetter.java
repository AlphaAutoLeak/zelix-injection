package zelix.hack.hacks;

import zelix.hack.*;
import zelix.utils.system.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.relauncher.*;
import zelix.managers.*;
import java.lang.reflect.*;

public class CommandGetter extends Hack
{
    public CommandGetter() {
        super("CommandGetter", HackCategory.ANOTHER);
        this.setToggled(true);
        this.setShow(false);
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        boolean send = true;
        if (side == Connection.Side.OUT && packet instanceof CPacketChatMessage) {
            final Field field = ReflectionHelper.findField((Class)CPacketChatMessage.class, new String[] { "message", "message" });
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (packet instanceof CPacketChatMessage) {
                    final CPacketChatMessage p = (CPacketChatMessage)packet;
                    if (p.getMessage().subSequence(0, 1).equals(".")) {
                        send = false;
                        CommandManager.getInstance().runCommands(p.getMessage());
                        return send;
                    }
                    send = true;
                }
            }
            catch (Exception ex) {}
        }
        return send;
    }
}
