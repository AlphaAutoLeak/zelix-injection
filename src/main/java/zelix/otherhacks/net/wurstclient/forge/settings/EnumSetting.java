package zelix.otherhacks.net.wurstclient.forge.settings;

import java.util.*;
import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.clickgui.*;
import com.google.gson.*;

public final class EnumSetting<T extends Enum> extends Setting
{
    private final T[] values;
    private T selected;
    private final T defaultSelected;
    
    public EnumSetting(final String name, final String description, final T[] values, final T selected) {
        super(name, description);
        this.values = Objects.requireNonNull(values);
        this.selected = Objects.requireNonNull(selected);
        this.defaultSelected = selected;
    }
    
    public EnumSetting(final String name, final T[] values, final T selected) {
        this(name, null, values, selected);
    }
    
    public T[] getValues() {
        return (T[])this.values;
    }
    
    public T getSelected() {
        return this.selected;
    }
    
    public T getDefaultSelected() {
        return this.defaultSelected;
    }
    
    public void setSelected(final T selected) {
        this.selected = Objects.requireNonNull(selected);
        ForgeWurst.getForgeWurst().getHax().saveSettings();
    }
    
    public void setSelected(final String selected) {
        for (final T value : this.values) {
            if (value.toString().equalsIgnoreCase(selected)) {
                this.setSelected(value);
                break;
            }
        }
    }
    
    @Override
    public Component getComponent() {
        return new ComboBox(this);
    }
    
    @Override
    public void fromJson(final JsonElement json) {
        if (!json.isJsonPrimitive()) {
            return;
        }
        final JsonPrimitive primitive = json.getAsJsonPrimitive();
        if (!primitive.isString()) {
            return;
        }
        this.setSelected(primitive.getAsString());
    }
    
    @Override
    public JsonElement toJson() {
        return (JsonElement)new JsonPrimitive(this.selected.toString());
    }
}
