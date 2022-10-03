package zelix.hack.hacks;

import net.minecraft.world.*;
import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.gui.inventory.*;
import zelix.utils.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.common.gameevent.*;

public class FakeCreative extends Hack
{
    public GameType gameType;
    public BooleanValue showItemsId;
    
    public FakeCreative() {
        super("FakeCreative", HackCategory.ANOTHER);
        this.showItemsId = new BooleanValue("ShowItemsID", Boolean.valueOf(true));
        this.addValue(this.showItemsId);
    }
    
    @Override
    public void onGuiOpen(final GuiOpenEvent event) {
        if (event.getGui() instanceof GuiContainerCreative) {
            event.setGui((GuiScreen)new zelix.gui.clickguis.GuiContainerCreative((EntityPlayer)Wrapper.INSTANCE.player()));
        }
        super.onGuiOpen(event);
    }
    
    @Override
    public void onDisable() {
        if (this.gameType == null) {
            return;
        }
        Wrapper.INSTANCE.controller().setGameType(this.gameType);
        this.gameType = null;
        super.onDisable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Wrapper.INSTANCE.controller().getCurrentGameType() == GameType.CREATIVE) {
            return;
        }
        this.gameType = Wrapper.INSTANCE.controller().getCurrentGameType();
        Wrapper.INSTANCE.controller().setGameType(GameType.CREATIVE);
        super.onClientTick(event);
    }
}
