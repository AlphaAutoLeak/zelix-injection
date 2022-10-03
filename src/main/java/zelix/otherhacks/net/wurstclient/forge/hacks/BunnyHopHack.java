package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraft.block.material.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.fml.common.eventhandler.*;
import java.util.function.*;

public final class BunnyHopHack extends Hack
{
    private final EnumSetting<JumpIf> jumpIf;
    
    public BunnyHopHack() {
        super("BunnyHop", "Makes you jump automatically.");
        this.jumpIf = new EnumSetting<JumpIf>("Jump if", JumpIf.values(), JumpIf.SPRINTING);
        this.setCategory(Category.MOVEMENT);
        this.addSetting(this.jumpIf);
    }
    
    @Override
    public String getRenderName() {
        return this.getName() + " [" + this.jumpIf.getSelected().name + "]";
    }
    
    @Override
    protected void onEnable() {
        BunnyHopHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        final EntityPlayerSP player = event.getPlayer();
        if (!player.onGround || player.isSneaking() || player.isInsideOfMaterial(Material.WATER)) {
            return;
        }
        if (this.jumpIf.getSelected().condition.test(player)) {
            player.jump();
        }
    }
    
    private enum JumpIf
    {
        SPRINTING("Sprinting", p -> p.isSprinting() && (p.moveForward != 0.0f || p.moveStrafing != 0.0f)), 
        WALKING("Walking", p -> p.moveForward != 0.0f || p.moveStrafing != 0.0f), 
        ALWAYS("Always", p -> true);
        
        private final String name;
        private final Predicate<EntityPlayerSP> condition;
        
        private JumpIf(final String name, final Predicate<EntityPlayerSP> condition) {
            this.name = name;
            this.condition = condition;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
}
