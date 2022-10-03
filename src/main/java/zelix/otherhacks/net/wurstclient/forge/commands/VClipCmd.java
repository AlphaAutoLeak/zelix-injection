package zelix.otherhacks.net.wurstclient.forge.commands;

import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import net.minecraft.client.entity.*;

public final class VClipCmd extends Command
{
    public VClipCmd() {
        super("vclip", "Lets you clip through blocks vertically.", new String[] { "Syntax: .vclip <height>" });
    }
    
    @Override
    public void call(final String[] args) throws CmdException {
        if (args.length != 1) {
            throw new CmdSyntaxError();
        }
        if (!MathUtils.isInteger(args[0])) {
            throw new CmdSyntaxError();
        }
        final EntityPlayerSP player = WMinecraft.getPlayer();
        player.setPosition(player.posX, player.posY + Integer.parseInt(args[0]), player.posZ);
    }
}
