package zelix.gui.clickguis;

import net.minecraftforge.fml.relauncher.*;
import javax.annotation.*;
import net.minecraft.creativetab.*;
import zelix.utils.*;
import net.minecraft.entity.player.*;
import java.io.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import net.minecraft.client.util.*;
import net.minecraft.client.resources.*;
import net.minecraft.init.*;
import com.google.common.collect.*;
import net.minecraft.inventory.*;
import org.lwjgl.input.*;
import net.minecraft.util.math.*;
import java.util.*;
import java.util.Locale;

import zelix.managers.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.entity.*;
import net.minecraft.client.gui.achievement.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import net.minecraft.util.text.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.settings.*;
import net.minecraft.client.renderer.texture.*;

@SideOnly(Side.CLIENT)
public class GuiContainerCreative extends InventoryEffectRenderer
{
    private static final ResourceLocation CREATIVE_INVENTORY_TABS;
    private static final InventoryBasic basicInventory;
    private static int selectedTabIndex;
    private float currentScroll;
    private boolean isScrolling;
    private boolean wasClicking;
    private GuiTextField searchField;
    private List<Slot> originalSlots;
    private Slot destroyItemSlot;
    private boolean clearSearch;
    private CreativeCrafting listener;
    private static int tabPage;
    private int maxPages;
    
    public GuiContainerCreative(final EntityPlayer player) {
        super((Container)new ContainerCreative(player));
        this.maxPages = 0;
        player.openContainer = this.inventorySlots;
        this.allowUserInput = true;
        this.ySize = 136;
        this.xSize = 195;
    }
    
    public void updateScreen() {
    }
    
