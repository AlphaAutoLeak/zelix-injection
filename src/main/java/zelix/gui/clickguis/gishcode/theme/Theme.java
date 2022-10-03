package zelix.gui.clickguis.gishcode.theme;

import net.minecraft.client.gui.*;
import net.minecraftforge.client.model.obj.*;
import java.util.*;
import zelix.gui.clickguis.gishcode.base.*;

public class Theme
{
    public FontRenderer fontRenderer;
    public OBJModel.Texture icons;
    private HashMap<ComponentType, ComponentRenderer> rendererHashMap;
    private String themeName;
    private int frameHeight;
    
    public Theme(final String themeName) {
        this.rendererHashMap = new HashMap<ComponentType, ComponentRenderer>();
        this.frameHeight = 15;
        this.themeName = themeName;
    }
    
    public void addRenderer(final ComponentType componentType, final ComponentRenderer componentRenderer) {
        this.rendererHashMap.put(componentType, componentRenderer);
    }
    
    public HashMap<ComponentType, ComponentRenderer> getRenderer() {
        return this.rendererHashMap;
    }
    
    public String getThemeName() {
        return this.themeName;
    }
    
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
    
    public int getFrameHeight() {
        return this.frameHeight;
    }
}
