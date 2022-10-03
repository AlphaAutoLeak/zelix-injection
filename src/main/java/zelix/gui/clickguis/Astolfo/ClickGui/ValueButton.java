package zelix.gui.clickguis.Astolfo.ClickGui;

import zelix.hack.*;
import zelix.utils.hooks.visual.font.*;
import org.lwjgl.input.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import java.util.*;
import zelix.value.*;

public class ValueButton
{
    public Value value;
    public String name;
    public boolean custom;
    public boolean change;
    public int x;
    public int y;
    public double opacity;
    public HackCategory category;
    public ModeValue priority;
    public ModeValue mode;
    public ModeValue rotations;
    int press;
    
    public ValueButton(final HackCategory category, final Value value, final int x, final int y) {
        this.press = 0;
        this.category = category;
        this.custom = false;
        this.opacity = 0.0;
        this.value = value;
        this.x = x;
        this.y = y;
        this.name = "";
        if (this.value instanceof BooleanValue) {
            this.change = ((BooleanValue)this.value).getValue();
        }
        else if (this.value instanceof ModeValue) {
            this.name = "" + ((Value<Object>)this.value).getValue();
        }
        else if (value instanceof NumberValue) {
            final NumberValue v = (NumberValue)value;
            this.name = String.valueOf(this.name + (double)(int)(Object)v.getValue());
        }
        this.opacity = 0.0;
    }
    
    public void render(final int mouseX, final int mouseY, final Limitation limitation) {
        if (!this.custom) {
            this.opacity = ((mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + FontLoaders.kiona18.getStringHeight(this.value.getName()) + 6) ? ((this.opacity + 10.0 < 200.0) ? (this.opacity += 10.0) : 200.0) : ((this.opacity - 6.0 > 0.0) ? (this.opacity -= 6.0) : 0.0));
            if (this.value instanceof BooleanValue) {
                this.change = ((BooleanValue)this.value).getValue();
            }
            else if (this.value instanceof ModeValue) {
                this.name = "" + ((ModeValue)this.value).getSelectMode().getName();
            }
            else if (this.value instanceof NumberValue) {
                final NumberValue v = (NumberValue)this.value;
                this.name = "" + (double)(v).getValue().intValue();
                if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y + FontLoaders.kiona14.getStringHeight(this.value.getName()) - 10 && mouseY < this.y + FontLoaders.kiona14.getStringHeight(this.value.getName()) + 2 && Mouse.isButtonDown(0)) {
                    final double min = v.getMin().doubleValue();
                    final double max = v.getMax().doubleValue();
                    final double inc = (v).getValue().doubleValue();
                    final double valAbs = mouseX - (this.x + 1.0);
                    double perc = valAbs / 68.0;
                    perc = Math.min(Math.max(0.0, perc), 1.0);
                    final double valRel = (max - min) * perc;
                    double val = min + valRel;
                    val = Math.round(val * (1.0 / inc)) / (1.0 / inc);
                    v.setValue(val);
                }
            }
            final int staticColor = this.category.name().equals("COMBAT") ? new Color(231, 76, 60).getRGB() : (this.category.name().equals("VISUAL") ? new Color(54, 1, 205).getRGB() : (this.category.name().equals("MOVEMENTF") ? new Color(45, 203, 113).getRGB() : (this.category.name().equals("PLAYER") ? new Color(141, 68, 173).getRGB() : (this.category.name().equals("ANOTHER") ? new Color(38, 154, 255).getRGB() : new Color(38, 154, 255).getRGB()))));
            GL11.glEnable(3089);
            limitation.cut();
            Gui.drawRect(this.x - 10, this.y - 4, this.x + 80, this.y + 11, new Color(39, 39, 39).getRGB());
            if (this.value instanceof BooleanValue) {
                FontLoaders.kiona14.drawString(this.value.getName(), this.x - 7, this.y + 2, ((boolean)((BooleanValue)this.value).getValue()) ? new Color(255, 255, 255).getRGB() : new Color(108, 108, 108).getRGB());
            }
            final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
            if (this.value instanceof ModeValue) {
                FontLoaders.kiona14.drawString(this.value.getName(), this.x - 7, this.y + 3, new Color(255, 255, 255).getRGB());
                FontLoaders.kiona14.drawString(this.name, this.x + 77 - FontLoaders.kiona14.getStringWidth(this.name), this.y + 3, new Color(182, 182, 182).getRGB());
            }
            if (this.value instanceof NumberValue) {
                final NumberValue v2 = (NumberValue)this.value;
                final double render = 82.0f * ((v2).getValue().floatValue() - v2.getMin().floatValue()) / (v2.getMax().floatValue() - v2.getMin().floatValue());
                Gui.drawRect(this.x - 8, this.y + FontLoaders.kiona14.getStringHeight(this.value.getName()) + 2, this.x + 78, this.y + FontLoaders.kiona14.getStringHeight(this.value.getName()) - 9, new Color(50, 50, 50, 180).getRGB());
                Gui.drawRect(this.x - 8, this.y + FontLoaders.kiona14.getStringHeight(this.value.getName()) + 2, (int)(this.x - 4 + render), this.y + FontLoaders.kiona14.getStringHeight(this.value.getName()) - 9, staticColor);
            }
            if (this.value instanceof NumberValue) {
                FontLoaders.kiona14.drawString(this.value.getName(), this.x - 7, this.y, new Color(255, 255, 255).getRGB());
                FontLoaders.kiona14.drawString(this.name, this.x + FontLoaders.kiona14.getStringWidth(this.value.getName()), this.y, -1);
            }
            GL11.glDisable(3089);
        }
    }
    
    public void key(final char typedChar, final int keyCode) {
    }
    
    private boolean isHovering(final int n, final int n2) {
        final boolean b = n >= this.x && n <= this.x - 7 && n2 >= this.y && n2 <= this.y + FontLoaders.kiona18.getStringHeight(this.value.getName());
        return b;
    }
    
    public void click(final int mouseX, final int mouseY, final int button) {
        if (!this.custom && mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + FontLoaders.kiona18.getStringHeight(this.value.getName())) {
            if (this.value instanceof BooleanValue) {
                BooleanValue v = (BooleanValue)this.value;
                v.setValue(!(v = (BooleanValue)this.value).getValue());
                return;
            }
            if (this.value instanceof ModeValue) {
                ++this.press;
                final ModeValue m = (ModeValue)this.value;
                final ArrayList<Mode> modes = new ArrayList<Mode>();
                for (final Mode mode : m.getModes()) {
                    modes.add(mode);
                }
                String t1 = null;
                if (this.press <= modes.size()) {
                    modes.get(this.press - 1).setToggled(true);
                    t1 = modes.get(this.press - 1).getName();
                }
                else {
                    this.press = 0;
                }
                for (final Mode mode2 : m.getModes()) {
                    if (mode2.getName() != t1) {
                        mode2.setToggled(false);
                    }
                }
            }
        }
    }
    
    private class mode
    {
    }
}
