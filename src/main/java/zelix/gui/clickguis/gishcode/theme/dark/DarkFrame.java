package zelix.gui.clickguis.gishcode.theme.dark;

import zelix.gui.clickguis.gishcode.base.Component;
import zelix.gui.clickguis.gishcode.elements.Frame;
import zelix.gui.clickguis.gishcode.theme.*;
import zelix.gui.clickguis.gishcode.base.*;
import zelix.gui.clickguis.gishcode.elements.*;
import zelix.hack.hacks.*;
import zelix.utils.*;
import zelix.utils.hooks.visual.*;
import java.awt.*;
import java.util.*;

public class DarkFrame extends ComponentRenderer
{
    public DarkFrame(final Theme theme) {
        super(ComponentType.FRAME, theme);
    }
    
    @Override
    public void drawComponent(final Component component, final int mouseX, final int mouseY) {
        final Frame frame = (Frame)component;
        final Dimension dimension = frame.getDimension();
        if (frame.isMaximized()) {
            this.isMaximized(frame, dimension, mouseX, mouseY);
        }
        RenderUtils.drawRect(frame.getX(), frame.getY(), frame.getX() + dimension.width, frame.getY() + 15, ClickGui.getColor());
        if (frame.isMaximizible()) {
            this.isMaximizible(frame, dimension, mouseX, mouseY);
        }
        this.theme.fontRenderer.drawStringWithShadow(frame.getText(), (float)(frame.getX() + 4), (float)(MathUtils.getMiddle(frame.getY(), frame.getY() + 10) - this.theme.fontRenderer.FONT_HEIGHT / 10 - 1), ColorUtils.color(1.0f, 1.0f, 1.0f, 1.0f));
    }
    
    private void isPinnable(final Frame frame, final Dimension dimension, final int mouseX, final int mouseY) {
    }
    
    private void isMaximizible(final Frame frame, final Dimension dimension, final int mouseX, final int mouseY) {
        Color color;
        if (mouseX >= frame.getX() + dimension.width - 19 && mouseY >= frame.getY() && mouseY <= frame.getY() + 19 && mouseX <= frame.getX() + dimension.width) {
            color = new Color(255, 255, 255, 255);
        }
        else {
            color = new Color(155, 155, 155, 255);
        }
        this.theme.fontRenderer.drawStringWithShadow(frame.isMaximized() ? "-" : "+", (float)(frame.getX() + dimension.width - 12), (float)(frame.getY() + 3), color.getRGB());
    }
    
    private void isMaximized(final Frame frame, final Dimension dimension, final int mouseX, final int mouseY) {
        final int mainColor = ClickGui.isLight ? ColorUtils.color(255, 255, 255, 255) : ColorUtils.color(0, 0, 0, 255);
        final int mainColorInv = ClickGui.isLight ? ColorUtils.color(0, 0, 0, 255) : ColorUtils.color(255, 255, 255, 255);
        for (final Component component : frame.getComponents()) {
            component.setxPos(frame.getX());
        }
        float height = 5.0f;
        float maxHeight = 0.0f;
        height = dimension.height - 40;
        for (final Component component2 : frame.getComponents()) {
            maxHeight += component2.getDimension().height;
        }
        final float barHeight = height * (height / maxHeight);
        double y = (frame.getDimension().getHeight() - 16.0 - barHeight) * (frame.getScrollAmmount() / frame.getMaxScroll());
        y += frame.getY() + 16;
        frame.renderChildren(mouseX, mouseY);
        if (barHeight < height) {
            RenderUtils.drawRect((int)(frame.getX() + dimension.getWidth() - 1.0), (int)y, (int)(frame.getX() + frame.getDimension().getWidth()), (int)(y + barHeight), ClickGui.getColor());
        }
    }
    
    @Override
    public void doInteractions(final Component component, final int mouseX, final int mouseY) {
        final Frame frame = (Frame)component;
        final Dimension area = frame.getDimension();
        if (mouseX >= frame.getX() + area.width - 16 && frame.isMaximizible() && mouseY >= frame.getY() && mouseY <= frame.getY() + 16 && mouseX <= frame.getX() + area.width) {
            frame.setMaximized(!frame.isMaximized());
        }
        if (mouseX >= frame.getX() + area.width - 38 && mouseY >= frame.getY() && mouseY <= frame.getY() + 16 && mouseX <= frame.getX() + area.width - 16) {
            frame.setPinned(!frame.isPinned());
        }
    }
}
