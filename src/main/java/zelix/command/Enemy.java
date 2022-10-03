package zelix.command;

import zelix.utils.*;
import net.minecraft.entity.player.*;
import zelix.managers.*;
import zelix.utils.hooks.visual.*;
import java.util.*;
import net.minecraft.entity.*;

public class Enemy extends Command
{
    public Enemy() {
        super("enemy");
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
                            EnemyManager.addEnemy(Utils.getPlayerName(player));
                        }
                    }
                }
                else {
                    EnemyManager.addEnemy(args[1]);
                }
            }
            else if (args[0].equalsIgnoreCase("remove")) {
                EnemyManager.removeEnemy(args[1]);
            }
            else if (args[0].equalsIgnoreCase("clear")) {
                EnemyManager.clear();
            }
        }
        catch (Exception e) {
            ChatUtils.error("Usage: " + this.getSyntax());
        }
    }
    
    @Override
    public String getDescription() {
        return "Enemy manager.";
    }
    
    @Override
    public String getSyntax() {
        return "enemy <add/remove/clear> <nick>";
    }
}
