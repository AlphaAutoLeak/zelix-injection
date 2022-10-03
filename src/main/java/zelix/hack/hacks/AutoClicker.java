package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraft.client.gui.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import org.lwjgl.input.*;
import net.minecraft.client.gui.inventory.*;
import java.lang.reflect.*;
import zelix.utils.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraftforge.common.*;
import net.minecraft.util.*;
import net.minecraft.block.material.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.item.*;

public class AutoClicker extends Hack
{
    TimerUtils time;
    int delay;
    static ModeValue mode;
    static BooleanValue OnClick;
    static BooleanValue inventoryFill;
    static BooleanValue weaponOnly;
    static NumberValue CPS;
    static NumberValue RandomMS;
    BooleanValue onlySword;
    BooleanValue BlockOnly;
    private Method playerMouseInput;
    
    public AutoClicker() {
        super("AutoClicker", HackCategory.COMBAT);
        this.time = new TimerUtils();
        this.playerMouseInput = ReflectionHelper.findMethod(GuiScreen.class, null, new String[] { "mouseClicked", "mouseClicked" }, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        AutoClicker.mode = new ModeValue("Mode", new Mode[] { new Mode("Left", true), new Mode("Right", false), new Mode("Both", false) });
        AutoClicker.OnClick = new BooleanValue("OnClick", Boolean.valueOf(true));
        AutoClicker.inventoryFill = new BooleanValue("InvFill", Boolean.valueOf(true));
        AutoClicker.weaponOnly = new BooleanValue("Weapon", Boolean.valueOf(true));
        AutoClicker.CPS = new NumberValue("CPS", 9.0, 1.0, 20.0);
        AutoClicker.RandomMS = new NumberValue("RandomMS", 5.0, 0.0, 250.0);
        this.onlySword = new BooleanValue("AutoSword", Boolean.valueOf(false));
        this.BlockOnly = new BooleanValue("BlockOnly", Boolean.valueOf(false));
        this.addValue(AutoClicker.mode, AutoClicker.OnClick, AutoClicker.inventoryFill, AutoClicker.weaponOnly, AutoClicker.CPS, AutoClicker.RandomMS, this.onlySword, this.BlockOnly);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.time.isDelay(this.delay)) {
            if (AutoClicker.OnClick.getValue()) {
                if (AutoClicker.mode.getMode("Left").isToggled()) {
                    if (Mouse.isButtonDown(0)) {
                        this.click();
                    }
                }
                else if (AutoClicker.mode.getMode("Right").isToggled()) {
                    if (Mouse.isButtonDown(1)) {
                        this.click();
                    }
                }
                else if (Mouse.isButtonDown(1) || Mouse.isButtonDown(0)) {
                    this.click();
                }
            }
            else {
                this.click();
            }
        }
    }
    
    private void click() {
        if (this.onlySword.getValue()) {
            AutoTool.equipBestWeapon();
        }
        this.delay = (int)Math.round(1000.0 / AutoClicker.CPS.getValue());
        final int random = (int)(Math.random() * AutoClicker.RandomMS.getValue());
        this.delay += random;
        this.time.setLastMS();
        if (AutoClicker.mode.getMode("Left").isToggled()) {
            this.clickMouse();
        }
        else if (AutoClicker.mode.getMode("Right").isToggled()) {
            if (this.BlockOnly.getValue()) {
                if (Wrapper.INSTANCE.player().getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock || Wrapper.INSTANCE.player().getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemBlock) {
                    rightClickMouse();
                }
            }
            else {
                rightClickMouse();
            }
        }
        else {
            if (Mouse.isButtonDown(0)) {
                this.clickMouse();
            }
            if (Mouse.isButtonDown(1)) {
                if (this.BlockOnly.getValue()) {
                    if (Wrapper.INSTANCE.player().getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock || Wrapper.INSTANCE.player().getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemBlock) {
                        rightClickMouse();
                    }
                }
                else {
                    rightClickMouse();
                }
            }
        }
    }
    
    public void doInventoryClick() {
        if (AutoClicker.inventoryFill.getValue() && (Wrapper.INSTANCE.mc().currentScreen instanceof GuiInventory || Wrapper.INSTANCE.mc().currentScreen instanceof GuiChest)) {
            this.inInvClick(Wrapper.INSTANCE.mc().currentScreen);
        }
    }
    
    private void inInvClick(final GuiScreen guiScreen) {
        final int mouseInGUIPosX = Mouse.getX() * guiScreen.width / Wrapper.INSTANCE.mc().displayWidth;
        final int mouseInGUIPosY = guiScreen.height - Mouse.getY() * guiScreen.height / Wrapper.INSTANCE.mc().displayHeight - 1;
        try {
            this.playerMouseInput.invoke(guiScreen, mouseInGUIPosX, mouseInGUIPosY, 0);
        }
        catch (IllegalAccessException ex) {}
        catch (InvocationTargetException ex2) {}
    }
    
