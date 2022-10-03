package zelix.hack.hacks.xray.gui.manage;

import zelix.hack.hacks.xray.gui.utils.*;
import net.minecraft.block.state.*;
import javax.annotation.*;
import net.minecraft.client.resources.*;
import net.minecraft.block.*;
import zelix.hack.hacks.xray.gui.utils.GuiSlider;
import zelix.hack.hacks.xray.xray.*;
import zelix.hack.hacks.xray.utils.*;
import zelix.hack.hacks.xray.reference.block.*;
import zelix.hack.hacks.xray.*;
import zelix.hack.hacks.xray.gui.*;
import net.minecraft.client.gui.*;
import java.io.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import java.util.*;

public class GuiAddBlock extends GuiBase
{
    private static final int BUTTON_ADD = 98;
    private static final int BUTTON_CANCEL = 99;
    private GuiTextField oreName;
    private GuiButton addBtn;
    private GuiSlider redSlider;
    private GuiSlider greenSlider;
    private GuiSlider blueSlider;
    private BlockItem selectBlock;
    private boolean oreNameCleared;
    private IBlockState state;
    
    public GuiAddBlock(final BlockItem selectedBlock, @Nullable final IBlockState state) {
        super(false);
        this.oreNameCleared = false;
        this.selectBlock = selectedBlock;
        this.state = state;
    }
    
    public void initGui() {
        this.buttonList.add(this.addBtn = new GuiButton(98, this.width / 2 - 100, this.height / 2 + 85, 128, 20, I18n.format("xray.single.add", new Object[0])));
        this.buttonList.add(new GuiButton(99, this.width / 2 + 30, this.height / 2 + 85, 72, 20, I18n.format("xray.single.cancel", new Object[0])));
        this.buttonList.add(this.redSlider = new GuiSlider(3, this.width / 2 - 100, this.height / 2 + 7, I18n.format("xray.color.red", new Object[0]), 0.0f, 255.0f));
        this.buttonList.add(this.greenSlider = new GuiSlider(2, this.width / 2 - 100, this.height / 2 + 30, I18n.format("xray.color.green", new Object[0]), 0.0f, 255.0f));
        this.buttonList.add(this.blueSlider = new GuiSlider(1, this.width / 2 - 100, this.height / 2 + 53, I18n.format("xray.color.blue", new Object[0]), 0.0f, 255.0f));
        this.redSlider.sliderValue = 0.0f;
        this.greenSlider.sliderValue = 0.654f;
        this.blueSlider.sliderValue = 1.0f;
        (this.oreName = new GuiTextField(1, this.fontRenderer, this.width / 2 - 100, this.height / 2 - 70, 202, 20)).setText(this.selectBlock.getItemStack().getDisplayName());
    }
    
    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 98: {
                this.mc.player.closeScreen();
                if (this.state == null) {
                    this.state = Block.getBlockFromItem(this.selectBlock.getItemStack().getItem()).getDefaultState();
                }
                Controller.getBlockStore().put(this.state.toString(), new BlockData(this.state.toString(), this.oreName.getText(), Block.getStateId(this.state), new OutlineColor((int)(this.redSlider.sliderValue * 255.0f), (int)(this.greenSlider.sliderValue * 255.0f), (int)(this.blueSlider.sliderValue * 255.0f)), this.selectBlock.getItemStack(), true, Controller.getBlockStore().getStore().size() + 1));
                XRay.blockStore.write(Controller.getBlockStore().getStore());
                this.mc.displayGuiScreen((GuiScreen)new GuiSelectionScreen());
                break;
            }
            case 99: {
                this.mc.player.closeScreen();
                this.mc.displayGuiScreen((GuiScreen)new GuiSelectionScreen());
                break;
            }
        }
    }
    
    @Override
    protected void keyTyped(final char par1, final int par2) throws IOException {
        super.keyTyped(par1, par2);
        if (this.oreName.isFocused()) {
            this.oreName.textboxKeyTyped(par1, par2);
        }
        else if (par2 == 15) {
            if (!this.oreNameCleared) {
                this.oreName.setText("");
            }
            this.oreName.setFocused(true);
        }
    }
    
    public void updateScreen() {
        this.oreName.updateCursorCounter();
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float f) {
        super.drawScreen(x, y, f);
        this.getFontRender().drawStringWithShadow(this.selectBlock.getItemStack().getDisplayName(), this.width / 2.0f - 100.0f, this.height / 2.0f - 90.0f, 16777215);
        this.oreName.drawTextBox();
        renderPreview(this.width / 2 - 100, this.height / 2 - 40, this.redSlider.sliderValue, this.greenSlider.sliderValue, this.blueSlider.sliderValue);
        if (this.state == null && this.addBtn.isMouseOver()) {
            this.drawHoveringText((List)Arrays.asList(I18n.format("xray.message.state_warning", new Object[0]).split("\n")), this.addBtn.x - 30, this.addBtn.y - 45);
        }
        RenderHelper.enableGUIStandardItemLighting();
        this.itemRender.renderItemAndEffectIntoGUI(this.selectBlock.getItemStack(), this.width / 2 + 85, this.height / 2 - 105);
        RenderHelper.disableStandardItemLighting();
    }
    
    static void renderPreview(final int x, final int y, final float r, final float g, final float b) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder tessellate = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(r, g, b, 1.0f);
        tessellate.begin(7, DefaultVertexFormats.POSITION);
        tessellate.pos((double)x, (double)y, 0.0).endVertex();
        tessellate.pos((double)x, (double)(y + 45), 0.0).endVertex();
        tessellate.pos((double)(x + 202), (double)(y + 45), 0.0).endVertex();
        tessellate.pos((double)(x + 202), (double)y, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int mouse) throws IOException {
        super.mouseClicked(x, y, mouse);
        this.oreName.mouseClicked(x, y, mouse);
        if (this.oreName.isFocused() && !this.oreNameCleared) {
            this.oreName.setText("");
            this.oreNameCleared = true;
        }
        if (!this.oreName.isFocused() && this.oreNameCleared && Objects.equals(this.oreName.getText(), "")) {
            this.oreNameCleared = false;
            this.oreName.setText(I18n.format("xray.input.gui", new Object[0]));
        }
    }
    
    @Override
    public boolean hasTitle() {
        return true;
    }
    
    @Override
    public String title() {
        return I18n.format("xray.title.config", new Object[0]);
    }
}
