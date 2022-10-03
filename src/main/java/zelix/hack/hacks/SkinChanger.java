package zelix.hack.hacks;

import net.minecraft.util.*;
import zelix.hack.*;
import zelix.utils.*;
import zelix.managers.*;
import com.mojang.authlib.minecraft.*;
import net.minecraft.client.entity.*;
import zelix.utils.hooks.visual.*;

public class SkinChanger extends Hack
{
    public ResourceLocation defaultSkin;
    
    public SkinChanger() {
        super("SkinChanger", HackCategory.VISUAL);
    }
    
    @Override
    public String getDescription() {
        return "Changing your skin/cape. (BETA)";
    }
    
    @Override
    public void onEnable() {
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        if (this.defaultSkin == null) {
            this.defaultSkin = player.getLocationSkin();
        }
        final ResourceLocation location = SkinChangerManager.playerTextures.get(MinecraftProfileTexture.Type.SKIN);
        if (location != null && !SkinChangerManager.setTexture(MinecraftProfileTexture.Type.SKIN, (AbstractClientPlayer)player, location)) {
            this.failed();
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        if (this.defaultSkin != null && !SkinChangerManager.setTexture(MinecraftProfileTexture.Type.SKIN, (AbstractClientPlayer)Wrapper.INSTANCE.player(), this.defaultSkin)) {
            this.failed();
        }
        this.defaultSkin = null;
        super.onDisable();
    }
    
    void failed() {
        ChatUtils.error("SkinChanger: Set texture failed!");
    }
}
