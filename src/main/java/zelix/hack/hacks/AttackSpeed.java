package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;

public class AttackSpeed extends Hack
{
    NumberValue speed;
    
    public AttackSpeed() {
        super("AttackSpeed", HackCategory.ANOTHER);
        this.speed = new NumberValue("Ticks", 1.0, 0.0, 5.0);
        this.addValue(this.speed);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        Minecraft.getMinecraft().player.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue((double)this.speed.getValue());
    }
}
