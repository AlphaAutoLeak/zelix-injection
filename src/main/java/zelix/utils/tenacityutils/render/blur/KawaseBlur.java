package zelix.utils.tenacityutils.render.blur;

import zelix.utils.ManagerUtil.*;
import net.minecraft.client.shader.*;
import zelix.utils.tenacityutils.render.*;
import java.util.*;

public class KawaseBlur implements Utils
{
    public static ShaderUtil kawaseDown;
    public static ShaderUtil kawaseUp;
    public static Framebuffer framebuffer;
    private static int currentIterations;
    private static final List<Framebuffer> framebufferList;
    
    public static void setupUniforms(final float offset) {
        KawaseBlur.kawaseDown.setUniformf("offset", offset, offset);
        KawaseBlur.kawaseUp.setUniformf("offset", offset, offset);
    }
    
    private static void initFramebuffers(final float iterations) {
        for (final Framebuffer framebuffer : KawaseBlur.framebufferList) {
            framebuffer.deleteFramebuffer();
        }
        KawaseBlur.framebufferList.clear();
        KawaseBlur.framebufferList.add(RenderUtil.createFrameBuffer(KawaseBlur.framebuffer));
        for (int i = 1; i <= iterations; ++i) {
            final Framebuffer framebuffer = new Framebuffer(KawaseBlur.mc.displayWidth, KawaseBlur.mc.displayHeight, false);
            KawaseBlur.framebufferList.add(RenderUtil.createFrameBuffer(framebuffer));
        }
    }
    
    public static void renderBlur(final int iterations, final int offset) {
        if (KawaseBlur.currentIterations != iterations) {
            initFramebuffers(iterations);
            KawaseBlur.currentIterations = iterations;
        }
        renderFBO(KawaseBlur.framebufferList.get(1), KawaseBlur.mc.getFramebuffer().framebufferTexture, KawaseBlur.kawaseDown, offset);
        for (int i = 1; i < iterations; ++i) {
            renderFBO(KawaseBlur.framebufferList.get(i + 1), KawaseBlur.framebufferList.get(i).framebufferTexture, KawaseBlur.kawaseDown, offset);
        }
        for (int i = iterations; i > 1; --i) {
            renderFBO(KawaseBlur.framebufferList.get(i - 1), KawaseBlur.framebufferList.get(i).framebufferTexture, KawaseBlur.kawaseUp, offset);
        }
        KawaseBlur.mc.getFramebuffer().bindFramebuffer(true);
        RenderUtil.bindTexture(KawaseBlur.framebufferList.get(1).framebufferTexture);
        KawaseBlur.kawaseUp.init();
        KawaseBlur.kawaseUp.setUniformf("offset", offset, offset);
        KawaseBlur.kawaseUp.setUniformf("halfpixel", 0.5f / KawaseBlur.mc.displayWidth, 0.5f / KawaseBlur.mc.displayHeight);
        KawaseBlur.kawaseUp.setUniformi("inTexture", 0);
        ShaderUtil.drawQuads();
        KawaseBlur.kawaseUp.unload();
    }
    
    private static void renderFBO(final Framebuffer framebuffer, final int framebufferTexture, final ShaderUtil shader, final float offset) {
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        shader.init();
        RenderUtil.bindTexture(framebufferTexture);
        shader.setUniformf("offset", offset, offset);
        shader.setUniformi("inTexture", 0);
        shader.setUniformf("halfpixel", 0.5f / KawaseBlur.mc.displayWidth, 0.5f / KawaseBlur.mc.displayHeight);
        ShaderUtil.drawQuads();
        shader.unload();
        framebuffer.unbindFramebuffer();
    }
    
    static {
        KawaseBlur.kawaseDown = new ShaderUtil("Tenacity/Shaders/kawaseDown.frag");
        KawaseBlur.kawaseUp = new ShaderUtil("Tenacity/Shaders/kawaseUp.frag");
        KawaseBlur.framebuffer = new Framebuffer(1, 1, false);
        framebufferList = new ArrayList<Framebuffer>();
    }
}
