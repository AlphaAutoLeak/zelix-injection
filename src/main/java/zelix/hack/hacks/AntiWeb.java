package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.entity.*;
import zelix.utils.system.*;
import zelix.utils.*;
import java.lang.reflect.*;

public class AntiWeb extends Hack
{
    public AntiWeb() {
        super("AntiWeb", HackCategory.PLAYER);
    }
    
    @Override
    public String getDescription() {
        return "Does not change walking speed in web.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        try {
            final Field isInWeb = Entity.class.getDeclaredField(Mapping.isInWeb);
            isInWeb.setAccessible(true);
            isInWeb.setBoolean(Wrapper.INSTANCE.player(), false);
        }
        catch (Exception ex) {
            this.setToggled(false);
        }
        super.onClientTick(event);
    }
}