    private void clickMouse() {
        if (Wrapper.INSTANCE.mc().currentScreen != null || !Wrapper.INSTANCE.mc().inGameHasFocus) {
            this.doInventoryClick();
            return;
        }
        if (AutoClicker.weaponOnly.getValue() && !Utils.isPlayerHoldingWeapon()) {
            return;
        }
        final int leftClickCounter = 0;
        final Minecraft mc = Minecraft.getMinecraft();
        if (leftClickCounter <= 0) {
            if (Minecraft.getMinecraft().objectMouseOver != null) {
                if (!Wrapper.INSTANCE.mc().player.isRowingBoat()) {
                    switch (Wrapper.INSTANCE.mc().objectMouseOver.typeOfHit) {
                        case ENTITY: {
                            Wrapper.INSTANCE.mc().playerController.attackEntity((EntityPlayer)Wrapper.INSTANCE.mc().player, Wrapper.INSTANCE.mc().objectMouseOver.entityHit);
                            break;
                        }
                        case BLOCK: {
                            final BlockPos blockpos = Wrapper.INSTANCE.mc().objectMouseOver.getBlockPos();
                            if (!Wrapper.INSTANCE.mc().world.isAirBlock(blockpos)) {
                                Wrapper.INSTANCE.mc().playerController.clickBlock(blockpos, Wrapper.INSTANCE.mc().objectMouseOver.sideHit);
                                break;
                            }
                        }
                        case MISS: {
                            Wrapper.INSTANCE.mc().player.resetCooldown();
                            ForgeHooks.onEmptyLeftClick((EntityPlayer)Wrapper.INSTANCE.mc().player);
                            break;
                        }
                    }
                    Wrapper.INSTANCE.mc().player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
    }
    
    public static void rightClickMouse() {
        final Minecraft mc = Minecraft.getMinecraft();
        if (!Wrapper.INSTANCE.mc().playerController.getIsHittingBlock() && !Wrapper.INSTANCE.mc().player.isRowingBoat()) {
            if (Wrapper.INSTANCE.mc().objectMouseOver == null) {}
            for (final EnumHand enumhand : EnumHand.values()) {
                final ItemStack itemstack = Wrapper.INSTANCE.mc().player.getHeldItem(enumhand);
                if (Wrapper.INSTANCE.mc().objectMouseOver != null) {
                    switch (Wrapper.INSTANCE.mc().objectMouseOver.typeOfHit) {
                        case ENTITY: {
                            if (Wrapper.INSTANCE.mc().playerController.interactWithEntity((EntityPlayer)Wrapper.INSTANCE.mc().player, Wrapper.INSTANCE.mc().objectMouseOver.entityHit, Wrapper.INSTANCE.mc().objectMouseOver, enumhand) == EnumActionResult.SUCCESS) {
                                return;
                            }
                            if (Wrapper.INSTANCE.mc().playerController.interactWithEntity((EntityPlayer)Wrapper.INSTANCE.mc().player, Wrapper.INSTANCE.mc().objectMouseOver.entityHit, enumhand) == EnumActionResult.SUCCESS) {
                                return;
                            }
                            break;
                        }
                        case BLOCK: {
                            final BlockPos blockpos = Wrapper.INSTANCE.mc().objectMouseOver.getBlockPos();
                            if (Wrapper.INSTANCE.mc().world.getBlockState(blockpos).getMaterial() == Material.AIR) {
                                break;
                            }
                            final int i = itemstack.getCount();
                            final EnumActionResult enumactionresult = Wrapper.INSTANCE.mc().playerController.processRightClickBlock(Wrapper.INSTANCE.mc().player, Wrapper.INSTANCE.mc().world, blockpos, Wrapper.INSTANCE.mc().objectMouseOver.sideHit, Wrapper.INSTANCE.mc().objectMouseOver.hitVec, enumhand);
                            if (enumactionresult == EnumActionResult.SUCCESS) {
                                Wrapper.INSTANCE.mc().player.swingArm(enumhand);
                                if (!itemstack.isEmpty() && (itemstack.getCount() != i || Wrapper.INSTANCE.mc().playerController.isInCreativeMode())) {
                                    Wrapper.INSTANCE.mc().entityRenderer.itemRenderer.resetEquippedProgress(enumhand);
                                }
                                return;
                            }
                            break;
                        }
                    }
                }
                if (itemstack.isEmpty() && (Wrapper.INSTANCE.mc().objectMouseOver == null || Wrapper.INSTANCE.mc().objectMouseOver.typeOfHit == RayTraceResult.Type.MISS)) {
                    ForgeHooks.onEmptyClick((EntityPlayer)Wrapper.INSTANCE.mc().player, enumhand);
                }
                if (!itemstack.isEmpty() && Wrapper.INSTANCE.mc().playerController.processRightClick((EntityPlayer)Wrapper.INSTANCE.mc().player, (World)Wrapper.INSTANCE.mc().world, enumhand) == EnumActionResult.SUCCESS) {
                    Wrapper.INSTANCE.mc().entityRenderer.itemRenderer.resetEquippedProgress(enumhand);
                    return;
                }
            }
        }
    }
}
