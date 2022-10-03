package zelix.gui.clickguis.tenacity.Frame;

import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import org.lwjgl.input.*;
import zelix.utils.tenacityutils.render.*;
import zelix.utils.hooks.visual.font.*;

public class tenacity
{
    public ScaledResolution scaledResolution;
    public int width;
    public int height;
    public boolean onMoving;
    public float moveX;
    public float moveY;
    public int x;
    public int y;
    
    public tenacity() {
        this.scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        this.width = 335;
        this.height = 235;
        this.onMoving = false;
        this.moveX = 0.0f;
        this.moveY = 0.0f;
        this.x = this.scaledResolution.getScaledWidth() / 2 - this.width / 2;
        this.y = this.scaledResolution.getScaledHeight() / 2 - this.height / 2;
    }
    
    public void initialize() {
        final Minecraft mc = Minecraft.getMinecraft();
        if (OpenGlHelper.shadersSupported) {
            if (mc.entityRenderer.getShaderGroup() != null) {
                mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (isHovered(this.x, this.y, this.x + this.width, this.y + this.height, mouseX, mouseY) && mouseButton == 0) {
            this.onMoving = true;
        }
    }
    
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.onMoving = false;
    }
    
    public void render(final int mouseX, final int mouseY) {
        if (isHovered(this.x, this.y, this.x + this.width, this.y + this.height, mouseX, mouseY) && Mouse.isButtonDown(0) && this.onMoving) {
            if (this.moveX == 0.0f && this.moveY == 0.0f) {
                this.moveX = mouseX - this.x;
                this.moveY = mouseY - this.y;
            }
            else {
                this.x = (int)(mouseX - this.moveX);
                this.y = (int)(mouseY - this.moveY);
                final float n = this.y + 20;
            }
        }
        else if (this.moveX != 0.0f || this.moveY != 0.0f) {
            this.moveX = 0.0f;
            this.moveY = 0.0f;
        }
        RenderUtil.renderRoundedRect(this.x, this.y, this.width, this.height, 10.0f, -14869217);
        FontLoaders.default20.drawStringWithShadow("Zelix CloudConfig", this.x + 14, this.y + 19, -1);
        RenderUtil.drawBorderedRect(this.x + 14, this.y + 37, 254.0f, 52.0f, 1.0f, -15526892, -15526892);
    }
    
    public static boolean isHovered(final float x, final float y, final float x2, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}
