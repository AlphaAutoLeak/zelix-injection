package zelix.otherhacks.net.wurstclient.forge.settings;

import zelix.otherhacks.net.wurstclient.forge.utils.*;
import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.clickgui.*;
import com.google.gson.*;

public final class SliderSetting extends Setting
{
    private double value;
    private final double defaultValue;
    private final double min;
    private final double max;
    private final double increment;
    private final ValueDisplay display;
    
    public SliderSetting(final String name, final String description, final double value, final double min, final double max, final double increment, final ValueDisplay display) {
        super(name, description);
        this.value = value;
        this.defaultValue = value;
        this.min = min;
        this.max = max;
        this.increment = increment;
        this.display = display;
    }
    
    public SliderSetting(final String name, final double value, final double min, final double max, final double increment, final ValueDisplay display) {
        this(name, null, value, min, max, increment, display);
    }
    
    public double getValue() {
        return this.value;
    }
    
    public float getValueF() {
        return (float)this.value;
    }
    
    public int getValueI() {
        return (int)this.value;
    }
    
    public String getValueString() {
        return this.display.getValueString(this.value);
    }
    
    public double getDefaultValue() {
        return this.defaultValue;
    }
    
    public void setValue(double value) {
        value = Math.round(value / this.increment) * this.increment;
        value = MathUtils.clamp(value, this.min, this.max);
        this.value = value;
        ForgeWurst.getForgeWurst().getHax().saveSettings();
    }
    
    public double getMin() {
        return this.min;
    }
    
    public double getMax() {
        return this.max;
    }
    
    @Override
    public Component getComponent() {
        return new Slider(this);
    }
    
    @Override
    public void fromJson(final JsonElement json) {
        if (!json.isJsonPrimitive()) {
            return;
        }
        final JsonPrimitive primitive = json.getAsJsonPrimitive();
        if (!primitive.isNumber()) {
            return;
        }
        this.setValue(primitive.getAsDouble());
    }
    
    @Override
    public JsonElement toJson() {
        return (JsonElement)new JsonPrimitive((Number)(Math.round(this.value * 1000000.0) / 1000000.0));
    }
    
    public interface ValueDisplay
    {
        public static final ValueDisplay DECIMAL = v -> Math.round(v * 1000000.0) / 1000000.0 + "";
        public static final ValueDisplay INTEGER = v -> (int)v + "";
        public static final ValueDisplay PERCENTAGE = v -> (int)(Math.round(v * 1.0E8) / 1000000.0) + "%";
        public static final ValueDisplay DEGREES = v -> (int)v + "бу";
        public static final ValueDisplay NONE = v -> "";
        
        String getValueString(final double p0);
    }
}
