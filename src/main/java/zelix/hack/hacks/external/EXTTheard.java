package zelix.hack.hacks.external;

import zelix.managers.*;
import zelix.utils.hooks.visual.*;

public class EXTTheard extends Thread
{
    @Override
    public void run() {
        External.connect();
        do {
            External.handleInput();
        } while (HackManager.getHack("External").isToggled());
        ChatUtils.error("Disconnect from IRC :-(");
    }
}
