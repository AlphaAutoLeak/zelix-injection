package zelix.otherhacks.net.wurstclient.forge;

import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import net.minecraft.client.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import java.util.*;
import java.lang.annotation.*;

public abstract class Hack extends WForgeRegistryEntry<Hack>
{
    protected static final ForgeWurst wurst;
    protected static final Minecraft mc;
    private final String name;
    private final String description;
    private Category category;
    private final LinkedHashMap<String, Setting> settings;
    private boolean enabled;
    private final boolean stateSaved;
    
    public Hack(final String name, final String description) {
        this.settings = new LinkedHashMap<String, Setting>();
        this.stateSaved = !this.getClass().isAnnotationPresent(DontSaveState.class);
        this.name = name;
        this.description = description;
    }
    
    public final String getName() {
        return this.name;
    }
    
    public String getRenderName() {
        return this.name;
    }
    
    public final String getDescription() {
        return this.description;
    }
    
    public final Category getCategory() {
        return this.category;
    }
    
    protected final void setCategory(final Category category) {
        this.category = category;
    }
    
    public final Map<String, Setting> getSettings() {
        return Collections.unmodifiableMap((Map<? extends String, ? extends Setting>)this.settings);
    }
    
    protected final void addSetting(final Setting setting) {
        final String key = setting.getName().toLowerCase();
        if (this.settings.containsKey(key)) {
            throw new IllegalArgumentException("Duplicate setting: " + this.name + " " + key);
        }
        this.settings.put(key, setting);
    }
    
    public final boolean isEnabled() {
        return this.enabled;
    }
    
    public final void setEnabled(final boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        if (enabled) {
            this.onEnable();
        }
        else {
            this.onDisable();
        }
        if (this.stateSaved) {
            Hack.wurst.getHax().saveEnabledHacks();
        }
    }
    
    public final boolean isStateSaved() {
        return this.stateSaved;
    }
    
    protected void onEnable() {
    }
    
    protected void onDisable() {
    }
    
    static {
        wurst = ForgeWurst.getForgeWurst();
        mc = Minecraft.getMinecraft();
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public @interface DontSaveState {
    }
}
