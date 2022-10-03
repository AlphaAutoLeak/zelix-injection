package zelix.hack.hacks.skinchanger.resources;

import java.io.*;
import com.google.gson.*;

public class BetterJsonObject
{
    private static final Gson prettyPrinter;
    private JsonObject data;
    
    public BetterJsonObject() {
        this.data = new JsonObject();
    }
    
    public BetterJsonObject(final String jsonIn) {
        if (jsonIn == null || jsonIn.isEmpty()) {
            this.data = new JsonObject();
            return;
        }
        try {
            this.data = new JsonParser().parse(jsonIn).getAsJsonObject();
        }
        catch (JsonSyntaxException | JsonIOException ex3) {

            ex3.printStackTrace();
        }
    }
    
    public BetterJsonObject(final JsonObject objectIn) {
        this.data = ((objectIn != null) ? objectIn : new JsonObject());
    }
    
    public String optString(final String key) {
        return this.optString(key, "");
    }
    
    public String optString(final String key, final String value) {
        if (key == null || key.isEmpty() || !this.has(key)) {
            return value;
        }
        final JsonPrimitive primitive = this.asPrimitive(this.get(key));
        if (primitive != null && primitive.isString()) {
            return primitive.getAsString();
        }
        return value;
    }
    
    public int optInt(final String key) {
        return this.optInt(key, 0);
    }
    
    public int optInt(final String key, final int value) {
        if (key == null || key.isEmpty() || !this.has(key)) {
            return value;
        }
        final JsonPrimitive primitive = this.asPrimitive(this.get(key));
        try {
            if (primitive != null && primitive.isNumber()) {
                return primitive.getAsInt();
            }
        }
        catch (NumberFormatException ex) {}
        return value;
    }
    
    public double optDouble(final String key) {
        return this.optDouble(key, 0.0);
    }
    
    public double optDouble(final String key, final double value) {
        if (key == null || key.isEmpty() || !this.has(key)) {
            return value;
        }
        final JsonPrimitive primitive = this.asPrimitive(this.get(key));
        try {
            if (primitive != null && primitive.isNumber()) {
                return primitive.getAsDouble();
            }
        }
        catch (NumberFormatException ex) {}
        return value;
    }
    
    public boolean optBoolean(final String key) {
        return this.optBoolean(key, false);
    }
    
    public boolean optBoolean(final String key, final boolean value) {
        if (key == null || key.isEmpty() || !this.has(key)) {
            return value;
        }
        final JsonPrimitive primitive = this.asPrimitive(this.get(key));
        if (primitive != null && primitive.isBoolean()) {
            return primitive.getAsBoolean();
        }
        return value;
    }
    
    public boolean has(final String key) {
        return this.data.has(key);
    }
    
    public JsonElement get(final String key) {
        return this.data.get(key);
    }
    
    public JsonObject getData() {
        return this.data;
    }
    
    public BetterJsonObject addProperty(final String key, final String value) {
        if (key != null) {
            this.data.addProperty(key, value);
        }
        return this;
    }
    
    public BetterJsonObject addProperty(final String key, final Number value) {
        if (key != null) {
            this.data.addProperty(key, value);
        }
        return this;
    }
    
    public BetterJsonObject addProperty(final String key, final Boolean value) {
        if (key != null) {
            this.data.addProperty(key, value);
        }
        return this;
    }
    
    public BetterJsonObject add(final String key, final BetterJsonObject object) {
        if (key != null) {
            this.data.add(key, (JsonElement)object.getData());
        }
        return this;
    }
    
    public void writeToFile(final File file) {
        if (file == null || (file.exists() && file.isDirectory())) {
            return;
        }
        try {
            if (!file.exists()) {
                final File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
            }
            final FileWriter writer = new FileWriter(file);
            final BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(this.toPrettyString());
            bufferedWriter.close();
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private JsonPrimitive asPrimitive(final JsonElement element) {
        return (element instanceof JsonPrimitive) ? (JsonPrimitive) element : null;
    }
    
    @Override
    public String toString() {
        return this.data.toString();
    }
    
    public String toPrettyString() {
        return BetterJsonObject.prettyPrinter.toJson((JsonElement)this.data);
    }
    
    public Gson getGsonData() {
        return BetterJsonObject.prettyPrinter;
    }
    
    static {
        prettyPrinter = new GsonBuilder().setPrettyPrinting().create();
    }
}
