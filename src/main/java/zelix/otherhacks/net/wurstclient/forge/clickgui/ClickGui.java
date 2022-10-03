package zelix.otherhacks.net.wurstclient.forge.clickgui;

import zelix.otherhacks.net.wurstclient.forge.settings.*;
import net.minecraft.client.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import java.util.*;
import com.google.gson.*;
import java.nio.file.*;
import java.io.*;
import org.lwjgl.opengl.*;
import org.lwjgl.input.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import net.minecraft.client.gui.*;
import zelix.otherhacks.net.wurstclient.forge.*;

public final class ClickGui
{
    private final ArrayList<Window> windows;
    private final ArrayList<Popup> popups;
    private final Path windowsFile;
    private float[] bgColor;
    private float[] acColor;
    private float opacity;
    private int maxHeight;
    private String tooltip;
    
    public ClickGui(final Path windowsFile) {
        this.windows = new ArrayList<Window>();
        this.popups = new ArrayList<Popup>();
        this.bgColor = new float[3];
        this.acColor = new float[3];
        this.windowsFile = windowsFile;
    }
    
    public void init(final HackList hax) {
        final LinkedHashMap<Category, Window> windowMap = new LinkedHashMap<Category, Window>();
        for (final Category category : Category.values()) {
            windowMap.put(category, new Window(category.getName()));
        }
        for (final Hack hack : hax.getRegistry()) {
            if (hack.getCategory() != null) {
                windowMap.get(hack.getCategory()).add(new HackButton(hack));
            }
        }
        this.windows.addAll(windowMap.values());
        final Window uiSettings = new Window("UI Settings");
        for (final Setting setting : hax.clickGuiHack.getSettings().values()) {
            uiSettings.add(setting.getComponent());
        }
        this.windows.add(uiSettings);
        for (final Window window : this.windows) {
            window.setMinimized(true);
        }
        this.windows.add(hax.radarHack.getWindow());
        int x = 5;
        int y = 5;
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        for (final Window window2 : this.windows) {
            window2.pack();
            if (x + window2.getWidth() + 5 > sr.getScaledWidth()) {
                x = 5;
                y += 18;
            }
            window2.setX(x);
            window2.setY(y);
            x += window2.getWidth() + 5;
        }
        JsonObject json;
        try (final BufferedReader reader = Files.newBufferedReader(this.windowsFile)) {
            json = JsonUtils.jsonParser.parse((Reader)reader).getAsJsonObject();
        }
        catch (NoSuchFileException e2) {
            this.saveWindows();
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.saveWindows();
            return;
        }
        for (final Window window3 : this.windows) {
            final JsonElement jsonWindow = json.get(window3.getTitle());
            if (jsonWindow != null) {
                if (!jsonWindow.isJsonObject()) {
                    continue;
                }
                final JsonElement jsonX = jsonWindow.getAsJsonObject().get("x");
                if (jsonX.isJsonPrimitive() && jsonX.getAsJsonPrimitive().isNumber()) {
                    window3.setX(jsonX.getAsInt());
                }
                final JsonElement jsonY = jsonWindow.getAsJsonObject().get("y");
                if (jsonY.isJsonPrimitive() && jsonY.getAsJsonPrimitive().isNumber()) {
                    window3.setY(jsonY.getAsInt());
                }
                final JsonElement jsonMinimized = jsonWindow.getAsJsonObject().get("minimized");
                if (jsonMinimized.isJsonPrimitive() && jsonMinimized.getAsJsonPrimitive().isBoolean()) {
                    window3.setMinimized(jsonMinimized.getAsBoolean());
                }
                final JsonElement jsonPinned = jsonWindow.getAsJsonObject().get("pinned");
                if (!jsonPinned.isJsonPrimitive() || !jsonPinned.getAsJsonPrimitive().isBoolean()) {
                    continue;
                }
                window3.setPinned(jsonPinned.getAsBoolean());
            }
        }
        this.saveWindows();
    }
    
