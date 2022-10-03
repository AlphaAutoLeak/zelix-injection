package zelix.hack.hacks;

import zelix.hack.*;
import zelix.utils.frame.*;

public class CommandFrame extends Hack
{
    public CommandFrame() {
        super("CommandFrame", HackCategory.ANOTHER);
        this.setShow(false);
    }
    
    @Override
    public void onEnable() {
        FCommand.main(null);
        this.setToggled(false);
    }
}
