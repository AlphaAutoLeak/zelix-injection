package zelix.gui.clickguis.caesium.components;

import zelix.gui.clickguis.caesium.*;
import org.lwjgl.input.*;
import zelix.gui.clickguis.caesium.Panel;
import zelix.gui.clickguis.caesium.util.*;
import java.awt.*;
import java.util.*;

public class GuiFrame implements Frame
{
    private ArrayList<GuiButton> buttons;
    private boolean isExpaned;
    private boolean isDragging;
    private int id;
    private int posX;
    private int posY;
    private int prevPosX;
    private int prevPosY;
    private int scrollHeight;
    public static int dragID;
    private String title;
    private String CN;
    
    public GuiFrame(final String title, final int posX, final int posY, final boolean expanded, final String CN) {
        this.buttons = new ArrayList<GuiButton>();
        this.title = title;
        this.CN = CN;
        this.posX = posX;
        this.posY = posY;
        this.isExpaned = expanded;
        this.id = ++ClickGui.compID;
        this.scrollHeight = 0;
    }
    
    @Override
    public void render(final int mouseX, final int mouseY) {
        final String theme = Panel.theme;
        switch (theme) {
            case "Caesium": {
                this.renderCaesium(mouseX, mouseY);
                break;
            }
        }
    }
    
    private void renderCaesium(final int mouseX, final int mouseY) {
        final int color = Panel.color;
        final int fontColor = Panel.fontColor;
        int width = Math.max(Panel.FRAME_WIDTH, Panel.fR.getStringWidth(zelix.hack.hacks.ClickGui.language.getMode("Chinese").isToggled() ? this.CN : this.title) + 15);
        if (this.isDragging && Mouse.isButtonDown(0)) {
            this.posX = mouseX - this.prevPosX;
            this.posY = mouseY - this.prevPosY;
            GuiFrame.dragID = this.id;
        }
        else {
            this.isDragging = false;
            GuiFrame.dragID = -1;
        }
        for (final GuiButton button : this.buttons) {
            width = Math.max(width, button.getWidth() + 15);
        }
        RenderUtil.drawRect(this.posX + 1, this.posY - 5, this.posX + width, this.posY + 12, color);
        RenderUtil.drawVerticalGradient(this.posX + 1, this.posY - 5, width - 1, 17.0f, new Color(color).brighter().getRGB(), new Color(color).darker().getRGB());
        Panel.fR.drawStringWithShadow(zelix.hack.hacks.ClickGui.language.getMode("Chinese").isToggled() ? this.CN : this.title, (int) (this.posX + width / 2 - Panel.fR.getStringWidth(zelix.hack.hacks.ClickGui.language.getMode("Chinese").isToggled() ? this.CN : this.title) / 2), (float)this.posY, fontColor);
        Panel.fR.drawStringWithShadow(this.isExpaned ? "-" : "+", (int) (this.posX + width - Panel.fR.getStringWidth(this.isExpaned ? "-" : "+") - 4), (float)this.posY, fontColor);
        if (this.isExpaned) {
            int height = 0;
            final int background = Panel.grey40_240;
            for (final GuiButton button2 : this.buttons) {
                button2.render(this.posX + 1, this.posY + height + 12, width, mouseX, mouseY);
                if (button2.getButtonID() == GuiButton.expandedID) {
                    final ArrayList<GuiComponent> components = button2.getComponents();
                    if (!components.isEmpty()) {
                        int xOffset = 10;
                        int yOffset = 0;
                        for (final GuiComponent component : components) {
                            xOffset = Math.max(xOffset, component.getWidth());
                            yOffset += component.getHeight();
                        }
                        final int left = this.posX + width + 2;
                        final int right = left + xOffset;
                        final int top = this.posY + height + 12;
                        final int bottom = top + yOffset + 1;
                        int wheelY = Mouse.getDWheel() * -1 / 8;
                        if (bottom + this.scrollHeight < 30) {
                            wheelY *= -1;
                            this.scrollHeight += 10;
                        }
                        this.scrollHeight += wheelY;
                        RenderUtil.drawRect(left + 1, top + 1 + this.scrollHeight, right, bottom + this.scrollHeight, Panel.black100);
                        int height2 = 0;
                        for (final GuiComponent component2 : components) {
                            component2.render(left, top + height2 + 2 + this.scrollHeight, xOffset, mouseX, mouseY);
                            height2 += component2.getHeight();
                        }
                        RenderUtil.drawVerticalLine(left, top + this.scrollHeight, bottom + this.scrollHeight, color);
                        RenderUtil.drawVerticalLine(right, top + this.scrollHeight, bottom + this.scrollHeight, color);
                        RenderUtil.drawHorizontalLine(left, right, top + this.scrollHeight, color);
                        RenderUtil.drawHorizontalLine(left, right, bottom + this.scrollHeight, color);
                    }
                }
                height += button2.getHeight();
            }
            RenderUtil.drawHorizontalLine(this.posX + 1, this.posX + width - 1, this.posY + height + 12, color);
            RenderUtil.drawVerticalLine(this.posX + width, this.posY - 5, this.posY + height + 14, Panel.black100);
            RenderUtil.drawVerticalLine(this.posX + width, this.posY - 4, this.posY + height + 14, Panel.black100);
            RenderUtil.drawVerticalLine(this.posX + width + 1, this.posY - 4, this.posY + height + 15, Panel.black100);
            RenderUtil.drawHorizontalLine(this.posX + 2, this.posX + width - 1, this.posY + height + 13, Panel.black100);
            RenderUtil.drawHorizontalLine(this.posX + 2, this.posX + width - 1, this.posY + height + 13, Panel.black100);
            RenderUtil.drawHorizontalLine(this.posX + 3, this.posX + width, this.posY + height + 14, Panel.black100);
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        int width = Panel.FRAME_WIDTH;
        if (this.isExpaned) {
            for (final GuiButton button : this.buttons) {
                width = Math.max(width, button.getWidth());
                button.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        if (RenderUtil.isHovered(this.posX, this.posY, width, 13, mouseX, mouseY)) {
            if (mouseButton == 0) {
                this.prevPosX = mouseX - this.posX;
                this.prevPosY = mouseY - this.posY;
                this.isDragging = true;
                GuiFrame.dragID = this.id;
            }
            else if (mouseButton == 1) {
                this.isExpaned = !this.isExpaned;
                this.scrollHeight = 0;
                this.isDragging = false;
                GuiFrame.dragID = -1;
            }
        }
    }
    
    @Override
    public void keyTyped(final int keyCode, final char typedChar) {
        if (this.isExpaned) {
            for (final GuiButton button : this.buttons) {
                button.keyTyped(keyCode, typedChar);
            }
        }
    }
    
    @Override
    public void initialize() {
    }
    
    public void addButton(final GuiButton button) {
        if (!this.buttons.contains(button)) {
            this.buttons.add(button);
        }
    }
    
    public int getButtonID() {
        return this.id;
    }
    
    public boolean isExpaned() {
        return this.isExpaned;
    }
    
    public int getPosX() {
        return this.posX;
    }
    
    public int getPosY() {
        return this.posY;
    }
    
    public String getTitle() {
        return this.title;
    }
}
