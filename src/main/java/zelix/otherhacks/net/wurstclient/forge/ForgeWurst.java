package zelix.otherhacks.net.wurstclient.forge;

import zelix.otherhacks.net.wurstclient.forge.clickgui.*;
import net.minecraft.client.*;
import net.minecraftforge.fml.common.asm.transformers.deobf.*;
import java.nio.file.attribute.*;
import java.nio.file.*;
import java.io.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import com.google.common.collect.*;
import com.google.common.reflect.*;
import java.lang.annotation.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.*;
import java.lang.reflect.*;
import java.util.*;

public final class ForgeWurst
{
    public static final String MODID = "forgewurst";
    public static final String VERSION = "0.11";
    private static ForgeWurst forgeWurst;
    private boolean obfuscated;
    private Path configFolder;
    private HackList hax;
    private CommandList cmds;
    private KeybindList keybinds;
    private ClickGui gui;
    private IngameHUD hud;
    private CommandProcessor cmdProcessor;
    private KeybindProcessor keybindProcessor;
    private Method register;
    
    public ForgeWurst() {
        (ForgeWurst.forgeWurst = this).init();
    }
    
    public void init() {
        try {
            (this.register = EventBus.class.getDeclaredMethod("register", Class.class, Object.class, Method.class, ModContainer.class)).setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        final String mcClassName = Minecraft.class.getName().replace(".", "/");
        final FMLDeobfuscatingRemapper remapper = FMLDeobfuscatingRemapper.INSTANCE;
        this.obfuscated = !mcClassName.equals(remapper.unmap(mcClassName));
        this.configFolder = Minecraft.getMinecraft().mcDataDir.toPath().resolve("wurst");
        try {
            Files.createDirectories(this.configFolder, (FileAttribute<?>[])new FileAttribute[0]);
        }
        catch (IOException e2) {
            throw new RuntimeException(e2);
        }
        (this.hax = new HackList(this.configFolder.resolve("enabled-hacks.json"), this.configFolder.resolve("settings.json"))).loadEnabledHacks();
        this.hax.loadSettings();
        this.cmds = new CommandList();
        (this.keybinds = new KeybindList(this.configFolder.resolve("keybinds.json"))).init();
        (this.gui = new ClickGui(this.configFolder.resolve("windows.json"))).init(this.hax);
        this.register(this.hud = new IngameHUD(this.hax, this.gui));
        this.register(this.cmdProcessor = new CommandProcessor(this.cmds));
        this.register(this.keybindProcessor = new KeybindProcessor(this.hax, this.keybinds, this.cmdProcessor));
        this.register(this.keybindProcessor);
        this.register(new WEventFactory());
    }
    
    public static ForgeWurst getForgeWurst() {
        return ForgeWurst.forgeWurst;
    }
    
    public boolean isObfuscated() {
        return this.obfuscated;
    }
    
    public HackList getHax() {
        return this.hax;
    }
    
    public CommandList getCmds() {
        return this.cmds;
    }
    
    public KeybindList getKeybinds() {
        return this.keybinds;
    }
    
    public ClickGui getGui() {
        return this.gui;
    }
    
    public void register(final Object target) {
        final boolean isStatic = target.getClass() == Class.class;
        final Set<? extends Class<?>> supers = (Set<? extends Class<?>>)(isStatic ? Sets.newHashSet((Object[])new Class[] { (Class)target }) : TypeToken.of((Class)target.getClass()).getTypes().rawTypes());
        for (final Method method : ((Class)(isStatic ? target : target.getClass())).getMethods()) {
            if (!isStatic || Modifier.isStatic(method.getModifiers())) {
                if (isStatic || !Modifier.isStatic(method.getModifiers())) {
                    for (final Class<?> cls : supers) {
                        try {
                            final Method real = cls.getDeclaredMethod(method.getName(), method.getParameterTypes());
                            if (!real.isAnnotationPresent((Class<? extends Annotation>)SubscribeEvent.class)) {
                                continue;
                            }
                            final Class<?>[] nameeterTypes = method.getParameterTypes();
                            if (nameeterTypes.length != 1) {
                                throw new IllegalArgumentException("Method " + method + " has @SubscribeEvent annotation, but requires " + nameeterTypes.length + " arguments.  Event handler methods must require a single argument.");
                            }
                            final Class<?> eventType = nameeterTypes[0];
                            if (!Event.class.isAssignableFrom(eventType)) {
                                throw new IllegalArgumentException("Method " + method + " has @SubscribeEvent annotation, but takes a argument that is not an Event " + eventType);
                            }
                            try {
                                this.register.invoke(MinecraftForge.EVENT_BUS, eventType, target, real, Loader.instance().getMinecraftModContainer());
                            }
                            catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            catch (InvocationTargetException e2) {
                                e2.printStackTrace();
                            }
                            break;
                        }
                        catch (NoSuchMethodException ex) {}
                    }
                }
            }
        }
    }
    
    static {
        ForgeWurst.forgeWurst = null;
    }
}
