package zelix.gui.clickguis.reflection.clickgui.component.components.sub;

import zelix.gui.clickguis.reflection.clickgui.component.*;
import zelix.gui.clickguis.reflection.clickgui.component.Component;
import zelix.gui.clickguis.reflection.clickgui.component.components.*;
import net.minecraft.client.gui.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;
import java.math.*;

import zelix.gui.clickguis.reflection.clickgui.component.components.Button;
import zelix.value.*;

public class Slider extends Component
{
    private boolean hovered;
    private NumberValue set;
    private Button parent;
    private int offset;
    private int x;
    private int y;
    private boolean dragging;
    private double renderWidth;
    
    public Slider(final NumberValue value, final Button button, final int offset) {
        this.dragging = false;
        this.set = value;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
    }
    
    @Override
    public void renderComponent() {
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 12, -15658735);
        final int drag = (int)(this.set.getValue() / this.set.getMax() * this.parent.parent.getWidth());
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 12, -15658735);
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 8, this.parent.parent.getX() + 88, this.parent.parent.getY() + this.offset + 12, Color.GRAY.darker().darker().darker().getRGB());
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 8, this.parent.parent.getX() + (int)this.renderWidth, this.parent.parent.getY() + this.offset + 12, this.hovered ? -11184811 : -12303292);
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.set.getRenderName() + ": " + (this.set).getValue(), (float)(this.parent.parent.getX() * 2 + 15), (float)((this.parent.parent.getY() + this.offset - 1) * 2 + 5), -1);
        GL11.glPopMatrix();
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.hovered = (this.isMouseOnButtonD(mouseX, mouseY) || this.isMouseOnButtonI(mouseX, mouseY));
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
        final double diff = Math.min(88, Math.max(0, mouseX - this.x));
        final double min = this.set.getMin();
        final double max = this.set.getMax();
        this.renderWidth = 88.0 * (this.set.getValue() - min) / (max - min);
        if (this.dragging) {
            if (diff == 0.0) {
                this.set.setValue(this.set.getMin());
            }
            else {
                final double newValue = roundToPlace(diff / 88.0 * (max - min) + min, 2);
                this.set.setValue(newValue);
            }
        }
    }
    
    private static double roundToPlace(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.dragging = true;
        }
        if (this.isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.dragging = true;
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        this.dragging = false;
    }
    
    public boolean isMouseOnButtonD(final int x, final int y) {
        return x > this.x && x < this.x + (this.parent.parent.getWidth() / 2 + 1) && y > this.y && y < this.y + 12;
    }
    
    public boolean isMouseOnButtonI(final int x, final int y) {
        return x > this.x + this.parent.parent.getWidth() / 2 && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + 12;
    }
}
