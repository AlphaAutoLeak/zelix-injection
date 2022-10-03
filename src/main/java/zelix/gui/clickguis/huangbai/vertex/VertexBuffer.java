package zelix.gui.clickguis.huangbai.vertex;

import java.nio.*;
import org.lwjgl.opengl.*;

public class VertexBuffer
{
    private int glBufferId;
    private final VertexFormat vertexFormat;
    private int count;
    
    public VertexBuffer(final VertexFormat vertexFormatIn) {
        this.vertexFormat = vertexFormatIn;
    }
    
    public void bindBuffer() {
    }
    
    public void func_181722_a(final ByteBuffer p_181722_1_) {
        this.bindBuffer();
        this.unbindBuffer();
        this.count = p_181722_1_.limit() / this.vertexFormat.getNextOffset();
    }
    
    public void drawArrays(final int mode) {
        GL11.glDrawArrays(mode, 0, this.count);
    }
    
    public void unbindBuffer() {
    }
    
    public void deleteGlBuffers() {
        if (this.glBufferId >= 0) {
            this.glBufferId = -1;
        }
    }
}
