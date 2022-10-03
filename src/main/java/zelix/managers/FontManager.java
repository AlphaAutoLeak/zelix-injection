package zelix.managers;

import net.minecraft.util.*;
import java.util.*;
import zelix.utils.hooks.visual.font.render.*;
import java.awt.*;
import java.util.concurrent.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import zelix.utils.hooks.visual.font.render.GlStateManager;

import java.io.*;

public class FontManager
{
    private ResourceLocation darrow;
    private TTFFontRenderer defaultFont;
    private FontManager instance;
    private HashMap<String, TTFFontRenderer> fonts;
    
    public FontManager getInstance() {
        return this.instance;
    }
    
    public TTFFontRenderer getFont(final String key) {
        return this.fonts.getOrDefault(key, this.defaultFont);
    }
    
    public FontManager() {
        this.darrow = new ResourceLocation("SF-UI-Display-Regular.otf");
        this.fonts = new HashMap<String, TTFFontRenderer>();
        this.instance = this;
        final ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(8);
        final ConcurrentLinkedQueue<TextureData> textureQueue = new ConcurrentLinkedQueue<TextureData>();
        this.defaultFont = new TTFFontRenderer(executorService, textureQueue, new Font("Verdana", 0, 18));
        try {
            for (final int i : new int[] { 6, 7, 8, 10, 11, 12, 14 }) {
                final ResourceLocation font = new ResourceLocation("rsaaaaaa", "Suffer-through-2.ttf");
                font.getResourcePath();
                final InputStream istream = this.getClass().getResourceAsStream(font.getResourcePath());
                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(0, i);
                this.fonts.put("SFR " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            for (final int i : new int[] { 6, 7, 8, 9, 11 }) {
                final ResourceLocation font = new ResourceLocation("rsaaaaaa", "the-antter-2.ttf");
                final InputStream istream = this.getClass().getResourceAsStream(font.getResourcePath());
                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(0, i);
                this.fonts.put("SFB " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            for (final int i : new int[] { 6, 7, 8, 9, 11, 12 }) {
                final ResourceLocation font = new ResourceLocation("rsaaaaaa", "SF-UI-Display-Bold.otf");
                font.getResourcePath();
                final InputStream istream = this.getClass().getResourceAsStream(font.getResourcePath());
                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(0, i);
                this.fonts.put("SFM " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            for (final int i : new int[] { 17, 10, 9, 8, 7, 6 }) {
                final InputStream istream2 = this.getClass().getResourceAsStream("assets/shadow/SF-UI-Display-Light.otf");
                Font myFont2 = Font.createFont(0, istream2);
                myFont2 = myFont2.deriveFont(0, i);
                this.fonts.put("SFL " + i, new TTFFontRenderer(executorService, textureQueue, myFont2));
            }
            for (final int i : new int[] { 19 }) {
                final ResourceLocation font = new ResourceLocation("rsaaaaaa", "Jigsaw-Regular.otf");
                font.getResourcePath();
                final InputStream istream = this.getClass().getResourceAsStream(font.getResourcePath());
                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(0, i);
                this.fonts.put("JIGR " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            this.fonts.put("Verdana 12", new TTFFontRenderer(executorService, textureQueue, new Font("Verdana", 0, 12)));
            this.fonts.put("Verdana Bold 16", new TTFFontRenderer(executorService, textureQueue, new Font("Verdana Bold", 0, 16)));
            this.fonts.put("Verdana Bold 20", new TTFFontRenderer(executorService, textureQueue, new Font("Verdana Bold", 0, 20)));
        }
        catch (Exception ex) {}
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(10L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!textureQueue.isEmpty()) {
                final TextureData textureData = textureQueue.poll();
                GlStateManager.bindTexture(textureData.getTextureId());
                GL11.glTexParameteri(3553, 10241, 9728);
                GL11.glTexParameteri(3553, 10240, 9728);
                GL11.glTexImage2D(3553, 0, 6408, textureData.getWidth(), textureData.getHeight(), 0, 6408, 5121, textureData.getBuffer());
            }
        }
    }
}
