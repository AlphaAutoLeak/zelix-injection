package zelix.gui.clickguis.Astolfo.ClickGui;

import zelix.hack.*;
import zelix.value.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;
import java.awt.*;
import zelix.utils.hooks.visual.font.*;

public class KeyBindButton extends ValueButton
{
    public Hack cheat;
    public double opacity;
    public boolean bind;
    public HackCategory category;
    
    public KeyBindButton(final HackCategory category, final Hack cheat, final int x, final int y) {
        super(category, null, x, y);
        this.opacity = 0.0;
        this.category = category;
        this.custom = true;
        this.bind = false;
        this.cheat = cheat;
    }
    
    @Override
    public void render(final int mouseX, final int mouseY, final Limitation limitation) {
        GL11.glEnable(3089);
        limitation.cut();
        Gui.drawRect(0, 0, 0, 0, 0);
        Gui.drawRect(this.x - 10, this.y - 4, this.x + 80, this.y + 11, new Color(39, 39, 39).getRGB());
        FontLoaders.kiona14.drawString("Bind", this.x - 7, this.y + 2, new Color(108, 108, 108).getRGB());
        GL11.glDisable(3089);
    }
    
    @Override
    public void key(final char typedChar, final int keyCode) {
        if (this.bind) {
            this.cheat.setKey(keyCode);
            if (keyCode == 1) {
                this.cheat.setKey(0);
            }
            ClickUi.binding = false;
            this.bind = false;
        }
        super.key(typedChar, keyCode);
    }
    
    @Override
    public void click(final int mouseX, final int mouseY, final int button) {
        if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + FontLoaders.kiona18.getStringHeight(this.cheat.getName()) + 5 && button == 0) {
            this.bind = !this.bind;
            ClickUi.binding = this.bind;
        }
        super.click(mouseX, mouseY, button);
    }
}
