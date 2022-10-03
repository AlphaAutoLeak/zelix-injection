package zelix.utils.system;

import java.util.regex.*;
import net.minecraftforge.fml.relauncher.*;
import java.util.*;

public enum EnumChatFormatting
{
    BLACK('0'), 
    DARK_BLUE('1'), 
    DARK_GREEN('2'), 
    DARK_AQUA('3'), 
    DARK_RED('4'), 
    DARK_PURPLE('5'), 
    GOLD('6'), 
    GRAY('7'), 
    DARK_GRAY('8'), 
    BLUE('9'), 
    GREEN('a'), 
    AQUA('b'), 
    RED('c'), 
    LIGHT_PURPLE('d'), 
    YELLOW('e'), 
    WHITE('f'), 
    OBFUSCATED('k', true), 
    BOLD('l', true), 
    STRIKETHROUGH('m', true), 
    UNDERLINE('n', true), 
    ITALIC('o', true), 
    RESET('r');
    
    private static final Map formattingCodeMapping;
    private static final Map nameMapping;
    private static final Pattern formattingCodePattern;
    private final char formattingCode;
    private final boolean fancyStyling;
    private final String controlString;
    private static final String __OBFID = "CL_00000342";
    
    private EnumChatFormatting(final char p_i1336_3_) {
        this(p_i1336_3_, false);
    }
    
    private EnumChatFormatting(final char p_i1337_3_, final boolean p_i1337_4_) {
        this.formattingCode = p_i1337_3_;
        this.fancyStyling = p_i1337_4_;
        this.controlString = "¡ì" + p_i1337_3_;
    }
    
    public char getFormattingCode() {
        return this.formattingCode;
    }
    
    public boolean isFancyStyling() {
        return this.fancyStyling;
    }
    
    public boolean isColor() {
        return !this.fancyStyling && this != EnumChatFormatting.RESET;
    }
    
    public String getFriendlyName() {
        return this.name().toLowerCase();
    }
    
    @Override
    public String toString() {
        return this.controlString;
    }
    
    @SideOnly(Side.CLIENT)
    public static String getTextWithoutFormattingCodes(final String text) {
        return (text == null) ? null : EnumChatFormatting.formattingCodePattern.matcher(text).replaceAll("");
    }
    
    public static EnumChatFormatting getValueByName(final String friendlyName) {
        return (friendlyName == null) ? null : (EnumChatFormatting) EnumChatFormatting.nameMapping.get(friendlyName.toLowerCase());
    }
    
    public static Collection getValidValues(final boolean p_96296_0_, final boolean p_96296_1_) {
        final ArrayList arraylist = new ArrayList();
        for (final EnumChatFormatting enumchatformatting : values()) {
            if ((!enumchatformatting.isColor() || p_96296_0_) && (!enumchatformatting.isFancyStyling() || p_96296_1_)) {
                arraylist.add(enumchatformatting.getFriendlyName());
            }
        }
        return arraylist;
    }
    
    static {
        formattingCodeMapping = new HashMap();
        nameMapping = new HashMap();
        formattingCodePattern = Pattern.compile("(?i)" + String.valueOf('¡ì') + "[0-9A-FK-OR]");
        for (final EnumChatFormatting var4 : values()) {
            EnumChatFormatting.formattingCodeMapping.put(var4.getFormattingCode(), var4);
            EnumChatFormatting.nameMapping.put(var4.getFriendlyName(), var4);
        }
    }
}
