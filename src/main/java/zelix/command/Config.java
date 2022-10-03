package zelix.command;

import zelix.*;
import zelix.utils.hooks.visual.*;
import zelix.gui.cloudconfig.*;
import zelix.managers.*;
import zelix.hack.*;
import zelix.value.*;
import java.util.*;
import java.net.*;
import org.lwjgl.opengl.*;
import java.io.*;
import com.google.gson.*;

public class Config extends Command
{
    public static File configs;
    public static URL configurl;
    private static JsonParser jsonParser;
    private static Gson gsonPretty;
    
    public Config() {
        super("config");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        if (args[0].equals("reload")) {
            Core.fileManager = new FileManager();
        }
        else if (args[0].equals("save") && args[1] != null) {
            if (!FileManager.CONFIG.exists()) {
                FileManager.CONFIG.mkdir();
            }
            Config.configs = new File(FileManager.CONFIG, args[1] + ".json");
            saveHacks();
            ChatUtils.message("Successfully Save Config");
        }
        else if (args[0].equals("load") && args[1] != null) {
            Config.configs = new File(FileManager.CONFIG, args[1] + ".json");
            if (Config.configs.exists()) {
                loadHacks();
                ChatUtils.message("Successfully Load Config");
            }
            else {
                ChatUtils.message("NoFound Config");
            }
        }
        else if (args[0].equals("upload") && args[1] != null) {
            CloudConfig.UploadConfig(args[1]);
        }
    }
    
    @Override
    public String getDescription() {
        return "load cloud/local config";
    }
    
    @Override
    public String getSyntax() {
        return "config";
    }
    
    public static void saveHacks() {
        try {
            final JsonObject json = new JsonObject();
            for (final Hack module : HackManager.getHacks()) {
                final JsonObject jsonHack = new JsonObject();
                jsonHack.addProperty("toggled", module.isToggled());
                jsonHack.addProperty("key", (Number)module.getKey());
                if (!module.getValues().isEmpty()) {
                    for (final Value value : module.getValues()) {
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
                json.add(module.getName(), (JsonElement)jsonHack);
            }
            final PrintWriter saveJson = new PrintWriter(new FileWriter(Config.configs));
            saveJson.println(Config.gsonPretty.toJson((JsonElement)json));
            saveJson.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void loadHacks() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(Config.configs));
            final JsonObject jsonObject = (JsonObject)Config.jsonParser.parse((Reader)bufferedReader);
            bufferedReader.close();
            for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                final Hack module = HackManager.getHack(entry.getKey());
                if (module == null) {
                    continue;
                }
                final JsonObject jsonObjectHack = (JsonObject)entry.getValue();
                module.setKey(jsonObjectHack.get("key").getAsInt());
                module.setToggled(jsonObjectHack.get("toggled").getAsBoolean());
                if (module.getValues().isEmpty()) {
                    continue;
                }
                for (final Value value : module.getValues()) {
                    if (jsonObjectHack.get(value.getName()) == null) {
                        if (value instanceof BooleanValue) {
                            jsonObjectHack.addProperty(value.getName(), (Boolean)value.getValue());
                        }
                        if (value instanceof NumberValue) {
                            jsonObjectHack.addProperty(value.getName(), (Number)value.getValue());
                        }
                        if (value instanceof ModeValue) {
                            jsonObjectHack.addProperty(value.getName(), (Boolean)value.getValue());
                        }
                        saveHacks();
                    }
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
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void loadCloudHacks() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Config.configurl.openStream()));
            final JsonObject jsonObject = (JsonObject)Config.jsonParser.parse((Reader)bufferedReader);
            bufferedReader.close();
            for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                final Hack module = HackManager.getHack(entry.getKey());
                if (module == null) {
                    continue;
                }
                final JsonObject jsonObjectHack = (JsonObject)entry.getValue();
                module.setKey(jsonObjectHack.get("key").getAsInt());
                module.setToggled(jsonObjectHack.get("toggled").getAsBoolean());
                if (module.getValues().isEmpty()) {
                    continue;
                }
                for (final Value value : module.getValues()) {
                    if (jsonObjectHack.get(value.getName()) == null) {
                        if (value instanceof BooleanValue) {
                            jsonObjectHack.addProperty(value.getName(), (Boolean)value.getValue());
                        }
                        if (value instanceof NumberValue) {
                            jsonObjectHack.addProperty(value.getName(), (Number)value.getValue());
                        }
                        if (value instanceof ModeValue) {
                            jsonObjectHack.addProperty(value.getName(), (Boolean)value.getValue());
                        }
                        saveHacks();
                    }
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
            ChatUtils.message("Successfully Load Cloud Config");
        }
        catch (Exception e) {
            ChatUtils.error("Failed To Load Cloud Config");
            e.printStackTrace();
        }
    }
    
    public static void saveCloudHacks() {
        try {
            final JsonObject json = new JsonObject();
            for (final Hack module : HackManager.getHacks()) {
                final JsonObject jsonHack = new JsonObject();
                jsonHack.addProperty("toggled", module.isToggled());
                jsonHack.addProperty("key", (Number)module.getKey());
                if (!module.getValues().isEmpty()) {
                    for (final Value value : module.getValues()) {
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
                json.add(module.getName(), (JsonElement)jsonHack);
            }
            final String ConfigMain = json.toString();
            Send("localhost", 19731, "[Target][SaveConfig][String][" + ConfigMain + "]");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String Send(final String IP, final int Port, final String Message) {
        try {
            final Socket socket = new Socket(IP, Port);
            final OutputStream ops = socket.getOutputStream();
            final OutputStreamWriter opsw = new OutputStreamWriter(ops);
            final BufferedWriter bw = new BufferedWriter(opsw);
            bw.write(Message);
            bw.flush();
            final InputStream ips = socket.getInputStream();
            final InputStreamReader ipsr = new InputStreamReader(ips);
            final BufferedReader br = new BufferedReader(ipsr);
            String s = null;
            if ((s = br.readLine()) != null) {
                return s;
            }
            socket.close();
        }
        catch (Exception e) {
            Display.setTitle("Failed Connect to The Server(0x66FF)");
            e.printStackTrace();
        }
        return null;
    }
    
    static {
        Config.jsonParser = new JsonParser();
        Config.gsonPretty = new GsonBuilder().setPrettyPrinting().create();
    }
}
