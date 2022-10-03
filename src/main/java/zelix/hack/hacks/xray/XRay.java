package zelix.hack.hacks.xray;

import net.minecraft.client.*;
import zelix.hack.hacks.xray.gui.*;
import zelix.utils.hooks.visual.*;
import zelix.hack.hacks.xray.keybinding.*;
import net.minecraftforge.common.*;
import zelix.hack.hacks.xray.store.*;
import zelix.hack.hacks.xray.xray.*;
import java.util.*;
import zelix.hack.hacks.xray.reference.block.*;
import net.minecraftforge.fml.client.event.*;
import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class XRay
{
    public static GameBlockStore gameBlockStore;
    public static Minecraft mc;
    public static JsonStore blockStore;
    public static XRay instance;
    public InputEvent InputEvent;
    public Events Events;
    public GuiOverlay GuiOverlay;
    
    public void onEnable() {
        ChatUtils.message("Loading Anti-AntiXray");
        if (AntiAntiXrayMod.isFirst) {
            KeyBindings.setup();
        }
        MinecraftForge.EVENT_BUS.register((Object)(this.InputEvent = new InputEvent()));
        MinecraftForge.EVENT_BUS.register((Object)(this.Events = new Events()));
        MinecraftForge.EVENT_BUS.register((Object)(this.GuiOverlay = new GuiOverlay()));
        MinecraftForge.EVENT_BUS.register((Object)this);
        final List<SimpleBlockData> data = XRay.blockStore.read();
        if (data.isEmpty()) {
            return;
        }
        final HashMap<String, BlockData> map = BlockStore.getFromSimpleBlockList(data);
        Controller.getBlockStore().setStore(map);
        XRay.gameBlockStore.populate();
        XRay.instance = this;
    }
    
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this.InputEvent);
        MinecraftForge.EVENT_BUS.unregister((Object)this.Events);
        MinecraftForge.EVENT_BUS.unregister((Object)this.GuiOverlay);
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        Controller.shutdownExecutor();
        XRay.instance = null;
    }
    
    @SubscribeEvent
    public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals("xray")) {
            ConfigManager.sync("xray", Config.Type.INSTANCE);
        }
    }
    
    static {
        XRay.gameBlockStore = new GameBlockStore();
        XRay.mc = Minecraft.getMinecraft();
        XRay.blockStore = new JsonStore();
    }
}
