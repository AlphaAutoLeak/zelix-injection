package zelix.hack.hacks.xray.gui;

import zelix.hack.hacks.xray.gui.utils.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.resources.*;
import zelix.hack.hacks.xray.xray.*;
import java.util.*;
import net.minecraft.client.gui.*;
import net.minecraft.util.*;
import zelix.hack.hacks.xray.utils.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.block.*;
import zelix.hack.hacks.xray.reference.block.*;
import zelix.hack.hacks.xray.gui.manage.*;
import net.minecraft.block.state.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import java.io.*;
import java.util.function.*;
import java.util.stream.*;
import zelix.hack.hacks.xray.*;
import java.awt.*;
import net.minecraftforge.common.config.*;
import org.lwjgl.input.*;
import zelix.hack.hacks.xray.xray.Controller;

public class GuiSelectionScreen extends GuiBase
{
    private GuiTextField search;
    public RenderItem render;
    private String lastSearch;
    private static final int BUTTON_RADIUS = 0;
    private static final int BUTTON_ADD_BLOCK = 1;
    private static final int BUTTON_ADD_HAND = 4;
    private static final int BUTTON_ADD_LOOK = 5;
    private static final int BUTTON_HELP = 6;
    private static final int BUTTON_CLOSE = 7;
    private ArrayList<BlockData> itemList;
    private ArrayList<BlockData> originalList;
    
    public GuiSelectionScreen() {
        super(true);
        this.lastSearch = "";
        this.setSideTitle(I18n.format("xray.single.tools", new Object[0]));
        this.itemList = new ArrayList<BlockData>(Controller.getBlockStore().getStore().values());
        this.originalList = this.itemList;
    }
    
    public void initGui() {
        this.render = this.itemRender;
        this.buttonList.clear();
        (this.search = new GuiTextField(150, this.getFontRender(), this.width / 2 - 137, this.height / 2 - 105, 202, 18)).setCanLoseFocus(true);
        this.buttonList.add(new GuiButton(1, this.width / 2 + 79, this.height / 2 - 60, 120, 20, I18n.format("xray.input.add", new Object[0])));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 79, this.height / 2 - 38, 120, 20, I18n.format("xray.input.add_hand", new Object[0])));
        this.buttonList.add(new GuiButton(5, this.width / 2 + 79, this.height / 2 - 16, 120, 20, I18n.format("xray.input.add_look", new Object[0])));
        this.buttonList.add(new GuiButton(6, this.width / 2 + 79, this.height / 2 + 58, 60, 20, I18n.format("xray.single.help", new Object[0])));
        this.buttonList.add(new GuiButton(7, this.width / 2 + 79 + 62, this.height / 2 + 58, 59, 20, I18n.format("xray.single.close", new Object[0])));
    }
    
    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                Controller.incrementCurrentDist();
                break;
            }
            case 1: {
                this.mc.player.closeScreen();
                this.mc.displayGuiScreen((GuiScreen)new GuiBlockListScrollable());
                break;
            }
            case 6: {
                this.mc.player.closeScreen();
                this.mc.displayGuiScreen((GuiScreen)new GuiHelp());
                break;
            }
            case 4: {
                this.mc.player.closeScreen();
                final ItemStack handItem = this.mc.player.getHeldItem(EnumHand.MAIN_HAND);
                if (!(handItem.getItem() instanceof ItemBlock)) {
                    Utils.sendMessage(this.mc.player, "[XRay] " + I18n.format("xray.message.invalid_hand", new Object[] { handItem.getDisplayName() }));
                    return;
                }
                final IBlockState iBlockState = Utils.getStateFromPlacement((World)this.mc.world, (EntityLivingBase)this.mc.player, handItem);
                this.mc.displayGuiScreen((GuiScreen)new GuiAddBlock(new BlockItem(Block.getStateId(iBlockState), handItem), null));
                break;
            }
            case 5: {
                this.mc.player.closeScreen();
                try {
                    final RayTraceResult ray = this.mc.player.rayTrace(100.0, 20.0f);
                    if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
                        final IBlockState state = this.mc.world.getBlockState(ray.getBlockPos());
                        final Block lookingAt = this.mc.world.getBlockState(ray.getBlockPos()).getBlock();
                        final ItemStack lookingStack = lookingAt.getPickBlock(state, ray, (World)this.mc.world, ray.getBlockPos(), (EntityPlayer)this.mc.player);
                        this.mc.player.closeScreen();
                        this.mc.displayGuiScreen((GuiScreen)new GuiAddBlock(new BlockItem(Block.getStateId(state), lookingStack), state));
                    }
                    else {
                        Utils.sendMessage(this.mc.player, "[XRay] " + I18n.format("xray.message.nothing_infront", new Object[0]));
                    }
                }
                catch (NullPointerException ex) {
                    Utils.sendMessage(this.mc.player, "[XRay] " + I18n.format("xray.message.thats_odd", new Object[0]));
                }
                break;
            }
            case 7: {
                this.mc.player.closeScreen();
                break;
            }
        }
        this.initGui();
    }
    
    @Override
    protected void keyTyped(final char charTyped, final int hex) throws IOException {
        super.keyTyped(charTyped, hex);
        this.search.textboxKeyTyped(charTyped, hex);
        this.updateSearch();
    }
    
    private void updateSearch() {
        if (this.search.getText().equals("")) {
            this.itemList = this.originalList;
            this.lastSearch = "";
            return;
        }
        if (this.lastSearch.equals(this.search.getText())) {
            return;
        }
        this.itemList = this.originalList.stream().filter(b -> b.getEntryName().toLowerCase().contains(this.search.getText().toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
        this.lastSearch = this.search.getText();
    }
    
    public void updateScreen() {
        this.search.updateCursorCounter();
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int mouse) throws IOException {
        super.mouseClicked(x, y, mouse);
        this.search.mouseClicked(x, y, mouse);
        if (mouse == 1) {}
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float f) {
        super.drawScreen(x, y, f);
        this.search.drawTextBox();
        if (!this.search.isFocused() && this.search.getText().equals("")) {
            XRay.mc.fontRenderer.drawStringWithShadow(I18n.format("xray.single.search", new Object[0]), this.width / 2.0f - 130.0f, this.height / 2.0f - 101.0f, Color.GRAY.getRGB());
        }
    }
    
    public void onGuiClosed() {
        ConfigManager.sync("xray", Config.Type.INSTANCE);
        XRay.blockStore.write(Controller.getBlockStore().getStore());
        Controller.requestBlockFinder(true);
    }
    
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        final int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        final int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
    }
}
