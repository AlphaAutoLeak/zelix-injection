package zelix.gui.clickguis.reflection.clickgui.component;

import zelix.*;
import zelix.gui.clickguis.reflection.clickgui.component.components.Button;
import zelix.managers.*;
import zelix.hack.*;
import zelix.gui.clickguis.reflection.clickgui.component.components.*;
import java.util.*;
import net.minecraft.client.gui.*;
import java.awt.*;

public class Frame
{
    public ArrayList<Component> components;
    public HackCategory category;
    private boolean open;
    private int width;
    private int y;
    private int x;
    private int barHeight;
    private boolean isDragging;
    public int dragX;
    public int dragY;
    public static int color;
    
    public Frame(final HackCategory cat) {
        this.components = new ArrayList<Component>();
        this.category = cat;
        this.width = 88;
        this.x = 0;
        this.y = 60;
        this.dragX = 0;
        this.barHeight = 12;
        this.open = true;
        this.isDragging = false;
        int tY = this.barHeight;
        final HackManager hackManager = Core.hackManager;
        for (final Hack mod : HackManager.getModulesInType(this.category)) {
            final Button modButton = new Button(mod, this, tY);
            this.components.add(modButton);
            tY += 12;
        }
    }
    
    public ArrayList<Component> getComponents() {
        return this.components;
    }
    
    public void setX(final int newX) {
        this.x = newX;
    }
    
    public void setY(final int newY) {
        this.y = newY;
    }
    
    public void setDrag(final boolean drag) {
        this.isDragging = drag;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
    }
    
    public void renderFrame(final FontRenderer fontRenderer) {
        if (Calendar.getInstance().get(5) == 31 && Calendar.getInstance().get(2) == 11) {
            Gui.drawRect(this.x, this.y - 1, this.x + this.width, this.y, -1);
        }
        else {
            Gui.drawRect(this.x, this.y - 1, this.x + this.width, this.y, Color.MAGENTA.getRGB());
        }
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + 12, -15658735);
        if (Calendar.getInstance().get(5) == 31 && Calendar.getInstance().get(2) == 11) {
            if (this.category == HackCategory.COMBAT) {
                fontRenderer.drawStringWithShadow("¡ìb\u2694 ¡ìr" + this.category.name(), (float)(this.x + 5), (float)(this.y + 2), -1);
            }
            if (this.category == HackCategory.PLAYER) {
                fontRenderer.drawStringWithShadow("¡ìb\u2620 ¡ìr" + this.category.name(), (float)(this.x + 5), (float)(this.y + 2), -1);
            }
            if (this.category == HackCategory.MOVEMENT) {
                fontRenderer.drawStringWithShadow("¡ìb\u2607 ¡ìr" + this.category.name(), (float)(this.x + 5), (float)(this.y + 2), -1);
            }
            if (this.category == HackCategory.ANOTHER) {
                fontRenderer.drawStringWithShadow("¡ìb\u2600 ¡ìr" + this.category.name(), (float)(this.x + 5), (float)(this.y + 2), -1);
            }
            if (this.category == HackCategory.VISUAL) {
                fontRenderer.drawStringWithShadow("¡ìb\u2761 ¡ìr" + this.category.name(), (float)(this.x + 5), (float)(this.y + 2), -1);
            }
        }
        else {
            if (this.category == HackCategory.COMBAT) {
                fontRenderer.drawStringWithShadow("¡ìd\u2694 ¡ìr" + this.category.name(), (float)(this.x + 5), (float)(this.y + 2), -1);
            }
            if (this.category == HackCategory.PLAYER) {
                fontRenderer.drawStringWithShadow("¡ìd\u2620 ¡ìr" + this.category.name(), (float)(this.x + 5), (float)(this.y + 2), -1);
            }
            if (this.category == HackCategory.MOVEMENT) {
                fontRenderer.drawStringWithShadow("¡ìd\u2607 ¡ìr" + this.category.name(), (float)(this.x + 5), (float)(this.y + 2), -1);
            }
            if (this.category == HackCategory.ANOTHER) {
                fontRenderer.drawStringWithShadow("¡ìd\u2600 ¡ìr" + this.category.name(), (float)(this.x + 5), (float)(this.y + 2), -1);
            }
            if (this.category == HackCategory.VISUAL) {
                fontRenderer.drawStringWithShadow("¡ìd\u2761 ¡ìr" + this.category.name(), (float)(this.x + 5), (float)(this.y + 2), -1);
            }
        }
        if (this.open && !this.components.isEmpty()) {
            for (final Component component : this.components) {
                component.renderComponent();
            }
        }
    }
    
    public void refresh() {
        int off = this.barHeight;
        for (final Component comp : this.components) {
            comp.setOff(off);
            off += comp.getHeight();
        }
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void updatePosition(final int mouseX, final int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
    }
    
    public boolean isWithinHeader(final int x, final int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }
}
