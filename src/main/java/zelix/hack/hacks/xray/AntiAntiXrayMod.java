package zelix.hack.hacks.xray;

import zelix.hack.*;

public class AntiAntiXrayMod extends Hack
{
    public static boolean isFirst;
    XRay XRAY;
    
    public AntiAntiXrayMod() {
        super("Xray", HackCategory.VISUAL);
        this.setToggled(false);
    }
    
    @Override
    public void onEnable() {
        (this.XRAY = new XRay()).onEnable();
        if (AntiAntiXrayMod.isFirst) {
            AntiAntiXrayMod.isFirst = false;
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        if (this.XRAY != null) {
            this.XRAY.onDisable();
        }
    }
    
    static {
        AntiAntiXrayMod.isFirst = true;
    }
}
