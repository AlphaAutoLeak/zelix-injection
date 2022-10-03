package zelix.managers;

import zelix.hack.hacks.skinchanger.resources.*;
import net.minecraft.util.*;
import com.mojang.authlib.minecraft.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.network.*;
import zelix.utils.hooks.visual.*;
import zelix.hack.*;
import zelix.utils.*;
import zelix.hack.hacks.skinchanger.*;
import net.minecraft.client.resources.*;
import net.minecraft.client.*;
import java.nio.charset.*;
import com.google.gson.*;
import java.util.*;
import java.net.*;
import org.apache.commons.io.*;
import com.google.common.collect.*;

public class SkinChangerManager
{
    private static final HashMap<String, String> responses;
    private static final LinkedHashMap<String, String[]> idCaches;
    private static final LinkedHashMap<String, Boolean> slimSkins;
    private static final LinkedHashMap<String, BetterJsonObject> idEncryptedTextures;
    private static final LinkedHashMap<String, ResourceLocation> skins;
    public static Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures;
    private static HashMap<AbstractClientPlayer, NetworkPlayerInfo> cachedPlayerInfo;
    private static CacheRetriever cacheRetriever;
    private static final String[] TRUSTED_DOMAINS;
    
    public static void addTexture(final MinecraftProfileTexture.Type type, final String content) {
        final boolean loaded = loadTexture(type, content);
        if (loaded) {
            ChatUtils.message(String.format("¡ì7TYPE: ¡ì3%s ¡ì7CONTENT: ¡ì3%s ¡ì7- ADDED.", type.toString(), content));
            final Hack hack = HackManager.getHack("SkinChanger");
            if (hack.isToggled()) {
                hack.onEnable();
            }
        }
        else {
            ChatUtils.error("SkinChanger: Failed load texture!");
        }
    }
    
    private static boolean loadTexture(final MinecraftProfileTexture.Type type, final String content) {
        String name = type.toString().substring(0, 1);
        for (int i = 0; i < 6; ++i) {
            name += Utils.random(0, 9);
        }
        ResourceLocation resource = null;
        if (content.startsWith("http://") || content.startsWith("https://")) {
            resource = SkinChangerManager.cacheRetriever.loadIntoGame(name, content, type);
        }
        else if (!content.contains("/")) {
            if (!content.contains("\\")) {
                final String playerId = getIdFromUsername(content);
                String userName = getRealNameFromName(content);
                if (userName == null) {
                    userName = content;
                }
                final String finalUserName = userName;
                final ResourceLocation resourceLocation = getSkinFromId(playerId);
                final boolean hasSlimSkin = hasSlimSkin(finalUserName);
                resource = resourceLocation;
            }
        }
        if (resource == null) {
            return false;
        }
        SkinChangerManager.playerTextures.put(type, resource);
        return true;
    }
    
    public static void removeTexture(final MinecraftProfileTexture.Type type) {
        SkinChangerManager.playerTextures.remove(type);
    }
    
    public static void clear() {
        SkinChangerManager.playerTextures.clear();
    }
    
    public static boolean setTexture(final MinecraftProfileTexture.Type type, final AbstractClientPlayer player, final ResourceLocation location) {
        final NetworkPlayerInfo playerInfo = getPlayerInfo(player);
        return TextureReflection.setTextureForPlayer(type, playerInfo, location);
    }
    
    private static NetworkPlayerInfo getPlayerInfo(final AbstractClientPlayer player) {
        if (SkinChangerManager.cachedPlayerInfo.containsKey(player)) {
            return SkinChangerManager.cachedPlayerInfo.get(player);
        }
        final NetworkPlayerInfo playerInfo = TextureReflection.getNetworkPlayerInfo(player);
        if (playerInfo != null) {
            SkinChangerManager.cachedPlayerInfo.put(player, playerInfo);
        }
        return playerInfo;
    }
    
    public static String getRealNameFromName(final String nameIn) {
        if (nameIn == null) {
            return null;
        }
        if (SkinChangerManager.idCaches.containsKey(nameIn)) {
            return SkinChangerManager.idCaches.get(nameIn)[1];
        }
        getIdFromUsername(nameIn);
        if (SkinChangerManager.idCaches.get(nameIn) == null) {
            return null;
        }
        return SkinChangerManager.idCaches.get(nameIn)[1];
    }
    
