package zelix.otherhacks.net.wurstclient.forge.utils;

import com.google.gson.*;

public final class JsonUtils
{
    public static final Gson gson;
    public static final Gson prettyGson;
    public static final JsonParser jsonParser;
    
    static {
        gson = new Gson();
        prettyGson = new GsonBuilder().setPrettyPrinting().create();
        jsonParser = new JsonParser();
    }
}
