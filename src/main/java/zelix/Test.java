package zelix;

import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;

@Mod(modid = "zeer112c2d2", name = "z", version = "220707", acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.12.2]")
public class Test
{
    public static final String MODID = "zeer112c2d2";
    public static final String NAME = "z";
    public static final String VERSION = "220707";
    
    public Test() {
        this.init(null);
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent E) {
        LoadClient.RLoad("zelix.Core", "zelix.Core");
    }
}
