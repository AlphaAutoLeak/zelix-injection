package zelix.gui.clickguis.gishcode;

import zelix.gui.clickguis.*;
import zelix.command.*;
import java.util.*;
import java.io.*;

import zelix.gui.clickguis.GuiTextField;
import zelix.utils.*;
import net.minecraft.entity.*;
import zelix.managers.*;
import org.lwjgl.input.*;
import net.minecraft.client.gui.*;
import zelix.utils.hooks.visual.*;

public class ClickGuiScreen extends GuiScreen
{
    public static final String AUTHOR_TEXT = "Just Remember Me! Zelix God!";
    public static ClickGui clickGui;
    public static int[] mouse;
    public static Tooltip tooltip;
    String title;
    ArrayList cmds;
    GuiTextField console;
    
    public ClickGuiScreen() {
        this.title = "Just Remember Me! Zelix God!";
        (this.cmds = new ArrayList()).clear();
        for (final Command c : CommandManager.commands) {
            this.cmds.add(c.getCommand() + " - " + c.getDescription());
        }
    }
    
    protected void mouseClicked(final int x, final int y, final int button) throws IOException {
        super.mouseClicked(x, y, button);
        this.console.mouseClicked(x, y, button);
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        ClickGuiScreen.tooltip = null;
        ClickGuiScreen.clickGui.render();
        if (ClickGuiScreen.tooltip != null) {
            ClickGuiScreen.tooltip.render();
        }
        final int mainColor = zelix.hack.hacks.ClickGui.isLight ? ColorUtils.color(255, 255, 255, 255) : ColorUtils.color(0, 0, 0, 255);
        this.console.drawTextBox(zelix.hack.hacks.ClickGui.getColor(), mainColor);
        this.console.setTextColor(zelix.hack.hacks.ClickGui.getColor());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        (this.console = new GuiTextField(0, this.fontRenderer, this.width / 2 - 100, 0, 200, 14)).setMaxStringLength(500);
        this.console.setText(this.title);
        this.console.setFocused(!Utils.isMoving((Entity)Wrapper.INSTANCE.player()));
        super.initGui();
    }
    
    public void updateScreen() {
        this.console.updateCursorCounter();
        ClickGuiScreen.clickGui.onUpdate();
        super.updateScreen();
    }
    
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }
    
    void setTitle() {
        if (!this.console.getText().equals("Just Remember Me! Zelix God!")) {
            this.title = "";
        }
    }
    
    private boolean handleKeyScroll(final int key) {
        if (Utils.isMoving((Entity)Wrapper.INSTANCE.player())) {
            return false;
        }
        if (key == 17) {
            return ClickGuiScreen.clickGui.onMouseScroll(3);
        }
        return key == 31 && ClickGuiScreen.clickGui.onMouseScroll(-3);
    }
    
    private void handleKeyboard() {
        if (Keyboard.isCreated()) {
            Keyboard.enableRepeatEvents(true);
            while (Keyboard.next()) {
                if (Keyboard.getEventKeyState()) {
                    if (!this.handleKeyScroll(Keyboard.getEventKey())) {
                        this.console.textboxKeyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
                    }
                    if (Keyboard.getEventKey() == 28) {
                        this.setTitle();
                        CommandManager.getInstance().runCommands("." + this.console.getText());
                        Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)null);
                        FileManager.saveHacks();
                        FileManager.saveClickGui();
                    }
                    else if (Keyboard.getEventKey() == 1) {
                        this.setTitle();
                        Wrapper.INSTANCE.mc().displayGuiScreen((GuiScreen)null);
                        FileManager.saveHacks();
                        FileManager.saveClickGui();
                    }
                    else {
                        ClickGuiScreen.clickGui.onkeyPressed(Keyboard.getEventKey(), Keyboard.getEventCharacter());
                    }
                }
                else {
                    ClickGuiScreen.clickGui.onKeyRelease(Keyboard.getEventKey(), Keyboard.getEventCharacter());
                }
            }
        }
    }
    
    private void handleMouse() {
        if (Mouse.isCreated()) {
            while (Mouse.next()) {
                final ScaledResolution scaledResolution = new ScaledResolution(this.mc);
                final int mouseX = Mouse.getEventX() * scaledResolution.getScaledWidth() / Wrapper.INSTANCE.mc().displayWidth;
                final int mouseY = scaledResolution.getScaledHeight() - Mouse.getEventY() * scaledResolution.getScaledHeight() / Wrapper.INSTANCE.mc().displayHeight - 1;
                if (Mouse.getEventButton() == -1) {
                    ClickGuiScreen.clickGui.onMouseScroll(Mouse.getEventDWheel() / 100 * 3);
                    ClickGuiScreen.clickGui.onMouseUpdate(mouseX, mouseY);
                    ClickGuiScreen.mouse[0] = mouseX;
                    ClickGuiScreen.mouse[1] = mouseY;
                }
                else if (Mouse.getEventButtonState()) {
                    ClickGuiScreen.clickGui.onMouseClick(mouseX, mouseY, Mouse.getEventButton());
                }
                else {
                    ClickGuiScreen.clickGui.onMouseRelease(mouseX, mouseY);
                }
            }
        }
    }
    
    public void handleInput() throws IOException {
        try {
            final int scale = Wrapper.INSTANCE.mc().gameSettings.guiScale;
            Wrapper.INSTANCE.mc().gameSettings.guiScale = 2;
            this.handleKeyboard();
            this.handleMouse();
            Wrapper.INSTANCE.mc().gameSettings.guiScale = scale;
            super.handleInput();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            ChatUtils.error("Exception: handleInput");
        }
    }
    
    static {
        ClickGuiScreen.mouse = new int[2];
        ClickGuiScreen.tooltip = null;
    }
}
