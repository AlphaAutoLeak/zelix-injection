package zelix.gui.clickguis.caesium;

import zelix.gui.clickguis.caesium.components.*;
import zelix.utils.*;
import java.util.*;
import java.io.*;
import net.minecraft.client.gui.*;

public class ClickGui extends GuiScreen
{
    public static int compID;
    private ArrayList<Frame> frames;
    private final FontRenderer fr;
    
    public ClickGui() {
        this.frames = new ArrayList<Frame>();
        this.fr = Wrapper.INSTANCE.fontRenderer();
        ClickGui.compID = 0;
    }
    
    protected void addFrame(final Frame frame) {
        if (!this.frames.contains(frame)) {
            this.frames.add(frame);
        }
    }
    
    protected ArrayList<Frame> getFrames() {
        return this.frames;
    }
    
    public void initGui() {
        for (final Frame frame : this.frames) {
            frame.initialize();
        }
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (final Frame frame : this.frames) {
            frame.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for (final Frame frame : this.frames) {
            frame.keyTyped(keyCode, typedChar);
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sR = new ScaledResolution(this.mc);
        this.fr.drawString("Thanks For Using!", 2, sR.getScaledHeight() - this.fr.FONT_HEIGHT, Panel.fontColor);
        for (final Frame frame : this.frames) {
            frame.render(mouseX, mouseY);
        }
    }
    
    static {
        ClickGui.compID = 0;
    }
}
