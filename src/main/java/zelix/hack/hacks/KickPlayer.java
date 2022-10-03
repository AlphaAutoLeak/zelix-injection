package zelix.hack.hacks;

import zelix.hack.*;
import zelix.utils.*;
import net.minecraft.client.entity.*;
import com.mojang.authlib.*;
import net.minecraft.network.login.client.*;
import net.minecraft.network.*;

public class KickPlayer extends Hack
{
    public static String kickname;
    
    public KickPlayer() {
        super("KickPlayer", HackCategory.ANOTHER);
    }
    
    @Override
    public void onEnable() {
        if (KickPlayer.kickname == null) {
            return;
        }
        final Wrapper instance = Wrapper.INSTANCE;
        Wrapper.INSTANCE.player();
        instance.sendPacket((Packet)new CPacketLoginStart(new GameProfile(EntityPlayerSP.getUUID(Wrapper.INSTANCE.player().getGameProfile()), KickPlayer.kickname)));
        super.onEnable();
    }
}
