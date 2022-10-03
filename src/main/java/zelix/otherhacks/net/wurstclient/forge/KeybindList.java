package zelix.otherhacks.net.wurstclient.forge;

import zelix.otherhacks.net.wurstclient.forge.utils.*;
import org.lwjgl.input.*;
import com.google.gson.*;
import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.util.function.*;

public final class KeybindList
{
    private final Path path;
    private final ArrayList<Keybind> keybinds;
    
    public KeybindList(final Path file) {
        this.keybinds = new ArrayList<Keybind>();
        this.path = file;
    }
    
    public void init() {
        JsonObject json;
        try (final BufferedReader reader = Files.newBufferedReader(this.path)) {
            json = JsonUtils.jsonParser.parse((Reader)reader).getAsJsonObject();
        }
        catch (NoSuchFileException e2) {
            this.loadDefaults();
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.loadDefaults();
            return;
        }
        this.keybinds.clear();
        final TreeMap<String, String> keybinds2 = new TreeMap<String, String>();
        for (final Map.Entry<String, JsonElement> entry : json.entrySet()) {
            final String key = entry.getKey().toUpperCase();
            if (Keyboard.getKeyIndex(key) == 0) {
                continue;
            }
            if (!entry.getValue().isJsonPrimitive()) {
                continue;
            }
            if (!entry.getValue().getAsJsonPrimitive().isString()) {
                continue;
            }
            final String commands = entry.getValue().getAsString();
            keybinds2.put(key, commands);
        }
        for (final Map.Entry<String, String> entry2 : keybinds2.entrySet()) {
            this.keybinds.add(new Keybind(entry2.getKey(), entry2.getValue()));
        }
        this.save();
    }
    
    public void loadDefaults() {
        this.keybinds.clear();
        this.keybinds.add(new Keybind("B", "fastplace;fastbreak"));
        this.keybinds.add(new Keybind("C", "fullbright"));
        this.keybinds.add(new Keybind("G", "flight"));
        this.keybinds.add(new Keybind("LCONTROL", "clickgui"));
        this.keybinds.add(new Keybind("N", "nuker"));
        this.keybinds.add(new Keybind("R", "killaura"));
        this.keybinds.add(new Keybind("RSHIFT", "clickgui"));
        this.save();
    }
    
    private void save() {
        final JsonObject json = new JsonObject();
        for (final Keybind keybind : this.keybinds) {
            json.addProperty(keybind.getKey(), keybind.getCommands());
        }
        try (final BufferedWriter writer = Files.newBufferedWriter(this.path, new OpenOption[0])) {
            JsonUtils.prettyGson.toJson((JsonElement)json, (Appendable)writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int size() {
        return this.keybinds.size();
    }
    
    public Keybind get(final int index) {
        return this.keybinds.get(index);
    }
    
    public String getCommands(final String key) {
        for (final Keybind keybind : this.keybinds) {
            if (!key.equals(keybind.getKey())) {
                continue;
            }
            return keybind.getCommands();
        }
        return null;
    }
    
    public void add(final String key, final String commands) {
        this.keybinds.removeIf(keybind -> key.equals(keybind.getKey()));
        this.keybinds.add(new Keybind(key, commands));
        this.keybinds.sort(Comparator.comparing((Function<? super Keybind, ? extends Comparable>)Keybind::getKey));
        this.save();
    }
    
    public void remove(final String key) {
        this.keybinds.removeIf(keybind -> key.equals(keybind.getKey()));
        this.save();
    }
    
    public void removeAll() {
        this.keybinds.clear();
        this.save();
    }
    
    public static class Keybind
    {
        private final String key;
        private final String commands;
        
        public Keybind(final String key, final String commands) {
            this.key = key;
            this.commands = commands;
        }
        
        public String getKey() {
            return this.key;
        }
        
        public String getCommands() {
            return this.commands;
        }
    }
}
