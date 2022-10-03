package zelix.gui.clickguis.caesium;

import zelix.gui.clickguis.caesium.components.Frame;
import zelix.gui.clickguis.caesium.components.GuiButton;
import zelix.gui.clickguis.caesium.util.*;
import net.minecraft.client.gui.*;
import zelix.utils.resourceloader.*;
import zelix.*;
import zelix.managers.*;
import zelix.hack.*;
import java.awt.event.*;
import zelix.gui.clickguis.caesium.listeners.*;
import zelix.gui.clickguis.caesium.components.listeners.*;
import zelix.gui.clickguis.caesium.components.*;
import java.util.*;
import zelix.utils.*;
import java.awt.*;

public class Panel extends ClickGui
{
    public static HashMap<String, FramePosition> framePositions;
    public static FontRenderer fR;
    public static String theme;
    public static int FRAME_WIDTH;
    public static int color;
    public static int fontColor;
    public static int grey40_240;
    public static int black195;
    public static int black100;
    
    public Panel(final String theme, final int fontSize) {
        Panel.theme = theme;
    }
    
    @Override
    public void initGui() {
        int x = 25;
        int n = 0;
        for (final HackCategory cat : HackCategory.values()) {
            GuiFrame frame;
            if (Panel.framePositions.containsKey(cat.name())) {
                final FramePosition curPos = Panel.framePositions.get(cat.name());
                frame = new GuiFrame(cat.name(), curPos.getPosX(), curPos.getPosY(), curPos.isExpanded(), Strings.Chinese_HackCategory.split("=")[n]);
            }
            else {
                frame = new GuiFrame(cat.name(), x, 50, true, Strings.Chinese_HackCategory.split("=")[n]);
            }
            final HackManager hackManager = Core.hackManager;
            for (final Hack m : HackManager.getHacks()) {
                if (cat == m.getCategory()) {
                    final GuiButton button = new GuiButton(m);
                    button.addClickListener(new ClickListener(button));
                    button.addExtendListener(new ComponentsListener(button));
                    frame.addButton(button);
                }
            }
            this.addFrame(frame);
            x += 140;
            ++n;
        }
        super.initGui();
    }
    
    public void onGuiClosed() {
        if (!this.getFrames().isEmpty()) {
            for (final Frame frame : this.getFrames()) {
                final GuiFrame guiFrame = (GuiFrame)frame;
                Panel.framePositions.put(guiFrame.getTitle(), new FramePosition(guiFrame.getPosX(), guiFrame.getPosY(), guiFrame.isExpaned()));
            }
        }
        final HackManager hackManager = Core.hackManager;
        HackManager.getHack("ClickGui").onDisable();
    }
    
    static {
        Panel.framePositions = new HashMap<String, FramePosition>();
        Panel.fR = Wrapper.INSTANCE.fontRenderer();
        Panel.FRAME_WIDTH = 100;
        Panel.color = new Color(193, 105, 170, 220).getRGB();
        Panel.fontColor = Color.white.getRGB();
        Panel.grey40_240 = new Color(40, 40, 40, 140).getRGB();
        Panel.black195 = new Color(0, 0, 0, 195).getRGB();
        Panel.black100 = new Color(0, 0, 0, 100).getRGB();
    }
}
