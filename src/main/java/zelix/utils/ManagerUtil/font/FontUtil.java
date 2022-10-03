package zelix.utils.ManagerUtil.font;

import java.awt.*;
import java.util.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import java.io.*;

public class FontUtil
{
    public static volatile int completed;
    public static final String BUG = "a";
    public static final String LIST = "b";
    public static final String BOMB = "c";
    public static final String EYE = "d";
    public static final String PERSON = "e";
    public static final String WHEELCHAIR = "f";
    public static final String SCRIPT = "g";
    public static final String SKIP_LEFT = "h";
    public static final String PAUSE = "i";
    public static final String PLAY = "j";
    public static final String SKIP_RIGHT = "k";
    public static final String SHUFFLE = "l";
    public static final String INFO = "m";
    public static final String SETTINGS = "n";
    public static final String CHECKMARK = "o";
    public static final String XMARK = "p";
    public static final String TRASH = "q";
    public static final String WARNING = "r";
    public static final String FOLDER = "s";
    public static final String LOAD = "t";
    public static final String SAVE = "u";
    public static MinecraftFontRenderer tenacityFont14;
    public static MinecraftFontRenderer tenacityFont16;
    public static MinecraftFontRenderer tenacityBoldFont16;
    public static MinecraftFontRenderer tenacityFont18;
    public static MinecraftFontRenderer tenacityBoldFont18;
    public static MinecraftFontRenderer tenacityFont20;
    public static MinecraftFontRenderer tenacityBoldFont20;
    public static MinecraftFontRenderer tenacityFont22;
    public static MinecraftFontRenderer tenacityBoldFont22;
    public static MinecraftFontRenderer tenacityFont24;
    public static MinecraftFontRenderer tenacityBoldFont26;
    public static MinecraftFontRenderer tenacityFont28;
    public static MinecraftFontRenderer tenacityFont32;
    public static MinecraftFontRenderer tenacityBoldFont32;
    public static MinecraftFontRenderer tenacityFont40;
    public static MinecraftFontRenderer tenacityBoldFont40;
    public static MinecraftFontRenderer neverlose22;
    public static MinecraftFontRenderer iconFont16;
    public static MinecraftFontRenderer iconFont20;
    public static MinecraftFontRenderer iconFont26;
    public static MinecraftFontRenderer iconFont35;
    public static MinecraftFontRenderer iconFont40;
    public static MinecraftFontRenderer arial20;
    public static Font tenacityFont14_;
    public static Font tenacityFont16_;
    public static Font tenacityBoldFont16_;
    public static Font tenacityFont18_;
    public static Font tenacityBoldFont18_;
    public static Font tenacityFont20_;
    public static Font tenacityBoldFont20_;
    public static Font tenacityFont22_;
    public static Font tenacityBoldFont22_;
    public static Font tenacityFont24_;
    public static Font tenacityBoldFont26_;
    public static Font tenacityFont28_;
    public static Font tenacityFont32_;
    public static Font tenacityBoldFont32_;
    public static Font tenacityFont40_;
    public static Font tenacityBoldFont40_;
    public static Font neverlose22_;
    public static Font iconFont16_;
    public static Font iconFont20_;
    public static Font iconFont26_;
    public static Font iconFont35_;
    public static Font iconFont40_;
    public static Font arial20_;
    
    public static Font getFont(final Map<String, Font> locationMap, final String location, final int size) {
        Font font;
        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(0, size);
            }
            else {
                final InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Tenacity/Fonts/" + location)).getInputStream();
                locationMap.put(location, font = Font.createFont(0, is));
                font = font.deriveFont(0, size);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, 10);
        }
        return font;
    }
    
    public static boolean hasLoaded() {
        return FontUtil.completed >= 3;
    }
}
