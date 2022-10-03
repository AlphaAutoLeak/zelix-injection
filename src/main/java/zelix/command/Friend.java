package zelix.command;

import zelix.utils.*;
import net.minecraft.entity.player.*;
import zelix.managers.*;
import zelix.utils.hooks.visual.*;
import java.util.*;
import net.minecraft.entity.*;

public class Friend extends Command
{
    public Friend() {
        super("friend");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        try {
            if (args[0].equalsIgnoreCase("add")) {
                if (args[1].equalsIgnoreCase("all")) {
                    for (final Object object : Utils.getEntityList()) {
                        if (object instanceof EntityPlayer) {
                            final EntityPlayer player = (EntityPlayer)object;
                            if (player.isInvisible()) {
                                continue;
                            }
                            FriendManager.addFriend(Utils.getPlayerName(player));
                        }
                    }
                }
                else {
                    FriendManager.addFriend(args[1]);
                }
            }
            else if (args[0].equalsIgnoreCase("remove")) {
                FriendManager.removeFriend(args[1]);
            }
            else if (args[0].equalsIgnoreCase("clear")) {
                FriendManager.clear();
            }
        }
        catch (Exception e) {
            ChatUtils.error("Usage: " + this.getSyntax());
        }
    }
    
    @Override
    public String getDescription() {
        return "Friend manager.";
    }
    
    @Override
    public String getSyntax() {
        return "friend <add/remove/clear> <nick>";
    }
}
