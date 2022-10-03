package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;

public class Teams extends Hack
{
    public ModeValue mode;
    
    public Teams() {
        super("Teams", HackCategory.ANOTHER);
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("Base", false), new Mode("ArmorColor", false), new Mode("NameColor", true) });
        this.addValue(this.mode);
    }
    
    @Override
    public String getDescription() {
        return "Ignore if player in your team.";
    }
}
