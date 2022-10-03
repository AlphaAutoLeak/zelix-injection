package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;

public class ElytraFly2b2t extends Hack
{
    private double hoverTarget;
    private boolean hoverState;
    BooleanValue hover;
    BooleanValue AutoOpen;
    NumberValue speed;
    NumberValue downSpeed;
    NumberValue sinkSpeed;
    
    public ElytraFly2b2t() {
        super("ElytraFly", HackCategory.MOVEMENT);
        this.hoverTarget = -1.0;
        this.hoverState = false;
        this.hover = new BooleanValue("Hover", Boolean.valueOf(true));
        this.AutoOpen = new BooleanValue("AutoOpen", Boolean.valueOf(true));
        this.speed = new NumberValue("Speed", 1.8, 0.0, 10.0);
        this.downSpeed = new NumberValue("Down Speed", 2.0, 0.0, 10.0);
        this.sinkSpeed = new NumberValue("Sink Speed", 0.001, 0.0, 10.0);
        this.addValue(this.hover, this.AutoOpen, this.sinkSpeed, this.speed, this.downSpeed);
    }
}
