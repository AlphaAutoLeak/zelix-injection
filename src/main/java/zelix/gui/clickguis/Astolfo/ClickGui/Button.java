package zelix.gui.clickguis.Astolfo.ClickGui;

import zelix.hack.*;
import com.google.common.collect.*;
import zelix.value.*;
import zelix.*;
import zelix.managers.*;
import java.util.*;
import net.minecraft.client.*;
import org.lwjgl.opengl.*;
import java.awt.*;
import zelix.utils.hooks.visual.font.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;

public class Button
{
    public Hack cheat;
    public Window parent;
    public int x;
    public int y;
    public int enable;
    public int arrow;
    public int index;
    public int remander;
    private int valueY;
    public double opacity;
    public ArrayList<ValueButton> buttons;
    public boolean expand;
    int staticColor;
    public HackCategory category;
    
    public Button(final HackCategory category, final Hack cheat, final int x, final int y) {
        this.opacity = 0.0;
        this.buttons = Lists.newArrayList();
        this.category = category;
        this.cheat = cheat;
        this.x = x;
        this.y = y;
        int y2 = y + 15;
        this.valueY = 0;
        for (final Value v : cheat.getValues()) {
            this.buttons.add(new ValueButton(category, v, x + 5, y2));
            y2 += 20;
        }
        final HackManager hackManager = Core.hackManager;
        if (cheat == HackManager.getHack("HUD")) {
            this.buttons.add(new ColorValueButton(category, x + 15, y2));
        }
        this.buttons.add(new KeyBindButton(category, cheat, x + 5, y2));
    }
    
    public static void doGlScissor(final int x, final int y, final int width, final int height) {
        final Minecraft mc = Minecraft.getMinecraft();
        int scaleFactor = 1;
        int k = mc.gameSettings.guiScale;
        if (k == 0) {
            k = 1000;
        }
        while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        GL11.glScissor(x * scaleFactor, mc.displayHeight - (y + height) * scaleFactor, width * scaleFactor, height * scaleFactor);
    }
    
    public void render(final int mouseX, final int mouseY, final Limitation limitation) {
        if (this.index != 0) {
            final Button b2 = this.parent.buttons.get(this.index - 1);
            this.y = b2.y + 15 + (b2.expand ? (15 * b2.buttons.size()) : 0);
        }
        for (int i = 0; i < this.buttons.size(); ++i) {
            this.buttons.get(i).y = this.y + 14 + 15 * i;
            this.buttons.get(i).x = this.x + 5;
        }
        if (this.parent.category.name().equals("COMBAT")) {
            this.staticColor = new Color(231, 76, 60).getRGB();
        }
        else if (this.parent.category.name().equals("VISUAL")) {
            this.staticColor = new Color(54, 1, 205).getRGB();
        }
        else if (this.parent.category.name().equals("MOVEMENT")) {
            this.staticColor = new Color(45, 203, 113).getRGB();
        }
        else if (this.parent.category.name().equals("PLAYERS")) {
            this.staticColor = new Color(141, 68, 173).getRGB();
        }
        else if (this.parent.category.name().equals("ANOTHER")) {
            this.staticColor = new Color(38, 154, 255).getRGB();
        }
        GL11.glPushMatrix();
        GL11.glEnable(3089);
        doGlScissor(this.x - 5, this.y - 5, 90, FontLoaders.kiona18.getStringHeight(this.cheat.getName().toLowerCase()) + 5);
        limitation.cut();
        Gui.drawRect(this.x - 5, this.y - 5, this.x + 85, this.y + 5 + FontLoaders.kiona18.getStringHeight(this.cheat.getName().toLowerCase()), new Color(39, 39, 39).getRGB());
        if (this.cheat.isToggled()) {
            limitation.cut();
            Gui.drawRect(this.x - 4, this.y - 5, this.x + 84, this.y + 10, this.staticColor);
        }
        if (this.cheat.isToggled()) {
            if (this.enable < 180) {
                this.enable += 10;
            }
            limitation.cut();
            FontLoaders.kiona16.drawString(this.cheat.getName().toLowerCase(), this.x + 81 - FontLoaders.kiona16.getStringWidth(this.cheat.getName().toLowerCase()), this.y, new Color(220, 220, 220).getRGB());
        }
        else {
            if (this.enable > 0) {
                this.enable -= 10;
            }
            limitation.cut();
            FontLoaders.kiona16.drawString(this.cheat.getName().toLowerCase(), this.x + 81 - FontLoaders.kiona16.getStringWidth(this.cheat.getName().toLowerCase()), this.y, new Color(220, 220, 220).getRGB());
        }
        if (this.cheat.getValues().size() > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(this.x + 78), (float)(this.y + 2), 0.0f);
            GlStateManager.rotate((float)this.arrow, 0.0f, 0.0f, 1.0f);
            if (this.expand && this.arrow < 180) {
                this.arrow += 10;
            }
            else if (!this.expand && this.arrow > 0) {
                this.arrow -= 10;
            }
            GlStateManager.translate((float)(-(this.x + 78)), (float)(-(this.y + 2)), 0.0f);
            GlStateManager.popMatrix();
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
        if (this.expand) {
            this.buttons.forEach(component -> component.render(mouseX, mouseY, limitation));
        }
    }
    
    public void key(final char typedChar, final int keyCode) {
        this.buttons.forEach(b -> b.key(typedChar, keyCode));
    }
    
    private boolean isHovering(final int n, final int n2) {
        final boolean b = n >= this.x && n <= this.x - 7 && n2 >= this.y && n2 <= this.y + FontLoaders.kiona18.getStringHeight(this.cheat.getName());
        return b;
    }
    
    public void click(final int mouseX, final int mouseY, final int button) {
        if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + FontLoaders.kiona18.getStringHeight(this.cheat.getName())) {
            if (button == 0) {
                this.cheat.setToggled(!this.cheat.isToggled());
            }
            if (button == 1 && !this.buttons.isEmpty()) {
                this.expand = !this.expand;
                final boolean expand = this.expand;
            }
        }
        if (this.expand) {
            this.buttons.forEach(b -> b.click(mouseX, mouseY, button));
        }
    }
    
    public void setParent(final Window parent) {
        this.parent = parent;
        for (int i = 0; i < this.parent.buttons.size(); ++i) {
            if (this.parent.buttons.get(i) == this) {
                this.index = i;
                this.remander = this.parent.buttons.size() - i;
                break;
            }
        }
    }
}
