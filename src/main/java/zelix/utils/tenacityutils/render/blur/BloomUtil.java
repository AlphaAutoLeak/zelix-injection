package zelix.utils.tenacityutils.render.blur;

import zelix.utils.ManagerUtil.*;
import net.minecraft.client.shader.*;
import zelix.utils.tenacityutils.render.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.*;
import zelix.utils.ManagerUtil.misc.*;
import java.nio.*;
import org.lwjgl.opengl.*;

public class BloomUtil implements Utils
{
    public static ShaderUtil gaussianBloom;
    public static Framebuffer framebuffer;
    
    public static void renderBlur(final int sourceTexture, final int radius, final int offset) {
        BloomUtil.framebuffer = RenderUtil.createFrameBuffer(BloomUtil.framebuffer);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.0f);
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; ++i) {
            weightBuffer.put(MathUtils.calculateGaussianValue(i, radius));
        }
        weightBuffer.rewind();
        RenderUtil.setAlphaLimit(0.0f);
        BloomUtil.framebuffer.framebufferClear();
        BloomUtil.framebuffer.bindFramebuffer(true);
        BloomUtil.gaussianBloom.init();
        setupUniforms(radius, offset, 0, weightBuffer);
        RenderUtil.bindTexture(sourceTexture);
        ShaderUtil.drawQuads();
        BloomUtil.gaussianBloom.unload();
        BloomUtil.framebuffer.unbindFramebuffer();
        BloomUtil.mc.getFramebuffer().bindFramebuffer(true);
        BloomUtil.gaussianBloom.init();
        setupUniforms(radius, 0, offset, weightBuffer);
        GL13.glActiveTexture(34000);
        RenderUtil.bindTexture(sourceTexture);
        GL13.glActiveTexture(33984);
        RenderUtil.bindTexture(BloomUtil.framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        BloomUtil.gaussianBloom.unload();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableAlpha();
        GlStateManager.bindTexture(0);
    }
    
    public static void setupUniforms(final int radius, final int directionX, final int directionY, final FloatBuffer weights) {
        BloomUtil.gaussianBloom.setUniformi("inTexture", 0);
        BloomUtil.gaussianBloom.setUniformi("textureToCheck", 16);
        BloomUtil.gaussianBloom.setUniformf("radius", radius);
        BloomUtil.gaussianBloom.setUniformf("texelSize", 1.0f / BloomUtil.mc.displayWidth, 1.0f / BloomUtil.mc.displayHeight);
        BloomUtil.gaussianBloom.setUniformf("direction", directionX, directionY);
        GL20.glUniform1(BloomUtil.gaussianBloom.getUniform("weights"), weights);
    }
    
    static {
        BloomUtil.gaussianBloom = new ShaderUtil("Tenacity/Shaders/bloom.frag");
        BloomUtil.framebuffer = new Framebuffer(1, 1, false);
    }
}
