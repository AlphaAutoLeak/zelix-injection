package zelix.command;

import com.mojang.authlib.minecraft.*;
import zelix.managers.*;
import zelix.utils.hooks.visual.*;

public class SkinSteal extends Command
{
    public SkinSteal() {
        super("skinsteal");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        try {
            SkinChangerManager.addTexture(MinecraftProfileTexture.Type.SKIN, args[0]);
        }
        catch (Exception e) {
            ChatUtils.error("Usage: " + this.getSyntax());
        }
    }
    
    @Override
    public String getDescription() {
        return "Steal skin for 'SkinChanger'.";
    }
    
    @Override
    public String getSyntax() {
        return "skinsteal <nickname/URL>";
    }
}
