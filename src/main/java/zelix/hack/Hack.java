package zelix.hack;

import java.util.*;

import zelix.hack.hacks.ClickGui;
import zelix.value.*;
import zelix.gui.clickguis.gishcode.*;
import zelix.utils.*;
import zelix.utils.system.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import zelix.eventapi.event.*;
import net.minecraftforge.client.event.*;
import zelix.utils.hooks.visual.*;
import zelix.hack.hacks.*;

public class Hack
{
    private String name;
    private String Chinese_name;
    private HackCategory category;
    private boolean toggled;
    private boolean show;
    private int key;
    private ArrayList<Value> values;
    
    public Hack(final String name, final HackCategory category) {
        this.values = new ArrayList<Value>();
        this.name = name;
        this.category = category;
        this.toggled = false;
        this.show = true;
        this.key = 0;
    }
    
    public void addValue(final Value... values) {
        for (final Value value : values) {
            this.getValues().add(value);
        }
    }
    
    public ArrayList<Value> getValues() {
        return this.values;
    }
    
    public boolean isToggledMode(final String modeName) {
        for (final Value value : this.values) {
            if (value instanceof ModeValue) {
                final ModeValue modeValue = (ModeValue)value;
                for (final Mode mode : modeValue.getModes()) {
                    if (mode.getName().equalsIgnoreCase(modeName) && mode.isToggled()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isToggledValue(final String valueName) {
        for (final Value value : this.values) {
            if (value instanceof BooleanValue) {
                final BooleanValue booleanValue = (BooleanValue)value;
                if (booleanValue.getName().equalsIgnoreCase(valueName) && booleanValue.getValue()) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    public void setValues(final ArrayList<Value> values) {
        for (final Value value : values) {
            for (final Value value2 : this.values) {
                if (value.getName().equalsIgnoreCase(value2.getName())) {
                    value2.setValue(value.getValue());
                }
            }
        }
    }
    
    public void toggle() {
        this.toggled = !this.toggled;
        if (this.toggled) {
            this.onEnable();
        }
        else {
            this.onDisable();
        }
        RenderUtils.splashTickPos = 0;
        if (!RenderUtils.isSplash && !(Wrapper.INSTANCE.mc().currentScreen instanceof ClickGuiScreen)) {
            RenderUtils.isSplash = true;
        }
    }
    
    public void setChinese(final String Ch) {
        this.Chinese_name = Ch;
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    public void onGuiContainer(final GuiContainerEvent event) {
    }
    
    public void onGuiOpen(final GuiOpenEvent event) {
    }
    
    public void onMouse(final MouseEvent event) {
    }
    
    public boolean onPacket(final Object packet, final Connection.Side side) {
        return true;
    }
    
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
    }
    
    public void onClientTick(final TickEvent.ClientTickEvent event) {
    }
    
    public void onCameraSetup(final EntityViewRenderEvent.CameraSetup event) {
    }
    
    public void onAttackEntity(final AttackEntityEvent event) {
    }
    
    public void onItemPickup(final EntityItemPickupEvent event) {
    }
    
    public void onProjectileImpact(final ProjectileImpactEvent event) {
    }
    
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
    }
    
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
    }
    
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Text event) {
    }
    
    public void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
    }
    
    public void onInputUpdate(final InputUpdateEvent event) {
    }
    
    public void onPlayerEventPre(final EventPlayerPre event) {
    }
    
    public void onPlayerEventPost(final EventPlayerPost event) {
    }
    
    public void onRender3D(final RenderBlockOverlayEvent event) {
    }
    
    public void d(final Object o) {
        final String str = "[DEBUG]: " + o;
        ChatUtils.warning(str);
    }
    
    public void d() {
        final String str = "[DEBUG]: !";
        ChatUtils.warning(str);
    }
    
    public String getDescription() {
        return null;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getRenderName() {
        if (ClickGui.language.getMode("Chinese").isToggled()) {
            return (this.Chinese_name == null) ? this.name : this.Chinese_name;
        }
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public HackCategory getCategory() {
        return this.category;
    }
    
    public void setCategory(final HackCategory category) {
        this.category = category;
    }
    
    public int getKey() {
        if (this.key == -1) {
            return 0;
        }
        return this.key;
    }
    
    public void setKey(final int key) {
        this.key = key;
    }
    
    public boolean isToggled() {
        return this.toggled;
    }
    
    public void setToggled(final boolean toggled) {
        this.toggled = toggled;
    }
    
    public boolean isShow() {
        return this.show;
    }
    
    public void setShow(final boolean show) {
        this.show = show;
    }
}