    public static ResourceLocation getSkinFromId(final String id) {
        try {
            return getSkinFromIdUnsafe(id);
        }
        catch (IllegalArgumentException ex) {
            if (ex.getMessage() != null) {
                System.err.println(ex.getMessage());
            }
            return DefaultPlayerSkin.getDefaultSkinLegacy();
        }
    }
    
    private static ResourceLocation getSkinFromIdUnsafe(final String id) {
        if (id == null || id.isEmpty()) {
            return DefaultPlayerSkin.getDefaultSkinLegacy();
        }
        if (SkinChangerManager.skins.containsKey(id)) {
            final ResourceLocation loc = SkinChangerManager.skins.get(id);
            if (Minecraft.getMinecraft().getTextureManager().getTexture(loc) != null) {
                return loc;
            }
            SkinChangerManager.skins.remove(id);
        }
        final JsonObject realTextures = getEncryptedTexturesUnsafe(id).getData();
        if (!realTextures.has("SKIN")) {
            SkinChangerManager.skins.put(id, DefaultPlayerSkin.getDefaultSkinLegacy());
            return DefaultPlayerSkin.getDefaultSkinLegacy();
        }
        final JsonObject skinData = realTextures.get("SKIN").getAsJsonObject();
        if (!skinData.has("url")) {
            SkinChangerManager.skins.put(id, DefaultPlayerSkin.getDefaultSkinLegacy());
            return DefaultPlayerSkin.getDefaultSkinLegacy();
        }
        final String url = skinData.get("url").getAsString();
        if (!isTrustedDomain(url)) {
            throw new IllegalArgumentException("Invalid payload, the domain issued was not trusted.");
        }
        final ResourceLocation playerSkin = SkinChangerManager.cacheRetriever.loadIntoGame(id, url, MinecraftProfileTexture.Type.SKIN);
        SkinChangerManager.skins.put(id, playerSkin);
        return playerSkin;
    }
    
