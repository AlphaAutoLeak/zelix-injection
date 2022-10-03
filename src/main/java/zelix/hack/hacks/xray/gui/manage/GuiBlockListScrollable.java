package zelix.hack.hacks.xray.gui.manage;

import zelix.hack.hacks.xray.gui.utils.*;
import net.minecraft.client.renderer.*;
import zelix.hack.hacks.xray.reference.block.*;
import zelix.hack.hacks.xray.*;
import net.minecraft.block.state.*;
import net.minecraft.client.resources.*;
import net.minecraft.client.gui.*;
import zelix.hack.hacks.xray.gui.*;
import java.io.*;
import java.util.*;
import org.lwjgl.input.*;
import net.minecraft.client.*;

public class GuiBlockListScrollable extends GuiBase
{
    private RenderItem render;
    private GuiBlocksList blockList;
    private ArrayList<BlockItem> blocks;
    private GuiTextField search;
    private String lastSearched;
    private int selected;
    private static final int BUTTON_CANCEL = 0;
    
    public GuiBlockListScrollable() {
        super(false);
        this.lastSearched = "";
        this.selected = -1;
        this.blocks = XRay.gameBlockStore.getStore();
    }
    
    boolean blockSelected(final int index) {
        return index == this.selected;
    }
    
    void selectBlock(final int index) {
        if (index == this.selected) {
            return;
        }
        this.selected = index;
        this.mc.player.closeScreen();
        this.mc.displayGuiScreen((GuiScreen)new GuiAddBlock(this.blocks.get(this.selected), null));
    }
    
    public void initGui() {
        this.render = this.itemRender;
        this.blockList = new GuiBlocksList(this, this.blocks);
        (this.search = new GuiTextField(150, this.getFontRender(), this.width / 2 - 100, this.height / 2 + 85, 140, 18)).setFocused(true);
        this.search.setCanLoseFocus(true);
        this.buttonList.add(new GuiButton(0, this.width / 2 + 43, this.height / 2 + 84, 60, 20, I18n.format("xray.single.cancel", new Object[0])));
    }
    
    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                this.mc.player.closeScreen();
                this.mc.displayGuiScreen((GuiScreen)new GuiSelectionScreen());
                break;
            }
        }
    }
    
    @Override
    protected void keyTyped(final char charTyped, final int hex) throws IOException {
        super.keyTyped(charTyped, hex);
        this.search.textboxKeyTyped(charTyped, hex);
    }
    
    public void updateScreen() {
        this.search.updateCursorCounter();
        if (!this.search.getText().equals(this.lastSearched)) {
            this.reloadBlocks();
        }
    }
    
    private void reloadBlocks() {
        this.blocks = new ArrayList<BlockItem>();
        final ArrayList<BlockItem> tmpBlocks = new ArrayList<BlockItem>();
        for (final BlockItem block : XRay.gameBlockStore.getStore()) {
            if (block.getItemStack().getDisplayName().toLowerCase().contains(this.search.getText().toLowerCase())) {
                tmpBlocks.add(block);
            }
        }
        this.blocks = tmpBlocks;
        this.blockList.updateBlockList(this.blocks);
        this.lastSearched = this.search.getText();
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float f) {
        super.drawScreen(x, y, f);
        this.search.drawTextBox();
        this.blockList.drawScreen(x, y, f);
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int button) throws IOException {
        super.mouseClicked(x, y, button);
        this.search.mouseClicked(x, y, button);
    }
    
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        final int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        final int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        this.blockList.handleMouseInput(mouseX, mouseY);
    }
    
    Minecraft getMinecraftInstance() {
        return this.mc;
    }
    
    RenderItem getRender() {
        return this.render;
    }
}
