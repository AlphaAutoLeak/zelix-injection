package zelix.gui.clickguis.kendall.components.impls;

import zelix.gui.clickguis.kendall.components.*;
import zelix.value.*;
import zelix.utils.hooks.visual.font.*;
import zelix.utils.hooks.visual.*;
import org.lwjgl.input.*;

public class KendallOption extends Component
{
    public KendallButton parent;
    public int x;
    public BooleanValue value;
    public int y;
    boolean previousmouse;
    boolean mouse;
    
    public KendallOption(final BooleanValue value, final KendallButton parent) {
        this.previousmouse = true;
        this.value = value;
        this.parent = parent;
    }
    
    @Override
    public void render(final float x, final float y, final int mouseX, final int mouseY) {
        RenderUtils.drawBorderedRect(x, y, x + 250.0f, y + 29.0f, 0.0f, 0, -14737374);
        FontLoaders.default14.drawString(this.value.getRenderName(), x + 10.0f, y + 5.0f, -1711933961);
        if (this.value.getValue()) {
            RenderUtils.drawBorderedRect(x + 10.0f, y + 14.0f, x + 10.0f + 24.0f, y + 23.0f, 1.0f, ColorUtils.rainbow(50), ColorUtils.rainbow(50));
            RenderUtils.drawBorderedRect(x + 10.0f + 15.0f, y + 15.0f, x + 10.0f + 22.0f, y + 21.0f, 1.0f, -15198184, -15198184);
        }
        else {
            RenderUtils.drawBorderedRect(x + 10.0f, y + 14.0f, x + 10.0f + 24.0f, y + 23.0f, 1.0f, -13749703, -13749703);
            RenderUtils.drawBorderedRect(x + 10.0f + 2.0f, y + 15.0f, x + 10.0f + 9.0f, y + 21.0f, 1.0f, ColorUtils.rainbow(50), ColorUtils.rainbow(50));
        }
        if (KendallButton.isButtonHovered(x + 10.0f, y + 13.0f, x + 10.0f + 24.0f, y + 22.0f, mouseX, mouseY)) {
            if (!this.previousmouse && Mouse.isButtonDown(0)) {
                this.previousmouse = true;
                this.mouse = true;
            }
            if (this.mouse) {
                this.value.setValue(!this.value.getValue());
                this.mouse = false;
            }
        }
        if (!Mouse.isButtonDown(0)) {
            this.previousmouse = false;
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    public static boolean isHovered(final float x, final float y, final float x2, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}
