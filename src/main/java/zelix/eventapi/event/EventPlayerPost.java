package zelix.eventapi.event;

import net.minecraftforge.fml.common.eventhandler.*;

public class EventPlayerPost extends Event
{
    private float yaw;
    private float pitch;
    
    public EventPlayerPost(final float yaw, final float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
}
