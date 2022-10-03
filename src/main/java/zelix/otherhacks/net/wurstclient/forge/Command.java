package zelix.otherhacks.net.wurstclient.forge;

import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import net.minecraft.client.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;

public abstract class Command extends WForgeRegistryEntry<Command>
{
    protected static final ForgeWurst wurst;
    protected static final Minecraft mc;
    private final String name;
    private final String description;
    private final String[] syntax;
    
    public Command(final String name, final String description, final String... syntax) {
        this.name = name;
        this.description = description;
        this.syntax = syntax;
    }
    
    public abstract void call(final String[] p0) throws CmdException;
    
    public final String getName() {
        return this.name;
    }
    
    public final String getDescription() {
        return this.description;
    }
    
    public final String[] getSyntax() {
        return this.syntax;
    }
    
    static {
        wurst = ForgeWurst.getForgeWurst();
        mc = Minecraft.getMinecraft();
    }
    
    public abstract class CmdException extends Exception
    {
        public CmdException() {
        }
        
        public CmdException(final String s) {
            super(s);
        }
        
        public abstract void printToChat();
    }
    
    public final class CmdError extends CmdException
    {
        public CmdError(final String s) {
            super(s);
        }
        
        @Override
        public void printToChat() {
            ChatUtils.error(this.getMessage());
        }
    }
    
    public final class CmdSyntaxError extends CmdException
    {
        public CmdSyntaxError() {
        }
        
        public CmdSyntaxError(final String s) {
            super(s);
        }
        
        @Override
        public void printToChat() {
            if (this.getMessage() != null) {
                ChatUtils.message("¡ì4Syntax error:¡ìr " + this.getMessage());
            }
            for (final String line : Command.this.syntax) {
                ChatUtils.message(line);
            }
        }
    }
}
