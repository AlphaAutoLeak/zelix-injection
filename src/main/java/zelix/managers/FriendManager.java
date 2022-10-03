package zelix.managers;

import java.util.*;
import zelix.utils.hooks.visual.*;

public class FriendManager
{
    public static ArrayList<String> friendsList;
    
    public static void addFriend(final String friendname) {
        if (!FriendManager.friendsList.contains(friendname)) {
            FriendManager.friendsList.add(friendname);
            FileManager.saveFriends();
            ChatUtils.message(friendname + " Added to ��bfreinds ��7list.");
        }
    }
    
    public static void removeFriend(final String friendname) {
        if (FriendManager.friendsList.contains(friendname)) {
            FriendManager.friendsList.remove(friendname);
            FileManager.saveFriends();
            ChatUtils.message(friendname + " Removed from ��bfriends ��7list.");
        }
    }
    
    public static void clear() {
        if (!FriendManager.friendsList.isEmpty()) {
            FriendManager.friendsList.clear();
            FileManager.saveFriends();
            ChatUtils.message("��bFriends ��7list clear.");
        }
    }
    
    static {
        FriendManager.friendsList = new ArrayList<String>();
    }
}
