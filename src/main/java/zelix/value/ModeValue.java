package zelix.value;

public class ModeValue extends Value<Mode>
{
    private Mode[] modes;
    private String modeName;
    
    public Mode[] getModes() {
        return this.modes;
    }
    
    public ModeValue(final String modeName, final Mode... modes) {
        super(modeName, null);
        this.modeName = modeName;
        this.modes = modes;
    }
    
    public ModeValue(final String modeName, final String[] modes) {
        super(modeName, null);
        this.modeName = modeName;
        for (int i = 0; i < modes.length; ++i) {
            if (i == 0) {
                this.modes[i] = new Mode(modes[i], true);
            }
            else {
                this.modes[i] = new Mode(modes[i], false);
            }
        }
    }
    
    public Mode getMode(final String name) {
        Mode m = null;
        for (final Mode mode : this.modes) {
            if (mode.getName().equals(name)) {
                m = mode;
            }
        }
        return m;
    }
    
    public Mode getSelectMode() {
        Mode m = null;
        for (final Mode mode : this.modes) {
            if (mode.isToggled()) {
                m = mode;
            }
        }
        return m;
    }
    
    public String getModeName() {
        return this.modeName;
    }
}
