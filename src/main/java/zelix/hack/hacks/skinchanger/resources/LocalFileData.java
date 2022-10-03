package zelix.hack.hacks.skinchanger.resources;

import java.awt.image.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import net.minecraft.client.resources.*;
import javax.imageio.*;
import zelix.utils.hooks.visual.*;
import java.io.*;
import net.minecraft.client.renderer.texture.*;

public class LocalFileData extends SimpleTexture
{
    private BufferedImage bufferedImage;
    private boolean textureUploaded;
    private final IImageBuffer imageBuffer;
    private final File fileLocation;
    
    public LocalFileData(final ResourceLocation textureLocation, final File fileToLoad) {
        this(textureLocation, fileToLoad, null);
    }
    
    public LocalFileData(final ResourceLocation textureLocation, final File fileToLoad, final IImageBuffer imageBuffer) {
        super(textureLocation);
        this.fileLocation = fileToLoad;
        this.imageBuffer = imageBuffer;
    }
    
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
        if (this.bufferedImage == null && this.textureLocation != null) {
            super.loadTexture(resourceManager);
        }
        if (this.fileLocation != null && this.fileLocation.isFile()) {
            try {
                this.bufferedImage = ImageIO.read(this.fileLocation);
                if (this.imageBuffer != null) {
                    this.bufferedImage = this.imageBuffer.parseUserSkin(this.bufferedImage);
                    if (this.imageBuffer != null) {
                        this.imageBuffer.skinAvailable();
                    }
                }
            }
            catch (IOException ex) {
                ChatUtils.error("SkinChanger: Unable to read file.");
            }
        }
        else {
            ChatUtils.error("SkinChanger: File did not exist.");
        }
    }
    
    public int getGlTextureId() {
        this.checkTextureUploaded();
        return super.getGlTextureId();
    }
    
    private void checkTextureUploaded() {
        if (!this.textureUploaded && this.bufferedImage != null) {
            if (this.textureLocation != null) {
                this.deleteGlTexture();
            }
            TextureUtil.uploadTextureImage(super.getGlTextureId(), this.bufferedImage);
            this.textureUploaded = true;
        }
    }
}
