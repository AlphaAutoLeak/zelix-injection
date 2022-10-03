package zelix.otherhacks.net.wurstclient.forge.compatibility;

import zelix.otherhacks.net.wurstclient.forge.*;
import net.minecraft.util.*;
import net.minecraftforge.registries.*;
import java.util.*;

public abstract class WHackList
{
    private static IForgeRegistry<Hack> registry;
    
    public WHackList() {
        if (WHackList.registry != null) {
            throw new IllegalStateException("Multiple instances of HackList!");
        }
        final RegistryBuilder<Hack> registryBuilder = (RegistryBuilder<Hack>)new RegistryBuilder();
        registryBuilder.setName(new ResourceLocation("forgewurst", "hax"));
        registryBuilder.setType((Class)Hack.class);
        registryBuilder.disableSaving();
        WHackList.registry = (IForgeRegistry<Hack>)registryBuilder.create();
    }
    
    protected final <T extends Hack> T register(final T hack) {
        hack.setRegistryName("forgewurst", hack.getName().toLowerCase());
        WHackList.registry.register(hack);
        return hack;
    }
    
    public final IForgeRegistry<Hack> getRegistry() {
        return WHackList.registry;
    }
    
    public final Collection<Hack> getValues() {
        try {
            return (Collection<Hack>)WHackList.registry.getValuesCollection();
        }
        catch (NoSuchMethodError e) {
            return (Collection<Hack>)WHackList.registry.getValues();
        }
    }
    
    public final Hack get(final String name) {
        final ResourceLocation location = new ResourceLocation("forgewurst", name.toLowerCase());
        return (Hack)WHackList.registry.getValue(location);
    }
}
