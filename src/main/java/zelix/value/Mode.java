package zelix.value;

public class Mode
{
    private String name;
    private boolean toggled;
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public boolean isToggled() {
        return this.toggled;
    }
    
    public void setToggled(final boolean toggled) {
        this.toggled = toggled;
    }
    
    public Mode(final String name, final boolean toggled) {
        this.name = name;
        this.toggled = toggled;
    }
}
