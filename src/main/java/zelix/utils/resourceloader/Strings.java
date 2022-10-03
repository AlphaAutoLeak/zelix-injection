package zelix.utils.resourceloader;

import zelix.*;
import zelix.managers.*;
import java.io.*;

public class Strings
{
    public static String Chinese_Hacks;
    public static String Chinese_HackCategory;
    public static String Chinese_ModeValuesMappings;
    
    public static void main(final String[] args) {
        loadTranslation();
    }
    
    public static void loadTranslation() {
        try {
            final String Translations = "AimAssist=\u81ea\u7784\nAntiHunger=\u53cd\u9965\u997f\nCloudConfig=\u4e91\u914d\u7f6e\nAntiAfk=\u9632\u6302\u673a\nAntiBot=\u9632\u5047\u4eba\nNoFall=\u65e0\u6389\u843d\u4f24\u5bb3\nAntiRain=\u65e0\u96e8\u5929\nAntiWeb=\u65e0\u89c6\u8718\u86db\u7f51\nArmorHUD=\u88c5\u5907\u663e\u793a\nAttackPacketCIA=CIA\u5305\u653b\u51fb\nAttackPacketCW=CW\u5305\u653b\u51fb\nAutoArmor=\u81ea\u52a8\u88c5\u5907\nAutoEat=\u81ea\u52a8\u5403\nAutoShield=\u81ea\u52a8\u9632\u780d\nAutoSoup=\u81ea\u52a8\u559d\u6c64\nAutoSprint=\u81ea\u52a8\u75be\u8dd1\nAutoStep=\u81ea\u52a8\u53f0\u9636\nAutoSwim=\u81ea\u52a8\u6e38\u6cf3\nAutoTool=\u81ea\u52a8\u5de5\u5177\nAutoTotem=\u81ea\u52a8\u56fe\u817e\nAutoWalk=\u81ea\u52a8\u884c\u8d70\nBedNuker=\u81ea\u52a8\u6316\u5e8a\nBlink=\u77ac\u79fb\nBlinkAttack=\u77ac\u79fb\u653b\u51fb\nBlockOverlay=\u65b9\u5757\u8fb9\u6846\nBowAimBot=\u5f13\u81ea\u7784\nChestESP=\u663e\u793a\u7bb1\u5b50\nChestStealer=\u7bb1\u5b50\u5c0f\u5077\nClickGui=\u70b9\u51fb\u754c\u9762\nCommandFrame=\u6307\u4ee4\u7a97\u53e3\nCommandGetter=\u6307\u4ee4\u83b7\u53d6\u5668\nCriticals=\u5200\u5200\u66b4\u51fb\nDisabler=\u5173\u6389\u53cd\u4f5c\u5f0a\nDisconnect=\u81ea\u52a8\u9000\u51fa\nEnemys=\u654c\u4eba\nEntityESP=\u663e\u793a\u751f\u7269\nFakeCreative=\u5047\u521b\u9020\nAntiFall=\u9632\u6389\u843d\u865a\u7a7a\nFastBreak=\u5feb\u901f\u6316\u6398\nFastLadder=\u5feb\u901f\u68af\u5b50\nFireballReturn=\u81ea\u52a8\u5f39\u706b\u7403\nFlight=\u98de\u884c\nFreeCam=\u7075\u9b42\u51fa\u7a8d\nGhost=\u9b3c\nGhostMode=\u5173\u6389\u6240\u6709\u6a21\u5757\nGlide=\u6ed1\u884c\nGlowing=\u9ad8\u4eae\u663e\u793a\nGuiWalk=\u80cc\u5305\u884c\u8d70\nCilp=\u9ad8\u8df3\nHitBox=\u547d\u4e2d\u6846\nHUD=\u663e\u793a\nInteractClick=\u4e92\u52a8\u70b9\u51fb\nItemESP=\u663e\u793a\u7269\u54c1\nKillAura=\u6740\u622e\u5149\u73af\nLongJump=\u8fdc\u8df3\nNightVision=\u591c\u89c6\nNoSlow=\u65e0\u51cf\u901f\nNoSwing=\u65e0\u6325\u821e\nNuker=\u81ea\u52a8\u6316\u6398\nPacketFilter=\u5305\u8fc7\u6ee4\u5668\nParkour=\u8dd1\u9177\nPickupFilter=\u8fc7\u6ee4\u6389\u843d\u7269\nPlayerRadar=\u73a9\u5bb6\u96f7\u8fbe\nPluginsGetter=\u83b7\u53d6\u63d2\u4ef6\nPortalGodMode=\u4f20\u9001\u95e8\u795e\nProfiler=\u8be6\u7ec6\u663e\u793a\nRage=\u8f6c\u5934\nReach=\u8ddd\u79bb\nScaffold=\u81ea\u52a8\u642d\u8def\nSearch=\u77ff\u7269\u641c\u7d22\nSelfDamage=\u81ea\u4f24\nSelfKick=\u81ea\u8e22\nSkinChanger=\u76ae\u80a4\u66f4\u6362\u5668\nSkinStealer=\u76ae\u80a4\u5c0f\u5077\nSpeed=\u901f\u5ea6\nSpider=\u8718\u86db\nSuicide=\u81ea\u6740\nSuperKick=\u8d85\u7ea7\u51fb\u9000\nTargets=\u76ee\u6807\nTeams=\u56e2\u961f\nTeleport=\u4f20\u9001\nTestHack=\u6d4b\u8bd5\nTimer=\u53d8\u901f\u9f7f\u8f6e\nTpAura=\u767e\u7c73\u5927\u5200\nTracers=\u8fde\u7ebf\u73a9\u5bb6\nTrajectories=\u629b\u7269\u7ebf\nTrigger=\u81ea\u52a8\u6253\u4eba\nVelocity=\u53cd\u51fb\u9000\nWallHack=\u9694\u5899\u7ed8\u5236\nXRay=\u77ff\u900f\nSafeWalk=\u5b89\u5168\u884c\u8d70\nAntiSneak=\u53cd\u6f5c\u884c\nIRCChat=\u804a\u5929\u5ba4\nAutoClicker=\u81ea\u52a8\u70b9\u51fb\nFastBow=\u5feb\u901f\u5f13\u7bad\nFastUse=\u5feb\u901f\u4f7f\u7528\nFastPlace=\u81ea\u52a8\u70b9\u51fb\nItemTeleport=\u7269\u54c1\u4f20\u9001\nEnemyInfo=\u654c\u4eba\u4fe1\u606f\nServerCrasher=\u670d\u52a1\u5668\u5d29\u6e83\u5668\nAntiItemLag=\u53cd\u7269\u54c1\u5361\u987f\nSelfDestruct=\u81ea\u6bc1\nAttackSpeed=\u653b\u51fb\u901f\u5ea6\nHighJump=\u9ad8\u8df3\nAutoMine=\u81ea\u52a8\u6316\u77ff@@@@@@@\u73a9\u5bb6\u7c7b=\u663e\u793a\u7c7b=\u6218\u6597\u7c7b=\u5176\u4ed6\u7c7b=\u79fb\u52a8\u7c7b=\n@@@@@@@Mode=\u6a21\u5f0f\nCPS=\u70b9\u51fb\u6570\nReach=\u8ddd\u79bb\nMin=\u6700\u5c0f\nMax=\u6700\u5927";
            final String[] STR = Translations.split("@@@@@@@");
            Strings.Chinese_Hacks = STR[0];
            Strings.Chinese_HackCategory = STR[1];
            Strings.Chinese_ModeValuesMappings = STR[2];
            final String[] translations = Strings.Chinese_Hacks.split("\n");
            final String[] translationsValue = Strings.Chinese_ModeValuesMappings.split("\n");
            for (int i = 0; i < translations.length; ++i) {
                final String[] Mappings = translations[i].split("=");
                final HackManager hackManager = Core.hackManager;
                if (HackManager.getHack(Mappings[0]) != null) {
                    final HackManager hackManager2 = Core.hackManager;
                    HackManager.getHack(Mappings[0]).setChinese(Mappings[1]);
                }
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    public static String readToString(final String fileName) {
        final String encoding = "UTF-8";
        final File file = new File(fileName);
        final Long filelength = file.length();
        final byte[] filecontent = new byte[(int)(Object)filelength];
        try {
            final FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        }
        catch (UnsupportedEncodingException e3) {
            System.err.println("The OS does not support " + encoding);
            e3.printStackTrace();
            return null;
        }
    }
    
    static {
        Strings.Chinese_Hacks = null;
        Strings.Chinese_HackCategory = null;
        Strings.Chinese_ModeValuesMappings = null;
    }
}
