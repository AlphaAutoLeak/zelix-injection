package zelix.hack.hacks.xray.gui.manage;

import net.minecraftforge.fml.client.*;
import zelix.hack.hacks.xray.reference.block.*;
import net.minecraft.util.*;
import java.util.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.*;

public class GuiBlocksList extends GuiScrollingList
{
    private static final int HEIGHT = 35;
    private final GuiBlockListScrollable parent;
    private List<BlockItem> blockList;
    
    GuiBlocksList(final GuiBlockListScrollable parent, final List<BlockItem> blockList) {
        super(parent.getMinecraftInstance(), 202, 210, parent.height / 2 - 105, parent.height / 2 + 80, parent.width / 2 - 100, 35, parent.width, parent.height);
        this.parent = parent;
        this.blockList = blockList;
    }
    
    protected int getSize() {
        return this.blockList.size();
    }
    
    protected void elementClicked(final int index, final boolean doubleClick) {
        this.parent.selectBlock(index);
    }
    
    protected boolean isSelected(final int index) {
        return this.parent.blockSelected(index);
    }
    
    protected void drawBackground() {
    }
    
    protected int getContentHeight() {
        return this.getSize() * 35;
    }
    
    protected void drawSlot(final int idx, final int right, final int top, final int height, final Tessellator tess) {
        final BlockItem block = this.blockList.get(idx);
        final FontRenderer font = this.parent.getFontRender();
        font.drawString(block.getItemStack().getDisplayName(), this.left + 30, top + 7, 16777215);
        font.drawString(Objects.requireNonNull(block.getItemStack().getItem().getRegistryName()).getResourceDomain(), this.left + 30, top + 17, 13750223);
        RenderHelper.enableGUIStandardItemLighting();
        this.parent.getRender().renderItemAndEffectIntoGUI(block.getItemStack(), this.left + 5, top + 7);
        RenderHelper.disableStandardItemLighting();
    }
    
    void updateBlockList(final List<BlockItem> blocks) {
        this.blockList = blocks;
    }
}
