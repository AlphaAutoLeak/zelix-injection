package zelix.hack.hacks.skinchanger;

import com.mojang.authlib.minecraft.*;
import net.minecraft.client.network.*;
import net.minecraft.util.*;
import zelix.utils.hooks.visual.*;
import zelix.utils.system.*;
import java.util.concurrent.*;
import net.minecraft.client.entity.*;
import java.lang.reflect.*;

public class TextureReflection
{
    public static boolean setTextureForPlayer(final MinecraftProfileTexture.Type type, final NetworkPlayerInfo playerInfo, final ResourceLocation location) {
        if (playerInfo == null) {
            ChatUtils.error("getPlayerInfo is null!");
            return false;
        }
        try {
            final Field playerTextures = NetworkPlayerInfo.class.getDeclaredField(Mapping.playerTextures);
            final boolean accessible = playerTextures.isAccessible();
            playerTextures.setAccessible(true);
            final ConcurrentHashMap<Object, Object> concHashMap = new ConcurrentHashMap<Object, Object>();
            concHashMap.put(type, location);
            playerTextures.set(playerInfo, concHashMap);
            playerTextures.setAccessible(accessible);
            return true;
        }
        catch (Exception ex) {
            ChatUtils.error(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
    
    public static NetworkPlayerInfo getNetworkPlayerInfo(final AbstractClientPlayer player) {
        try {
            final Method method = AbstractClientPlayer.class.getDeclaredMethod(Mapping.getPlayerInfo, (Class<?>[])new Class[0]);
            method.setAccessible(true);
            final NetworkPlayerInfo playerInfo = (NetworkPlayerInfo)method.invoke(player, new Object[0]);
            return playerInfo;
        }
        catch (Exception ex) {
            ChatUtils.error(ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
}
