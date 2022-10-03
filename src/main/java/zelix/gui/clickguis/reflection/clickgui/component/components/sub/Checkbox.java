package zelix.gui.clickguis.reflection.clickgui.component.components.sub;

import zelix.gui.clickguis.reflection.clickgui.component.*;
import zelix.gui.clickguis.reflection.clickgui.component.Component;
import zelix.gui.clickguis.reflection.clickgui.component.components.Button;
import zelix.value.*;
import zelix.gui.clickguis.reflection.clickgui.component.components.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;
import java.awt.*;

public class Checkbox extends Component
{
    private boolean hovered;
    private BooleanValue op;
    private Button parent;
    private int offset;
    private int x;
    private int y;
    
    public Checkbox(final BooleanValue option, final Button button, final int offset) {
        this.op = option;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
    }
    
    @Override
    public void renderComponent() {
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth() * 1, this.parent.parent.getY() + this.offset + 12, this.hovered ? -14540254 : -15658735);
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.op.getRenderName(), (float)((this.parent.parent.getX() + 10 + 4) * 2 + 5), (float)((this.parent.parent.getY() + this.offset + 2) * 2 + 4), -1);
        GL11.glPopMatrix();
        if (this.op.getValue()) {
            Gui.drawRect(this.parent.parent.getX() + 4 + 4, this.parent.parent.getY() + this.offset + 4, this.parent.parent.getX() + 8 + 4, this.parent.parent.getY() + this.offset + 8, Color.MAGENTA.getRGB());
        }
        else {
            Gui.drawRect(this.parent.parent.getX() + 5 + 4, this.parent.parent.getY() + this.offset + 5, this.parent.parent.getX() + 7 + 4, this.parent.parent.getY() + this.offset + 7, Color.MAGENTA.getRGB());
        }
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.op.setValue(!this.op.getValue());
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }
}
