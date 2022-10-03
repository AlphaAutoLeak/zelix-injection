package zelix.hack.hacks.xray.gui.manage;

import zelix.hack.hacks.xray.gui.utils.*;
import zelix.hack.hacks.xray.gui.utils.GuiSlider;
import zelix.hack.hacks.xray.reference.block.*;
import net.minecraft.client.resources.*;
import zelix.hack.hacks.xray.utils.*;
import zelix.hack.hacks.xray.xray.*;
import zelix.hack.hacks.xray.*;
import zelix.hack.hacks.xray.gui.*;
import net.minecraft.client.gui.*;
import java.io.*;
import net.minecraft.client.renderer.*;

public class GuiEdit extends GuiBase
{
    private GuiTextField oreName;
    private GuiSlider redSlider;
    private GuiSlider greenSlider;
    private GuiSlider blueSlider;
    private BlockData block;
    private String storeKey;
    private static final int BUTTON_DELETE = 100;
    private static final int BUTTON_SAVE = 98;
    private static final int BUTTON_CANCEL = 99;
    
    public GuiEdit(final String storeKey, final BlockData block) {
        super(true);
        this.setSideTitle(I18n.format("xray.single.tools", new Object[0]));
        this.storeKey = storeKey;
        this.block = block;
    }
    
    public void initGui() {
        this.buttonList.add(new GuiButton(100, this.width / 2 + 78, this.height / 2 - 60, 120, 20, I18n.format("xray.single.delete", new Object[0])));
        this.buttonList.add(new GuiButton(99, this.width / 2 + 78, this.height / 2 + 58, 120, 20, I18n.format("xray.single.cancel", new Object[0])));
        this.buttonList.add(new GuiButton(98, this.width / 2 - 138, this.height / 2 + 83, 202, 20, I18n.format("xray.single.save", new Object[0])));
        this.buttonList.add(this.redSlider = new GuiSlider(3, this.width / 2 - 138, this.height / 2 + 7, I18n.format("xray.color.red", new Object[0]), 0.0f, 255.0f));
        this.buttonList.add(this.greenSlider = new GuiSlider(2, this.width / 2 - 138, this.height / 2 + 30, I18n.format("xray.color.green", new Object[0]), 0.0f, 255.0f));
        this.buttonList.add(this.blueSlider = new GuiSlider(1, this.width / 2 - 138, this.height / 2 + 53, I18n.format("xray.color.blue", new Object[0]), 0.0f, 255.0f));
        this.redSlider.sliderValue = this.block.getColor().getRed() / 255.0f;
        this.greenSlider.sliderValue = this.block.getColor().getGreen() / 255.0f;
        this.blueSlider.sliderValue = this.block.getColor().getBlue() / 255.0f;
        (this.oreName = new GuiTextField(1, this.fontRenderer, this.width / 2 - 138, this.height / 2 - 63, 202, 20)).setText(this.block.getEntryName());
    }
    
    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 98: {
                final BlockData block = new BlockData(this.storeKey, this.oreName.getText(), this.block.getStateId(), new OutlineColor((int)(this.redSlider.sliderValue * 255.0f), (int)(this.greenSlider.sliderValue * 255.0f), (int)(this.blueSlider.sliderValue * 255.0f)), this.block.getItemStack(), this.block.isDrawing(), this.block.getOrder());
                Controller.getBlockStore().getStore().remove(this.storeKey);
                Controller.getBlockStore().getStore().put(this.storeKey, block);
                XRay.blockStore.write(Controller.getBlockStore().getStore());
                this.mc.player.closeScreen();
                this.mc.displayGuiScreen((GuiScreen)new GuiSelectionScreen());
                break;
            }
            case 100: {
                Controller.getBlockStore().getStore().remove(this.storeKey);
                XRay.blockStore.write(Controller.getBlockStore().getStore());
                this.mc.player.closeScreen();
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
    }
    
    public void updateScreen() {
        this.oreName.updateCursorCounter();
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float f) {
        super.drawScreen(x, y, f);
        this.getFontRender().drawStringWithShadow(this.block.getItemStack().getDisplayName(), (float)(this.width / 2 - 138), (float)(this.height / 2 - 90), 16777215);
        this.oreName.drawTextBox();
        GuiAddBlock.renderPreview(this.width / 2 - 138, this.height / 2 - 40, this.redSlider.sliderValue, this.greenSlider.sliderValue, this.blueSlider.sliderValue);
        RenderHelper.enableGUIStandardItemLighting();
        this.itemRender.renderItemAndEffectIntoGUI(this.block.getItemStack(), this.width / 2 + 50, this.height / 2 - 105);
        RenderHelper.disableStandardItemLighting();
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int mouse) throws IOException {
        super.mouseClicked(x, y, mouse);
        this.oreName.mouseClicked(x, y, mouse);
    }
    
    @Override
    public boolean hasTitle() {
        return true;
    }
    
    @Override
    public String title() {
        return I18n.format("xray.title.edit", new Object[0]);
    }
}
