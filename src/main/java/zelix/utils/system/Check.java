package zelix.utils.system;

import zelix.utils.hooks.visual.*;
import java.io.*;
import java.security.*;

public class Check
{
    public static boolean checkuser() {
        try {
            if (WebUtils.get("https://gitee.com/VortexTeam/Zelix/raw/master/1122HWID.txt").contains(getHWID())) {
                ChatUtils.message("OK!");
                return true;
            }
            ChatUtils.error("verification failed! your uuid is" + getHWID());
            return false;
        }
        catch (NoSuchAlgorithmException e) {
            ChatUtils.error("Network Error!");
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e2) {
            ChatUtils.error("Error");
            e2.printStackTrace();
        }
        catch (IOException e3) {
            ChatUtils.error("Error");
            e3.printStackTrace();
        }
        return true;
    }
    
    public static String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final StringBuilder sb = new StringBuilder();
        final String main = System.getenv("PROCESS_IDENTIFIER") + System.getenv("COMPUTERNAME");
        final byte[] bytes = main.getBytes("UTF-8");
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] md2 = md.digest(bytes);
        int i = 0;
        for (final byte b : md2) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x300), 0, 3);
            if (i != md2.length - 1) {
                sb.append("-");
            }
            ++i;
        }
        return sb.toString();
    }
}
