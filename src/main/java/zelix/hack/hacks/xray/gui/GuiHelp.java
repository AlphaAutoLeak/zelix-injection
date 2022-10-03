package zelix.hack.hacks.xray.gui;

import zelix.hack.hacks.xray.gui.utils.*;
import net.minecraft.client.resources.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import zelix.hack.hacks.xray.*;
import net.minecraft.client.gui.*;

public class GuiHelp extends GuiBase
{
    private List<LinedText> areas;
    
    public GuiHelp() {
        super(false);
        this.areas = new ArrayList<LinedText>();
        this.setSize(380, 210);
    }
    
    public void initGui() {
        super.initGui();
        this.areas.clear();
        this.areas.add(new LinedText("xray.message.help.state"));
        this.areas.add(new LinedText("xray.message.help.gui"));
        this.areas.add(new LinedText("xray.message.help.warning"));
        this.addButton(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 80, I18n.format("xray.single.close", new Object[0])));
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float f) {
        super.drawScreen(x, y, f);
        float lineY = this.height / 2.0f - 85.0f;
        for (final LinedText linedText : this.areas) {
            for (final String line : linedText.getLines()) {
                lineY += 12.0f;
                this.getFontRender().drawStringWithShadow(line, this.width / 2.0f - 176.0f, lineY, Color.WHITE.getRGB());
            }
            lineY += 10.0f;
        }
    }
    
    protected void actionPerformed(final GuiButton button) {
        if (button.id == 1) {
            XRay.mc.player.closeScreen();
            XRay.mc.displayGuiScreen((GuiScreen)new GuiSelectionScreen());
        }
    }
    
    @Override
    public boolean hasTitle() {
        return true;
    }
    
    @Override
    public Color colorBackground() {
        return Color.LIGHT_GRAY;
    }
    
    @Override
    public String title() {
        return I18n.format("xray.single.help", new Object[0]);
    }
    
    private static class LinedText
    {
        private String[] lines;
        
        public LinedText(final String key) {
            this.lines = I18n.format(key, new Object[0]).split("\n");
        }
        
        public String[] getLines() {
            return this.lines;
        }
    }
}
