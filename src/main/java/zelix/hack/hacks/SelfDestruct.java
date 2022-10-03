package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import zelix.*;
import zelix.managers.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.*;
import java.util.*;

public class SelfDestruct extends Hack
{
    public static boolean isDes;
    
    public SelfDestruct() {
        super("SelfDestruct", HackCategory.ANOTHER);
    }
    
    @Override
    public void onEnable() {
        Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
        final HackManager hackManager = Core.hackManager;
        for (final Hack m : HackManager.getHacks()) {
            m.setToggled(false);
        }
        MinecraftForge.EVENT_BUS.unregister((Object)Core.eventsHandler);
        FMLCommonHandler.instance().bus().unregister((Object)Core.eventsHandler);
        SelfDestruct.isDes = true;
        super.onEnable();
    }
}