    protected void handleMouseClick(@Nullable final Slot slotIn, final int slotId, final int mouseButton, ClickType type) {
        this.clearSearch = true;
        final boolean flag = type == ClickType.QUICK_MOVE;
        type = ((slotId == -999 && type == ClickType.PICKUP) ? ClickType.THROW : type);
        if (slotIn == null && GuiContainerCreative.selectedTabIndex != CreativeTabs.INVENTORY.getTabIndex() && type != ClickType.QUICK_CRAFT) {
            final InventoryPlayer inventoryplayer1 = Wrapper.INSTANCE.mc().player.inventory;
            if (!inventoryplayer1.getItemStack().isEmpty()) {
                if (mouseButton == 0) {
                    Wrapper.INSTANCE.mc().player.dropItem(inventoryplayer1.getItemStack(), true);
                    Wrapper.INSTANCE.mc().playerController.sendPacketDropItem(inventoryplayer1.getItemStack());
                    inventoryplayer1.setItemStack(ItemStack.EMPTY);
                }
                if (mouseButton == 1) {
                    final ItemStack itemstack6 = inventoryplayer1.getItemStack().splitStack(1);
                    Wrapper.INSTANCE.mc().player.dropItem(itemstack6, true);
                    Wrapper.INSTANCE.mc().playerController.sendPacketDropItem(itemstack6);
                }
            }
        }
        else {
            if (slotIn != null && !slotIn.canTakeStack((EntityPlayer)Wrapper.INSTANCE.mc().player)) {
                return;
            }
            if (slotIn == this.destroyItemSlot && flag) {
                for (int j = 0; j < Wrapper.INSTANCE.mc().player.inventoryContainer.getInventory().size(); ++j) {
                    Wrapper.INSTANCE.mc().playerController.sendSlotPacket(ItemStack.EMPTY, j);
                }
            }
            else if (GuiContainerCreative.selectedTabIndex == CreativeTabs.INVENTORY.getTabIndex()) {
                if (slotIn == this.destroyItemSlot) {
                    Wrapper.INSTANCE.mc().player.inventory.setItemStack(ItemStack.EMPTY);
                }
                else if (type == ClickType.THROW && slotIn != null && slotIn.getHasStack()) {
                    final ItemStack itemstack7 = slotIn.decrStackSize((mouseButton == 0) ? 1 : slotIn.getStack().getMaxStackSize());
                    final ItemStack itemstack8 = slotIn.getStack();
                    Wrapper.INSTANCE.mc().player.dropItem(itemstack7, true);
                    Wrapper.INSTANCE.mc().playerController.sendPacketDropItem(itemstack7);
                    Wrapper.INSTANCE.mc().playerController.sendSlotPacket(itemstack8, ((CreativeSlot)slotIn).slot.slotNumber);
                }
                else if (type == ClickType.THROW && !Wrapper.INSTANCE.mc().player.inventory.getItemStack().isEmpty()) {
                    Wrapper.INSTANCE.mc().player.dropItem(Wrapper.INSTANCE.mc().player.inventory.getItemStack(), true);
                    Wrapper.INSTANCE.mc().playerController.sendPacketDropItem(Wrapper.INSTANCE.mc().player.inventory.getItemStack());
                    Wrapper.INSTANCE.mc().player.inventory.setItemStack(ItemStack.EMPTY);
                }
                else {
                    Wrapper.INSTANCE.mc().player.inventoryContainer.slotClick((slotIn == null) ? slotId : ((CreativeSlot)slotIn).slot.slotNumber, mouseButton, type, (EntityPlayer)Wrapper.INSTANCE.mc().player);
                    Wrapper.INSTANCE.mc().player.inventoryContainer.detectAndSendChanges();
                }
            }
            else if (type != ClickType.QUICK_CRAFT && slotIn.inventory == GuiContainerCreative.basicInventory) {
                final InventoryPlayer inventoryplayer2 = Wrapper.INSTANCE.mc().player.inventory;
                ItemStack itemstack9 = inventoryplayer2.getItemStack();
                final ItemStack itemstack10 = slotIn.getStack();
                if (type == ClickType.SWAP) {
                    if (!itemstack10.isEmpty() && mouseButton >= 0 && mouseButton < 9) {
                        final ItemStack itemstack11 = itemstack10.copy();
                        itemstack11.setCount(itemstack11.getMaxStackSize());
                        Wrapper.INSTANCE.mc().player.inventory.setInventorySlotContents(mouseButton, itemstack11);
                        Wrapper.INSTANCE.mc().player.inventoryContainer.detectAndSendChanges();
                    }
                    return;
                }
                if (type == ClickType.CLONE) {
                    if (inventoryplayer2.getItemStack().isEmpty() && slotIn.getHasStack()) {
                        final ItemStack itemstack12 = slotIn.getStack().copy();
                        itemstack12.setCount(itemstack12.getMaxStackSize());
                        inventoryplayer2.setItemStack(itemstack12);
                    }
                    return;
                }
                if (type == ClickType.THROW) {
                    if (!itemstack10.isEmpty()) {
                        final ItemStack itemstack13 = itemstack10.copy();
                        itemstack13.setCount((mouseButton == 0) ? 1 : itemstack13.getMaxStackSize());
                        Wrapper.INSTANCE.mc().player.dropItem(itemstack13, true);
                        Wrapper.INSTANCE.mc().playerController.sendPacketDropItem(itemstack13);
                    }
                    return;
                }
                if (!itemstack9.isEmpty() && !itemstack10.isEmpty() && itemstack9.isItemEqual(itemstack10) && ItemStack.areItemStackTagsEqual(itemstack9, itemstack10)) {
                    if (mouseButton == 0) {
                        if (flag) {
                            itemstack9.setCount(itemstack9.getMaxStackSize());
                        }
                        else if (itemstack9.getCount() < itemstack9.getMaxStackSize()) {
                            itemstack9.grow(1);
                        }
                    }
                    else {
                        itemstack9.shrink(1);
                    }
                }
                else if (!itemstack10.isEmpty() && itemstack9.isEmpty()) {
                    inventoryplayer2.setItemStack(itemstack10.copy());
                    itemstack9 = inventoryplayer2.getItemStack();
                    if (flag) {
                        itemstack9.setCount(itemstack9.getMaxStackSize());
                    }
                }
                else if (mouseButton == 0) {
                    inventoryplayer2.setItemStack(ItemStack.EMPTY);
                }
                else {
                    inventoryplayer2.getItemStack().shrink(1);
                }
            }
            else if (this.inventorySlots != null) {
                final ItemStack itemstack14 = (slotIn == null) ? ItemStack.EMPTY : this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                this.inventorySlots.slotClick((slotIn == null) ? slotId : slotIn.slotNumber, mouseButton, type, (EntityPlayer)Wrapper.INSTANCE.mc().player);
                if (Container.getDragEvent(mouseButton) == 2) {
                    for (int k = 0; k < 9; ++k) {
                        Wrapper.INSTANCE.mc().playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + k).getStack(), 36 + k);
                    }
                }
                else if (slotIn != null) {
                    final ItemStack itemstack15 = this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                    Wrapper.INSTANCE.mc().playerController.sendSlotPacket(itemstack15, slotIn.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
                    final int i = 45 + mouseButton;
                    if (type == ClickType.SWAP) {
                        Wrapper.INSTANCE.mc().playerController.sendSlotPacket(itemstack14, i - this.inventorySlots.inventorySlots.size() + 9 + 36);
                    }
                    else if (type == ClickType.THROW && !itemstack14.isEmpty()) {
                        final ItemStack itemstack16 = itemstack14.copy();
                        itemstack16.setCount((mouseButton == 0) ? 1 : itemstack16.getMaxStackSize());
                        Wrapper.INSTANCE.mc().player.dropItem(itemstack16, true);
                        Wrapper.INSTANCE.mc().playerController.sendPacketDropItem(itemstack16);
                    }
                    Wrapper.INSTANCE.mc().player.inventoryContainer.detectAndSendChanges();
                }
            }
        }
    }
    
    protected void updateActivePotionEffects() {
        final int i = this.guiLeft;
        super.updateActivePotionEffects();
        if (this.searchField != null && this.guiLeft != i) {
            this.searchField.x = this.guiLeft + 82;
        }
    }
    
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        (this.searchField = new GuiTextField(0, this.fontRenderer, this.guiLeft + 82, this.guiTop + 6, 80, this.fontRenderer.FONT_HEIGHT)).setMaxStringLength(50);
        this.searchField.setEnableBackgroundDrawing(false);
        this.searchField.setVisible(false);
        this.searchField.setTextColor(16777215);
        final int i = GuiContainerCreative.selectedTabIndex;
        GuiContainerCreative.selectedTabIndex = -1;
        this.setCurrentCreativeTab(CreativeTabs.CREATIVE_TAB_ARRAY[i]);
        this.listener = new CreativeCrafting(this.mc);
        Wrapper.INSTANCE.mc().player.inventoryContainer.addListener((IContainerListener)this.listener);
        final int tabCount = CreativeTabs.CREATIVE_TAB_ARRAY.length;
        if (tabCount > 12) {
            this.buttonList.add(new GuiButton(101, this.guiLeft, this.guiTop - 50, 20, 20, "<"));
            this.buttonList.add(new GuiButton(102, this.guiLeft + this.xSize - 20, this.guiTop - 50, 20, 20, ">"));
            this.maxPages = (int)Math.ceil((tabCount - 12) / 10.0);
        }
    }
    
    public void onGuiClosed() {
        super.onGuiClosed();
        if (Wrapper.INSTANCE.mc().player != null && Wrapper.INSTANCE.mc().player.inventory != null) {
            Wrapper.INSTANCE.mc().player.inventoryContainer.removeListener((IContainerListener)this.listener);
        }
        Keyboard.enableRepeatEvents(false);
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (!CreativeTabs.CREATIVE_TAB_ARRAY[GuiContainerCreative.selectedTabIndex].hasSearchBar()) {
            if (GameSettings.isKeyDown(Wrapper.INSTANCE.mc().gameSettings.keyBindChat)) {
                this.setCurrentCreativeTab(CreativeTabs.SEARCH);
            }
            else {
                super.keyTyped(typedChar, keyCode);
            }
        }
        else {
            if (this.clearSearch) {
                this.clearSearch = false;
                this.searchField.setText("");
            }
            if (!this.checkHotbarKeys(keyCode)) {
                if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
                    this.updateCreativeSearch();
                }
                else {
                    super.keyTyped(typedChar, keyCode);
                }
            }
        }
    }
    
    private void updateCreativeSearch() {
        final ContainerCreative containerCreative = (ContainerCreative)this.inventorySlots;
        containerCreative.itemList.clear();
        final CreativeTabs creativeTabs = CreativeTabs.CREATIVE_TAB_ARRAY[GuiContainerCreative.selectedTabIndex];
        if (creativeTabs.hasSearchBar() && creativeTabs != CreativeTabs.SEARCH) {
            creativeTabs.displayAllRelevantItems((NonNullList)containerCreative.itemList);
            if (!this.searchField.getText().isEmpty()) {
                final String lowerCase = this.searchField.getText().toLowerCase(Locale.ROOT);
                final Iterator<ItemStack> itr = (Iterator<ItemStack>)containerCreative.itemList.iterator();
                while (itr.hasNext()) {
                    final ItemStack itemStack = itr.next();
                    boolean b = false;
                    final Iterator iterator = itemStack.getTooltip((EntityPlayer)Wrapper.INSTANCE.mc().player, (ITooltipFlag)(Wrapper.INSTANCE.mc().gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL)).iterator();
                    while (iterator.hasNext()) {
                        if (TextFormatting.getTextWithoutFormattingCodes((String)iterator.next()).toLowerCase(Locale.ROOT).contains(lowerCase)) {
                            b = true;
                            break;
                        }
                    }
                    if (!b) {
                        itr.remove();
                    }
                }
            }
            containerCreative.scrollTo(this.currentScroll = 0.0f);
            return;
        }
        if (this.searchField.getText().isEmpty()) {
            final Iterator<Item> iterator2 = Item.REGISTRY.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().getSubItems(CreativeTabs.SEARCH, (NonNullList)containerCreative.itemList);
            }
        }
        else {
            containerCreative.itemList.addAll((Collection)Wrapper.INSTANCE.mc().getSearchTree(SearchTreeManager.ITEMS).search(this.searchField.getText().toLowerCase(Locale.ROOT)));
        }
        containerCreative.scrollTo(this.currentScroll = 0.0f);
    }
    
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final CreativeTabs creativetabs = CreativeTabs.CREATIVE_TAB_ARRAY[GuiContainerCreative.selectedTabIndex];
        if (creativetabs != null && creativetabs.drawInForegroundOfTab()) {
            GlStateManager.disableBlend();
            this.fontRenderer.drawString(I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]), 8, 6, 268015649);
        }
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (mouseButton == 0) {
            final int i = mouseX - this.guiLeft;
            final int j = mouseY - this.guiTop;
            for (final CreativeTabs creativetabs : CreativeTabs.CREATIVE_TAB_ARRAY) {
                if (this.isMouseOverTab(creativetabs, i, j)) {
                    return;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (state == 0) {
            final int i = mouseX - this.guiLeft;
            final int j = mouseY - this.guiTop;
            for (final CreativeTabs creativetabs : CreativeTabs.CREATIVE_TAB_ARRAY) {
                if (creativetabs != null && this.isMouseOverTab(creativetabs, i, j)) {
                    this.setCurrentCreativeTab(creativetabs);
                    return;
                }
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    private boolean needsScrollBars() {
        return CreativeTabs.CREATIVE_TAB_ARRAY[GuiContainerCreative.selectedTabIndex] != null && GuiContainerCreative.selectedTabIndex != CreativeTabs.INVENTORY.getTabIndex() && CreativeTabs.CREATIVE_TAB_ARRAY[GuiContainerCreative.selectedTabIndex].shouldHidePlayerInventory() && ((ContainerCreative)this.inventorySlots).canScroll();
    }
    
    private void setCurrentCreativeTab(final CreativeTabs creativeTabs) {
        if (creativeTabs == null) {
            return;
        }
        final int selectedTabIndex = GuiContainerCreative.selectedTabIndex;
        GuiContainerCreative.selectedTabIndex = creativeTabs.getTabIndex();
        final ContainerCreative containerCreative = (ContainerCreative)this.inventorySlots;
        this.dragSplittingSlots.clear();
        containerCreative.itemList.clear();
        if (creativeTabs == CreativeTabs.HOTBAR) {
            for (int i = 0; i < 9; ++i) {
                final HotbarSnapshot getHotbarSnapshot = Wrapper.INSTANCE.mc().creativeSettings.getHotbarSnapshot(i);
                if (getHotbarSnapshot.isEmpty()) {
                    for (int j = 0; j < 9; ++j) {
                        if (j == i) {
                            final ItemStack itemStack = new ItemStack(Items.PAPER);
                            itemStack.getOrCreateSubCompound("CustomCreativeLock");
                            itemStack.setStackDisplayName(new TextComponentTranslation("inventory.hotbarInfo", new Object[] { GameSettings.getKeyDisplayString(Wrapper.INSTANCE.mc().gameSettings.keyBindSaveToolbar.getKeyCode()), GameSettings.getKeyDisplayString(Wrapper.INSTANCE.mc().gameSettings.keyBindsHotbar[i].getKeyCode()) }).getUnformattedText());
                            containerCreative.itemList.add(itemStack);
                        }
                        else {
                            containerCreative.itemList.add(ItemStack.EMPTY);
                        }
                    }
                }
                else {
                    containerCreative.itemList.addAll((Collection)getHotbarSnapshot);
                }
            }
        }
        else if (creativeTabs != CreativeTabs.SEARCH) {
            creativeTabs.displayAllRelevantItems((NonNullList)containerCreative.itemList);
        }
        if (creativeTabs == CreativeTabs.INVENTORY) {
            final Container inventoryContainer = Wrapper.INSTANCE.mc().player.inventoryContainer;
            if (this.originalSlots == null) {
                this.originalSlots = (List<Slot>)containerCreative.inventorySlots;
            }
            containerCreative.inventorySlots = Lists.newArrayList();
            for (int k = 0; k < inventoryContainer.inventorySlots.size(); ++k) {
                final CreativeSlot creativeSlot = new CreativeSlot(inventoryContainer.inventorySlots.get(k), k);
                containerCreative.inventorySlots.add(creativeSlot);
                if (k >= 5 && k < 9) {
                    final int n = k - 5;
                    final int n2 = n / 2;
                    final int n3 = n % 2;
                    creativeSlot.xPos = 54 + n2 * 54;
                    creativeSlot.yPos = 6 + n3 * 27;
                }
                else if (k >= 0 && k < 5) {
                    creativeSlot.xPos = -2000;
                    creativeSlot.yPos = -2000;
                }
                else if (k == 45) {
                    creativeSlot.xPos = 35;
                    creativeSlot.yPos = 20;
                }
                else if (k < inventoryContainer.inventorySlots.size()) {
                    final int n4 = k - 9;
                    final int n5 = n4 % 9;
                    final int n6 = n4 / 9;
                    creativeSlot.xPos = 9 + n5 * 18;
                    if (k >= 36) {
                        creativeSlot.yPos = 112;
                    }
                    else {
                        creativeSlot.yPos = 54 + n6 * 18;
                    }
                }
            }
            this.destroyItemSlot = new Slot((IInventory)GuiContainerCreative.basicInventory, 0, 173, 112);
            containerCreative.inventorySlots.add(this.destroyItemSlot);
        }
        else if (selectedTabIndex == CreativeTabs.INVENTORY.getTabIndex()) {
            containerCreative.inventorySlots = this.originalSlots;
            this.originalSlots = null;
        }
        if (this.searchField != null) {
            if (creativeTabs.hasSearchBar()) {
                this.searchField.setVisible(true);
                this.searchField.setCanLoseFocus(false);
                this.searchField.setFocused(true);
                this.searchField.setText("");
                this.searchField.width = creativeTabs.getSearchbarWidth();
                this.searchField.x = this.guiLeft + 171 - this.searchField.width;
                this.updateCreativeSearch();
            }
            else {
                this.searchField.setVisible(false);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setFocused(false);
            }
        }
        containerCreative.scrollTo(this.currentScroll = 0.0f);
    }
    
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0 && this.needsScrollBars()) {
            final int j = (((ContainerCreative)this.inventorySlots).itemList.size() + 9 - 1) / 9 - 5;
            if (i > 0) {
                i = 1;
            }
            if (i < 0) {
                i = -1;
            }
            this.currentScroll -= i / j;
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0f, 1.0f);
            ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        final boolean flag = Mouse.isButtonDown(0);
        final int i = this.guiLeft;
        final int j = this.guiTop;
        final int k = i + 175;
        final int l = j + 18;
        final int i2 = k + 14;
        final int j2 = l + 112;
        if (!this.wasClicking && flag && mouseX >= k && mouseY >= l && mouseX < i2 && mouseY < j2) {
            this.isScrolling = this.needsScrollBars();
        }
        if (!flag) {
            this.isScrolling = false;
        }
        this.wasClicking = flag;
        if (this.isScrolling) {
            this.currentScroll = (mouseY - l - 7.5f) / (j2 - l - 15.0f);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0f, 1.0f);
            ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        int start = GuiContainerCreative.tabPage * 10;
        final int end = Math.min(CreativeTabs.CREATIVE_TAB_ARRAY.length, (GuiContainerCreative.tabPage + 1) * 10 + 2);
        if (GuiContainerCreative.tabPage != 0) {
            start += 2;
        }
        boolean rendered = false;
        for (final CreativeTabs creativetabs : Arrays.copyOfRange(CreativeTabs.CREATIVE_TAB_ARRAY, start, end)) {
            if (creativetabs != null) {
                if (this.renderCreativeInventoryHoveringText(creativetabs, mouseX, mouseY)) {
                    rendered = true;
                    break;
                }
            }
        }
        if (!rendered && !this.renderCreativeInventoryHoveringText(CreativeTabs.SEARCH, mouseX, mouseY)) {
            this.renderCreativeInventoryHoveringText(CreativeTabs.INVENTORY, mouseX, mouseY);
        }
        if (this.destroyItemSlot != null && GuiContainerCreative.selectedTabIndex == CreativeTabs.INVENTORY.getTabIndex() && this.isPointInRegion(this.destroyItemSlot.xPos, this.destroyItemSlot.yPos, 16, 16, mouseX, mouseY)) {
            this.drawHoveringText(I18n.format("inventory.binSlot", new Object[0]), mouseX, mouseY);
        }
        if (this.maxPages != 0) {
            final String page = String.format("%d / %d", GuiContainerCreative.tabPage + 1, this.maxPages + 1);
            final int width = this.fontRenderer.getStringWidth(page);
            GlStateManager.disableLighting();
            this.zLevel = 300.0f;
            this.itemRender.zLevel = 300.0f;
            this.fontRenderer.drawString(page, this.guiLeft + this.xSize / 2 - width / 2, this.guiTop - 44, -1);
            this.zLevel = 0.0f;
            this.itemRender.zLevel = 0.0f;
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
        if (HackManager.getHack("FakeCreative").isToggledValue("ShowItemsID")) {
            this.renderCustomHoveredToolTip(mouseX, mouseY);
        }
        else {
            this.renderHoveredToolTip(mouseX, mouseY);
        }
    }
    
    private void renderCustomHoveredToolTip(final int x, final int y) {
        if (Wrapper.INSTANCE.mc().player.inventory.getItemStack().isEmpty() && this.getSlotUnderMouse() != null && this.getSlotUnderMouse().getHasStack()) {
            final ItemStack stack = this.getSlotUnderMouse().getStack();
            final List<String> textLines = (List<String>)this.getItemToolTip(stack);
            textLines.add(0, Item.getIdFromItem(stack.getItem()) + ":" + stack.getItem().getMetadata(stack));
            this.drawHoveringText((List)textLines, x, y);
        }
    }
    
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderHelper.enableGUIStandardItemLighting();
        final CreativeTabs creativetabs = CreativeTabs.CREATIVE_TAB_ARRAY[GuiContainerCreative.selectedTabIndex];
        int start = GuiContainerCreative.tabPage * 10;
        final int end = Math.min(CreativeTabs.CREATIVE_TAB_ARRAY.length, (GuiContainerCreative.tabPage + 1) * 10 + 2);
        if (GuiContainerCreative.tabPage != 0) {
            start += 2;
        }
        for (final CreativeTabs creativetabs2 : Arrays.copyOfRange(CreativeTabs.CREATIVE_TAB_ARRAY, start, end)) {
            Wrapper.INSTANCE.mc().getTextureManager().bindTexture(GuiContainerCreative.CREATIVE_INVENTORY_TABS);
            if (creativetabs2 != null) {
                if (creativetabs2.getTabIndex() != GuiContainerCreative.selectedTabIndex) {
                    this.drawTab(creativetabs2);
                }
            }
        }
        if (GuiContainerCreative.tabPage != 0) {
            if (creativetabs != CreativeTabs.SEARCH) {
                Wrapper.INSTANCE.mc().getTextureManager().bindTexture(GuiContainerCreative.CREATIVE_INVENTORY_TABS);
                this.drawTab(CreativeTabs.SEARCH);
            }
            if (creativetabs != CreativeTabs.INVENTORY) {
                Wrapper.INSTANCE.mc().getTextureManager().bindTexture(GuiContainerCreative.CREATIVE_INVENTORY_TABS);
                this.drawTab(CreativeTabs.INVENTORY);
            }
        }
        Wrapper.INSTANCE.mc().getTextureManager().bindTexture(creativetabs.getBackgroundImage());
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        //TODO may be fix
        this.searchField.drawTextBox(0,0);

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int i = this.guiLeft + 175;
        final int j = this.guiTop + 18;
        final int k = j + 112;
        Wrapper.INSTANCE.mc().getTextureManager().bindTexture(GuiContainerCreative.CREATIVE_INVENTORY_TABS);
        if (creativetabs.shouldHidePlayerInventory()) {
            this.drawTexturedModalRect(i, j + (int)((k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }
        if ((creativetabs == null || creativetabs.getTabPage() != GuiContainerCreative.tabPage) && creativetabs != CreativeTabs.SEARCH && creativetabs != CreativeTabs.INVENTORY) {
            return;
        }
        this.drawTab(creativetabs);
        if (creativetabs == CreativeTabs.INVENTORY) {
            GuiInventory.drawEntityOnScreen(this.guiLeft + 88, this.guiTop + 45, 20, (float)(this.guiLeft + 88 - mouseX), (float)(this.guiTop + 45 - 30 - mouseY), (EntityLivingBase)Wrapper.INSTANCE.mc().player);
        }
    }
    
    protected boolean isMouseOverTab(final CreativeTabs tab, final int mouseX, final int mouseY) {
        if (tab.getTabPage() != GuiContainerCreative.tabPage && tab != CreativeTabs.SEARCH && tab != CreativeTabs.INVENTORY) {
            return false;
        }
        final int i = tab.getTabColumn();
        int j = 28 * i;
        int k = 0;
        if (tab.isAlignedRight()) {
            j = this.xSize - 28 * (6 - i) + 2;
        }
        else if (i > 0) {
            j += i;
        }
        if (tab.isTabInFirstRow()) {
            k -= 32;
        }
        else {
            k += this.ySize;
        }
        return mouseX >= j && mouseX <= j + 28 && mouseY >= k && mouseY <= k + 32;
    }
    
    protected boolean renderCreativeInventoryHoveringText(final CreativeTabs tab, final int mouseX, final int mouseY) {
        final int i = tab.getTabColumn();
        int j = 28 * i;
        int k = 0;
        if (tab.isAlignedRight()) {
            j = this.xSize - 28 * (6 - i) + 2;
        }
        else if (i > 0) {
            j += i;
        }
        if (tab.isTabInFirstRow()) {
            k -= 32;
        }
        else {
            k += this.ySize;
        }
        if (this.isPointInRegion(j + 3, k + 3, 23, 27, mouseX, mouseY)) {
            this.drawHoveringText(I18n.format(tab.getTranslatedTabLabel(), new Object[0]), mouseX, mouseY);
            return true;
        }
        return false;
    }
    
    protected void drawTab(final CreativeTabs tab) {
        final boolean flag = tab.getTabIndex() == GuiContainerCreative.selectedTabIndex;
        final boolean flag2 = tab.isTabInFirstRow();
        final int i = tab.getTabColumn();
        final int j = i * 28;
        int k = 0;
        int l = this.guiLeft + 28 * i;
        int i2 = this.guiTop;
        final int j2 = 32;
        if (flag) {
            k += 32;
        }
        if (tab.isAlignedRight()) {
            l = this.guiLeft + this.xSize - 28 * (6 - i);
        }
        else if (i > 0) {
            l += i;
        }
        if (flag2) {
            i2 -= 28;
        }
        else {
            k += 64;
            i2 += this.ySize - 4;
        }
        GlStateManager.disableLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        this.drawTexturedModalRect(l, i2, j, k, 28, 32);
        this.zLevel = 100.0f;
        this.itemRender.zLevel = 100.0f;
        l += 6;
        i2 = i2 + 8 + (flag2 ? 1 : -1);
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        final ItemStack itemstack = tab.getIconItemStack();
        this.itemRender.renderItemAndEffectIntoGUI(itemstack, l, i2);
        this.itemRender.renderItemOverlays(this.fontRenderer, itemstack, l, i2);
        GlStateManager.disableLighting();
        this.itemRender.zLevel = 0.0f;
        this.zLevel = 0.0f;
    }
    
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1) {
            Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)new GuiStats((GuiScreen)this, Wrapper.INSTANCE.mc().player.getStatFileWriter()));
        }
        if (button.id == 101) {
            GuiContainerCreative.tabPage = Math.max(GuiContainerCreative.tabPage - 1, 0);
        }
        else if (button.id == 102) {
            GuiContainerCreative.tabPage = Math.min(GuiContainerCreative.tabPage + 1, this.maxPages);
        }
    }
    
    public int getSelectedTabIndex() {
        return GuiContainerCreative.selectedTabIndex;
    }
    
    public static void handleHotbarSnapshots(final Minecraft p_192044_0_, final int p_192044_1_, final boolean p_192044_2_, final boolean p_192044_3_) {
        final EntityPlayerSP entityplayersp = p_192044_0_.player;
        final CreativeSettings creativesettings = p_192044_0_.creativeSettings;
        final HotbarSnapshot hotbarsnapshot = creativesettings.getHotbarSnapshot(p_192044_1_);
        if (p_192044_2_) {
            for (int i = 0; i < InventoryPlayer.getHotbarSize(); ++i) {
                final ItemStack itemstack = ((ItemStack)hotbarsnapshot.get(i)).copy();
                entityplayersp.inventory.setInventorySlotContents(i, itemstack);
                p_192044_0_.playerController.sendSlotPacket(itemstack, 36 + i);
            }
            entityplayersp.inventoryContainer.detectAndSendChanges();
        }
        else if (p_192044_3_) {
            for (int j = 0; j < InventoryPlayer.getHotbarSize(); ++j) {
                hotbarsnapshot.set(j, entityplayersp.inventory.getStackInSlot(j).copy());
            }
            final String s = GameSettings.getKeyDisplayString(p_192044_0_.gameSettings.keyBindsHotbar[p_192044_1_].getKeyCode());
            final String s2 = GameSettings.getKeyDisplayString(p_192044_0_.gameSettings.keyBindLoadToolbar.getKeyCode());
            p_192044_0_.ingameGUI.setOverlayMessage((ITextComponent)new TextComponentTranslation("inventory.hotbarSaved", new Object[] { s2, s }), false);
            creativesettings.write();
        }
    }
    
    static {
        CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
        basicInventory = new InventoryBasic("tmp", true, 45);
        GuiContainerCreative.selectedTabIndex = CreativeTabs.BUILDING_BLOCKS.getTabIndex();
        GuiContainerCreative.tabPage = 0;
    }
    
    @SideOnly(Side.CLIENT)
    public static class ContainerCreative extends Container
    {
        public NonNullList<ItemStack> itemList;
        
        public ContainerCreative(final EntityPlayer player) {
            this.itemList = NonNullList.create();
            final InventoryPlayer inventoryplayer = player.inventory;
            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.addSlotToContainer((Slot)new LockedSlot((IInventory)GuiContainerCreative.basicInventory, i * 9 + j, 9 + j * 18, 18 + i * 18));
                }
            }
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot((IInventory)inventoryplayer, k, 9 + k * 18, 112));
            }
            this.scrollTo(0.0f);
        }
        
        public boolean canInteractWith(final EntityPlayer playerIn) {
            return true;
        }
        
        public void scrollTo(final float pos) {
            final int i = (this.itemList.size() + 9 - 1) / 9 - 5;
            int j = (int)(pos * i + 0.5);
            if (j < 0) {
                j = 0;
            }
            for (int k = 0; k < 5; ++k) {
                for (int l = 0; l < 9; ++l) {
                    final int i2 = l + (k + j) * 9;
                    if (i2 >= 0 && i2 < this.itemList.size()) {
                        GuiContainerCreative.basicInventory.setInventorySlotContents(l + k * 9, (ItemStack)this.itemList.get(i2));
                    }
                    else {
                        GuiContainerCreative.basicInventory.setInventorySlotContents(l + k * 9, ItemStack.EMPTY);
                    }
                }
            }
        }
        
        public boolean canScroll() {
            return this.itemList.size() > 45;
        }
        
        public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
            if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size()) {
                final Slot slot = this.inventorySlots.get(index);
                if (slot != null && slot.getHasStack()) {
                    slot.putStack(ItemStack.EMPTY);
                }
            }
            return ItemStack.EMPTY;
        }
        
        public boolean canMergeSlot(final ItemStack stack, final Slot slotIn) {
            return slotIn.yPos > 90;
        }
        
        public boolean canDragIntoSlot(final Slot slotIn) {
            return slotIn.inventory instanceof InventoryPlayer || (slotIn.yPos > 90 && slotIn.xPos <= 162);
        }
    }
    
    @SideOnly(Side.CLIENT)
    class CreativeSlot extends Slot
    {
        private final Slot slot;
        
        public CreativeSlot(final Slot slot, final int n) {
            super(slot.inventory, n, 0, 0);
            this.slot = slot;
        }
        
        public ItemStack onTake(final EntityPlayer thePlayer, final ItemStack stack) {
            this.slot.onTake(thePlayer, stack);
            return stack;
        }
        
        public boolean isItemValid(final ItemStack stack) {
            return this.slot.isItemValid(stack);
        }
        
        public ItemStack getStack() {
            return this.slot.getStack();
        }
        
        public boolean getHasStack() {
            return this.slot.getHasStack();
        }
        
        public void putStack(final ItemStack stack) {
            this.slot.putStack(stack);
        }
        
        public void onSlotChanged() {
            this.slot.onSlotChanged();
        }
        
        public int getSlotStackLimit() {
            return this.slot.getSlotStackLimit();
        }
        
        public int getItemStackLimit(final ItemStack stack) {
            return this.slot.getItemStackLimit(stack);
        }
        
        @Nullable
        public String getSlotTexture() {
            return this.slot.getSlotTexture();
        }
        
        public ItemStack decrStackSize(final int amount) {
            return this.slot.decrStackSize(amount);
        }
        
        public boolean isHere(final IInventory inv, final int slotIn) {
            return this.slot.isHere(inv, slotIn);
        }
        
        public boolean isEnabled() {
            return this.slot.isEnabled();
        }
        
        public boolean canTakeStack(final EntityPlayer playerIn) {
            return this.slot.canTakeStack(playerIn);
        }
        
        public ResourceLocation getBackgroundLocation() {
            return this.slot.getBackgroundLocation();
        }
        
        public void setBackgroundLocation(final ResourceLocation texture) {
            this.slot.setBackgroundLocation(texture);
        }
        
        public void setBackgroundName(@Nullable final String name) {
            this.slot.setBackgroundName(name);
        }
        
        @Nullable
        public TextureAtlasSprite getBackgroundSprite() {
            return this.slot.getBackgroundSprite();
        }
        
        public int getSlotIndex() {
            return this.slot.getSlotIndex();
        }
        
        public boolean isSameInventory(final Slot other) {
            return this.slot.isSameInventory(other);
        }
    }
    
    @SideOnly(Side.CLIENT)
    static class LockedSlot extends Slot
    {
        public LockedSlot(final IInventory p_i47453_1_, final int p_i47453_2_, final int p_i47453_3_, final int p_i47453_4_) {
            super(p_i47453_1_, p_i47453_2_, p_i47453_3_, p_i47453_4_);
        }
        
        public boolean canTakeStack(final EntityPlayer playerIn) {
            if (super.canTakeStack(playerIn) && this.getHasStack()) {
                return this.getStack().getSubCompound("CustomCreativeLock") == null;
            }
            return !this.getHasStack();
        }
    }
}
