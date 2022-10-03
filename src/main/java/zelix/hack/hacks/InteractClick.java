package zelix.hack.hacks;

import zelix.hack.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.item.*;
import zelix.utils.*;
import org.lwjgl.input.*;
import zelix.managers.*;
import net.minecraft.entity.*;

public class InteractClick extends Hack
{
    public InteractClick() {
        super("InteractClick", HackCategory.COMBAT);
    }
    
    @Override
    public String getDescription() {
        return "Left - Add to Enemys, Rigth - Add to Friends, Wheel - Remove from All.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        final RayTraceResult object = Wrapper.INSTANCE.mc().objectMouseOver;
        if (object == null) {
            return;
        }
        if (object.typeOfHit == RayTraceResult.Type.ENTITY) {
            final Entity entity = object.entityHit;
            if (entity instanceof EntityPlayer && !(entity instanceof EntityArmorStand) && !Wrapper.INSTANCE.player().isDead && Wrapper.INSTANCE.player().canEntityBeSeen(entity)) {
                final EntityPlayer player = (EntityPlayer)entity;
                final String ID = Utils.getPlayerName(player);
                if (Mouse.isButtonDown(1) && Wrapper.INSTANCE.mc().currentScreen == null) {
                    FriendManager.addFriend(ID);
                }
                else if (Mouse.isButtonDown(0) && Wrapper.INSTANCE.mc().currentScreen == null) {
                    EnemyManager.addEnemy(ID);
                }
                else if (Mouse.isButtonDown(2) && Wrapper.INSTANCE.mc().currentScreen == null) {
                    EnemyManager.removeEnemy(ID);
                    FriendManager.removeFriend(ID);
                }
            }
        }
        super.onClientTick(event);
    }
}
