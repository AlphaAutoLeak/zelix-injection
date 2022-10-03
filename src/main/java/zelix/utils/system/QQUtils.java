package zelix.utils.system;

import java.util.*;
import com.sun.jna.*;
import com.sun.jna.win32.*;

public class QQUtils
{
    private static final String QQ_WINDOW_TEXT_PRE = "qqexchangewnd_shortcut_prefix_";
    private static final User32 user32;
    
    public static Map<String, String> getLoginQQList() {
        final Map<String, String> map = new HashMap<String, String>(5);
        QQUtils.user32.EnumWindows(new User32.WNDENUMPROC() {
            @Override
            public boolean callback(final Pointer hWnd, final Pointer userData) {
                final byte[] windowText = new byte[512];
                QQUtils.user32.GetWindowTextA(hWnd, windowText, 512);
                final String wText = Native.toString(windowText);
                if (_filterQQInfo(wText)) {
                    map.put(hWnd.toString(), wText.substring(wText.indexOf("qqexchangewnd_shortcut_prefix_") + "qqexchangewnd_shortcut_prefix_".length()));
                }
                return true;
            }
        }, null);
        return map;
    }
    
    private static boolean _filterQQInfo(final String windowText) {
        return windowText.startsWith("qqexchangewnd_shortcut_prefix_");
    }
    
    static {
        user32 = User32.INSTANCE;
    }
    
    public interface User32 extends StdCallLibrary
    {
        public static final User32 INSTANCE = (User32)Native.loadLibrary("user32", (Class)User32.class);
        
        boolean EnumWindows(final WNDENUMPROC p0, final Pointer p1);
        
        int GetWindowTextA(final Pointer p0, final byte[] p1, final int p2);
        
        public interface WNDENUMPROC extends StdCallLibrary.StdCallCallback
        {
            boolean callback(final Pointer p0, final Pointer p1);
        }
    }
}
