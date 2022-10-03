package zelix.otherhacks.net.wurstclient.forge.clickgui;

import zelix.otherhacks.net.wurstclient.forge.settings.*;
import net.minecraft.block.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import org.lwjgl.input.*;
import java.io.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.client.*;
import net.minecraft.client.*;
import java.util.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import net.minecraft.init.*;
import com.mojang.realmsclient.gui.*;

public final class EditBlockListScreen extends GuiScreen
{
    private final GuiScreen prevScreen;
    private final BlockListSetting blockList;
    private ListGui listGui;
    private GuiTextField blockNameField;
    private GuiButton addButton;
    private GuiButton removeButton;
    private GuiButton doneButton;
    private Block blockToAdd;
    
    public EditBlockListScreen(final GuiScreen prevScreen, final BlockListSetting slider) {
        this.prevScreen = prevScreen;
        this.blockList = slider;
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public void initGui() {
        this.listGui = new ListGui(this.mc, this, this.blockList.getBlockNames());
        this.blockNameField = new GuiTextField(1, WMinecraft.getFontRenderer(), 64, this.height - 55, 150, 18);
        this.buttonList.add(this.addButton = new GuiButton(0, 214, this.height - 56, 30, 20, "Add"));
        this.buttonList.add(this.removeButton = new GuiButton(1, this.width - 150, this.height - 56, 100, 20, "Remove Selected"));
        this.buttonList.add(new GuiButton(2, this.width - 108, 8, 100, 20, "Reset to Defaults"));
        this.buttonList.add(this.doneButton = new GuiButton(3, this.width / 2 - 100, this.height - 28, "Done"));
    }
    
    protected void actionPerformed(final GuiButton button) {
        if (!button.enabled) {
            return;
        }
        switch (button.id) {
            case 0: {
                this.blockList.add(this.blockToAdd);
                this.blockNameField.setText("");
                break;
            }
            case 1: {
                this.blockList.remove(this.listGui.selected);
                break;
            }
            case 2: {
                this.mc.displayGuiScreen((GuiScreen)new GuiYesNo((GuiYesNoCallback)this, "Reset to Defaults", "Are you sure?", 0));
                break;
            }
            case 3: {
                this.mc.renderGlobal.loadRenderers();
                this.mc.displayGuiScreen(this.prevScreen);
                break;
            }
        }
    }
    
    public void confirmClicked(final boolean result, final int id) {
        if (id == 0 && result) {
            this.blockList.resetToDefaults();
        }
        super.confirmClicked(result, id);
        this.mc.displayGuiScreen((GuiScreen)this);
    }
    
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        final int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        final int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        this.listGui.handleMouseInput(mouseX, mouseY);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.blockNameField.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseX < 50 || mouseX > this.width - 50 || mouseY < 32 || mouseY > this.height - 64) {
            this.listGui.selected = -1;
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.blockNameField.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == 28) {
            this.actionPerformed(this.addButton);
        }
        else if (keyCode == 211) {
            this.actionPerformed(this.removeButton);
        }
        else if (keyCode == 1) {
            this.actionPerformed(this.doneButton);
        }
    }
    
    public void updateScreen() {
        this.blockNameField.updateCursorCounter();
        this.blockToAdd = Block.getBlockFromName(this.blockNameField.getText());
        this.addButton.enabled = (this.blockToAdd != null);
        this.removeButton.enabled = (this.listGui.selected >= 0 && this.listGui.selected < this.listGui.list.size());
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(WMinecraft.getFontRenderer(), this.blockList.getName() + " (" + this.listGui.getSize() + ")", this.width / 2, 12, 16777215);
        this.listGui.drawScreen(mouseX, mouseY, partialTicks);
        this.blockNameField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.blockNameField.getText().isEmpty() && !this.blockNameField.isFocused()) {
            this.drawString(WMinecraft.getFontRenderer(), "block name or ID", 68, this.height - 50, 8421504);
        }
        drawRect(48, this.height - 56, 64, this.height - 36, -6250336);
        drawRect(49, this.height - 55, 64, this.height - 37, -16777216);
        drawRect(214, this.height - 56, 244, this.height - 55, -6250336);
        drawRect(214, this.height - 37, 244, this.height - 36, -6250336);
        drawRect(244, this.height - 56, 246, this.height - 36, -6250336);
        drawRect(214, this.height - 55, 243, this.height - 52, -16777216);
        drawRect(214, this.height - 40, 243, this.height - 37, -16777216);
        drawRect(215, this.height - 55, 216, this.height - 37, -16777216);
        drawRect(242, this.height - 55, 245, this.height - 37, -16777216);
        this.listGui.renderIconAndGetName(new ItemStack(this.blockToAdd), this.height - 52);
    }
    
    private static class ListGui extends GuiScrollingList
    {
        private final Minecraft mc;
        private final List<String> list;
        private int selected;
        
        public ListGui(final Minecraft mc, final EditBlockListScreen screen, final List<String> list) {
            super(mc, screen.width - 100, screen.height, 32, screen.height - 64, 50, 16, screen.width, screen.height);
            this.selected = -1;
            this.mc = mc;
            this.list = list;
        }
        
        protected int getSize() {
            return this.list.size();
        }
        
        protected void elementClicked(final int index, final boolean doubleClick) {
            if (index >= 0 && index < this.list.size()) {
                this.selected = index;
            }
        }
        
        protected boolean isSelected(final int index) {
            return index == this.selected;
        }
        
        protected void drawBackground() {
            Gui.drawRect(50, this.top, 66, this.bottom, -1);
        }
        
        protected void drawSlot(final int slotIdx, final int entryRight, final int slotTop, final int slotBuffer, final Tessellator tess) {
            final String name = this.list.get(slotIdx);
            final ItemStack stack = new ItemStack(Block.getBlockFromName(name));
            final FontRenderer fr = WMinecraft.getFontRenderer();
            final String displayName = this.renderIconAndGetName(stack, slotTop);
            fr.drawString(displayName + " (" + name + ")", 68, slotTop + 2, 15790320);
        }
        
        private String renderIconAndGetName(final ItemStack stack, final int y) {
            if (stack == null || stack.isEmpty()) {
                GL11.glPushMatrix();
                GL11.glTranslated(52.0, (double)y, 0.0);
                GL11.glScaled(0.75, 0.75, 0.75);
                RenderHelper.enableGUIStandardItemLighting();
                this.mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack((Block)Blocks.GRASS), 0, 0);
                RenderHelper.disableStandardItemLighting();
                GL11.glPopMatrix();
                GL11.glDisable(2929);
                final FontRenderer fr = WMinecraft.getFontRenderer();
                fr.drawString("?", 55.0f, (float)(y + 2), 15790320, true);
                GL11.glEnable(2929);
                return ChatFormatting.ITALIC + "unknown block" + ChatFormatting.RESET;
            }
            GL11.glPushMatrix();
            GL11.glTranslated(52.0, (double)y, 0.0);
            GL11.glScaled(0.75, 0.75, 0.75);
            RenderHelper.enableGUIStandardItemLighting();
            this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
            RenderHelper.disableStandardItemLighting();
            GL11.glPopMatrix();
            return stack.getDisplayName();
        }
    }
}
