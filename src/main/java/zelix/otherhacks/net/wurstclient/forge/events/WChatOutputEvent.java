package zelix.otherhacks.net.wurstclient.forge.events;

import net.minecraftforge.fml.common.eventhandler.*;
import com.google.common.base.*;

@Cancelable
public final class WChatOutputEvent extends Event
{
    private String message;
    private final String originalMessage;
    
    public WChatOutputEvent(final String message) {
        this.setMessage(message);
        this.originalMessage = Strings.nullToEmpty(message);
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = Strings.nullToEmpty(message);
    }
    
    public String getOriginalMessage() {
        return this.originalMessage;
    }
}
