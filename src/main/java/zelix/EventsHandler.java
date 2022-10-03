package zelix;

import net.minecraft.client.multiplayer.*;
import net.minecraftforge.event.ForgeEventFactory;
import zelix.hack.hacks.ClickGui;
import zelix.utils.system.*;
import zelix.hack.*;
import java.util.*;
import zelix.utils.*;
import net.minecraftforge.fml.common.eventhandler.*;
import zelix.managers.*;
import org.lwjgl.input.*;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.cape.*;
import net.minecraft.client.renderer.entity.layers.*;
import zelix.hack.hacks.*;
import net.minecraft.client.*;
import zelix.gui.clickguis.gishcode.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.client.event.*;
import zelix.eventapi.*;
import zelix.eventapi.event.*;

public class EventsHandler
{
    private boolean initialized;
    private boolean flag;
    private boolean flag1;
    public String map;
    public WorldClient upworld;
    
    public EventsHandler() {
        this.initialized = false;
        this.map = "";
        this.upworld = null;
        EventManager.register(this);
    }
    
    public boolean onPacket(final Object packet, final Connection.Side side) {
        boolean suc = true;
        for (final Hack hack : HackManager.getHacks()) {
            if (hack.isToggled()) {
                if (Wrapper.INSTANCE.world() == null) {
                    continue;
                }
                suc &= hack.onPacket(packet, side);
            }
        }
        return suc;
    }
    
    @SubscribeEvent
    public void onGuiContainer(final GuiContainerEvent event) {
        if (Utils.nullCheck()) {
            return;
        }
        try {
            HackManager.onGuiContainer(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onRender3D(final RenderBlockOverlayEvent event) {
        if (Utils.nullCheck() || Wrapper.INSTANCE.mcSettings().hideGUI) {
            return;
        }
        try {
            HackManager.onRender3D(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onGuiOpen(final GuiOpenEvent event) {
        if (Utils.nullCheck()) {
            return;
        }
        try {
            FileManager.saveHacks();
            HackManager.onGuiOpen(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onMouse(final MouseEvent event) {
        if (Utils.nullCheck()) {
            return;
        }
        try {
            HackManager.onMouse(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (Utils.nullCheck()) {
            return;
        }
        try {
            final int key = Keyboard.getEventKey();
            if (key == 0 || key == -1) {
                return;
            }
            if (Keyboard.getEventKeyState()) {
                HackManager.onKeyPressed(key);
            }
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onCameraSetup(final EntityViewRenderEvent.CameraSetup event) {
        if (Utils.nullCheck() || GhostMode.enabled) {
            return;
        }
        try {
            HackManager.onCameraSetup(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onItemPickup(final EntityItemPickupEvent event) {
        if (Utils.nullCheck() || GhostMode.enabled) {
            return;
        }
        try {
            HackManager.onItemPickup(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
//    @SubscribeEvent
//    public void onProjectileImpact(ForgeEventFactory event) {
//        if (Utils.nullCheck() || GhostMode.enabled) {
//            return;
//        }
//        try {
//            HackManager.onProjectileImpact(event);
//        }
//        catch (RuntimeException ex) {
//            ex.printStackTrace();
//        }
//    }
    
    @SubscribeEvent
    public void onAttackEntity(final AttackEntityEvent event) {
        if (Utils.nullCheck() || GhostMode.enabled) {
            return;
        }
        try {
            HackManager.onAttackEntity(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (Utils.nullCheck() || GhostMode.enabled) {
            return;
        }
        try {
            HackManager.onPlayerTick(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
    @SubscribeEvent(receiveCanceled = true)
    public void onRenderPlayer(final RenderPlayerEvent.Pre event) {
        if (event.getEntity() != null && !this.map.contains(event.getEntity().getName() + ";")) {
            event.getRenderer().addLayer((LayerRenderer)new CustomLayerCape(event.getRenderer()));
            this.map = this.map + event.getEntity().getName() + ";";
        }
    }
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Utils.nullCheck()) {
            AntiBot.bots.clear();
            this.initialized = false;
            return;
        }
        try {
            if (!this.initialized) {
                new Connection(this);
                ClickGui.setColor();
                this.initialized = true;
            }
            if (!Minecraft.getMinecraft().world.equals(this.upworld)) {
                this.upworld = Minecraft.getMinecraft().world;
                this.map = "";
            }
            if (!(Wrapper.INSTANCE.mc().currentScreen instanceof ClickGuiScreen)) {
                HackManager.getHack("ClickGui").setToggled(false);
            }
            if (!GhostMode.enabled) {
                HackManager.onClientTick(event);
            }
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (Utils.nullCheck() || GhostMode.enabled) {
            return;
        }
        try {
            HackManager.onLivingUpdate(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (Utils.nullCheck() || GhostMode.enabled || Wrapper.INSTANCE.mcSettings().hideGUI) {
            return;
        }
        try {
            HackManager.onRenderWorldLast(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Text event) {
        if (Utils.nullCheck() || GhostMode.enabled) {
            return;
        }
        try {
            HackManager.onRenderGameOverlay(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
        if (Utils.nullCheck() || GhostMode.enabled) {
            return;
        }
        try {
            HackManager.onLeftClickBlock(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onInputUpdateEvent(InputUpdateEvent event) {
        if (Utils.nullCheck() || GhostMode.enabled) {
            return;
        }
        try {
            HackManager.onInputUpdate(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
    @EventTarget
    public void onPlayerEventPre(final EventPlayerPre event) {
        if (Utils.nullCheck() || GhostMode.enabled) {
            return;
        }
        try {
            HackManager.onPlayerEventPre(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
    @EventTarget
    public void onPlayerEventPost(final EventPlayerPost event) {
        if (Utils.nullCheck() || GhostMode.enabled) {
            return;
        }
        try {
            HackManager.onPlayerEventPost(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
}
