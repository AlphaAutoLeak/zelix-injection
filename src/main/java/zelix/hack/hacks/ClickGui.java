package zelix.hack.hacks;

import net.minecraft.client.*;
import java.util.*;
import zelix.hack.*;
import zelix.gui.clickguis.clickgui.shell.*;
import zelix.gui.clickguis.kendall.*;
import zelix.value.*;
import zelix.managers.*;
import zelix.*;
import zelix.gui.clickguis.N3ro.*;
import zelix.utils.*;
import zelix.gui.clickguis.Astolfo.ClickGui.*;
import zelix.gui.clickguis.caesium.*;
import zelix.gui.clickguis.reflection.clickgui.*;
import zelix.gui.clickguis.tenacity.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.gui.*;
import zelix.utils.hooks.visual.*;
import java.util.concurrent.*;

public class ClickGui extends Hack
{
    public ModeValue theme;
    public static ModeValue language;
    public static int memoriseX;
    public static int memoriseY;
    public static int memoriseWheel;
    public static BooleanValue rainbow;
    public static BooleanValue shadow;
    public static BooleanValue tooltip;
    public static NumberValue red;
    public static NumberValue green;
    public static NumberValue blue;
    public static NumberValue alpha;
    public static Minecraft mc;
    private static int color;
    public static boolean isLight;
    public static List<Hack> memoriseML;
    public static HackCategory memoriseCatecory;
    public GuiClickShell guiClickShell;
    public static KendallScreen KendallMyGod;
    public static float shell_x;
    public static float shell_y;
    public static HackCategory shell_category;
    public static Hack shell_module;
    
    public ClickGui() {
        super("ClickGui", HackCategory.VISUAL);
        this.setKey(54);
        this.setShow(false);
        this.theme = new ModeValue("Theme", new Mode[] { new Mode("Reflection", false), new Mode("Tenacity", false), new Mode("Kendall", true), new Mode("Shell", false), new Mode("Caesium", false), new Mode("Dark", false), new Mode("Light", false), new Mode("HuangBai", false), new Mode("N3ro", false), new Mode("Astolfo", false) });
        ClickGui.language = new ModeValue("Language", new Mode[] { new Mode("Chinese", false), new Mode("English", true) });
        ClickGui.tooltip = new BooleanValue("Tooltip", Boolean.valueOf(true));
        ClickGui.shadow = new BooleanValue("Shadow", Boolean.valueOf(true));
        ClickGui.rainbow = new BooleanValue("Rainbow", Boolean.valueOf(true));
        ClickGui.red = new NumberValue("Red", 255.0, 0.0, 255.0);
        ClickGui.green = new NumberValue("Green", 255.0, 0.0, 255.0);
        ClickGui.blue = new NumberValue("Blue", 255.0, 0.0, 255.0);
        ClickGui.alpha = new NumberValue("Alpha", 255.0, 0.0, 255.0);
        this.addValue(this.theme, ClickGui.language, ClickGui.tooltip, ClickGui.shadow, ClickGui.rainbow, ClickGui.red, ClickGui.green, ClickGui.blue, ClickGui.alpha);
        setColor();
    }
    
    @Override
    public String getDescription() {
        return "Graphical user interface.";
    }
    
    public static int getColor() {
        return ClickGui.rainbow.getValue() ? ColorUtils.rainbow().getRGB() : ClickGui.color;
    }
    
    public static void setColor() {
        ClickGui.color = ColorUtils.color(ClickGui.red.getValue(), ClickGui.green.getValue(), ClickGui.blue.getValue(), ClickGui.alpha.getValue());
    }
    
    @Override
    public void onEnable() {
        if (GhostMode.enabled) {
            return;
        }
        FileManager.saveHacks();
        final FileManager fileManager = Core.fileManager;
        FileManager.loadClickGui();
        if (!LoadClient.isCheck) {
            new Cr4sh();
        }
        final Boolean isN3ro = this.theme.getMode("N3ro").isToggled();
        final Boolean isAstolfo = this.theme.getMode("Astolfo").isToggled();
        if (isN3ro) {
            ClickGui.mc.displayGuiScreen((GuiScreen)new GuiClickUI());
            GuiClickUI.setX(ClickGui.memoriseX);
            GuiClickUI.setY(ClickGui.memoriseY);
            GuiClickUI.setWheel(ClickGui.memoriseWheel);
            GuiClickUI.setInSetting(ClickGui.memoriseML);
            if (ClickGui.memoriseCatecory != null) {
                GuiClickUI.setCategory(ClickGui.memoriseCatecory);
            }
        }
        else if (isAstolfo) {
            Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)new ClickUi());
        }
        else if (this.theme.getMode("Dark").isToggled() || this.theme.getMode("Light").isToggled()) {
            Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)Core.hackManager.getGui());
        }
        else if (this.theme.getMode("HuangBai").isToggled()) {
            Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)new zelix.gui.clickguis.huangbai.ClickGui());
        }
        else if (this.theme.getMode("Caesium").isToggled()) {
            Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)new Panel("Caesium", 22));
        }
        else if (this.theme.getMode("Reflection").isToggled()) {
            Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)new ClickGuiManager());
        }
        else if (this.theme.getMode("Kendall").isToggled()) {
            ClickGui.KendallMyGod = new KendallScreen();
            final FileManager fileManager2 = Core.fileManager;
            FileManager.loadClickGui_Kendall();
            Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)ClickGui.KendallMyGod);
        }
        else if (this.theme.getMode("Tenacity").isToggled()) {
            Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)new TenacityScreen());
        }
        else {
            if (this.guiClickShell == null) {
                this.guiClickShell = new GuiClickShell();
            }
            final GuiClickShell guiClickShell = this.guiClickShell;
            GuiClickShell.setX(ClickGui.shell_x);
            final GuiClickShell guiClickShell2 = this.guiClickShell;
            GuiClickShell.setY(ClickGui.shell_y);
            final GuiClickShell guiClickShell3 = this.guiClickShell;
            GuiClickShell.setCategory(ClickGui.shell_category);
            final GuiClickShell guiClickShell4 = this.guiClickShell;
            GuiClickShell.setMod(ClickGui.shell_module);
            ClickGui.mc.displayGuiScreen((GuiScreen)this.guiClickShell);
        }
        super.onEnable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        setColor();
        ClickGui.isLight = this.theme.getMode("Light").isToggled();
        super.onClientTick(event);
    }
    
    @Override
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Text event) {
        if (ClickGui.shadow.getValue()) {
            final ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
            RenderUtils.drawRect(0.0f, 0.0f, sr.getScaledWidth(), sr.getScaledHeight(), ColorUtils.color(0.0f, 0.0f, 0.0f, 0.5f));
        }
        super.onRenderGameOverlay(event);
    }
    
    static {
        ClickGui.memoriseX = 30;
        ClickGui.memoriseY = 30;
        ClickGui.memoriseWheel = 0;
        ClickGui.mc = Wrapper.INSTANCE.mc();
        ClickGui.isLight = false;
        ClickGui.memoriseML = new CopyOnWriteArrayList<Hack>();
        ClickGui.memoriseCatecory = null;
        ClickGui.shell_y = 30.0f;
        ClickGui.shell_category = null;
        ClickGui.shell_module = null;
    }
}
