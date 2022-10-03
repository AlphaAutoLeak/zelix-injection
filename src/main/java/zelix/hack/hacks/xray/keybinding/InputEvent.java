package zelix.hack.hacks.xray.keybinding;

import net.minecraftforge.fml.client.*;
import zelix.hack.hacks.xray.gui.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import net.minecraft.util.text.*;
import zelix.hack.hacks.xray.*;
import zelix.hack.hacks.xray.xray.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.block.state.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class InputEvent
{
    @SubscribeEvent
    public void onKeyInput(final net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent event) {
        if (!FMLClientHandler.instance().isGUIOpen((Class)GuiChat.class) && XRay.mc.currentScreen == null && XRay.mc.world != null) {
            if (KeyBindings.keyBind_keys[0].isPressed()) {
                Controller.toggleDrawOres();
            }
            else if (KeyBindings.keyBind_keys[1].isPressed()) {
                XRay.mc.displayGuiScreen((GuiScreen)new GuiSelectionScreen());
            }
            else if (KeyBindings.keyBind_keys[2].isPressed()) {
                assert Minecraft.getMinecraft().player != null;
                Minecraft.getMinecraft().player.sendStatusMessage((ITextComponent)new TextComponentString("¡ì6[ ¡ìa\uff01 ¡ì6] ¡ìfRefreshing blocks..."), true);
                AntiAntiXray.revealNewBlocks(Configuration.radius_x, Configuration.radius_y, Configuration.radius_z, Configuration.delay);
            }
            else if (KeyBindings.keyBind_keys[3].isPressed()) {
                Minecraft.getMinecraft().gameSettings.gammaSetting = 114514.0f;
                for (int cx = -2; cx <= 2; ++cx) {
                    for (int cy = 0; cy <= 2; ++cy) {
                        for (int cz = -2; cz <= 2; ++cz) {
                            assert Minecraft.getMinecraft().player != null;
                            final BlockPos b2r = Minecraft.getMinecraft().player.getPosition();
                            final Block s = Block.getBlockFromItem(Minecraft.getMinecraft().player.inventory.getCurrentItem().getItem());
                            IBlockState b = Blocks.AIR.getDefaultState();
                            if (s != null) {
                                b = s.getDefaultState();
                            }
                            Minecraft.getMinecraft().player.world.setBlockState(b2r.add(cx, cy, cz), b);
                        }
                    }
                }
            }
            else if (KeyBindings.keyBind_keys[4].isPressed()) {
                Configuration.freeze = !Configuration.freeze;
                Minecraft.getMinecraft().player.sendStatusMessage((ITextComponent)new TextComponentString("¡ì6[ ¡ìa\uff01 ¡ì6] ¡ìfFreeze now " + (Configuration.freeze ? "opened" : "closed")), true);
            }
        }
    }
}
