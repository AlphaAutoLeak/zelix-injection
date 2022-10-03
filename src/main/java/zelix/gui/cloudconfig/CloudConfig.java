package zelix.gui.cloudconfig;

import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import zelix.gui.clickguis.caesium.components.*;
import org.lwjgl.input.*;
import javax.swing.*;
import zelix.utils.*;
import java.awt.*;
import zelix.command.*;
import zelix.utils.hooks.visual.*;
import java.io.*;
import com.google.gson.*;
import zelix.managers.*;
import java.util.*;
import zelix.hack.*;
import zelix.value.*;

public class CloudConfig extends GuiScreen
{
    public static Minecraft mc;
    public static ScaledResolution sr;
    public static float startX;
    public static float startY;
    public float moveX;
    public float moveY;
    public static FontManager font;
    public static ConfigFrame[] NEW;
    public static ConfigFrame[] OWN;
    public static String Mode;
    public static String verify;
    public boolean LoadButton_isHovered;
    public String str;
    public static int pages;
    public int nowpage;
    int ConfigNumbers;
    private static JsonParser jsonParser;
    
    public CloudConfig() {
        this.moveX = 0.0f;
        this.moveY = 0.0f;
        this.LoadButton_isHovered = false;
        this.str = "";
        this.nowpage = 1;
    }
    
