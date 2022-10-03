package zelix.hack.hacks.skinchanger;

import net.minecraft.util.*;
import zelix.managers.*;
import com.mojang.authlib.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.client.resources.*;
import zelix.hack.hacks.skinchanger.resources.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.*;
import zelix.utils.hooks.visual.*;
import java.io.*;
import java.util.*;

public class CacheRetriever
{
    private static final ResourceLocation undefinedTexture;
    private static final boolean FORCE_HTTPS = true;
    private final HashMap<String, String> cachedValues;
    private final File cacheDirectory;
    
    public CacheRetriever() {
        this.cachedValues = new HashMap<String, String>();
        this.cacheDirectory = FileManager.SKINCHANGER;
        this.genCacheDirectory();
    }
    
    public ResourceLocation loadIntoGame(final String name, final String url) {
        return this.loadIntoGame(name, url, null);
    }
    
    public ResourceLocation loadIntoGame(final String name, String url, final MinecraftProfileTexture.Type type) {
        CacheType cacheType = null;
        if (type == null) {
            cacheType = CacheType.OTHER;
        }
        else {
            cacheType = CacheType.valueOf(type.toString());
        }
        if (cacheType == null) {
            return null;
        }
        final File cacheDirectory = this.getCacheDirForName(name);
        final File cachedFile = (cacheType != CacheType.OTHER) ? this.getCacheFileIfIExists(name, ".png") : null;
        final ResourceLocation location = new ResourceLocation("skins/" + this.getCacheName(name));
        final IImageBuffer buffer = (IImageBuffer)((cacheType == CacheType.CAPE) ? new CapeBuffer() : ((cacheType == CacheType.SKIN) ? new SkinBuffer() : null));
        if (cachedFile != null) {
            Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().renderEngine.loadTexture(location, (ITextureObject)new LocalFileData(DefaultPlayerSkin.getDefaultSkinLegacy(), cachedFile, buffer)));
            return location;
        }
        final File dataFile = new File(cacheDirectory, cacheDirectory.getName() + ".png");
        if (url.startsWith("http://") && !url.contains("optifine")) {
            url = "https://" + url.substring("http://".length());
        }
        final ThreadDownloadImageData imageData = new ThreadDownloadImageData(dataFile, url, (cacheType == CacheType.CAPE) ? new ResourceLocation("skinchanger", "light.png") : DefaultPlayerSkin.getDefaultSkinLegacy(), buffer);
        Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().renderEngine.loadTexture(location, (ITextureObject)imageData));
        if (this.generateCacheFiles(name)) {
            return location;
        }
        return null;
    }
    
    public boolean generateCacheFiles(final String name) {
        final File cacheDirectory = this.getCacheDirForName(name);
        if (this.isCacheExpired(name)) {
            cacheDirectory.delete();
        }
        if (!cacheDirectory.exists() && !cacheDirectory.mkdir()) {
            ChatUtils.error("Failed to create a cache directory.");
            return false;
        }
        final File dataFile = new File(cacheDirectory, cacheDirectory.getName() + ".png");
        final File cacheFile = new File(cacheDirectory, cacheDirectory.getName() + ".lock");
        try {
            if (!dataFile.exists() && !dataFile.createNewFile()) {
                ChatUtils.error("SkinChanger: Failed to create a data file.");
                return false;
            }
            if (!cacheFile.exists()) {
                if (!cacheFile.createNewFile()) {
                    ChatUtils.error("Failed to create a cache file.");
                    return false;
                }
                final FileWriter writer = new FileWriter(cacheFile);
                final BufferedWriter bufferedWriter = new BufferedWriter(writer);
                long expirationTime = System.currentTimeMillis();
                expirationTime += 86400000L;
                bufferedWriter.write(expirationTime + System.lineSeparator());
                bufferedWriter.close();
            }
        }
        catch (Exception ex) {
            ChatUtils.error(ex.getMessage());
            return false;
        }
        return true;
    }
    
    public File getCacheFileIfIExists(final String name, final String extension) {
        if (!extension.startsWith(".")) {
            return null;
        }
        if (!this.doesCacheExist(name) || this.isCacheExpired(name)) {
            return null;
        }
        final String cached_name = this.getCacheName(name);
        final File dir = new File(this.cacheDirectory, cached_name);
        final File data_file = new File(dir, cached_name + extension);
        if (!data_file.exists()) {
            return null;
        }
        if (!data_file.isDirectory()) {
            return data_file;
        }
        if (data_file.delete()) {
            return null;
        }
        return null;
    }
    
    public boolean isCacheExpired(final String name) {
        if (name == null) {
            return true;
        }
        final File fileCache = this.getCacheDirForName(name);
        if (!fileCache.exists()) {
            return true;
        }
        final File cacheLock = new File(fileCache, fileCache.getName() + ".lock");
        if (!cacheLock.exists()) {
            return true;
        }
        try {
            final FileReader fileReader = new FileReader(cacheLock);
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            final String line = bufferedReader.readLine();
            bufferedReader.close();
            final long time = Long.parseLong(line);
            return System.currentTimeMillis() > time;
        }
        catch (IOException ex) {
            ChatUtils.error("SkinChanger: Unable to read cache file for " + name);
            return true;
        }
        catch (NumberFormatException ex2) {
            ChatUtils.error("SkinChanger: Cache file had an invalid number.");
            return true;
        }
    }
    
    public boolean doesCacheExist(final String name) {
        if (!this.genCacheDirectory()) {
            return false;
        }
        final File cacheDirectory = this.getCacheDirForName(name);
        if (!cacheDirectory.exists()) {
            return false;
        }
        final File dataFile = new File(cacheDirectory, cacheDirectory.getName() + ".png");
        final File cacheFile = new File(cacheDirectory, cacheDirectory.getName() + ".lock");
        return dataFile.exists() && dataFile.length() > 2L && cacheFile.exists();
    }
    
    private String getCacheName(String name) {
        name = name.toLowerCase();
        if (this.cachedValues.containsKey(name)) {
            return this.cachedValues.get(name);
        }
        final UUID id = UUID.nameUUIDFromBytes(name.getBytes());
        final String subStrName = name.substring(0, Math.min(7, name.length()));
        final String[] uuidSplit = id.toString().split("-");
        final String idFirstComponent = uuidSplit[0] + uuidSplit[1];
        final String finalCacheName = subStrName + "_" + idFirstComponent;
        this.cachedValues.put(name, finalCacheName);
        return finalCacheName;
    }
    
    private File getCacheDirForName(final String nameOfFile) {
        final String cacheName = this.getCacheName(nameOfFile);
        return new File(this.cacheDirectory, cacheName);
    }
    
    private boolean genCacheDirectory() {
        boolean existed = true;
        if (!this.cacheDirectory.getParentFile().exists()) {
            existed = false;
            if (!this.cacheDirectory.getParentFile().mkdirs()) {
                ChatUtils.error("SkinChanger: Unable to create the mod directory.");
                return false;
            }
        }
        if (!this.cacheDirectory.exists()) {
            existed = false;
            if (!this.cacheDirectory.mkdir()) {
                ChatUtils.error("SkinChanger: Unable to create cache directory.");
                return false;
            }
        }
        return existed;
    }
    
    public File getCacheDirectory() {
        return this.cacheDirectory;
    }
    
    static {
        undefinedTexture = new ResourceLocation("skinchanger", "light.png");
    }
    
    public enum CacheType
    {
        SKIN, 
        CAPE, 
        OTHER;
    }
}
