package zelix.otherhacks.net.wurstclient.forge.hacks;

import com.mojang.realmsclient.gui.*;
import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import zelix.otherhacks.net.wurstclient.forge.clickgui.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.client.entity.*;
import net.minecraft.world.*;
import java.util.stream.*;
import net.minecraftforge.fml.common.eventhandler.*;
import java.util.*;

public final class RadarHack extends Hack
{
    private final Window window;
    private final ArrayList<Entity> entities;
    private final SliderSetting radius;
    private final CheckboxSetting rotate;
    private final CheckboxSetting filterPlayers;
    private final CheckboxSetting filterSleeping;
    private final CheckboxSetting filterMonsters;
    private final CheckboxSetting filterAnimals;
    private final CheckboxSetting filterInvisible;
    
    public RadarHack() {
        super("Radar", "Shows the location of nearby entities.\n" + ChatFormatting.RED + "red" + ChatFormatting.RESET + " - players\n" + ChatFormatting.GOLD + "orange" + ChatFormatting.RESET + " - monsters\n" + ChatFormatting.GREEN + "green" + ChatFormatting.RESET + " - animals\n" + ChatFormatting.GRAY + "gray" + ChatFormatting.RESET + " - others\n");
        this.entities = new ArrayList<Entity>();
        this.radius = new SliderSetting("Radius", "Radius in blocks.", 100.0, 1.0, 100.0, 1.0, SliderSetting.ValueDisplay.INTEGER);
        this.rotate = new CheckboxSetting("Rotate with player", true);
        this.filterPlayers = new CheckboxSetting("Filter players", "Won't show other players.", false);
        this.filterSleeping = new CheckboxSetting("Filter sleeping", "Won't show sleeping players.", false);
        this.filterMonsters = new CheckboxSetting("Filter monsters", "Won't show zombies, creepers, etc.", false);
        this.filterAnimals = new CheckboxSetting("Filter animals", "Won't show pigs, cows, etc.", false);
        this.filterInvisible = new CheckboxSetting("Filter invisible", "Won't show invisible entities.", false);
        this.setCategory(Category.RENDER);
        this.addSetting(this.radius);
        this.addSetting(this.rotate);
        this.addSetting(this.filterPlayers);
        this.addSetting(this.filterSleeping);
        this.addSetting(this.filterMonsters);
        this.addSetting(this.filterAnimals);
        this.addSetting(this.filterInvisible);
        (this.window = new Window("Radar")).setPinned(true);
        this.window.setInvisible(true);
        this.window.add(new Radar(this));
    }
    
    @Override
    protected void onEnable() {
        RadarHack.wurst.register(this);
        this.window.setInvisible(false);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        this.window.setInvisible(true);
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        final EntityPlayerSP player = event.getPlayer();
        final World world = player.world;
        this.entities.clear();
        final Entity entity = null;
        Stream<Entity> stream = (Stream<Entity>)world.loadedEntityList.parallelStream().filter(e -> !e.isDead && e != entity).filter(e -> !(e instanceof EntityFakePlayer)).filter(e -> e instanceof EntityLivingBase).filter(e -> ((EntityLivingBase) e).getHealth() > 0.0f);
        if (this.filterPlayers.isChecked()) {
            stream = stream.filter(e -> !(e instanceof EntityPlayer));
        }
        if (this.filterSleeping.isChecked()) {
            stream = stream.filter(e -> !(e instanceof EntityPlayer) || !((EntityPlayer) e).isPlayerSleeping());
        }
        if (this.filterMonsters.isChecked()) {
            stream = stream.filter(e -> !(e instanceof IMob));
        }
        if (this.filterAnimals.isChecked()) {
            stream = stream.filter(e -> !(e instanceof EntityAnimal) && !(e instanceof EntityAmbientCreature) && !(e instanceof EntityWaterMob));
        }
        if (this.filterInvisible.isChecked()) {
            stream = stream.filter(e -> !e.isInvisible());
        }
        this.entities.addAll(stream.collect(Collectors.toList()));
    }
    
    public Window getWindow() {
        return this.window;
    }
    
    public Iterable<Entity> getEntities() {
        return this.entities;
    }
    
    public double getRadius() {
        return this.radius.getValue();
    }
    
    public boolean isRotateEnabled() {
        return this.rotate.isChecked();
    }
}
