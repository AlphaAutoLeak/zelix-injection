package zelix.managers;

import zelix.gui.clickguis.gishcode.*;
import zelix.hack.hacks.ClickGui;
import zelix.hack.hacks.Timer;
import zelix.hack.hacks.automine.AutoMine;
import zelix.hack.hacks.hud.*;
import zelix.hack.hacks.xray.*;
import zelix.hack.hacks.*;
import zelix.hack.hacks.HytUtils.*;
import zelix.*;
import zelix.hack.hacks.external.*;
import zelix.gui.clickguis.gishcode.theme.dark.*;
import zelix.gui.clickguis.gishcode.theme.*;
import zelix.utils.*;
import zelix.value.*;
import net.minecraft.init.*;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.client.event.*;
import zelix.eventapi.event.*;
import zelix.hack.*;
import java.util.*;

public class HackManager
{
    private static Hack toggleHack;
    public static ArrayList<Hack> hacks;
    public static HashMap<Hack, Object> pluginhacks;
    private GuiManager guiManager;
    private ClickGuiScreen guiScreen;
    
    public HackManager() {
        HackManager.hacks = new ArrayList<Hack>();
        addHack(new Disabler());
        addHack(new Targets());
        addHack(new Enemys());
        addHack(new Teams());
        addHack(new Glowing());
        addHack(new Trajectories());
        addHack(new EntityESP());
        addHack(new ItemESP());
        addHack(new ChestESP());
        addHack(new Tracers());
        addHack(new WallHack());
        addHack(new FastUse());
        addHack(new Flight());
        addHack(new NightVision());
        addHack(new Profiler());
        addHack(new AntiBot());
        addHack(new AimAssist());
        addHack(new BowAimBot());
        addHack(new Trigger());
        addHack(new Criticals());
        addHack(new KillAura());
        addHack(new Velocity());
        addHack(new AutoSprint());
        addHack(new AutoArmor());
        addHack(new Regen());
        addHack(new FastPlace());
        addHack(new ChestStealer());
        addHack(new Glide());
        addHack(new Nuker());
        addHack(new AntiFall());
        addHack(new Ghost());
        addHack(new Blink());
        addHack(new Scaffold());
        addHack(new FastLadder());
        addHack(new Speed());
        addHack(new AutoStep());
        addHack(new AntiSneak());
        addHack(new FreeCam());
        addHack(new BlockOverlay());
        addHack(new PluginsGetter());
        addHack(new Teleport());
        addHack(new FireballReturn());
        addHack(new GuiWalk());
        addHack(new PlayerRadar());
        addHack(new SkinChanger());
        addHack(new Parkour());
        addHack(new AntiRain());
        addHack(new AntiWeb());
        addHack(new Spider());
        addHack(new AutoEat());
        addHack(new AutoSwim());
        addHack(new AutoTotem());
        addHack(new AutoShield());
        addHack(new Rage());
        addHack(new SelfDamage());
        addHack(new Hitbox());
        addHack(new AntiAfk());
        addHack(new FastBreak());
        addHack(new PortalGodMode());
        addHack(new PickupFilter());
        addHack(new PacketFilter());
        addHack(new FakeCreative());
        addHack(new ArmorHUD());
        addHack(new HUD());
        addHack(new HighJump());
        addHack(new ClickGui());
        addHack(new CommandFrame());
        addHack(new CommandGetter());
        addHack(new TpAura());
        addHack(new AutoTool());
        addHack(new BedNuker());
        addHack(new Cilp());
        addHack(new LongJump());
        addHack(new NoSlow());
        addHack(new NoSwing());
        addHack(new SafeWalk());
        addHack(new Fall());
        addHack(new AutoSoup());
        addHack(new SuperKick());
        addHack(new Timer());
        addHack(new BlinkAttack());
        addHack(new Search());
        addHack(new Reach());
        addHack(new AutoClicker());
        addHack(new AntiHunger());
        addHack(new EnemyInfo());
        addHack(new AutoMine()); // wtf is this
        addHack(new SelfDestruct());
        addHack(new FastBow());
        addHack(new AntiAntiXrayMod());
        addHack(new ItemTeleport());
        addHack(new AutoPlatform());
        if (Core.Love.equals("External")) {
            addHack(new External());
        }
    }
    
