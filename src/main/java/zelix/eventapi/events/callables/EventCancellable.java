package zelix.eventapi.events.callables;

import zelix.eventapi.events.*;

public abstract class EventCancellable implements Event, Cancellable
{
    private boolean cancelled;
    
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    @Override
    public void setCancelled(final boolean state) {
        this.cancelled = state;
    }
}
