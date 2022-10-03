package zelix.hack.hacks.hud;

import net.minecraft.client.*;
import java.text.*;
import net.minecraft.util.*;
import java.awt.image.*;

import zelix.gui.clickguis.N3ro.Utils.RenderUtil;
import zelix.hack.*;
import zelix.utils.hooks.visual.font.render.GlStateManager;
import zelix.value.*;
import net.minecraftforge.client.event.*;
import java.awt.*;
import zelix.*;
import zelix.utils.hooks.visual.*;
import zelix.utils.*;
import zelix.managers.*;
import zelix.hack.hacks.*;
import zelix.hack.hacks.xray.gui.*;
import zelix.utils.tenacityutils.render.*;
import zelix.gui.clickguis.N3ro.Utils.*;
import java.util.*;
import net.minecraft.client.renderer.*;
import zelix.utils.hooks.visual.font.render.*;
import net.minecraft.client.gui.*;
import net.minecraft.inventory.*;
import net.minecraft.client.gui.inventory.*;
import zelix.utils.hooks.visual.font.*;

public class HUD extends Hack
{
    public static Minecraft mc;
    public BooleanValue effects;
    public BooleanValue SessionInfo;
    public BooleanValue Inentory;
    public BooleanValue notification;
    public BooleanValue MUSICPLAYER;
    public BooleanValue ListRainBow;
    public BooleanValue ListBackRainBow;
    public BooleanValue OraginRainBow;
    public BooleanValue ListBackVis;
    public BooleanValue ListBoxAlpha;
    public ModeValue HUDmode;
    SimpleDateFormat sdf;
    public static FontManager font;
    public ColorUtils c;
    private ResourceLocation logo;
    int rainbowTick;
    NumberValue invx;
    NumberValue ListBackTick;
    NumberValue ListOnUP;
    NumberValue ListAlpha;
    Boolean create;
    NumberValue invy;
    public BufferedImage trayIcon;
    public SimpleDateFormat formatter;
    
