package zelix.otherhacks.net.wurstclient.forge.settings;

import net.minecraft.block.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.clickgui.*;
import java.util.stream.*;
import java.util.*;
import java.util.function.*;
import com.google.gson.*;

public final class BlockListSetting extends Setting
{
    private final ArrayList<String> blockNames;
    private final String[] defaultNames;
    
    public BlockListSetting(final String name, final String description, final Block... blocks) {
        super(name, description);
        this.blockNames = new ArrayList<String>();
        Arrays.stream(blocks).parallel().map(b -> BlockUtils.getName(b)).distinct().sorted().forEachOrdered(s -> this.blockNames.add(s));
        this.defaultNames = this.blockNames.toArray(new String[0]);
    }
    
    public BlockListSetting(final String name, final Block... blocks) {
        this(name, (String)null, blocks);
    }
    
    public List<String> getBlockNames() {
        return Collections.unmodifiableList((List<? extends String>)this.blockNames);
    }
    
    public void add(final Block block) {
        final String name = BlockUtils.getName(block);
        if (Collections.binarySearch(this.blockNames, name) >= 0) {
            return;
        }
        this.blockNames.add(name);
        Collections.sort(this.blockNames);
        ForgeWurst.getForgeWurst().getHax().saveSettings();
    }
    
    public void remove(final int index) {
        if (index < 0 || index >= this.blockNames.size()) {
            return;
        }
        this.blockNames.remove(index);
        ForgeWurst.getForgeWurst().getHax().saveSettings();
    }
    
    public void resetToDefaults() {
        this.blockNames.clear();
        this.blockNames.addAll(Arrays.asList(this.defaultNames));
        ForgeWurst.getForgeWurst().getHax().saveSettings();
    }
    
    @Override
    public Component getComponent() {
        return new BlockListEditButton(this);
    }
    
    @Override
    public void fromJson(final JsonElement json) {
        if (!json.isJsonArray()) {
            return;
        }
        this.blockNames.clear();
        StreamSupport.stream(json.getAsJsonArray().spliterator(), true).filter(e -> e.isJsonPrimitive()).filter(e -> e.getAsJsonPrimitive().isString()).map(e -> Block.getBlockFromName(e.getAsString())).filter(Objects::nonNull).map(b -> BlockUtils.getName(b)).distinct().sorted().forEachOrdered(s -> this.blockNames.add(s));
    }
    
    @Override
    public JsonElement toJson() {
        final JsonArray json = new JsonArray();
        this.blockNames.forEach(s -> json.add((JsonElement)new JsonPrimitive(s)));
        return (JsonElement)json;
    }
}
