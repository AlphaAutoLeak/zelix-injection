package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import zelix.utils.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.entity.*;

public class AutoStep extends Hack
{
    public ModeValue mode;
    public NumberValue height;
    public float tempHeight;
    public int ticks;
    
    public AutoStep() {
        super("AutoStep", HackCategory.PLAYER);
        this.ticks = 0;
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("Simple", true), new Mode("AAC", false) });
        this.height = new NumberValue("Height", 0.5, 0.0, 10.0);
        this.addValue(this.mode, this.height);
    }
    
    @Override
    public String getDescription() {
        return "Allows you to walk on value blocks height.";
    }
    
    @Override
    public void onEnable() {
        this.ticks = 0;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        Wrapper.INSTANCE.player().stepHeight = 0.5f;
        super.onDisable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.mode.getMode("AAC").isToggled()) {
            final EntityPlayerSP player = Wrapper.INSTANCE.player();
            if (player.collidedVertically) {
                switch (this.ticks) {
                    case 0: {
                        if (player.onGround) {
                            player.jump();
                            break;
                        }
                        break;
                    }
                    case 7: {
                        player.motionY = 0.0;
                        break;
                    }
                    case 8: {
                        if (!player.onGround) {
                            player.setPosition(player.posX, player.posY + 1.0, player.posZ);
                            break;
                        }
                        break;
                    }
                }
                ++this.ticks;
            }
            else {
                this.ticks = 0;
            }
        }
        else if (this.mode.getMode("Simple").isToggled()) {
            Wrapper.INSTANCE.player().stepHeight = (float)(Object)this.height.getValue();
        }
        super.onClientTick(event);
    }
}
