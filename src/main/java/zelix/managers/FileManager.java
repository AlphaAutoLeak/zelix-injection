package zelix.managers;

import zelix.utils.*;
import zelix.hack.*;
import zelix.value.*;
import zelix.hack.hacks.*;
import zelix.gui.clickguis.kendall.frame.*;
import net.minecraft.item.*;
import java.io.*;
import java.util.*;
import com.google.gson.*;

public class FileManager
{
    private static Gson gsonPretty;
    private static JsonParser jsonParser;
    public static File GISHCODE_DIR;
    public static File CONFIG;
    private static File HACKS;
    private static File XRAYDATA;
    private static File PICKUPFILTER;
    private static File FRIENDS;
    private static File ENEMYS;
    public static File CLICKGUI;
    public static File SKINCHANGER;
    
    public FileManager() {
        FileManager.GISHCODE_DIR = getDirectory();
        if (FileManager.GISHCODE_DIR == null) {
            return;
        }
        FileManager.HACKS = new File(FileManager.GISHCODE_DIR, "hacks.json");
        FileManager.XRAYDATA = new File(FileManager.GISHCODE_DIR, "xraydata.json");
        FileManager.CONFIG = new File(FileManager.GISHCODE_DIR, "configs");
        FileManager.PICKUPFILTER = new File(FileManager.GISHCODE_DIR, "pickupfilter.json");
        FileManager.SKINCHANGER = new File(FileManager.GISHCODE_DIR, "cachedtextures");
        FileManager.CLICKGUI = new File(FileManager.GISHCODE_DIR, "clickgui.json");
        FileManager.FRIENDS = new File(FileManager.GISHCODE_DIR, "friends.json");
        FileManager.ENEMYS = new File(FileManager.GISHCODE_DIR, "enemys.json");
        if (!FileManager.GISHCODE_DIR.exists()) {
            FileManager.GISHCODE_DIR.mkdir();
        }
        if (!FileManager.HACKS.exists()) {
            saveHacks();
        }
        else {
            loadHacks();
        }
        if (!FileManager.PICKUPFILTER.exists()) {
            savePickupFilter();
        }
        else {
            loadPickupFilter();
        }
        if (!FileManager.FRIENDS.exists()) {
            saveFriends();
        }
        else {
            loadFriends();
        }
        if (!FileManager.ENEMYS.exists()) {
            saveEnemys();
        }
        else {
            loadEnemys();
        }
        if (!FileManager.SKINCHANGER.exists()) {
            FileManager.SKINCHANGER.mkdir();
        }
    }
    
    public static File getDirectory() {
        final String var = System.getenv("GISHCODE_DIR");
        final File dir = (var == null || var == "") ? Wrapper.INSTANCE.mc().mcDataDir : new File(var);
        return new File(String.format("%s%s%s-%s%s", dir, File.separator, "Zelix", "1.12.2", File.separator));
    }
    
