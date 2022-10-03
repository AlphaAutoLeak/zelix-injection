package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.utils.*;

public class Suicide extends Hack
{
    public NumberValue damage;
    
    public Suicide() {
        super("Suicide", HackCategory.COMBAT);
        this.damage = new NumberValue("Damage", 0.35, 0.0125, 0.5);
        this.addValue(this.damage);
    }
    
    @Override
    public String getDescription() {
        return "Kills you.";
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Wrapper.INSTANCE.player().isDead) {
            this.toggle();
        }
        Utils.selfDamage(this.damage.getValue());
        super.onClientTick(event);
    }
}
