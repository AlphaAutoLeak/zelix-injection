package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import zelix.utils.*;

public class SelfDamage extends Hack
{
    public NumberValue damage;
    
    public SelfDamage() {
        super("SelfDamage", HackCategory.COMBAT);
        this.damage = new NumberValue("Damage", 0.0625, 0.0125, 0.35);
        this.addValue(this.damage);
    }
    
    @Override
    public String getDescription() {
        return "Deals damage to you (useful for bypassing AC).";
    }
    
    @Override
    public void onEnable() {
        Utils.selfDamage(this.damage.getValue());
        this.toggle();
        super.onEnable();
    }
}
