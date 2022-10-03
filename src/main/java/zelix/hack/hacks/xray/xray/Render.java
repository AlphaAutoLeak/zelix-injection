package zelix.hack.hacks.xray.xray;

import zelix.hack.hacks.xray.reference.block.*;
import net.minecraft.client.renderer.vertex.*;
import zelix.hack.hacks.xray.utils.*;
import java.util.*;
import net.minecraft.client.renderer.*;
import zelix.hack.hacks.xray.*;

public class Render
{
    public static List<BlockInfo> ores;
    private static final int GL_FRONT_AND_BACK = 1032;
    private static final int GL_LINE = 6913;
    private static final int GL_FILL = 6914;
    private static final int GL_LINES = 1;
    
    public static void drawOres(final float playerX, final float playerY, final float playerZ) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();
        Profile.BLOCKS.apply();
        buffer.setTranslation((double)(-playerX), (double)(-playerY), (double)(-playerZ));
        final BufferBuilder buffer2 = buffer;
        final Tessellator tessellator2 = tessellator;
        Render.ores.forEach(b -> {
            buffer2.begin(1, DefaultVertexFormats.POSITION_COLOR);
            Utils.renderBlockBounding(buffer2, b, (int)b.alpha);
            tessellator2.draw();
            return;
        });
        buffer.setTranslation(0.0, 0.0, 0.0);
        Profile.BLOCKS.clean();
    }
    
    static {
        Render.ores = Collections.synchronizedList(new ArrayList<BlockInfo>());
    }
    
    private enum Profile
    {
        BLOCKS {
            @Override
            public void apply() {
                GlStateManager.disableTexture2D();
                GlStateManager.disableDepth();
                GlStateManager.depthMask(false);
                GlStateManager.glPolygonMode(1032, 6913);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.enableBlend();
                GlStateManager.glLineWidth((float)Configuration.outlineThickness);
            }
            
            @Override
            public void clean() {
                GlStateManager.glPolygonMode(1032, 6914);
                GlStateManager.disableBlend();
                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                GlStateManager.enableTexture2D();
            }
        }, 
        ENTITIES {
            @Override
            public void apply() {
            }
            
            @Override
            public void clean() {
            }
        };
        
        public abstract void apply();
        
        public abstract void clean();
    }
}
