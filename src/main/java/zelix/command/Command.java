package zelix.command;

public abstract class Command
{
    private String command;
    
    public Command(final String command) {
        this.command = command;
    }
    
    public abstract void runCommand(final String p0, final String[] p1);
    
    public abstract String getDescription();
    
    public abstract String getSyntax();
    
    public String getCommand() {
        return this.command;
    }
}
