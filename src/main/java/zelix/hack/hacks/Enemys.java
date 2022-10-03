package zelix.hack.hacks;

import zelix.hack.*;

public class Enemys extends Hack
{
    public Enemys() {
        super("Enemys", HackCategory.ANOTHER);
    }
    
    @Override
    public String getDescription() {
        return "Target only in enemy list.";
    }
}
