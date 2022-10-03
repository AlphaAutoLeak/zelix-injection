package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.client.*;
import zelix.utils.*;
import java.lang.reflect.*;

public class FastPlace extends Hack
{
    BooleanValue onBlock;
    NumberValue speed;
    
    public FastPlace() {
        super("FastPlace", HackCategory.PLAYER);
        this.onBlock = new BooleanValue("BlockOnly", Boolean.valueOf(false));
        this.speed = new NumberValue("Speed", 0.0, 0.0, 4.0);
        this.addValue(this.onBlock, this.speed);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.onBlock.getValue()) {
            if (Wrapper.INSTANCE.player().getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock || Wrapper.INSTANCE.player().getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemBlock) {
                final Field field = ReflectionHelper.findField(Minecraft.class, "rightClickDelayTimer", "rightClickDelayTimer");
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    field.setInt(Wrapper.INSTANCE.mc(), (int)(Object)this.speed.getValue());
                }
                catch (Exception ex) {}
            }
        }
        else {
            final Field field = ReflectionHelper.findField(Minecraft.class, "rightClickDelayTimer", "rightClickDelayTimer");
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.setInt(Wrapper.INSTANCE.mc(), (int)(Object)this.speed.getValue());
            }
            catch (Exception ex2) {}
        }
    }
}
