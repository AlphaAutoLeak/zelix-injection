package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.gui.*;

public class Disconnect extends Hack
{
    public NumberValue leaveHealth;
    
    public Disconnect() {
        super("Disconnect", HackCategory.COMBAT);
        this.leaveHealth = new NumberValue("LeaveHealth", 4.0, 0.0, 20.0);
        this.addValue(this.leaveHealth);
    }
    
    @Override
    public String getDescription() {
        return "Automatically leaves the server when your health is low.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Wrapper.INSTANCE.player().getHealth() <= (float)(Object)this.leaveHealth.getValue()) {
            final boolean flag = Wrapper.INSTANCE.mc().isIntegratedServerRunning();
            Wrapper.INSTANCE.world().sendQuittingDisconnectingPacket();
            Wrapper.INSTANCE.mc().loadWorld((WorldClient)null);
            if (flag) {
                Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)new GuiMainMenu());
            }
            else {
                Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)new GuiMainMenu()));
            }
            this.setToggled(false);
        }
        super.onClientTick(event);
    }
}
