package zelix.gui.clickguis.reflection.clickgui.component.components.sub;

import zelix.gui.clickguis.reflection.clickgui.component.*;
import zelix.gui.clickguis.reflection.clickgui.component.components.*;
import zelix.value.*;
import zelix.hack.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;

public class ModeButton extends Component
{
    private boolean hovered;
    private Button parent;
    private ModeValue set;
    private int offset;
    private int x;
    private int y;
    private Hack mod;
    private int modeIndex;
    
    public ModeButton(final ModeValue set, final Button button, final Hack mod, final int offset) {
        this.set = set;
        this.parent = button;
        this.mod = mod;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
        for (int i = 0; i < set.getModes().length; ++i) {
            if (set.getModes()[i] == set.getSelectMode()) {
                this.modeIndex = i;
                break;
            }
            this.modeIndex = 0;
        }
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }
    
    @Override
    public void renderComponent() {
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth() * 1, this.parent.parent.getY() + this.offset + 12, this.hovered ? -14540254 : -15658735);
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.set.getRenderName().replaceAll(" +", "") + ": " + this.set.getModes()[this.modeIndex].getName(), (float)((this.parent.parent.getX() + 7) * 2), (float)((this.parent.parent.getY() + this.offset + 2) * 2 + 5), -1);
        GL11.glPopMatrix();
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
            final int maxIndex = this.set.getModes().length;
            this.set.getModes()[this.modeIndex].setToggled(false);
            if (this.modeIndex + 1 >= maxIndex) {
                this.modeIndex = 0;
            }
            else {
                ++this.modeIndex;
            }
            this.set.getModes()[this.modeIndex].setToggled(true);
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }
}
