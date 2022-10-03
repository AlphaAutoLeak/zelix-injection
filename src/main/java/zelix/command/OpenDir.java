package zelix.command;

import java.awt.*;
import zelix.managers.*;
import zelix.utils.hooks.visual.*;

public class OpenDir extends Command
{
    public OpenDir() {
        super("opendir");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        try {
            Desktop.getDesktop().open(FileManager.GISHCODE_DIR);
        }
        catch (Exception e) {
            ChatUtils.error("Usage: " + this.getSyntax());
        }
    }
    
    @Override
    public String getDescription() {
        return "Opening directory of config.";
    }
    
    @Override
    public String getSyntax() {
        return "opendir";
    }
}
