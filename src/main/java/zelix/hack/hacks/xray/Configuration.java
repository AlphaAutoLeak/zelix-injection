package zelix.hack.hacks.xray;

import net.minecraftforge.common.config.*;

@Config(modid = "xray", name = "advanced_xray", type = Config.Type.INSTANCE)
public class Configuration
{
    @Config.RangeInt(min = 0)
    public static int radius_x;
    @Config.RangeInt(min = 0)
    public static int radius_y;
    @Config.RangeInt(min = 0)
    public static int radius_z;
    @Config.Name("Scan Delay")
    @Config.Comment({ "Send scan packet delay (ms) 1000ms=1s" })
    public static int delay;
    @Config.Name("Is Auto Scan")
    public static boolean auto;
    @Config.Name("Auto Scan when you walk over how many blocks")
    public static int movethreshhold;
    @Config.Comment({ "DO NOT TOUCH!", "This setting is for memory only and if changed to a value not supported", "the game will crash on start up. This value can be changed in game very simply.", "If you must change it then these are the valid values 0 -> 7", "but please leave it alone :P" })
    @Config.RangeInt(min = 0, max = 7)
    public static int radius;
    @Config.Name("Show XRay overlay")
    @Config.Comment({ "This allows you hide or show the overlay in the top right of the screen when XRay is enabled" })
    public static boolean showOverlay;
    @Config.Name("Fading Affect")
    @Config.Comment({ "By default the blocks will begin to fade out as you get further away, some may not like this as", "it can sometime causes things not to show. If you're a dirty cheater (lol) then you may want", "to disable this as you won't be able to see chests out of your range." })
    public static boolean shouldFade;
    @Config.Name("Freeze")
    public static boolean freeze;
    @Config.Name("Outline thickness")
    @Config.Comment({ "This allows you to set your own outline thickness, I find that 1.0 is perfect but others my", "think differently. The max is 5.0" })
    @Config.RangeDouble(min = 0.5, max = 5.0)
    public static double outlineThickness;
    
    static {
        Configuration.radius_x = 4;
        Configuration.radius_y = 4;
        Configuration.radius_z = 4;
        Configuration.delay = 20;
        Configuration.auto = false;
        Configuration.movethreshhold = 5;
        Configuration.radius = 3;
        Configuration.showOverlay = true;
        Configuration.shouldFade = true;
        Configuration.freeze = false;
        Configuration.outlineThickness = 1.0;
    }
}
