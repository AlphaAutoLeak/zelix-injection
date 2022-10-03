package zelix.gui.clickguis.gishcode.elements;

import zelix.gui.clickguis.gishcode.base.*;

public class Text extends Component
{
    private String[] text;
    
    public Text(final int xPos, final int yPos, final int width, final int height, final Component component, final String[] text) {
        super(xPos, yPos, width, height, ComponentType.TEXT, component, "");
        this.text = text;
    }
    
    public String[] getMessage() {
        return this.text;
    }
}
