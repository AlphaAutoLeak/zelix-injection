package zelix.hack.hacks.irc;

import zelix.managers.*;
import zelix.utils.hooks.visual.*;

public class IRCTheard extends Thread
{
    @Override
    public void run() {
        IRCChat.connect();
        do {
            IRCChat.handleInput();
        } while (HackManager.getHack("IRCChat").isToggled());
        ChatUtils.error("Disconnect from IRC :-(");
    }
}
