package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;
import net.minecraft.entity.*;
import org.lwjgl.input.*;
import net.minecraft.client.gui.inventory.*;
import zelix.gui.clickguis.gishcode.*;
import zelix.managers.*;
import net.minecraft.init.*;

public class AutoSprint extends Hack
{
    public AutoSprint() {
        super("AutoSprint", HackCategory.MOVEMENT);
    }
    
    @Override
    public String getDescription() {
        return "Sprints automatically when you should be walking.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.isMoveInGui() && this.canSprint(false)) {
            Wrapper.INSTANCE.player().setSprinting(true);
            return;
        }
        if (this.canSprint(true)) {
            Wrapper.INSTANCE.player().setSprinting(Utils.isMoving((Entity)Wrapper.INSTANCE.player()));
        }
        super.onClientTick(event);
    }
    
    boolean isMoveInGui() {
        return Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindForward.getKeyCode()) && (Wrapper.INSTANCE.mc().currentScreen instanceof GuiContainer || Wrapper.INSTANCE.mc().currentScreen instanceof ClickGuiScreen) && HackManager.getHack("HytGuiMove").isToggled();
    }
    
    boolean canSprint(final boolean forward) {
        return Wrapper.INSTANCE.player().onGround && !Wrapper.INSTANCE.player().isSprinting() && !Wrapper.INSTANCE.player().isOnLadder() && !Wrapper.INSTANCE.player().isInWater() && !Wrapper.INSTANCE.player().isInLava() && !Wrapper.INSTANCE.player().collidedVertically && (!forward || Wrapper.INSTANCE.player().moveForward >= 0.1f) && !Wrapper.INSTANCE.player().isSneaking() && Wrapper.INSTANCE.player().getFoodStats().getFoodLevel() >= 6 && !Wrapper.INSTANCE.player().isRiding() && !Wrapper.INSTANCE.player().isPotionActive(MobEffects.BLINDNESS);
    }
}