    private static boolean isTrustedDomain(final String url) {
        if (url == null) {
            return false;
        }
        URI uri;
        try {
            uri = new URI(url);
        }
        catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL '" + url + "'");
        }
        final String host = uri.getHost();
        for (final String domain : SkinChangerManager.TRUSTED_DOMAINS) {
            if (host.endsWith(domain)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean hasSlimSkin(final String id) {
        try {
            return hasSlimSkinUnsafe(id);
        }
        catch (NullPointerException | JsonParseException | IllegalStateException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static boolean hasSlimSkinUnsafe(final String id) throws NullPointerException, IllegalStateException, JsonParseException {
        if (id == null) {
            return false;
        }
        if (SkinChangerManager.slimSkins.containsKey(id)) {
            return SkinChangerManager.slimSkins.get(id);
        }
        final JsonObject realTextures = getEncryptedTexturesUnsafe(id).getData();
        if (!realTextures.has("SKIN")) {
            SkinChangerManager.slimSkins.put(id, false);
            return false;
        }
        final JsonObject skinData = realTextures.get("SKIN").getAsJsonObject();
        if (skinData.has("metadata")) {
            final JsonObject metaData = skinData.get("metadata").getAsJsonObject();
            SkinChangerManager.slimSkins.put(id, metaData.has("model"));
            return metaData.has("model") && metaData.get("model").getAsString().equals("slim");
        }
        SkinChangerManager.slimSkins.put(id, false);
        return false;
    }
    
    private static BetterJsonObject getEncryptedTexturesUnsafe(final String id) throws IllegalStateException, JsonParseException {
        if (id == null) {
            return new BetterJsonObject();
        }
        if (SkinChangerManager.idEncryptedTextures.containsKey(id)) {
            return SkinChangerManager.idEncryptedTextures.get(id);
        }
        final BetterJsonObject texturesIn = getTexturesFromId(id);
        if (!texturesIn.has("properties") || !texturesIn.get("properties").isJsonArray()) {
            return new BetterJsonObject();
        }
        final JsonArray propertyArray = texturesIn.get("properties").getAsJsonArray();
        for (final JsonElement propertyElement : propertyArray) {
            if (!propertyElement.isJsonObject()) {
                continue;
            }
            final JsonObject property = propertyElement.getAsJsonObject();
            if (!property.has("name") || !property.get("name").getAsString().equals("textures") || !property.has("value")) {
                continue;
            }
            final byte[] decoded = Base64.getDecoder().decode(property.get("value").getAsString());
            final JsonObject decodedObj = new JsonParser().parse(new String(decoded, StandardCharsets.UTF_8)).getAsJsonObject();
            if (decodedObj.has("textures") && decodedObj.has("profileId") && decodedObj.get("profileId").getAsString().equals(texturesIn.get("id").getAsString())) {
                SkinChangerManager.idEncryptedTextures.put(id, new BetterJsonObject(decodedObj.get("textures").getAsJsonObject()));
                return SkinChangerManager.idEncryptedTextures.get(id);
            }
        }
        SkinChangerManager.idEncryptedTextures.put(id, new BetterJsonObject());
        return SkinChangerManager.idEncryptedTextures.get(id);
    }
    
    public static BetterJsonObject getTexturesFromId(String id) {
        if (id == null || id.isEmpty()) {
            return new BetterJsonObject();
        }
        id = id.replace("-", "");
        return new BetterJsonObject(getUrl("https://sessionserver.mojang.com/session/minecraft/profile/" + id));
    }
    
    public static String getIdFromUsername(final String nameIn) {
        if (nameIn == null) {
            return null;
        }
        if (SkinChangerManager.idCaches.containsKey(nameIn)) {
            return SkinChangerManager.idCaches.get(nameIn)[0];
        }
        if (nameIn.isEmpty()) {
            SkinChangerManager.idCaches.put("", new String[] { "", "" });
            return "";
        }
        final BetterJsonObject profile = getProfileFromUsername(nameIn);
        if (profile.has("success") && !profile.get("success").getAsBoolean()) {
            SkinChangerManager.idCaches.put(nameIn, new String[] { "", "" });
            return "";
        }
        if (profile.has("id")) {
            SkinChangerManager.idCaches.put(nameIn, new String[] { profile.get("id").getAsString(), profile.get("name").getAsString() });
            return profile.get("id").getAsString();
        }
        SkinChangerManager.idCaches.put(nameIn, new String[] { "", "" });
        return "";
    }
    
    public static BetterJsonObject getProfileFromUsername(final String name) {
        if (name == null || name.isEmpty()) {
            return new BetterJsonObject();
        }
        return new BetterJsonObject(getUrl("https://api.mojang.com/users/profiles/minecraft/" + name));
    }
    
    public static final String getUrl(final String url) {
        if (SkinChangerManager.responses.containsKey(url)) {
            return SkinChangerManager.responses.get(url);
        }
        String response;
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL(url.replace(" ", "%20")).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (compatible; SkinChanger; 3.0.1) Chrome/83.0.4103.116");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);
            response = IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            final JsonObject object = new JsonObject();
            object.addProperty("success", false);
            object.addProperty("cause", "Exception");
            object.addProperty("message", (e.getMessage() != null) ? "" : e.getMessage());
            response = object.toString();
        }
        SkinChangerManager.responses.put(url, response);
        return response;
    }
    
    static {
        responses = new HashMap<String, String>();
        idCaches = new LinkedHashMap<String, String[]>();
        slimSkins = new LinkedHashMap<String, Boolean>();
        idEncryptedTextures = new LinkedHashMap<String, BetterJsonObject>();
        skins = new LinkedHashMap<String, ResourceLocation>();
        SkinChangerManager.playerTextures = (Map<MinecraftProfileTexture.Type, ResourceLocation>)Maps.newEnumMap((Class)MinecraftProfileTexture.Type.class);
        SkinChangerManager.cachedPlayerInfo = new HashMap<AbstractClientPlayer, NetworkPlayerInfo>();
        SkinChangerManager.cacheRetriever = new CacheRetriever();
        TRUSTED_DOMAINS = new String[] { ".minecraft.net", ".mojang.com" };
    }
}
