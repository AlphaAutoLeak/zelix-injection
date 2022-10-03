package zelix.utils.tenacityutils.render.blur;

import zelix.utils.ManagerUtil.*;
import net.minecraft.client.shader.*;
import org.lwjgl.*;
import zelix.utils.ManagerUtil.misc.*;
import org.lwjgl.opengl.*;
import java.nio.*;
import net.minecraft.client.renderer.*;
import zelix.utils.tenacityutils.render.*;

public class GaussianBlur implements Utils
{
    public static ShaderUtil blurShader;
    public static Framebuffer framebuffer;
    
    public static void setupUniforms(final float dir1, final float dir2, final float radius) {
        GaussianBlur.blurShader.setUniformi("textureIn", 0);
        GaussianBlur.blurShader.setUniformf("texelSize", 1.0f / GaussianBlur.mc.displayWidth, 1.0f / GaussianBlur.mc.displayHeight);
        GaussianBlur.blurShader.setUniformf("direction", dir1, dir2);
        GaussianBlur.blurShader.setUniformf("radius", radius);
        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; ++i) {
            weightBuffer.put(MathUtils.calculateGaussianValue(i, radius / 2.0f));
        }
        weightBuffer.rewind();
        GL20.glUniform1(GaussianBlur.blurShader.getUniform("weights"), weightBuffer);
    }
    
    public static void renderBlur(final float radius) {
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        (GaussianBlur.framebuffer = RenderUtil.createFrameBuffer(GaussianBlur.framebuffer)).framebufferClear();
        GaussianBlur.framebuffer.bindFramebuffer(true);
        GaussianBlur.blurShader.init();
        setupUniforms(1.0f, 0.0f, radius);
        RenderUtil.bindTexture(GaussianBlur.mc.getFramebuffer().framebufferTexture);
        ShaderUtil.drawQuads();
        GaussianBlur.framebuffer.unbindFramebuffer();
        GaussianBlur.blurShader.unload();
        GaussianBlur.mc.getFramebuffer().bindFramebuffer(true);
        GaussianBlur.blurShader.init();
        setupUniforms(0.0f, 1.0f, radius);
        RenderUtil.bindTexture(GaussianBlur.framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        GaussianBlur.blurShader.unload();
        RenderUtil.resetColor();
        GlStateManager.bindTexture(0);
    }
    
    static {
        GaussianBlur.blurShader = new ShaderUtil("Tenacity/Shaders/gaussian.frag");
        GaussianBlur.framebuffer = new Framebuffer(1, 1, false);
    }
}
