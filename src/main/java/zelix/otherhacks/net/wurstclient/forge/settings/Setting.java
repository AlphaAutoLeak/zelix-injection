package zelix.otherhacks.net.wurstclient.forge.settings;

import java.util.*;
import zelix.otherhacks.net.wurstclient.forge.clickgui.*;
import com.google.gson.*;

public abstract class Setting
{
    private final String name;
    private final String description;
    
    public Setting(final String name, final String description) {
        this.name = Objects.requireNonNull(name);
        this.description = description;
    }
    
    public final String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public abstract Component getComponent();
    
    public abstract void fromJson(final JsonElement p0);
    
    public abstract JsonElement toJson();
}
