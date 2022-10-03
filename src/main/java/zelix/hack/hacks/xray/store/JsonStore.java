package zelix.hack.hacks.xray.store;

import java.util.*;
import zelix.hack.hacks.xray.reference.block.*;
import zelix.utils.hooks.visual.*;
import com.google.gson.reflect.*;
import java.io.*;
import java.lang.reflect.*;
import zelix.hack.hacks.xray.*;
import com.google.gson.*;

public class JsonStore
{
    private static final String FILE = "block_store.json";
    private static final String CONFIG_DIR;
    private static final Gson gson;
    private File jsonFile;
    
    public JsonStore() {
        final File configDir = new File(JsonStore.CONFIG_DIR, "xray");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        this.jsonFile = new File(JsonStore.CONFIG_DIR + "xray", "block_store.json");
        if (!this.jsonFile.exists()) {
            final List<SimpleBlockData> simpleBlockData = new ArrayList<SimpleBlockData>(BlockStore.DEFAULT_BLOCKS);
            for (int i = 0; i < simpleBlockData.size(); ++i) {
                simpleBlockData.get(i).setOrder(i);
            }
            this.write(simpleBlockData);
        }
    }
    
    public void write(final HashMap<String, BlockData> blockData) {
        final List<SimpleBlockData> simpleBlockData = new ArrayList<SimpleBlockData>();
        blockData.forEach((k, v) -> simpleBlockData.add(new SimpleBlockData(v.getEntryName(), k, v.getStateId(), v.getColor(), v.isDrawing(), v.getOrder())));
        this.write(simpleBlockData);
    }
    
    private void write(final List<SimpleBlockData> simpleBlockData) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(this.jsonFile))) {
            JsonStore.gson.toJson((Object)simpleBlockData, (Appendable)writer);
        }
        catch (IOException e) {
            ChatUtils.error("Failed to write json data to block_store.json");
        }
    }
    
    public List<SimpleBlockData> read() {
        if (!this.jsonFile.exists()) {
            return new ArrayList<SimpleBlockData>();
        }
        try {
            final Type type = new TypeToken<List<SimpleBlockData>>() {}.getType();
            try (final BufferedReader reader = new BufferedReader(new FileReader(this.jsonFile))) {
                return (List<SimpleBlockData>)JsonStore.gson.fromJson((Reader)reader, type);
            }
        }
        catch (IOException e) {
            ChatUtils.error("Failed to read json data from block_store.json");
            return new ArrayList<SimpleBlockData>();
        }
    }
    
    static {
        CONFIG_DIR = XRay.mc.mcDataDir + "/config/";
        gson = new GsonBuilder().setPrettyPrinting().create();
    }
}
