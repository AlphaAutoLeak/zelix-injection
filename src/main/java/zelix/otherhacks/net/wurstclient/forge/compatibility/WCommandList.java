package zelix.otherhacks.net.wurstclient.forge.compatibility;

import zelix.otherhacks.net.wurstclient.forge.*;
import net.minecraft.util.*;
import net.minecraftforge.registries.*;
import java.util.*;

public abstract class WCommandList
{
    private static IForgeRegistry<Command> registry;
    
    public WCommandList() {
        if (WCommandList.registry != null) {
            throw new IllegalStateException("Multiple instances of CommandList!");
        }
        final RegistryBuilder<Command> registryBuilder = (RegistryBuilder<Command>)new RegistryBuilder();
        registryBuilder.setName(new ResourceLocation("forgewurst", "cmds"));
        registryBuilder.setType((Class)Command.class);
        registryBuilder.disableSaving();
        WCommandList.registry = (IForgeRegistry<Command>)registryBuilder.create();
    }
    
    protected final <T extends Command> T register(final T cmd) {
        cmd.setRegistryName("forgewurst", cmd.getName().toLowerCase());
        WCommandList.registry.register(cmd);
        return cmd;
    }
    
    public final IForgeRegistry<Command> getRegistry() {
        return WCommandList.registry;
    }
    
    public final Collection<Command> getValues() {
        try {
            return (Collection<Command>)WCommandList.registry.getValuesCollection();
        }
        catch (NoSuchMethodError e) {
            return (Collection<Command>)WCommandList.registry.getValues();
        }
    }
    
    public final Command get(final String name) {
        final ResourceLocation location = new ResourceLocation("forgewurst", name.toLowerCase());
        return (Command)WCommandList.registry.getValue(location);
    }
}