    public void initGui() {
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (int i = 0; i < CloudConfig.NEW.length; ++i) {
            if (CloudConfig.NEW[i] != null) {
                if (i / 6 + 1 == this.nowpage) {
                    final int x = i - (this.nowpage - 1) * 6;
                    if (x > 2) {
                        CloudConfig.NEW[i].mouseClicked(mouseX, mouseY, mouseButton);
                    }
                    else {
                        CloudConfig.NEW[i].mouseClicked(mouseX, mouseY, mouseButton);
                    }
                }
            }
        }
        if (isHovered(CloudConfig.startX + 10.0f, CloudConfig.startY + 280.0f, CloudConfig.startX + 100.0f, CloudConfig.startY + 300.0f, mouseX, mouseY) && mouseButton == 0) {
            this.nowpage = 1;
            if (CloudConfig.Mode == "All") {
                CloudConfig.Mode = "Own";
                CloudConfig.OWN = new ConfigFrame[CloudConfig.NEW.length];
                for (int i = 0; i < CloudConfig.OWN.length; ++i) {
                    if (CloudConfig.OWN[i] != null) {
                        final int page = i / 6;
                        CloudConfig.pages = page + 1;
                    }
                }
            }
            else {
                CloudConfig.Mode = "All";
                for (int i = 0; i < CloudConfig.NEW.length; ++i) {
                    if (CloudConfig.NEW[i] != null) {
                        final int page = i / 6;
                        CloudConfig.pages = page + 1;
                    }
                }
            }
        }
        if (CloudConfig.pages > 1) {
            if (this.nowpage == 1) {
                if (isHovered(CloudConfig.startX + 210.0f, CloudConfig.startY + 280.0f, CloudConfig.startX + 230.0f, CloudConfig.startY + 300.0f, mouseX, mouseY) && mouseButton == 0) {
                    ++this.nowpage;
                }
            }
            else if (this.nowpage == CloudConfig.pages) {
                if (isHovered(CloudConfig.startX + 170.0f, CloudConfig.startY + 280.0f, CloudConfig.startX + 190.0f, CloudConfig.startY + 300.0f, mouseX, mouseY) && mouseButton == 0) {
                    --this.nowpage;
                }
            }
            else {
                if (isHovered(CloudConfig.startX + 210.0f, CloudConfig.startY + 280.0f, CloudConfig.startX + 230.0f, CloudConfig.startY + 300.0f, mouseX, mouseY) && mouseButton == 0) {
                    ++this.nowpage;
                }
                if (isHovered(CloudConfig.startX + 170.0f, CloudConfig.startY + 280.0f, CloudConfig.startX + 190.0f, CloudConfig.startY + 300.0f, mouseX, mouseY) && mouseButton == 0) {
                    --this.nowpage;
                }
            }
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        RenderUtils.drawRect(CloudConfig.startX, CloudConfig.startY, CloudConfig.startX + 400.0f, CloudConfig.startY + 310.0f, ColorUtils.getColor(96, 96, 96));
        CloudConfig.font.getFont("SFR 14").drawString("Zelix Cloud Config Loader", CloudConfig.startX + 10.0f, CloudConfig.startY + 10.0f, ColorUtils.rainbow().getRGB());
        HGLUtils.drawGradientRect(CloudConfig.startX + 10.0f, CloudConfig.startY + 30.0f, CloudConfig.startX + 300.0f, CloudConfig.startY + 40.0f + CloudConfig.font.getFont("SFR 14").getHeight("HHHHHHH"), ColorUtils.getColor(224, 224, 224));
        CloudConfig.font.getFont("SFR 14").drawString(this.str, CloudConfig.startX + 10.0f, CloudConfig.startY + 35.0f, ColorUtils.getColor(32, 32, 32));
        RenderUtils.drawRect(CloudConfig.startX + 320.0f, CloudConfig.startY + 30.0f, CloudConfig.startX + 390.0f, CloudConfig.startY + 40.0f + CloudConfig.font.getFont("SFR 14").getHeight("HHHHHHH"), this.LoadButton_isHovered ? ColorUtils.getColor(240, 240, 240) : ColorUtils.getColor(224, 224, 224));
        CloudConfig.font.getFont("SFR 14").drawString("Load", CloudConfig.startX + 345.0f, CloudConfig.startY + 35.0f, ColorUtils.getColor(32, 32, 32));
        CloudConfig.font.getFont("SFR 11").drawString("Click [LOAD] Button To Choose Your Private Config Verification File And Load It!", CloudConfig.startX + 10.0f, CloudConfig.startY + 50.0f, ColorUtils.getColor(32, 32, 32));
        if ((isHovered(CloudConfig.startX, CloudConfig.startY, CloudConfig.startX + 450.0f, CloudConfig.startY + 50.0f, mouseX, mouseY) || isHovered(CloudConfig.startX, CloudConfig.startY + 315.0f, CloudConfig.startX + 450.0f, CloudConfig.startY + 350.0f, mouseX, mouseY) || isHovered(CloudConfig.startX + 430.0f, CloudConfig.startY, CloudConfig.startX + 450.0f, CloudConfig.startY + 350.0f, mouseX, mouseY)) && Mouse.isButtonDown(0)) {
            if (this.moveX == 0.0f && this.moveY == 0.0f) {
                this.moveX = mouseX - CloudConfig.startX;
                this.moveY = mouseY - CloudConfig.startY;
            }
            else {
                CloudConfig.startX = mouseX - this.moveX;
                CloudConfig.startY = mouseY - this.moveY;
            }
        }
        else if (this.moveX != 0.0f || this.moveY != 0.0f) {
            this.moveX = 0.0f;
            this.moveY = 0.0f;
        }
        if (isHovered(CloudConfig.startX + 320.0f, CloudConfig.startY + 30.0f, CloudConfig.startX + 390.0f, CloudConfig.startY + 40.0f + CloudConfig.font.getFont("SFR 14").getHeight("HHHHHHH"), mouseX, mouseY)) {
            this.LoadButton_isHovered = true;
            if (Mouse.isButtonDown(0)) {
                final JFrame frame = new JFrame();
                final JFileChooser chooser = new JFileChooser(".");
                final int i = chooser.showOpenDialog(frame);
                if (i == 0) {
                    this.str = Utils.readFile(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        }
        else {
            this.LoadButton_isHovered = false;
        }
        int y = 0;
        for (int j = 0; j < CloudConfig.NEW.length; ++j) {
            if (CloudConfig.NEW[j] != null) {
                if (CloudConfig.Mode == "Own") {
                    if (CloudConfig.NEW[j].isUserDev()) {
                        CloudConfig.OWN[y] = CloudConfig.NEW[j];
                        ++y;
                    }
                }
                else if (j / 6 + 1 == this.nowpage) {
                    final int x = j - (this.nowpage - 1) * 6;
                    if (x > 2) {
                        CloudConfig.NEW[j].render((int)CloudConfig.startX + 10 + (x - 3) * 10 + (x - 3) * 120, (int)CloudConfig.startY + 65 + 110, 0, mouseX, mouseY);
                    }
                    else {
                        CloudConfig.NEW[j].render((int)CloudConfig.startX + 10 + x * 10 + x * 120, (int)CloudConfig.startY + 65, 0, mouseX, mouseY);
                    }
                }
            }
        }
        if (CloudConfig.Mode == "Own") {
            for (int j = 0; j < CloudConfig.OWN.length; ++j) {
                if (CloudConfig.OWN[j] != null) {
                    if (j / 6 + 1 == this.nowpage) {
                        final int x = j - (this.nowpage - 1) * 6;
                        if (x > 2) {
                            CloudConfig.OWN[j].render((int)CloudConfig.startX + 10 + (x - 3) * 10 + (x - 3) * 120, (int)CloudConfig.startY + 65 + 110, 0, mouseX, mouseY);
                        }
                        else {
                            CloudConfig.OWN[j].render((int)CloudConfig.startX + 10 + x * 10 + x * 120, (int)CloudConfig.startY + 65, 0, mouseX, mouseY);
                        }
                    }
                }
            }
        }
        CloudConfig.font.getFont("SFR 14").drawString(String.valueOf(this.nowpage), CloudConfig.startX + 200.0f, CloudConfig.startY + 285.0f, ColorUtils.rainbow().getRGB());
        if (CloudConfig.pages > 1) {
            if (this.nowpage == 1) {
                RenderUtils.drawRect(CloudConfig.startX + 210.0f, CloudConfig.startY + 280.0f, CloudConfig.startX + 230.0f, CloudConfig.startY + 300.0f, ColorUtils.getColor(224, 224, 224));
            }
            else if (this.nowpage == CloudConfig.pages) {
                RenderUtils.drawRect(CloudConfig.startX + 170.0f, CloudConfig.startY + 280.0f, CloudConfig.startX + 190.0f, CloudConfig.startY + 300.0f, ColorUtils.getColor(224, 224, 224));
            }
            else {
                RenderUtils.drawRect(CloudConfig.startX + 175.0f, CloudConfig.startY + 280.0f, CloudConfig.startX + 195.0f, CloudConfig.startY + 300.0f, ColorUtils.getColor(224, 224, 224));
                RenderUtils.drawRect(CloudConfig.startX + 210.0f, CloudConfig.startY + 280.0f, CloudConfig.startX + 230.0f, CloudConfig.startY + 300.0f, ColorUtils.getColor(224, 224, 224));
            }
        }
        RenderUtils.drawRect(CloudConfig.startX + 10.0f, CloudConfig.startY + 280.0f, CloudConfig.startX + 100.0f, CloudConfig.startY + 300.0f, isHovered(CloudConfig.startX + 10.0f, CloudConfig.startY + 280.0f, CloudConfig.startX + 100.0f, CloudConfig.startY + 300.0f, mouseX, mouseY) ? new Color(80, 80, 80, 121).getRGB() : new Color(56, 56, 56, 121).getRGB());
        CloudConfig.font.getFont("SFR 14").drawString(CloudConfig.Mode, CloudConfig.startX + 30.0f, CloudConfig.startY + 285.0f, ColorUtils.rainbow().getRGB());
    }
    
    public static boolean isHovered(final float x, final float y, final float x2, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
    
    public void drawConfigList() {
    }
    
    public static void LoadConfig(final String UUID) {
        final String Config = zelix.command.Config.Send("121.62.61.198", 9990, CloudConfig.verify + "[TARGET][LOAD][UUID][" + UUID + "]");
        if (Config != null) {
            loadHacks(Config);
        }
        else {
            ChatUtils.error("null config");
        }
    }
    
    public static void UpdateConfig(final String UUID, final String Name) {
        final String Config = zelix.command.Config.Send("121.62.61.198", 9990, CloudConfig.verify + "[TARGET][UPDATE][NAME][" + Name + "][UUID][" + UUID + "][IN][" + GETIN() + "]");
        ChatUtils.message(Config);
    }
    
    public static void UploadConfig(final String Name) {
        new Thread() {
            @Override
            public void run() {
                final String s = CloudConfig.verify + "[TARGET][UPLOAD][NAME][" + Name + "][IN][" + CloudConfig.GETIN() + "]";
                final String Config = zelix.command.Config.Send("121.62.61.198", 9990, s);
                ChatUtils.message(Config);
                super.run();
            }
        }.start();
    }
    
    public static void loadHacks(final String IN) {
        final BufferedReader bufferedReader = new BufferedReader(new StringReader(IN));
        final JsonObject jsonObject = (JsonObject)CloudConfig.jsonParser.parse((Reader)bufferedReader);
        try {
            bufferedReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            final Hack module = HackManager.getHack(entry.getKey());
            if (module == null) {
                continue;
            }
            final JsonObject jsonObjectHack = (JsonObject)entry.getValue();
            module.setKey(jsonObjectHack.get("key").getAsInt());
            module.setToggled(jsonObjectHack.get("toggled").getAsBoolean());
            if (module.getValues().isEmpty()) {
                continue;
            }
            for (final Value value : module.getValues()) {
                if (value == null) {
                    continue;
                }
                if (jsonObjectHack.get(value.getName()) == null) {
                    if (value instanceof BooleanValue) {
                        jsonObjectHack.addProperty(value.getName(), (Boolean)value.getValue());
                    }
                    if (value instanceof NumberValue) {
                        jsonObjectHack.addProperty(value.getName(), (Number)value.getValue());
                    }
                    if (value instanceof ModeValue) {
                        jsonObjectHack.addProperty(value.getName(), (Boolean)value.getValue());
                    }
                }
                if (value instanceof BooleanValue) {
                    value.setValue(jsonObjectHack.get(value.getName()).getAsBoolean());
                }
                if (value instanceof NumberValue) {
                    value.setValue(jsonObjectHack.get(value.getName()).getAsDouble());
                }
                if (!(value instanceof ModeValue)) {
                    continue;
                }
                final ModeValue modeValue = (ModeValue)value;
                for (final Mode mode : modeValue.getModes()) {
                    mode.setToggled(jsonObjectHack.get(mode.getName()).getAsBoolean());
                }
            }
        }
        ChatUtils.message("Loaded!");
    }
    
    public static String GETIN() {
        try {
            final JsonObject json = new JsonObject();
            for (final Hack module : HackManager.getHacks()) {
                final JsonObject jsonHack = new JsonObject();
                jsonHack.addProperty("toggled", module.isToggled());
                jsonHack.addProperty("key", (Number)module.getKey());
                if (!module.getValues().isEmpty()) {
                    for (final Value value : module.getValues()) {
                        if (value instanceof BooleanValue) {
                            jsonHack.addProperty(value.getName(), (Boolean)value.getValue());
                        }
                        if (value instanceof NumberValue) {
                            jsonHack.addProperty(value.getName(), (Number)value.getValue());
                        }
                        if (value instanceof ModeValue) {
                            final ModeValue modeValue = (ModeValue)value;
                            for (final Mode mode : modeValue.getModes()) {
                                jsonHack.addProperty(mode.getName(), mode.isToggled());
                            }
                        }
                    }
                }
                json.add(module.getName(), (JsonElement)jsonHack);
            }
            return json.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    static {
        CloudConfig.mc = Minecraft.getMinecraft();
        CloudConfig.sr = new ScaledResolution(CloudConfig.mc);
        CloudConfig.startX = CloudConfig.sr.getScaledWidth() / 2 - 225;
        CloudConfig.startY = CloudConfig.sr.getScaledHeight() / 2 - 150;
        CloudConfig.font = new FontManager();
        CloudConfig.OWN = new ConfigFrame[1024];
        CloudConfig.Mode = "All";
        CloudConfig.verify = "";
        CloudConfig.jsonParser = new JsonParser();
    }
}
