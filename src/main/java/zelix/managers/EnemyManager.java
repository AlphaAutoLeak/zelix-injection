package zelix.managers;

import java.util.*;
import zelix.utils.hooks.visual.*;

public class EnemyManager
{
    public static ArrayList<String> enemysList;
    public static ArrayList<String> murders;
    public static ArrayList<String> detects;
    
    public static void addEnemy(final String enemyname) {
        if (!EnemyManager.enemysList.contains(enemyname)) {
            EnemyManager.enemysList.add(enemyname);
            FileManager.saveEnemys();
            ChatUtils.message(enemyname + " Added to ¡ìcenemys ¡ì7list.");
        }
    }
    
    public static void removeEnemy(final String enemyname) {
        if (EnemyManager.enemysList.contains(enemyname)) {
            EnemyManager.enemysList.remove(enemyname);
            FileManager.saveEnemys();
            ChatUtils.message(enemyname + " Removed from ¡ìcenemys ¡ì7list.");
        }
    }
    
    public static void clear() {
        if (!EnemyManager.enemysList.isEmpty()) {
            EnemyManager.enemysList.clear();
            FileManager.saveEnemys();
            ChatUtils.message("¡ìcEnemys ¡ì7list clear.");
        }
    }
    
    static {
        EnemyManager.enemysList = new ArrayList<String>();
        EnemyManager.murders = new ArrayList<String>();
        EnemyManager.detects = new ArrayList<String>();
    }
}
