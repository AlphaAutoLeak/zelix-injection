package zelix.otherhacks.net.wurstclient.forge.settings;

import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.clickgui.*;
import com.google.gson.*;

public final class CheckboxSetting extends Setting
{
    private boolean checked;
    private final boolean checkedByDefault;
    
    public CheckboxSetting(final String name, final String description, final boolean checked) {
        super(name, description);
        this.checked = checked;
        this.checkedByDefault = checked;
    }
    
    public CheckboxSetting(final String name, final boolean checked) {
        this(name, null, checked);
    }
    
    public boolean isChecked() {
        return this.checked;
    }
    
    public boolean isCheckedByDefault() {
        return this.checkedByDefault;
    }
    
    public void setChecked(final boolean checked) {
        this.checked = checked;
        ForgeWurst.getForgeWurst().getHax().saveSettings();
    }
    
    @Override
    public Component getComponent() {
        return new Checkbox(this);
    }
    
    @Override
    public void fromJson(final JsonElement json) {
        if (!json.isJsonPrimitive()) {
            return;
        }
        final JsonPrimitive primitive = json.getAsJsonPrimitive();
        if (!primitive.isBoolean()) {
            return;
        }
        this.setChecked(primitive.getAsBoolean());
    }
    
    @Override
    public JsonElement toJson() {
        return (JsonElement)new JsonPrimitive(this.checked);
    }
}
