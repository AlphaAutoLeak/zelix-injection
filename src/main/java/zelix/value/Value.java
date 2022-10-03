package zelix.value;

import zelix.hack.hacks.*;

public class Value<T>
{
    public T value;
    private String name;
    public static String chinese;
    private T defaultValue;
    
    public Value(final String name, final T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getRenderName() {
        if (Value.chinese != null && ClickGui.language.getMode("Chinese").isToggled()) {
            return Value.chinese;
        }
        return this.name;
    }
    
    public void setCNName(final String cnName) {
        Value.chinese = cnName;
    }
    
    public T getDefaultValue() {
        return this.defaultValue;
    }
    
    public T getValue() {
        return this.value;
    }
    
    public void setValue(final T value) {
        this.value = value;
    }
}
