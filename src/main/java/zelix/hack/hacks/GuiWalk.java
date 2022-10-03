package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.gui.inventory.*;
import zelix.gui.clickguis.gishcode.*;
import zelix.utils.*;
import net.minecraft.util.math.*;
import net.minecraft.client.entity.*;
import org.lwjgl.input.*;

public class GuiWalk extends Hack
{
    public GuiWalk() {
        super("HytGuiMove", HackCategory.HYT_UTILS);
    }
    
    @Override
    public String getDescription() {
        return "Allows you to walk while the gui is open.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (!(Wrapper.INSTANCE.mc().currentScreen instanceof GuiContainer) && !(Wrapper.INSTANCE.mc().currentScreen instanceof ClickGuiScreen)) {
            return;
        }
        double speed = 0.05;
        if (!Wrapper.INSTANCE.player().onGround) {
            speed /= 4.0;
        }
        this.handleJump();
        this.handleForward(speed);
        if (!Wrapper.INSTANCE.player().onGround) {
            speed /= 2.0;
        }
        this.handleBack(speed);
        this.handleLeft(speed);
        this.handleRight(speed);
        super.onClientTick(event);
    }
    
    void moveForward(final double speed) {
        final float direction = Utils.getDirection();
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        player.motionX -= MathHelper.sin(direction) * speed;
        final EntityPlayerSP player2 = Wrapper.INSTANCE.player();
        player2.motionZ += MathHelper.cos(direction) * speed;
    }
    
    void moveBack(final double speed) {
        final float direction = Utils.getDirection();
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        player.motionX += MathHelper.sin(direction) * speed;
        final EntityPlayerSP player2 = Wrapper.INSTANCE.player();
        player2.motionZ -= MathHelper.cos(direction) * speed;
    }
    
    void moveLeft(final double speed) {
        final float direction = Utils.getDirection();
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        player.motionZ += MathHelper.sin(direction) * speed;
        final EntityPlayerSP player2 = Wrapper.INSTANCE.player();
        player2.motionX += MathHelper.cos(direction) * speed;
    }
    
    void moveRight(final double speed) {
        final float direction = Utils.getDirection();
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        player.motionZ -= MathHelper.sin(direction) * speed;
        final EntityPlayerSP player2 = Wrapper.INSTANCE.player();
        player2.motionX -= MathHelper.cos(direction) * speed;
    }
    
    void handleForward(final double speed) {
        if (!Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindForward.getKeyCode())) {
            return;
        }
        this.moveForward(speed);
    }
    
    void handleBack(final double speed) {
        if (!Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindBack.getKeyCode())) {
            return;
        }
        this.moveBack(speed);
    }
    
    void handleLeft(final double speed) {
        if (!Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindLeft.getKeyCode())) {
            return;
        }
        this.moveLeft(speed);
    }
    
    void handleRight(final double speed) {
        if (!Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindRight.getKeyCode())) {
            return;
        }
        this.moveRight(speed);
    }
    
    void handleJump() {
        if (Wrapper.INSTANCE.player().onGround && Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindJump.getKeyCode())) {
            Wrapper.INSTANCE.player().jump();
        }
    }
}
