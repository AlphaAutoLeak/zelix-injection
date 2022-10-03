package zelix.gui.clickguis.reflection.clickgui.component.components;

import zelix.hack.*;
import zelix.gui.clickguis.reflection.clickgui.component.*;
import net.minecraft.client.*;
import zelix.value.*;
import zelix.gui.clickguis.reflection.clickgui.component.components.sub.*;
import java.util.*;
import net.minecraft.client.gui.*;

public class Button extends Component
{
    public Hack mod;
    public Frame parent;
    public int offset;
    private boolean isHovered;
    private ArrayList<Component> subcomponents;
    public boolean open;
    public int height;
    public FontRenderer fr;
    
    public Button(final Hack mod, final Frame parent, final int offset) {
        this.fr = Minecraft.getMinecraft().fontRenderer;
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.height = 12;
        this.subcomponents = new ArrayList<Component>();
        this.open = false;
        int opY = offset + 12;
        if (mod.getValues() != null) {
            for (final Value s : mod.getValues()) {
                if (s instanceof ModeValue) {
                    this.subcomponents.add(new ModeButton((ModeValue)s, this, mod, opY));
                    opY += 12;
                }
                if (s instanceof NumberValue) {
                    this.subcomponents.add(new Slider((NumberValue)s, this, opY));
                    opY += 12;
                }
                if (s instanceof BooleanValue) {
                    this.subcomponents.add(new Checkbox((BooleanValue)s, this, opY));
                    opY += 12;
                }
            }
        }
        this.subcomponents.add(new Keybind(this, opY));
        this.subcomponents.add(new VisibleButton(this, mod, opY));
        this.subcomponents.add(new InfoButton(this, mod, opY));
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
        int opY = this.offset + 12;
        for (final Component comp : this.subcomponents) {
            comp.setOff(opY);
            opY += 12;
        }
    }
    
    @Override
    public void renderComponent() {
        Gui.drawRect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 12 + this.offset, this.isHovered ? -14540254 : -15658735);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.mod.getRenderName(), (float)(this.parent.getX() + 5), (float)(this.parent.getY() + this.offset + 2), this.mod.isToggled() ? 16712414 : 16777215);
        if (this.subcomponents.size() > 3) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.open ? "¡ì7\u25bc" : "¡ì7\u25b6", (float)(this.parent.getX() + this.parent.getWidth() - 10), (float)(this.parent.getY() + this.offset + 2), -1);
        }
        if (this.open && !this.subcomponents.isEmpty()) {
            for (final Component comp : this.subcomponents) {
                comp.renderComponent();
            }
        }
    }
    
    @Override
    public int getHeight() {
        if (this.open) {
            return 12 * (this.subcomponents.size() + 1);
        }
        return 12;
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.isHovered = this.isMouseOnButton(mouseX, mouseY);
        if (!this.subcomponents.isEmpty()) {
            for (final Component comp : this.subcomponents) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.mod.toggle();
        }
        if (this.isMouseOnButton(mouseX, mouseY) && button == 1) {
            this.open = !this.open;
            this.parent.refresh();
        }
        for (final Component comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        for (final Component comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int key) {
        for (final Component comp : this.subcomponents) {
            comp.keyTyped(typedChar, key);
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
    }
}
