package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;

public class Targets extends Hack
{
    public BooleanValue players;
    public BooleanValue mobs;
    public BooleanValue invisibles;
    public BooleanValue murder;
    public BooleanValue sleeping;
    
    public Targets() {
        super("Targets", HackCategory.ANOTHER);
        this.setShow(false);
        this.setToggled(true);
        this.players = new BooleanValue("Players", Boolean.valueOf(true));
        this.mobs = new BooleanValue("Mobs", Boolean.valueOf(false));
        this.invisibles = new BooleanValue("Invisibles", Boolean.valueOf(false));
        this.murder = new BooleanValue("Murder", Boolean.valueOf(false));
        this.sleeping = new BooleanValue("Sleeping", Boolean.valueOf(false));
        this.addValue(this.players, this.mobs, this.invisibles, this.murder, this.sleeping);
    }
    
    @Override
    public String getDescription() {
        return "Manage targets for hacks.";
    }
}
