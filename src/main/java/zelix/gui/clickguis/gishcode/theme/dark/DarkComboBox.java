package zelix.gui.clickguis.gishcode.theme.dark;

import zelix.gui.clickguis.gishcode.base.Component;
import zelix.gui.clickguis.gishcode.theme.*;
import zelix.gui.clickguis.gishcode.base.*;
import zelix.gui.clickguis.gishcode.elements.*;
import org.lwjgl.opengl.*;
import zelix.utils.hooks.visual.*;
import java.awt.*;
import org.lwjgl.input.*;

public class DarkComboBox extends ComponentRenderer
{
    public DarkComboBox(final Theme theme) {
        super(ComponentType.COMBO_BOX, theme);
    }
    
    @Override
    public void drawComponent(final Component component, final int mouseX, final int mouseY) {
        final ComboBox comboBox = (ComboBox)component;
        final Dimension area = comboBox.getDimension();
        GL11.glEnable(3042);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glTranslated((double)(1 * comboBox.getX()), (double)(1 * comboBox.getY()), 0.0);
        int maxWidth = 0;
        for (final String element : comboBox.getElements()) {
            maxWidth = Math.max(maxWidth, this.theme.getFontRenderer().getStringWidth(element));
        }
        int extendedHeight = 0;
        if (comboBox.isSelected()) {
            final String[] elements = comboBox.getElements();
            for (int i = 0; i < elements.length - 1; ++i) {
                extendedHeight += this.theme.getFontRenderer().FONT_HEIGHT + 2;
            }
            extendedHeight += 2;
        }
        comboBox.setDimension(new Dimension(maxWidth + 8 + this.theme.getFontRenderer().FONT_HEIGHT, this.theme.getFontRenderer().FONT_HEIGHT));
        GLUtils.glColor(new Color(2, 2, 2, 40));
        GL11.glBegin(7);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glVertex2d((double)area.width, 0.0);
        GL11.glVertex2d((double)area.width, (double)(area.height + extendedHeight));
        GL11.glVertex2d(0.0, (double)(area.height + extendedHeight));
        GL11.glEnd();
        final Point mouse = new Point(mouseX, mouseY);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, Mouse.isButtonDown(0) ? 0.5f : 0.3f);
        if (GLUtils.isHovered(comboBox.getX(), comboBox.getY(), area.width, area.height, mouseX, mouseY)) {
            GL11.glBegin(7);
            GL11.glVertex2d(0.0, 0.0);
            GL11.glVertex2d((double)area.width, 0.0);
            GL11.glVertex2d((double)area.width, (double)area.height);
            GL11.glVertex2d(0.0, (double)area.height);
            GL11.glEnd();
        }
        else if (comboBox.isSelected() && mouse.x >= comboBox.getX() && mouse.x <= comboBox.getX() + area.width) {
            int offset = area.height;
            final String[] elements2 = comboBox.getElements();
            for (int j = 0; j < elements2.length; ++j) {
                if (j != comboBox.getSelectedIndex()) {
                    int height = this.theme.getFontRenderer().FONT_HEIGHT + 2;
                    Label_0535: {
                        Label_0532: {
                            if (comboBox.getSelectedIndex() == 0) {
                                if (j == 1) {
                                    break Label_0532;
                                }
                            }
                            else if (j == 0) {
                                break Label_0532;
                            }
                            if (comboBox.getSelectedIndex() == elements2.length - 1) {
                                if (j != elements2.length - 2) {
                                    break Label_0535;
                                }
                            }
                            else if (j != elements2.length - 1) {
                                break Label_0535;
                            }
                        }
                        ++height;
                    }
                    if (mouse.y >= comboBox.getY() + offset && mouse.y <= comboBox.getY() + offset + height) {
                        GL11.glBegin(7);
                        GL11.glVertex2d(0.0, (double)offset);
                        GL11.glVertex2d(0.0, (double)(offset + height));
                        GL11.glVertex2d((double)area.width, (double)(offset + height));
                        GL11.glVertex2d((double)area.width, (double)offset);
                        GL11.glEnd();
                        break;
                    }
                    offset += height;
                }
            }
        }
        final int height2 = this.theme.getFontRenderer().FONT_HEIGHT + 4;
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.3f);
        GL11.glBegin(4);
        if (comboBox.isSelected()) {
            GL11.glVertex2d(maxWidth + 4 + height2 / 2.0, height2 / 3.0);
            GL11.glVertex2d(maxWidth + 4 + height2 / 3.0, 2.0 * height2 / 3.0);
            GL11.glVertex2d(maxWidth + 4 + 2.0 * height2 / 3.0, 2.0 * height2 / 3.0);
        }
        else {
            GL11.glVertex2d(maxWidth + 4 + height2 / 3.0, height2 / 3.0);
            GL11.glVertex2d(maxWidth + 4 + 2.0 * height2 / 3.0, height2 / 3.0);
            GL11.glVertex2d(maxWidth + 4 + height2 / 2.0, 2.0 * height2 / 3.0);
        }
        GL11.glEnd();
        GL11.glLineWidth(1.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        if (comboBox.isSelected()) {
            GL11.glBegin(1);
            GL11.glVertex2d(2.0, (double)area.height);
            GL11.glVertex2d((double)(area.width - 2), (double)area.height);
            GL11.glEnd();
        }
        GL11.glBegin(1);
        GL11.glVertex2d((double)(maxWidth + 4), 2.0);
        GL11.glVertex2d((double)(maxWidth + 4), (double)(area.height - 2));
        GL11.glEnd();
        GL11.glBegin(2);
        if (comboBox.isSelected()) {
            GL11.glVertex2d(maxWidth + 4 + height2 / 2.0, height2 / 3.0);
            GL11.glVertex2d(maxWidth + 4 + height2 / 3.0, 2.0 * height2 / 3.0);
            GL11.glVertex2d(maxWidth + 4 + 2.0 * height2 / 3.0, 2.0 * height2 / 3.0);
        }
        else {
            GL11.glVertex2d(maxWidth + 4 + height2 / 3.0, height2 / 3.0);
            GL11.glVertex2d(maxWidth + 4 + 2.0 * height2 / 3.0, height2 / 3.0);
            GL11.glVertex2d(maxWidth + 4 + height2 / 2.0, 2.0 * height2 / 3.0);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        final String text = comboBox.getSelectedElement();
        this.theme.getFontRenderer().drawStringWithShadow(text, 2, area.height / 2 - this.theme.getFontRenderer().FONT_HEIGHT / 4, -1);
        if (comboBox.isSelected()) {
            int offset2 = area.height + 2;
            final String[] elements3 = comboBox.getElements();
            for (int k = 0; k < elements3.length; ++k) {
                if (k != comboBox.getSelectedIndex()) {
                    this.theme.getFontRenderer().drawStringWithShadow(elements3[k], 2, offset2, -1);
                    offset2 += this.theme.getFontRenderer().FONT_HEIGHT + 2;
                }
            }
        }
        GL11.glEnable(2884);
        GL11.glDisable(3042);
        GL11.glTranslated((double)(-1 * comboBox.getX()), (double)(-1 * comboBox.getY()), 0.0);
    }
    
    @Override
    public void doInteractions(final Component component, final int mouseX, final int mouseY) {
        final ComboBox comboBox = (ComboBox)component;
    }
}