    public static void loadHacks() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(FileManager.HACKS));
            final JsonObject jsonObject = (JsonObject)FileManager.jsonParser.parse((Reader)bufferedReader);
            bufferedReader.close();
            for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                final Hack hack = HackManager.getHack(entry.getKey());
                if (hack == null) {
                    continue;
                }
                final JsonObject jsonObjectHack = (JsonObject)entry.getValue();
                hack.setKey(jsonObjectHack.get("key").getAsInt());
                hack.setToggled(jsonObjectHack.get("toggled").getAsBoolean());
                if (hack.getValues().isEmpty()) {
                    continue;
                }
                for (final Value value : hack.getValues()) {
                    if (value instanceof BooleanValue) {
                        value.setValue(jsonObjectHack.get(value.getName()).getAsBoolean());
                    }
                    if (value instanceof NumberValue) {
                        value.setValue(jsonObjectHack.get(value.getName()).getAsDouble());
                    }
                    if (value instanceof ModeValue) {
                        final ModeValue modeValue = (ModeValue)value;
                        for (final Mode mode : modeValue.getModes()) {
                            mode.setToggled(jsonObjectHack.get(mode.getName()).getAsBoolean());
                        }
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public static void loadClickGui_Kendall() {
        try {
            final BufferedReader loadJson = new BufferedReader(new FileReader(FileManager.CLICKGUI));
            final JsonObject json = (JsonObject)FileManager.jsonParser.parse((Reader)loadJson);
            loadJson.close();
            for (final Map.Entry<String, JsonElement> entry : json.entrySet()) {
                final JsonObject jsonData = (JsonObject)entry.getValue();
                final String text = entry.getKey();
                final int posX = jsonData.get("posX").getAsInt();
                final int posY = jsonData.get("posY").getAsInt();
                for (final KendallFrame frame : ClickGui.KendallMyGod.frames) {
                    if (frame.category.name().equals(text)) {
                        frame.x = posX;
                        frame.y = posY;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void saveClickGui_Kendall() {
        try {
            final JsonObject json = new JsonObject();
            for (final KendallFrame frame : ClickGui.KendallMyGod.frames) {
                final JsonObject jsonData = new JsonObject();
                jsonData.addProperty("posX", (Number)frame.x);
                jsonData.addProperty("posY", (Number)frame.y);
                json.add(frame.category.name(), (JsonElement)jsonData);
            }
            final PrintWriter saveJson = new PrintWriter(new FileWriter(FileManager.CLICKGUI));
            saveJson.println(FileManager.gsonPretty.toJson((JsonElement)json));
            saveJson.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void loadFriends() {
        final List<String> friends = read(FileManager.FRIENDS);
        for (final String name : friends) {
            FriendManager.addFriend(name);
        }
    }
    
    public static void loadEnemys() {
        final List<String> enemys = read(FileManager.ENEMYS);
        for (final String name : enemys) {
            EnemyManager.addEnemy(name);
        }
    }
    
    public static void loadPickupFilter() {
        try {
            final BufferedReader loadJson = new BufferedReader(new FileReader(FileManager.PICKUPFILTER));
            final JsonObject json = (JsonObject)FileManager.jsonParser.parse((Reader)loadJson);
            loadJson.close();
            for (final Map.Entry<String, JsonElement> entry : json.entrySet()) {
                final JsonObject jsonData = (JsonObject)entry.getValue();
                final int id = Integer.parseInt(entry.getKey());
                PickupFilterManager.addItem(id);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void savePickupFilter() {
        try {
            final JsonObject json = new JsonObject();
            for (final int id : PickupFilterManager.items) {
                final JsonObject jsonData = new JsonObject();
                jsonData.addProperty("name", Item.getItemById(id).getUnlocalizedName());
                json.add("" + id, (JsonElement)jsonData);
            }
            final PrintWriter saveJson = new PrintWriter(new FileWriter(FileManager.PICKUPFILTER));
            saveJson.println(FileManager.gsonPretty.toJson((JsonElement)json));
            saveJson.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void loadClickGui() {
    }
    
    public static void saveClickGui() {
    }
    
    public static void saveFriends() {
        write(FileManager.FRIENDS, FriendManager.friendsList, true, true);
    }
    
    public static void saveEnemys() {
        write(FileManager.ENEMYS, EnemyManager.enemysList, true, true);
    }
    
    public static void saveHacks() {
        try {
            final JsonObject json = new JsonObject();
            for (final Hack hack : HackManager.getHacks()) {
                final JsonObject jsonHack = new JsonObject();
                jsonHack.addProperty("toggled", hack.isToggled());
                jsonHack.addProperty("key", (Number)hack.getKey());
                if (!hack.getValues().isEmpty()) {
                    for (final Value value : hack.getValues()) {
                        if (value instanceof BooleanValue) {
                            jsonHack.addProperty(value.getName(), (Boolean)value.getValue());
                        }
                        if (value instanceof NumberValue) {
                            jsonHack.addProperty(value.getName(), (Number)value.getValue());
                        }
                        if (value instanceof ModeValue) {
                            final ModeValue modeValue = (ModeValue)value;
                            for (final Mode mode : modeValue.getModes()) {
                                jsonHack.addProperty(mode.getName(), mode.isToggled());
                            }
                        }
                    }
                }
                json.add(hack.getName(), (JsonElement)jsonHack);
            }
            final PrintWriter saveJson = new PrintWriter(new FileWriter(FileManager.HACKS));
            saveJson.println(FileManager.gsonPretty.toJson((JsonElement)json));
            saveJson.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void write(final File outputFile, final List<String> writeContent, final boolean newline, final boolean overrideContent) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile, !overrideContent));
            for (final String outputLine : writeContent) {
                writer.write(outputLine);
                writer.flush();
                if (newline) {
                    writer.newLine();
                }
            }
        }
        catch (Exception ex) {
            try {
                if (writer != null) {
                    writer.close();
                }
            }
            catch (Exception ex2) {}
        }
    }
    
    public static List<String> read(final File inputFile) {
        final ArrayList<String> readContent = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = reader.readLine()) != null) {
                readContent.add(line);
            }
        }
        catch (Exception ex) {
            try {
                if (reader != null) {
                    reader.close();
                }
            }
            catch (Exception ex2) {}
        }
        return readContent;
    }
    
    static {
        FileManager.gsonPretty = new GsonBuilder().setPrettyPrinting().create();
        FileManager.jsonParser = new JsonParser();
        FileManager.GISHCODE_DIR = null;
        FileManager.CONFIG = null;
        FileManager.HACKS = null;
        FileManager.XRAYDATA = null;
        FileManager.PICKUPFILTER = null;
        FileManager.FRIENDS = null;
        FileManager.ENEMYS = null;
        FileManager.CLICKGUI = null;
        FileManager.SKINCHANGER = null;
    }
}
