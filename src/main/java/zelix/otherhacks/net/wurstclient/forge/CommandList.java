package zelix.otherhacks.net.wurstclient.forge;

import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import zelix.otherhacks.net.wurstclient.forge.commands.*;

public final class CommandList extends WCommandList
{
    public final BindsCmd bindsCmd;
    public final ClearCmd clearCmd;
    public final GmCmd gmCmd;
    public final HelpCmd helpCmd;
    public final SayCmd sayCmd;
    public final SetCheckboxCmd setCheckboxCmd;
    public final SetEnumCmd setEnumCmd;
    public final SetSliderCmd setSliderCmd;
    public final TCmd tCmd;
    public final VClipCmd vClipCmd;
    
    public CommandList() {
        this.bindsCmd = this.register(new BindsCmd());
        this.clearCmd = this.register(new ClearCmd());
        this.gmCmd = this.register(new GmCmd());
        this.helpCmd = this.register(new HelpCmd());
        this.sayCmd = this.register(new SayCmd());
        this.setCheckboxCmd = this.register(new SetCheckboxCmd());
        this.setEnumCmd = this.register(new SetEnumCmd());
        this.setSliderCmd = this.register(new SetSliderCmd());
        this.tCmd = this.register(new TCmd());
        this.vClipCmd = this.register(new VClipCmd());
    }
}