    public HUD() {
        super("HUD", HackCategory.VISUAL);
        this.sdf = new SimpleDateFormat("HH:mm");
        this.c = new ColorUtils();
        this.logo = new ResourceLocation("logo.png");
        this.rainbowTick = 0;
        this.create = false;
        this.formatter = new SimpleDateFormat("HH:mm:ss");
        this.setToggled(true);
        this.setShow(false);
        this.effects = new BooleanValue("Effects", Boolean.valueOf(false));
        this.SessionInfo = new BooleanValue("SessionInfo", Boolean.valueOf(false));
        this.Inentory = new BooleanValue("Inentory", Boolean.valueOf(false));
        this.invx = new NumberValue("InvX", 10.0, 0.0, 100.0);
        this.invy = new NumberValue("InvY", 20.0, 0.0, 100.0);
        this.notification = new BooleanValue("Notification", Boolean.valueOf(false));
        this.HUDmode = new ModeValue("Mode", new Mode[] { new Mode("Logo", false), new Mode("Normal", true), new Mode("OTC", false) });
        this.ListRainBow = new BooleanValue("New ListRainbow", Boolean.valueOf(false));
        this.ListBackRainBow = new BooleanValue("New ListBackRainBow", Boolean.valueOf(false));
        this.ListBackTick = new NumberValue("ListBackTick", 80.0, -60.0, 500.0);
        this.ListOnUP = new NumberValue("ListOnUP", 1.5, 1.5, 10.0);
        this.OraginRainBow = new BooleanValue("OraginRainBow", Boolean.valueOf(false));
        this.ListBackVis = new BooleanValue("ListBackVis", Boolean.valueOf(false));
        this.ListBoxAlpha = new BooleanValue("isListBoxAlpha", Boolean.valueOf(true));
        this.ListAlpha = new NumberValue("ListAlpha", 0.35, 0.1, 1.0);
        this.addValue(this.effects, this.invx, this.invy, this.Inentory, this.notification, this.HUDmode, this.ListRainBow, this.ListBackRainBow, this.ListBackTick, this.ListOnUP, this.OraginRainBow, this.ListBackVis, this.ListBoxAlpha, this.ListAlpha);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public String getDescription() {
        return "Heads-Up Display.";
    }
    
    @Override
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        int rainbowTickc = 0;
        if (this.notification.getValue()) {}
        if (this.Inentory.getValue()) {
            this.drawInventory();
        }
        if (this.HUDmode.getMode("Logo").isToggled()) {
            RenderUtils.drawImage(this.logo, 2, 2, 100, 100);
        }
        else if (this.HUDmode.getMode("Normal").isToggled()) {
            TTFFontRenderer font = HUD.font.getFont("SFR 11");
            String text = "Z";
            float x2 = 2.0f;
            float y2 = 2.0f;
            ColorUtils c = this.c;
            int red = ColorUtils.rainbow().getRed();
            ColorUtils c2 = this.c;
            int green = ColorUtils.rainbow().getGreen();
            ColorUtils c3 = this.c;
            font.drawStringWithShadow(text, x2, y2, new Color(red, green, ColorUtils.rainbow().getBlue()).getRGB());
            HUD.font.getFont("SFR 11").drawStringWithShadow("elix(" + this.sdf.format(new Date()) + ")", HUD.font.getFont("SFR 11").getStringWidth("Z") + 6, 2.0f, -1);
        }
        else if (this.HUDmode.getMode("OTC").isToggled()) {
            String test = "AutoWidthTest";
            String serverip = HUD.mc.isSingleplayer() ? "singleplayer" : (HUD.mc.getCurrentServerData().serverIP.contains(":") ? HUD.mc.getCurrentServerData().serverIP : (HUD.mc.getCurrentServerData().serverIP + ":25565"));
            String info = "Zelix | " + Core.UN + " | " + Minecraft.getDebugFPS() + " fps | " + serverip + " | " + this.formatter.format(new Date());
            GuiRenderUtils.drawRect(5.0f, 5.0f, HUD.font.getFont("SFR 5").getWidth(info) + 4.0f, 13.0f, new Color(40, 40, 40));
            GuiRenderUtils.drawRoundedRect(5.0f, 5.0f, HUD.font.getFont("SFR 5").getWidth(info) + 4.0f, 1.0f, 1.0f, new Color(255, 191, 0).getRGB(), 1.0f, new Color(255, 191, 0).getRGB());
            HUD.font.getFont("SFR 5").drawStringWithShadow(info, 7.0f, 7.0f, Colors.WHITE.c);
        }
        else if (this.HUDmode.getMode("GameSense").isToggled()) {}
        ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
        Double index = this.invx.getValue();
        Double index_right = this.invy.getValue();
        int DrawColor = -1;
        ArrayList<Hack> hacks = new ArrayList<Hack>();
        hacks.addAll(HackManager.hacks);
        double x = Wrapper.INSTANCE.player().posX;
        double y = Wrapper.INSTANCE.player().posY;
        double z = Wrapper.INSTANCE.player().posZ;
        String coords = String.format("¡ì7X: ¡ìf%s ¡ì7Y: ¡ìf%s ¡ì7Z: ¡ìf%s", RenderUtils.DF((float)x, 1), RenderUtils.DF((float)y, 1), RenderUtils.DF((float)z, 1));
        boolean isChatOpen = Wrapper.INSTANCE.mc().currentScreen instanceof GuiChat;
        int heightCoords = isChatOpen ? (sr.getScaledHeight() - 25) : (sr.getScaledHeight() - 10);
        int colorRect = ColorUtils.color(0.0f, 0.0f, 0.0f, 0.0f);
        int colorRect2 = ColorUtils.color(0.0f, 0.0f, 0.0f, 0.5f);
        int heightFPS = isChatOpen ? (sr.getScaledHeight() - 37) : (sr.getScaledHeight() - 22);
        if (!ClickGui.language.getMode("Chinese").isToggled()) {
            for (Hack h : HackManager.getSortedHacks3()) {
                String modeName = "";
                if (!h.isToggled()) {
                    continue;
                }
                if (++rainbowTickc > 100) {
                    rainbowTickc = 0;
                }
                if (h.getName().equals("Xray")) {
                    modeName = modeName + " ¡ì7" + GuiOverlay.XrayStr;
                }
                for (Value value : h.getValues()) {
                    if (value instanceof ModeValue) {
                        ModeValue modeValue = (ModeValue)value;
                        if (!modeValue.getModeName().equals("Mode")) {
                            continue;
                        }
                        for (Mode mode : modeValue.getModes()) {
                            if (mode.isToggled()) {
                                modeName = modeName + " ¡ì7" + mode.getName();
                            }
                        }
                    }
                }
                int yPos = 18;
                int xPos = 4;
                xPos = 6;
                Color rainbow = new Color(Color.HSBtoRGB((float)(Minecraft.getMinecraft().player.ticksExisted / 50.0 - Math.sin(rainbowTickc / 40.0 * 1.4)) % 1.0f, 1.0f, 1.0f));
                Color m_Backrainbow = new Color(Color.HSBtoRGB((float)(Minecraft.getMinecraft().player.ticksExisted / 50.0 - Math.sin(rainbowTickc / this.ListBackTick.getValue() * this.ListOnUP.getValue())) % 1.0f, 1.0f, 1.0f));
                if (this.ListRainBow.getValue()) {
                    DrawColor = rainbow.getRGB();
                }
                if (this.ListBackVis.getValue()) {
                    if (this.ListBoxAlpha.getValue()) {
                        float alphaAnimation = 1.0f;
                        int SDrawColor = ColorUtil.applyOpacity(new Color(10, 10, 10), (float)(this.ListAlpha.getValue() * alphaAnimation)).getRGB();
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() - HUD.font.getFont("SFB 6").getWidth(h.getName() + modeName) - index_right - 4.0f, index + 1.6, sr.getScaledWidth() - index_right, index + 13.6, 0.0f, 0, SDrawColor);
                    }
                    else if (this.ListBackRainBow.getValue()) {
                        if (this.OraginRainBow.getValue()) {
                            RenderUtil.drawBorderedRect(sr.getScaledWidth() - HUD.font.getFont("SFB 6").getWidth(h.getName() + modeName) - index_right - 4.0f, index + 1, sr.getScaledWidth() - index_right, index + 13.6, 1.0f, ColorUtils.rainbow((int)(this.ListOnUP.getValue() * 10.0)), ColorUtils.rainbow((int)(this.ListOnUP.getValue() * 10.0)));
                        }
                        else {
                            RenderUtil.drawBorderedRect(sr.getScaledWidth() - HUD.font.getFont("SFB 6").getWidth(h.getName() + modeName) - index_right - 4.0f, index + 1, sr.getScaledWidth() - index_right, index + 13.6, 1.0f, m_Backrainbow.getRGB(), m_Backrainbow.getRGB());
                        }
                    }
                    else {
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() - HUD.font.getFont("SFB 6").getWidth(h.getName() + modeName) - index_right - 4.0f, index + 1, sr.getScaledWidth() - index_right, index + 13.6, 1.0f, -14869217, -14869217);
                    }
                }
                HUD.font.getFont("SFB 6").drawStringWithShadow(h.getName() + modeName, (float) (sr.getScaledWidth() - HUD.font.getFont("SFB 6").getWidth(h.getName() + modeName) - index_right - 2.0f), (float) (index + 2), DrawColor);
                index += (int)(HUD.font.getFont("SFB 6").getHeight(h.getName()) + 3.0f);
            }
        }
        else {
            for (Hack h : HackManager.getSortedHacks()) {
                String modeName = "";
                if (!h.isToggled()) {
                    continue;
                }
                if (++rainbowTickc > 100) {
                    rainbowTickc = 0;
                }
                if (h.getName().equals("Xray") && !GuiOverlay.XrayStr.equals("")) {
                    modeName = modeName + " ¡ì7" + GuiOverlay.XrayStr;
                }
                for (Value value : h.getValues()) {
                    if (value instanceof ModeValue) {
                        ModeValue modeValue = (ModeValue)value;
                        if (!modeValue.getModeName().equals("Mode")) {
                            continue;
                        }
                        for (Mode mode : modeValue.getModes()) {
                            if (mode.isToggled()) {
                                modeName = modeName + " ¡ì7" + mode.getName();
                            }
                        }
                    }
                }
                int yPos = 18;
                int xPos = 4;
                xPos = 6;
                Color rainbow = new Color(Color.HSBtoRGB((float)(Minecraft.getMinecraft().player.ticksExisted / 50.0 - Math.sin(rainbowTickc / 40.0 * 1.4)) % 1.0f, 1.0f, 1.0f));
                Color m_Backrainbow = new Color(Color.HSBtoRGB((float)(Minecraft.getMinecraft().player.ticksExisted / 50.0 - Math.sin(rainbowTickc / this.ListBackTick.getValue() * this.ListOnUP.getValue())) % 1.0f, 1.0f, 1.0f));
                if (this.ListRainBow.getValue()) {
                    DrawColor = rainbow.getRGB();
                }
                if (this.ListBackVis.getValue()) {
                    if (this.ListBoxAlpha.getValue()) {
                        float alphaAnimation = 1.0f;
                        int SDrawColor = ColorUtil.applyOpacity(new Color(10, 10, 10), (float)(this.ListAlpha.getValue() * alphaAnimation)).getRGB();
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() - Wrapper.INSTANCE.fontRenderer().getStringWidth(h.getRenderName() + modeName) - index_right - 4, index + 1.6, sr.getScaledWidth() - index_right, index + 13.6, 0.0f, 0, SDrawColor);
                    }
                    else if (this.ListBackRainBow.getValue()) {
                        if (this.OraginRainBow.getValue()) {
                            RenderUtil.drawBorderedRect(sr.getScaledWidth() - Wrapper.INSTANCE.fontRenderer().getStringWidth(h.getRenderName() + modeName) - index_right - 4, index + 1, sr.getScaledWidth() - index_right, index + 13.6, 1.0f, ColorUtils.rainbow((int)(this.ListOnUP.getValue() * 10.0)), ColorUtils.rainbow((int)(this.ListOnUP.getValue() * 10.0)));
                        }
                        else {
                            RenderUtil.drawBorderedRect(sr.getScaledWidth() - Wrapper.INSTANCE.fontRenderer().getStringWidth(h.getRenderName() + modeName) - index_right - 4, index + 1, sr.getScaledWidth() - index_right, index + 13.6, 1.0f, m_Backrainbow.getRGB(), m_Backrainbow.getRGB());
                        }
                    }
                    else {
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() - Wrapper.INSTANCE.fontRenderer().getStringWidth(h.getRenderName() + modeName) - index_right - 4, index + 1, sr.getScaledWidth() - index_right, index + 13.6, 1.0f, -14869217, -14869217);
                    }
                }
                Wrapper.INSTANCE.fontRenderer().drawString(h.getRenderName() + modeName, (int) (sr.getScaledWidth() - Wrapper.INSTANCE.fontRenderer().getStringWidth(h.getRenderName() + modeName) - index_right - 2), (int) (index + 2), DrawColor);
                index += Wrapper.INSTANCE.fontRenderer().FONT_HEIGHT + 3;
            }
        }
        super.onRenderGameOverlay(event);
    }
    
    public void drawInventory() {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableDepth();
        int x = 5;
        int y = 22;
        Gui.drawRect(x, y, x + 167, y + 73, new Color(29, 29, 29, 255).getRGB());
        Gui.drawRect(x + 1, y + 13, x + 166, y + 72, new Color(40, 40, 40, 255).getRGB());
        FontLoaders fontLoaders = Core.fontLoaders;
        FontLoaders.default16.drawString("Your Inventory", x + 3, y + 3, -1, true);
        boolean hasStacks = false;
        for (int i1 = 9; i1 < Wrapper.INSTANCE.player().inventoryContainer.inventorySlots.size() - 9; ++i1) {
            Slot slot = Wrapper.INSTANCE.player().inventoryContainer.inventorySlots.get(i1);
            if (slot.getHasStack()) {
                hasStacks = true;
            }
            int j = slot.xPos;
            int k = slot.yPos;
            HUD.mc.getRenderItem().renderItemAndEffectIntoGUI(slot.getStack(), x + j - 4, y + k - 68);
            HUD.mc.getRenderItem().renderItemOverlayIntoGUI(Wrapper.INSTANCE.fontRenderer(), slot.getStack(), x + j - 4, y + k - 68, (String)null);
        }
        if (HUD.mc.currentScreen instanceof GuiInventory) {
            CFontRenderer default16 = FontLoaders.default16;
            String text = "Already in inventory";
            int n = x + 83;
            FontLoaders fontLoaders2 = Core.fontLoaders;
            default16.drawString(text, n - FontLoaders.default16.getStringWidth("Already in inventory") / 2, y + 36, -1, true);
        }
        else if (!hasStacks) {
            FontLoaders fontLoaders3 = Core.fontLoaders;
            CFontRenderer default17 = FontLoaders.default16;
            String text2 = "Empty...";
            int n2 = x + 83;
            FontLoaders fontLoaders4 = Core.fontLoaders;
            default17.drawString(text2, n2 - FontLoaders.default16.getStringWidth("Empty...") / 2, y + 36, -1, true);
        }
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableDepth();
    }
    
    static {
        HUD.mc = Wrapper.INSTANCE.mc();
        HUD.font = new FontManager();
    }
}
