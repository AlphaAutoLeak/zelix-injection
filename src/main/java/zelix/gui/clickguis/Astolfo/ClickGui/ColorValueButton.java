package zelix.gui.clickguis.Astolfo.ClickGui;

import zelix.hack.*;
import zelix.value.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;
import org.lwjgl.input.*;

public class ColorValueButton extends ValueButton
{
    public HackCategory category;
    private float[] hue;
    private int position;
    private int color;
    
    public ColorValueButton(final HackCategory category, final int x, final int y) {
        super(category, null, x, y);
        this.hue = new float[] { 0.0f };
        this.color = new Color(125, 125, 125).getRGB();
        this.category = category;
        this.custom = true;
        this.position = -1111;
    }
    
    @Override
    public void render(final int mouseX, final int mouseY, final Limitation limitation) {
        final float[] huee = { this.hue[0] };
        GL11.glEnable(3089);
        limitation.cut();
        Gui.drawRect(this.x - 10, this.y - 4, this.x + 80, this.y + 11, new Color(0, 0, 0, 100).getRGB());
        for (int i = this.x - 7; i < this.x + 79; ++i) {
            final Color color = Color.getHSBColor(huee[0] / 255.0f, 0.7f, 1.0f);
            if (mouseX > i - 1 && mouseX < i + 1 && mouseY > this.y - 6 && mouseY < this.y + 12 && Mouse.isButtonDown(0)) {
                this.color = color.getRGB();
                this.position = i;
            }
            if (this.color == color.getRGB()) {
                this.position = i;
            }
            Gui.drawRect(i - 1, this.y, i, this.y + 8, color.getRGB());
            final float[] arrf = huee;
            arrf[0] += 4.0f;
            if (huee[0] > 255.0f) {
                huee[0] -= 255.0f;
            }
        }
        Gui.drawRect(this.position, this.y, this.position + 1, this.y + 8, -1);
        if (this.hue[0] > 255.0f) {
            this.hue[0] -= 255.0f;
        }
        GL11.glDisable(3089);
    }
    
    @Override
    public void key(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void click(final int mouseX, final int mouseY, final int button) {
    }
}