    public void setGuiManager(final GuiManager guiManager) {
        this.guiManager = guiManager;
    }
    
    public ClickGuiScreen getGui() {
        if (this.guiManager == null) {
            this.guiManager = new GuiManager();
            this.guiScreen = new ClickGuiScreen();
            ClickGuiScreen.clickGui = this.guiManager;
            this.guiManager.Init();
            this.guiManager.setTheme(new DarkTheme());
        }
        return this.guiManager;
    }
    
    public static Hack getHack(final String name) {
        Hack hack = null;
        for (final Hack h : getHacks()) {
            if (h.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())) {
                hack = h;
            }
        }
        return hack;
    }
    
    public static List<Hack> getSortedHacks() {
        final List<Hack> list = new ArrayList<Hack>();
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                if (!hack.isShow()) {
                    continue;
                }
                list.add(hack);
            }
        }
        list.sort(new Comparator<Hack>() {
            @Override
            public int compare(final Hack h1, final Hack h2) {
                String s1 = h1.getRenderName();
                String s2 = h2.getRenderName();
                for (final Value value : h1.getValues()) {
                    if (value instanceof ModeValue) {
                        final ModeValue modeValue = (ModeValue)value;
                        if (modeValue.getModeName().equals("Priority")) {
                            continue;
                        }
                        for (final Mode mode : modeValue.getModes()) {
                            if (mode.isToggled()) {
                                s1 = s1 + " " + mode.getName();
                            }
                        }
                    }
                }
                for (final Value value : h2.getValues()) {
                    if (value instanceof ModeValue) {
                        final ModeValue modeValue = (ModeValue)value;
                        if (modeValue.getModeName().equals("Priority")) {
                            continue;
                        }
                        for (final Mode mode : modeValue.getModes()) {
                            if (mode.isToggled()) {
                                s2 = s2 + " " + mode.getName();
                            }
                        }
                    }
                }
                final int cmp = Wrapper.INSTANCE.fontRenderer().getStringWidth(s2) - Wrapper.INSTANCE.fontRenderer().getStringWidth(s1);
                return (cmp != 0) ? cmp : s2.compareTo(s1);
            }
        });
        return list;
    }
    
    public void sortModules() {
        HackManager.hacks.sort((m1, m2) -> {
            if (m1.getName().toCharArray()[0] > m2.getName().toCharArray()[0]) {
                return 1;
            }
            else {
                return -1;
            }
        });
    }
    
    public static List<Hack> getSortedHacks3() {
        final List<Hack> list = new ArrayList<Hack>();
        for (final Hack module : getHacks()) {
            if (module.isToggled()) {
                if (!module.isShow()) {
                    continue;
                }
                list.add(module);
            }
        }
        list.sort(new Comparator<Hack>() {
            @Override
            public int compare(final Hack h1, final Hack h2) {
                String s1 = h1.getRenderName();
                String s2 = h2.getRenderName();
                for (final Value value : h1.getValues()) {
                    if (value instanceof ModeValue) {
                        final ModeValue modeValue = (ModeValue)value;
                        if (!modeValue.getModeName().equals("Mode")) {
                            continue;
                        }
                        for (final Mode mode : modeValue.getModes()) {
                            if (mode.isToggled()) {
                                s1 = s1 + " " + mode.getName();
                            }
                        }
                    }
                }
                for (final Value value : h2.getValues()) {
                    if (value instanceof ModeValue) {
                        final ModeValue modeValue = (ModeValue)value;
                        if (!modeValue.getModeName().equals("Mode")) {
                            continue;
                        }
                        for (final Mode mode : modeValue.getModes()) {
                            if (mode.isToggled()) {
                                s2 = s2 + " " + mode.getName();
                            }
                        }
                    }
                }
                final int cmp = (int)(HUD.font.getFont("SFB 6").getWidth(s2) - HUD.font.getFont("SFB 6").getWidth(s1));
                return (cmp != 0) ? cmp : s2.compareTo(s1);
            }
        });
        return list;
    }
    
    public static void addHack(final Hack hack) {
        HackManager.hacks.add(hack);
    }
    
    public static void addPluginHack(final Hack hack, final Object obj) {
        HackManager.pluginhacks.put(hack, obj);
        HackManager.hacks.add(hack);
    }
    
    public static void unregist(final Hack hack, final Object obj) {
        HackManager.pluginhacks.get(obj);
        HackManager.hacks.add(hack);
    }
    
    public static ArrayList<Hack> getHacks() {
        return HackManager.hacks;
    }
    
    public static Hack getToggleHack() {
        return HackManager.toggleHack;
    }
    
    public static void onKeyPressed(final int key) {
        if (Wrapper.INSTANCE.mc().currentScreen != null) {
            return;
        }
        for (final Hack hack : getHacks()) {
            if (hack.getKey() == key) {
                hack.toggle();
                Wrapper.INSTANCE.player().playSound(SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, 0.15f, hack.isToggled() ? 0.7f : 0.6f);
                HackManager.toggleHack = hack;
            }
        }
    }
    
    public static void onGuiContainer(final GuiContainerEvent event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onGuiContainer(event);
            }
        }
    }
    
    public static void onGuiOpen(final GuiOpenEvent event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onGuiOpen(event);
            }
        }
    }
    
    public static void onRender3D(final RenderBlockOverlayEvent event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onRender3D(event);
            }
        }
    }
    
    public static void onMouse(final MouseEvent event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onMouse(event);
            }
        }
    }
    
    public static void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onLeftClickBlock(event);
            }
        }
    }
    
    public static void onCameraSetup(final EntityViewRenderEvent.CameraSetup event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onCameraSetup(event);
            }
        }
    }
    
    public static void onAttackEntity(final AttackEntityEvent event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onAttackEntity(event);
            }
        }
    }
    
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onProjectileImpact(event);
            }
        }
    }
    
    public static void onItemPickup(final EntityItemPickupEvent event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onItemPickup(event);
            }
        }
    }
    
    public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onPlayerTick(event);
            }
        }
    }
    
    public static void onClientTick(final TickEvent.ClientTickEvent event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onClientTick(event);
            }
        }
    }
    
    public static void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onLivingUpdate(event);
            }
        }
    }
    
    public static void onRenderWorldLast(final RenderWorldLastEvent event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onRenderWorldLast(event);
            }
        }
    }
    
    public static void onRenderGameOverlay(final RenderGameOverlayEvent.Text event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onRenderGameOverlay(event);
            }
        }
    }
    
    public static void onInputUpdate(final InputUpdateEvent event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onInputUpdate(event);
            }
        }
    }
    
    public static void onPlayerEventPre(final EventPlayerPre event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onPlayerEventPre(event);
            }
        }
    }
    
    public static void onPlayerEventPost(final EventPlayerPost event) {
        for (final Hack hack : getHacks()) {
            if (hack.isToggled()) {
                hack.onPlayerEventPost(event);
            }
        }
    }
    
    public static List<Hack> getModulesInType(final HackCategory t) {
        final ArrayList<Hack> output = new ArrayList<Hack>();
        final ArrayList<Hack> hack = new ArrayList<Hack>();
        hack.addAll(HackManager.hacks);
        for (final Hack m : HackManager.hacks) {
            if (m.getCategory() != t) {
                continue;
            }
            output.add(m);
        }
        return output;
    }
    
    static {
        HackManager.toggleHack = null;
    }
}