    private void saveWindows() {
        final JsonObject json = new JsonObject();
        for (final Window window : this.windows) {
            if (window.isClosable()) {
                continue;
            }
            final JsonObject jsonWindow = new JsonObject();
            jsonWindow.addProperty("x", (Number)window.getX());
            jsonWindow.addProperty("y", (Number)window.getY());
            jsonWindow.addProperty("minimized", window.isMinimized());
            jsonWindow.addProperty("pinned", window.isPinned());
            json.add(window.getTitle(), (JsonElement)jsonWindow);
        }
        try (final BufferedWriter writer = Files.newBufferedWriter(this.windowsFile, new OpenOption[0])) {
            JsonUtils.prettyGson.toJson((JsonElement)json, (Appendable)writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void handleMouseClick(final int mouseX, final int mouseY, final int mouseButton) {
        final boolean popupClicked = this.handlePopupMouseClick(mouseX, mouseY, mouseButton);
        if (!popupClicked) {
            this.handleWindowMouseClick(mouseX, mouseY, mouseButton);
        }
        for (final Popup popup : this.popups) {
            if (popup.getOwner().getParent().isClosing()) {
                popup.close();
            }
        }
        this.windows.removeIf(w -> w.isClosing());
        this.popups.removeIf(p -> p.isClosing());
    }
    
    private boolean handlePopupMouseClick(final int mouseX, final int mouseY, final int mouseButton) {
        for (int i = this.popups.size() - 1; i >= 0; --i) {
            final Popup popup = this.popups.get(i);
            final Component owner = popup.getOwner();
            final Window parent = owner.getParent();
            final int x0 = parent.getX() + owner.getX();
            final int y0 = parent.getY() + 13 + parent.getScrollOffset() + owner.getY();
            final int x2 = x0 + popup.getX();
            final int y2 = y0 + popup.getY();
            final int x3 = x2 + popup.getWidth();
            final int y3 = y2 + popup.getHeight();
            if (mouseX >= x2) {
                if (mouseY >= y2) {
                    if (mouseX < x3) {
                        if (mouseY < y3) {
                            final int cMouseX = mouseX - x0;
                            final int cMouseY = mouseY - y0;
                            popup.handleMouseClick(cMouseX, cMouseY, mouseButton);
                            this.popups.remove(i);
                            this.popups.add(popup);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private void handleWindowMouseClick(final int mouseX, final int mouseY, final int mouseButton) {
        for (int i = this.windows.size() - 1; i >= 0; --i) {
            final Window window = this.windows.get(i);
            if (!window.isInvisible()) {
                final int x1 = window.getX();
                final int y1 = window.getY();
                final int x2 = x1 + window.getWidth();
                final int y2 = y1 + window.getHeight();
                final int y3 = y1 + 13;
                if (mouseX >= x1) {
                    if (mouseY >= y1) {
                        if (mouseX < x2) {
                            if (mouseY < y2) {
                                if (mouseY < y3) {
                                    this.handleTitleBarMouseClick(window, mouseX, mouseY, mouseButton);
                                }
                                else {
                                    if (window.isMinimized()) {
                                        continue;
                                    }
                                    window.validate();
                                    final int cMouseX = mouseX - x1;
                                    int cMouseY = mouseY - y3;
                                    if (window.isScrollingEnabled() && mouseX >= x2 - 3) {
                                        this.handleScrollbarMouseClick(window, cMouseX, cMouseY, mouseButton);
                                    }
                                    else {
                                        if (window.isScrollingEnabled()) {
                                            cMouseY -= window.getScrollOffset();
                                        }
                                        this.handleComponentMouseClick(window, cMouseX, cMouseY, mouseButton);
                                    }
                                }
                                this.windows.remove(i);
                                this.windows.add(window);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void handleTitleBarMouseClick(final Window window, final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton != 0) {
            return;
        }
        if (mouseY < window.getY() + 2 || mouseY >= window.getY() + 11) {
            window.startDragging(mouseX, mouseY);
            return;
        }
        int x3 = window.getX() + window.getWidth();
        if (window.isClosable()) {
            x3 -= 11;
            if (mouseX >= x3 && mouseX < x3 + 9) {
                window.close();
                return;
            }
        }
        if (window.isPinnable()) {
            x3 -= 11;
            if (mouseX >= x3 && mouseX < x3 + 9) {
                window.setPinned(!window.isPinned());
                this.saveWindows();
                return;
            }
        }
        if (window.isMinimizable()) {
            x3 -= 11;
            if (mouseX >= x3 && mouseX < x3 + 9) {
                window.setMinimized(!window.isMinimized());
                this.saveWindows();
                return;
            }
        }
        window.startDragging(mouseX, mouseY);
    }
    
    private void handleScrollbarMouseClick(final Window window, final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton != 0) {
            return;
        }
        if (mouseX >= window.getWidth() - 1) {
            return;
        }
        final double outerHeight = window.getHeight() - 13;
        final double innerHeight = window.getInnerHeight();
        final double maxScrollbarHeight = outerHeight - 2.0;
        final int scrollbarY = (int)(outerHeight * (-window.getScrollOffset() / innerHeight) + 1.0);
        final int scrollbarHeight = (int)(maxScrollbarHeight * outerHeight / innerHeight);
        if (mouseY < scrollbarY || mouseY >= scrollbarY + scrollbarHeight) {
            return;
        }
        window.startDraggingScrollbar(window.getY() + 13 + mouseY);
    }
    
    private void handleComponentMouseClick(final Window window, final int mouseX, final int mouseY, final int mouseButton) {
        for (int i2 = window.countChildren() - 1; i2 >= 0; --i2) {
            final Component c = window.getChild(i2);
            if (mouseX >= c.getX()) {
                if (mouseY >= c.getY()) {
                    if (mouseX < c.getX() + c.getWidth()) {
                        if (mouseY < c.getY() + c.getHeight()) {
                            c.handleMouseClick(mouseX, mouseY, mouseButton);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glLineWidth(1.0f);
        final int dWheel = Mouse.getDWheel();
        if (dWheel != 0) {
            for (int i = this.windows.size() - 1; i >= 0; --i) {
                final Window window = this.windows.get(i);
                if (window.isScrollingEnabled() && !window.isMinimized()) {
                    if (!window.isInvisible()) {
                        if (mouseX >= window.getX()) {
                            if (mouseY >= window.getY() + 13) {
                                if (mouseX < window.getX() + window.getWidth()) {
                                    if (mouseY < window.getY() + window.getHeight()) {
                                        int scroll = window.getScrollOffset() + dWheel / 16;
                                        scroll = Math.min(scroll, 0);
                                        scroll = Math.max(scroll, -window.getInnerHeight() + window.getHeight() - 13);
                                        window.setScrollOffset(scroll);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this.tooltip = null;
        final Iterator<Window> iterator = this.windows.iterator();
        while (iterator.hasNext()) {
            final Window window = iterator.next();
            if (window.isInvisible()) {
                continue;
            }
            if (window.isDragging()) {
                if (Mouse.isButtonDown(0)) {
                    window.dragTo(mouseX, mouseY);
                }
                else {
                    window.stopDragging();
                    this.saveWindows();
                }
            }
            if (window.isDraggingScrollbar()) {
                if (Mouse.isButtonDown(0)) {
                    window.dragScrollbarTo(mouseY);
                }
                else {
                    window.stopDraggingScrollbar();
                }
            }
            this.renderWindow(window, mouseX, mouseY, partialTicks);
        }
        GL11.glDisable(3553);
        for (final Popup popup : this.popups) {
            final Component owner = popup.getOwner();
            final Window parent = owner.getParent();
            final int x1 = parent.getX() + owner.getX();
            final int y1 = parent.getY() + 13 + parent.getScrollOffset() + owner.getY();
            GL11.glPushMatrix();
            GL11.glTranslated((double)x1, (double)y1, 0.0);
            final int cMouseX = mouseX - x1;
            final int cMouseY = mouseY - y1;
            popup.render(cMouseX, cMouseY);
            GL11.glPopMatrix();
        }
        if (this.tooltip != null) {
            final String[] lines = this.tooltip.split("\n");
            final Minecraft mc = Minecraft.getMinecraft();
            final FontRenderer fr = WMinecraft.getFontRenderer();
            int tw = 0;
            final int th = lines.length * fr.FONT_HEIGHT;
            for (final String line : lines) {
                final int lw = fr.getStringWidth(line);
                if (lw > tw) {
                    tw = lw;
                }
            }
            final int sw = mc.currentScreen.width;
            final int sh = mc.currentScreen.height;
            final int xt1 = (mouseX + tw + 11 <= sw) ? (mouseX + 8) : (mouseX - tw - 8);
            final int xt2 = xt1 + tw + 3;
            final int yt1 = (mouseY + th - 2 <= sh) ? (mouseY - 4) : (mouseY - th - 4);
            final int yt2 = yt1 + th + 2;
            GL11.glDisable(3553);
            GL11.glColor4f(this.bgColor[0], this.bgColor[1], this.bgColor[2], 0.75f);
            GL11.glBegin(7);
            GL11.glVertex2i(xt1, yt1);
            GL11.glVertex2i(xt1, yt2);
            GL11.glVertex2i(xt2, yt2);
            GL11.glVertex2i(xt2, yt1);
            GL11.glEnd();
            GL11.glColor4f(this.acColor[0], this.acColor[1], this.acColor[2], 0.5f);
            GL11.glBegin(2);
            GL11.glVertex2i(xt1, yt1);
            GL11.glVertex2i(xt1, yt2);
            GL11.glVertex2i(xt2, yt2);
            GL11.glVertex2i(xt2, yt1);
            GL11.glEnd();
            GL11.glEnable(3553);
            for (int j = 0; j < lines.length; ++j) {
                fr.drawString(lines[j], xt1 + 2, yt1 + 2 + j * fr.FONT_HEIGHT, 16777215);
            }
        }
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public void renderPinnedWindows(final float partialTicks) {
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glLineWidth(1.0f);
        for (final Window window : this.windows) {
            if (window.isPinned() && !window.isInvisible()) {
                this.renderWindow(window, Integer.MIN_VALUE, Integer.MIN_VALUE, partialTicks);
            }
        }
        GL11.glEnable(2884);
        GL11.glEnable(3553);
    }
    
    public void updateColors() {
        final HackList hax = ForgeWurst.getForgeWurst().getHax();
        this.opacity = hax.clickGuiHack.getOpacity();
        this.bgColor = hax.clickGuiHack.getBgColor();
        this.maxHeight = hax.clickGuiHack.getMaxHeight();
        if (hax.rainbowUiHack.isEnabled()) {
            final float x = System.currentTimeMillis() % 2000L / 1000.0f;
            this.acColor[0] = 0.5f + 0.5f * (float)Math.sin(x * 3.141592653589793);
            this.acColor[1] = 0.5f + 0.5f * (float)Math.sin((x + 1.3333334f) * 3.141592653589793);
            this.acColor[2] = 0.5f + 0.5f * (float)Math.sin((x + 2.6666667f) * 3.141592653589793);
        }
        else {
            this.acColor = hax.clickGuiHack.getAcColor();
        }
    }
    
    private void renderWindow(final Window window, final int mouseX, final int mouseY, final float partialTicks) {
        final int x1 = window.getX();
        final int y1 = window.getY();
        final int x2 = x1 + window.getWidth();
        int y2 = y1 + window.getHeight();
        final int y3 = y1 + 13;
        if (window.isMinimized()) {
            y2 = y3;
        }
        if (mouseX >= x1 && mouseY >= y1 && mouseX < x2 && mouseY < y2) {
            this.tooltip = null;
        }
        GL11.glDisable(3553);
        if (!window.isMinimized()) {
            window.setMaxHeight(this.maxHeight);
            window.validate();
            if (window.isScrollingEnabled()) {
                final int xs1 = x2 - 3;
                final int xs2 = xs1 + 2;
                final int xs3 = x2;
                final double outerHeight = y2 - y3;
                final double innerHeight = window.getInnerHeight();
                final double maxScrollbarHeight = outerHeight - 2.0;
                final double scrollbarY = outerHeight * (-window.getScrollOffset() / innerHeight) + 1.0;
                final double scrollbarHeight = maxScrollbarHeight * outerHeight / innerHeight;
                final int ys1 = y3;
                final int ys2 = y2;
                final int ys3 = ys1 + (int)scrollbarY;
                final int ys4 = ys3 + (int)scrollbarHeight;
                GL11.glColor4f(this.bgColor[0], this.bgColor[1], this.bgColor[2], this.opacity);
                GL11.glBegin(7);
                GL11.glVertex2i(xs2, ys1);
                GL11.glVertex2i(xs2, ys2);
                GL11.glVertex2i(xs3, ys2);
                GL11.glVertex2i(xs3, ys1);
                GL11.glVertex2i(xs1, ys1);
                GL11.glVertex2i(xs1, ys3);
                GL11.glVertex2i(xs2, ys3);
                GL11.glVertex2i(xs2, ys1);
                GL11.glVertex2i(xs1, ys4);
                GL11.glVertex2i(xs1, ys2);
                GL11.glVertex2i(xs2, ys2);
                GL11.glVertex2i(xs2, ys4);
                GL11.glEnd();
                final boolean hovering = mouseX >= xs1 && mouseY >= ys3 && mouseX < xs2 && mouseY < ys4;
                GL11.glColor4f(this.acColor[0], this.acColor[1], this.acColor[2], hovering ? (this.opacity * 1.5f) : this.opacity);
                GL11.glBegin(7);
                GL11.glVertex2i(xs1, ys3);
                GL11.glVertex2i(xs1, ys4);
                GL11.glVertex2i(xs2, ys4);
                GL11.glVertex2i(xs2, ys3);
                GL11.glEnd();
                GL11.glColor4f(this.acColor[0], this.acColor[1], this.acColor[2], 0.5f);
                GL11.glBegin(2);
                GL11.glVertex2i(xs1, ys3);
                GL11.glVertex2i(xs1, ys4);
                GL11.glVertex2i(xs2, ys4);
                GL11.glVertex2i(xs2, ys3);
                GL11.glEnd();
            }
            final int x3 = x1 + 2;
            final int x4 = window.isScrollingEnabled() ? (x2 - 3) : x2;
            final int x5 = x4 - 2;
            final int y4 = y3 + window.getScrollOffset();
            GL11.glColor4f(this.bgColor[0], this.bgColor[1], this.bgColor[2], this.opacity);
            GL11.glBegin(7);
            GL11.glVertex2i(x1, y3);
            GL11.glVertex2i(x1, y2);
            GL11.glVertex2i(x3, y2);
            GL11.glVertex2i(x3, y3);
            GL11.glVertex2i(x5, y3);
            GL11.glVertex2i(x5, y2);
            GL11.glVertex2i(x4, y2);
            GL11.glVertex2i(x4, y3);
            GL11.glEnd();
            if (window.isScrollingEnabled()) {
                final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                final int sf = sr.getScaleFactor();
                GL11.glScissor(x1 * sf, (int)((sr.getScaledHeight_double() - y2) * sf), window.getWidth() * sf, (y2 - y3) * sf);
                GL11.glEnable(3089);
            }
            GL11.glPushMatrix();
            GL11.glTranslated((double)x1, (double)y4, 0.0);
            GL11.glColor4f(this.bgColor[0], this.bgColor[1], this.bgColor[2], this.opacity);
            GL11.glBegin(7);
            final int xc1 = 2;
            final int xc2 = x5 - x1;
            for (int i = 0; i < window.countChildren(); ++i) {
                final int yc1 = window.getChild(i).getY();
                final int yc2 = yc1 - 2;
                GL11.glVertex2i(xc1, yc2);
                GL11.glVertex2i(xc1, yc1);
                GL11.glVertex2i(xc2, yc1);
                GL11.glVertex2i(xc2, yc2);
            }
            int yc3;
            if (window.countChildren() == 0) {
                yc3 = 0;
            }
            else {
                final Component lastChild = window.getChild(window.countChildren() - 1);
                yc3 = lastChild.getY() + lastChild.getHeight();
            }
            final int yc4 = yc3 + 2;
            GL11.glVertex2i(xc1, yc4);
            GL11.glVertex2i(xc1, yc3);
            GL11.glVertex2i(xc2, yc3);
            GL11.glVertex2i(xc2, yc4);
            GL11.glEnd();
            final int cMouseX = mouseX - x1;
            final int cMouseY = mouseY - y4;
            for (int j = 0; j < window.countChildren(); ++j) {
                window.getChild(j).render(cMouseX, cMouseY, partialTicks);
            }
            GL11.glPopMatrix();
            if (window.isScrollingEnabled()) {
                GL11.glDisable(3089);
            }
        }
        GL11.glColor4f(this.acColor[0], this.acColor[1], this.acColor[2], 0.5f);
        GL11.glBegin(2);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y2);
        GL11.glVertex2i(x2, y2);
        GL11.glVertex2i(x2, y1);
        GL11.glEnd();
        if (!window.isMinimized()) {
            GL11.glBegin(1);
            GL11.glVertex2i(x1, y3);
            GL11.glVertex2i(x2, y3);
            GL11.glEnd();
        }
        int x3 = x2;
        final int y5 = y1 + 2;
        final int y6 = y3 - 2;
        final boolean hoveringY = mouseY >= y5 && mouseY < y6;
        if (window.isClosable()) {
            x3 -= 11;
            final int x6 = x3 + 9;
            final boolean hovering2 = hoveringY && mouseX >= x3 && mouseX < x6;
            this.renderCloseButton(x3, y5, x6, y6, hovering2);
        }
        if (window.isPinnable()) {
            x3 -= 11;
            final int x6 = x3 + 9;
            final boolean hovering2 = hoveringY && mouseX >= x3 && mouseX < x6;
            this.renderPinButton(x3, y5, x6, y6, hovering2, window.isPinned());
        }
        if (window.isMinimizable()) {
            x3 -= 11;
            final int x6 = x3 + 9;
            final boolean hovering2 = hoveringY && mouseX >= x3 && mouseX < x6;
            this.renderMinimizeButton(x3, y5, x6, y6, hovering2, window.isMinimized());
        }
        GL11.glColor4f(this.acColor[0], this.acColor[1], this.acColor[2], this.opacity);
        GL11.glBegin(7);
        GL11.glVertex2i(x3, y1);
        GL11.glVertex2i(x3, y5);
        GL11.glVertex2i(x2, y5);
        GL11.glVertex2i(x2, y1);
        GL11.glVertex2i(x3, y6);
        GL11.glVertex2i(x3, y3);
        GL11.glVertex2i(x2, y3);
        GL11.glVertex2i(x2, y6);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y3);
        GL11.glVertex2i(x3, y3);
        GL11.glVertex2i(x3, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        final FontRenderer fontRenderer = WMinecraft.getFontRenderer();
        final String title = fontRenderer.trimStringToWidth(window.getTitle(), x3 - x1);
        fontRenderer.drawString(title, x1 + 2, y1 + 3, 15790320);
    }
    
    private void renderTitleBarButton(final int x1, final int y1, final int x2, final int y2, final boolean hovering) {
        final int x3 = x2 + 2;
        GL11.glColor4f(this.bgColor[0], this.bgColor[1], this.bgColor[2], hovering ? (this.opacity * 1.5f) : this.opacity);
        GL11.glBegin(7);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y2);
        GL11.glVertex2i(x2, y2);
        GL11.glVertex2i(x2, y1);
        GL11.glEnd();
        GL11.glColor4f(this.acColor[0], this.acColor[1], this.acColor[2], this.opacity);
        GL11.glBegin(7);
        GL11.glVertex2i(x2, y1);
        GL11.glVertex2i(x2, y2);
        GL11.glVertex2i(x3, y2);
        GL11.glVertex2i(x3, y1);
        GL11.glEnd();
        GL11.glColor4f(this.acColor[0], this.acColor[1], this.acColor[2], 0.5f);
        GL11.glBegin(2);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y2);
        GL11.glVertex2i(x2, y2);
        GL11.glVertex2i(x2, y1);
        GL11.glEnd();
    }
    
    private void renderMinimizeButton(final int x1, final int y1, final int x2, final int y2, final boolean hovering, final boolean minimized) {
        this.renderTitleBarButton(x1, y1, x2, y2, hovering);
        final double xa1 = x1 + 1;
        final double xa2 = (x1 + x2) / 2.0;
        final double xa3 = x2 - 1;
        double ya1;
        double ya2;
        if (minimized) {
            ya1 = y1 + 3;
            ya2 = y2 - 2.5;
            GL11.glColor4f(0.0f, hovering ? 1.0f : 0.85f, 0.0f, 1.0f);
        }
        else {
            ya1 = y2 - 3;
            ya2 = y1 + 2.5;
            GL11.glColor4f(hovering ? 1.0f : 0.85f, 0.0f, 0.0f, 1.0f);
        }
        GL11.glBegin(4);
        GL11.glVertex2d(xa1, ya1);
        GL11.glVertex2d(xa3, ya1);
        GL11.glVertex2d(xa2, ya2);
        GL11.glEnd();
        GL11.glColor4f(0.0625f, 0.0625f, 0.0625f, 0.5f);
        GL11.glBegin(2);
        GL11.glVertex2d(xa1, ya1);
        GL11.glVertex2d(xa3, ya1);
        GL11.glVertex2d(xa2, ya2);
        GL11.glEnd();
    }
    
    private void renderPinButton(final int x1, final int y1, final int x2, final int y2, final boolean hovering, final boolean pinned) {
        this.renderTitleBarButton(x1, y1, x2, y2, hovering);
        final float h = hovering ? 1.0f : 0.85f;
        if (pinned) {
            final double xk1 = x1 + 2;
            final double xk2 = x2 - 2;
            final double xk3 = x1 + 1;
            final double xk4 = x2 - 1;
            final double yk1 = y1 + 2;
            final double yk2 = y2 - 2;
            final double yk3 = y2 - 0.5;
            GL11.glColor4f(h, 0.0f, 0.0f, 0.5f);
            GL11.glBegin(7);
            GL11.glVertex2d(xk1, yk1);
            GL11.glVertex2d(xk2, yk1);
            GL11.glVertex2d(xk2, yk2);
            GL11.glVertex2d(xk1, yk2);
            GL11.glVertex2d(xk3, yk2);
            GL11.glVertex2d(xk4, yk2);
            GL11.glVertex2d(xk4, yk3);
            GL11.glVertex2d(xk3, yk3);
            GL11.glEnd();
            final double xn1 = x1 + 3.5;
            final double xn2 = x2 - 3.5;
            final double yn1 = y2 - 0.5;
            final double yn2 = y2;
            GL11.glColor4f(h, h, h, 1.0f);
            GL11.glBegin(7);
            GL11.glVertex2d(xn1, yn1);
            GL11.glVertex2d(xn2, yn1);
            GL11.glVertex2d(xn2, yn2);
            GL11.glVertex2d(xn1, yn2);
            GL11.glEnd();
            GL11.glColor4f(0.0625f, 0.0625f, 0.0625f, 0.5f);
            GL11.glBegin(2);
            GL11.glVertex2d(xk1, yk1);
            GL11.glVertex2d(xk2, yk1);
            GL11.glVertex2d(xk2, yk2);
            GL11.glVertex2d(xk1, yk2);
            GL11.glEnd();
            GL11.glBegin(2);
            GL11.glVertex2d(xk3, yk2);
            GL11.glVertex2d(xk4, yk2);
            GL11.glVertex2d(xk4, yk3);
            GL11.glVertex2d(xk3, yk3);
            GL11.glEnd();
            GL11.glBegin(2);
            GL11.glVertex2d(xn1, yn1);
            GL11.glVertex2d(xn2, yn1);
            GL11.glVertex2d(xn2, yn2);
            GL11.glVertex2d(xn1, yn2);
            GL11.glEnd();
        }
        else {
            final double xk1 = x2 - 3.5;
            final double xk2 = x2 - 0.5;
            final double xk3 = x2 - 3;
            final double xk4 = x1 + 3;
            final double xk5 = x1 + 2;
            final double xk6 = x2 - 2;
            final double xk7 = x1 + 1;
            final double yk4 = y1 + 0.5;
            final double yk5 = y1 + 3.5;
            final double yk6 = y2 - 3;
            final double yk7 = y1 + 3;
            final double yk8 = y1 + 2;
            final double yk9 = y2 - 2;
            final double yk10 = y2 - 1;
            GL11.glColor4f(0.0f, h, 0.0f, 1.0f);
            GL11.glBegin(7);
            GL11.glVertex2d(xk1, yk4);
            GL11.glVertex2d(xk2, yk5);
            GL11.glVertex2d(xk3, yk6);
            GL11.glVertex2d(xk4, yk7);
            GL11.glVertex2d(xk5, yk8);
            GL11.glVertex2d(xk6, yk9);
            GL11.glVertex2d(xk3, yk10);
            GL11.glVertex2d(xk7, yk7);
            GL11.glEnd();
            final double xn3 = x1 + 3;
            final double xn4 = x1 + 4;
            final double xn5 = x1 + 1;
            final double yn3 = y2 - 4;
            final double yn4 = y2 - 3;
            final double yn5 = y2 - 1;
            GL11.glColor4f(h, h, h, 1.0f);
            GL11.glBegin(4);
            GL11.glVertex2d(xn3, yn3);
            GL11.glVertex2d(xn4, yn4);
            GL11.glVertex2d(xn5, yn5);
            GL11.glEnd();
            GL11.glColor4f(0.0625f, 0.0625f, 0.0625f, 0.5f);
            GL11.glBegin(2);
            GL11.glVertex2d(xk1, yk4);
            GL11.glVertex2d(xk2, yk5);
            GL11.glVertex2d(xk3, yk6);
            GL11.glVertex2d(xk4, yk7);
            GL11.glEnd();
            GL11.glBegin(2);
            GL11.glVertex2d(xk5, yk8);
            GL11.glVertex2d(xk6, yk9);
            GL11.glVertex2d(xk3, yk10);
            GL11.glVertex2d(xk7, yk7);
            GL11.glEnd();
            GL11.glBegin(2);
            GL11.glVertex2d(xn3, yn3);
            GL11.glVertex2d(xn4, yn4);
            GL11.glVertex2d(xn5, yn5);
            GL11.glEnd();
        }
    }
    
    private void renderCloseButton(final int x1, final int y1, final int x2, final int y2, final boolean hovering) {
        this.renderTitleBarButton(x1, y1, x2, y2, hovering);
        final double xc1 = x1 + 2;
        final double xc2 = x1 + 3;
        final double xc3 = x2 - 2;
        final double xc4 = x2 - 3;
        final double xc5 = x1 + 3.5;
        final double xc6 = (x1 + x2) / 2.0;
        final double xc7 = x2 - 3.5;
        final double yc1 = y1 + 3;
        final double yc2 = y1 + 2;
        final double yc3 = y2 - 3;
        final double yc4 = y2 - 2;
        final double yc5 = y1 + 3.5;
        final double yc6 = (y1 + y2) / 2.0;
        final double yc7 = y2 - 3.5;
        GL11.glColor4f(hovering ? 1.0f : 0.85f, 0.0f, 0.0f, 1.0f);
        GL11.glBegin(7);
        GL11.glVertex2d(xc1, yc1);
        GL11.glVertex2d(xc2, yc2);
        GL11.glVertex2d(xc3, yc3);
        GL11.glVertex2d(xc4, yc4);
        GL11.glVertex2d(xc3, yc1);
        GL11.glVertex2d(xc4, yc2);
        GL11.glVertex2d(xc6, yc5);
        GL11.glVertex2d(xc7, yc6);
        GL11.glVertex2d(xc6, yc7);
        GL11.glVertex2d(xc5, yc6);
        GL11.glVertex2d(xc1, yc3);
        GL11.glVertex2d(xc2, yc4);
        GL11.glEnd();
        GL11.glColor4f(0.0625f, 0.0625f, 0.0625f, 0.5f);
        GL11.glBegin(2);
        GL11.glVertex2d(xc1, yc1);
        GL11.glVertex2d(xc2, yc2);
        GL11.glVertex2d(xc6, yc5);
        GL11.glVertex2d(xc4, yc2);
        GL11.glVertex2d(xc3, yc1);
        GL11.glVertex2d(xc7, yc6);
        GL11.glVertex2d(xc3, yc3);
        GL11.glVertex2d(xc4, yc4);
        GL11.glVertex2d(xc6, yc7);
        GL11.glVertex2d(xc2, yc4);
        GL11.glVertex2d(xc1, yc3);
        GL11.glVertex2d(xc5, yc6);
        GL11.glEnd();
    }
    
    public float[] getBgColor() {
        return this.bgColor;
    }
    
    public float[] getAcColor() {
        return this.acColor;
    }
    
    public float getOpacity() {
        return this.opacity;
    }
    
    public void setTooltip(final String tooltip) {
        this.tooltip = tooltip;
    }
    
    public void addWindow(final Window window) {
        this.windows.add(window);
    }
    
    public void addPopup(final Popup popup) {
        this.popups.add(popup);
    }
}
