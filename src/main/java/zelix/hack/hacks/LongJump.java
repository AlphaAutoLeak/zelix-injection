package zelix.hack.hacks;

import zelix.hack.*;
import zelix.managers.*;
import zelix.value.*;
import zelix.utils.hooks.visual.*;
import zelix.utils.*;
import net.minecraft.client.entity.*;

public class LongJump extends Hack
{
    Hack h;
    TimerUtils timer;
    ModeValue mode;
    NumberValue distance;
    NumberValue blinktime;
    NumberValue y;
    
    public LongJump() {
        super("LongJump", HackCategory.MOVEMENT);
        this.h = HackManager.getHack("Blink");
        this.timer = new TimerUtils();
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("Simple", false), new Mode("Blink", true), new Mode("HYT", false) });
        this.blinktime = new NumberValue("BlinkTime", 500.0, 0.0, 2000.0);
        this.distance = new NumberValue("Distance", 4.0, 2.0, 8.0);
        this.y = new NumberValue("MotionY", 0.4, 0.0, 2.0);
        this.addValue(this.mode, this.blinktime, this.distance, this.y);
    }
    
    @Override
    public String getDescription() {
        return "Jump further";
    }
    
    @Override
    public void onEnable() {
        Utils.nullCheck();
        if (this.mode.getMode("Simple").isToggled()) {
            this.Jump();
        }
        else if (this.mode.getMode("Blink").isToggled()) {
            this.h.onEnable();
            this.h.setToggled(true);
            this.Jump();
        }
        else if (this.mode.getMode("HYT").isToggled()) {
            this.h.onEnable();
            this.h.setToggled(true);
            this.JumpHYT();
            if (this.timer.hasReached((float)(Object)this.blinktime.getValue())) {
                ChatUtils.message("OK");
                this.onDisable();
                this.timer.reset();
            }
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.h.onDisable();
        this.h.setToggled(false);
        super.onDisable();
    }
    
    public void Jump() {
        if (PlayerControllerUtils.isMoving()) {
            MoveUtils.strafeHYT((float)(Object)this.distance.getValue());
            Wrapper.INSTANCE.player().motionY = (float)(Object)this.y.getValue();
            MoveUtils.strafeHYT((float)(Object)this.distance.getValue());
        }
        else {
            final EntityPlayerSP player = Wrapper.INSTANCE.player();
            final EntityPlayerSP player2 = Wrapper.INSTANCE.player();
            final double n = 0.0;
            player2.motionZ = n;
            player.motionX = n;
        }
    }
    
    public void JumpHYT() {
        if (PlayerControllerUtils.isMoving()) {
            MoveUtils.strafeHYT((float)(Object)this.distance.getValue());
            Wrapper.INSTANCE.player().motionY = (float)(Object)this.y.getValue();
            MoveUtils.strafeHYT((float)(Object)this.distance.getValue());
        }
        else {
            final EntityPlayerSP player = Wrapper.INSTANCE.player();
            final EntityPlayerSP player2 = Wrapper.INSTANCE.player();
            final double n = 0.0;
            player2.motionZ = n;
            player.motionX = n;
        }
    }
}
