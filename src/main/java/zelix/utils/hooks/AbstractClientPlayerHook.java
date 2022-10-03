package zelix.utils.hooks;

import net.minecraft.client.entity.*;
import net.minecraft.world.*;
import com.mojang.authlib.*;
import net.minecraft.util.*;

public class AbstractClientPlayerHook extends AbstractClientPlayer
{
    public AbstractClientPlayerHook(final World worldIn, final GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }
    
    public ResourceLocation getLocationCape() {
        return null;
    }
}
