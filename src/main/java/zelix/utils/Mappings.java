package zelix.utils;

import net.minecraft.client.*;
import net.minecraftforge.fml.relauncher.*;

public class Mappings
{
    public static String timer;
    public static String anti;
    public static String isInWeb;
    public static String registerReloadListener;
    public static String session;
    public static String yaw;
    public static String pitch;
    public static String rightClickDelayTimer;
    public static String getPlayerInfo;
    public static String playerTextures;
    public static String currentGameType;
    public static String connection;
    public static String blockHitDelay;
    public static String curBlockDamageMP;
    public static String isHittingBlock;
    public static String onUpdateWalkingPlayer;
    
    public static boolean isNotObfuscated() {
        try {
            return Minecraft.class.getDeclaredField("instance") != null;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    private static boolean isMCP() {
        try {
            return ReflectionHelper.findField((Class)Minecraft.class, new String[] { "theMinecraft" }) != null;
        }
        catch (Exception var1) {
            return false;
        }
    }
    
    static {
        Mappings.timer = (isMCP() ? "timer" : "timer");
        Mappings.anti = (isMCP() ? "MovementInput" : "movementInput");
        Mappings.isInWeb = (isMCP() ? "isInWeb" : "isInWeb");
        Mappings.registerReloadListener = (isMCP() ? "registerReloadListener" : "registerReloadListener");
        Mappings.session = (isNotObfuscated() ? "session" : "session");
        Mappings.yaw = (isNotObfuscated() ? "yaw" : "yaw");
        Mappings.pitch = (isNotObfuscated() ? "pitch" : "pitch");
        Mappings.rightClickDelayTimer = (isNotObfuscated() ? "rightClickDelayTimer" : "rightClickDelayTimer");
        Mappings.getPlayerInfo = (isNotObfuscated() ? "getPlayerInfo" : "getPlayerInfo");
        Mappings.playerTextures = (isNotObfuscated() ? "playerTextures" : "playerTextures");
        Mappings.currentGameType = (isNotObfuscated() ? "currentGameType" : "currentGameType");
        Mappings.connection = (isNotObfuscated() ? "connection" : "connection");
        Mappings.blockHitDelay = (isNotObfuscated() ? "blockHitDelay" : "blockHitDelay");
        Mappings.curBlockDamageMP = (isNotObfuscated() ? "curBlockDamageMP" : "curBlockDamageMP");
        Mappings.isHittingBlock = (isNotObfuscated() ? "isHittingBlock" : "isHittingBlock");
        Mappings.onUpdateWalkingPlayer = (isNotObfuscated() ? "onUpdateWalkingPlayer" : "onUpdateWalkingPlayer");
    }
}
