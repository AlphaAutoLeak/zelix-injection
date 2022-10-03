package zelix.utils.system;

import java.awt.*;
import java.awt.datatransfer.*;

public class ClipBoardUtils
{
    public static void setSysClipboardText(final String writeMe) {
        final Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        final Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }
}
