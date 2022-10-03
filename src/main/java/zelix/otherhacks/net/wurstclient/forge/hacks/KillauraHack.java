package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.monster.*;
import java.util.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.client.entity.*;
import net.minecraft.world.*;
import java.util.stream.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.util.math.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import net.minecraft.client.*;
import java.util.function.*;

public final class KillauraHack extends Hack
{
    private final SliderSetting range;
    private final EnumSetting<Priority> priority;
    private final CheckboxSetting filterPlayers;
    private final CheckboxSetting filterSleeping;
    private final SliderSetting filterFlying;
    private final CheckboxSetting filterMonsters;
    private final CheckboxSetting filterPigmen;
    private final CheckboxSetting filterEndermen;
    private final CheckboxSetting filterAnimals;
    private final CheckboxSetting filterBabies;
    private final CheckboxSetting filterPets;
    private final CheckboxSetting filterVillagers;
    private final CheckboxSetting filterGolems;
    private final CheckboxSetting filterInvisible;
    private EntityLivingBase target;
    
    public KillauraHack() {
        super("Killaura", "Automatically attacks entities around you.");
        this.range = new SliderSetting("Range", 5.0, 1.0, 10.0, 0.05, SliderSetting.ValueDisplay.DECIMAL);
        this.priority = new EnumSetting<Priority>("Priority", "Determines which entity will be attacked first.\n¡ìlDistance¡ìr - Attacks the closest entity.\n¡ìlAngle¡ìr - Attacks the entity that requires\nthe least head movement.\n¡ìlHealth¡ìr - Attacks the weakest entity.", Priority.values(), Priority.ANGLE);
        this.filterPlayers = new CheckboxSetting("Filter players", "Won't attack other players.", false);
        this.filterSleeping = new CheckboxSetting("Filter sleeping", "Won't attack sleeping players.", false);
        this.filterFlying = new SliderSetting("Filter flying", "Won't attack players that\nare at least the given\ndistance above ground.", 0.0, 0.0, 2.0, 0.05, v -> (v == 0.0) ? "off" : SliderSetting.ValueDisplay.DECIMAL.getValueString(v));
        this.filterMonsters = new CheckboxSetting("Filter monsters", "Won't attack zombies, creepers, etc.", false);
        this.filterPigmen = new CheckboxSetting("Filter pigmen", "Won't attack zombie pigmen.", false);
        this.filterEndermen = new CheckboxSetting("Filter endermen", "Won't attack endermen.", false);
        this.filterAnimals = new CheckboxSetting("Filter animals", "Won't attack pigs, cows, etc.", false);
        this.filterBabies = new CheckboxSetting("Filter babies", "Won't attack baby pigs,\nbaby villagers, etc.", false);
        this.filterPets = new CheckboxSetting("Filter pets", "Won't attack tamed wolves,\ntamed horses, etc.", false);
        this.filterVillagers = new CheckboxSetting("Filter villagers", "Won't attack villagers.", false);
        this.filterGolems = new CheckboxSetting("Filter golems", "Won't attack iron golems,\nsnow golems and shulkers.", false);
        this.filterInvisible = new CheckboxSetting("Filter invisible", "Won't attack invisible entities.", false);
        this.setCategory(Category.COMBAT);
        this.addSetting(this.range);
        this.addSetting(this.priority);
        this.addSetting(this.filterPlayers);
        this.addSetting(this.filterSleeping);
        this.addSetting(this.filterFlying);
        this.addSetting(this.filterMonsters);
        this.addSetting(this.filterPigmen);
        this.addSetting(this.filterEndermen);
        this.addSetting(this.filterAnimals);
        this.addSetting(this.filterBabies);
        this.addSetting(this.filterPets);
        this.addSetting(this.filterVillagers);
        this.addSetting(this.filterGolems);
        this.addSetting(this.filterInvisible);
    }
    
    @Override
    protected void onEnable() {
        KillauraHack.wurst.register(this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        this.target = null;
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        final EntityPlayerSP player = event.getPlayer();
        final World world = player.world;
        if (player.getCooledAttackStrength(0.0f) < 1.0f) {
            return;
        }
        final double rangeSq = Math.pow(this.range.getValue(), 2.0);
        Stream<EntityLivingBase> stream = world.loadedEntityList.parallelStream()
                .filter(e -> e instanceof EntityLivingBase)
                .map(e -> (EntityLivingBase)e).filter(e -> !e.isDead && ((EntityLivingBase) e).getHealth() > 0.0f)
                .filter(e -> player.getDistanceSq(e) <= rangeSq)
                .filter(e -> e != player)
                .filter(e -> !(e instanceof EntityFakePlayer)
                );
        if (this.filterPlayers.isChecked()) {
            stream = stream.filter(e -> !(e instanceof EntityPlayer));
        }
        if (this.filterSleeping.isChecked()) {
            stream = stream.filter(e -> !(e instanceof EntityPlayer) || !((EntityPlayer) e).isPlayerSleeping());
        }
        if (this.filterFlying.getValue() > 0.0) {
            final AxisAlignedBB[] box = new AxisAlignedBB[1];
            final AxisAlignedBB[] box2 = new AxisAlignedBB[1];
            final World world2 = null;
            stream = stream.filter(e -> {
                if (!(e instanceof EntityPlayer)) {
                    return true;
                }
                else {
                    box[0] = e.getEntityBoundingBox();
                    box2[0] = box[0].union(box[0].offset(0.0, -this.filterFlying.getValue(), 0.0));
                    return world2.collidesWithAnyBlock(box2[0]);
                }
            });
        }
        if (this.filterMonsters.isChecked()) {
            stream = stream.filter(e -> !(e instanceof IMob));
        }
        if (this.filterPigmen.isChecked()) {
            stream = stream.filter(e -> !(e instanceof EntityPigZombie));
        }
        if (this.filterEndermen.isChecked()) {
            stream = stream.filter(e -> !(e instanceof EntityEnderman));
        }
        if (this.filterAnimals.isChecked()) {
            stream = stream.filter(e -> !(e instanceof EntityAnimal) && !(e instanceof EntityAmbientCreature) && !(e instanceof EntityWaterMob));
        }
        if (this.filterBabies.isChecked()) {
            stream = stream.filter(e -> !(e instanceof EntityAgeable) || !((EntityAgeable) e).isChild());
        }
        if (this.filterPets.isChecked()) {
            stream = stream.filter(e -> !(e instanceof EntityTameable) || !((EntityTameable) e).isTamed()).filter(e -> !(e instanceof AbstractHorse) || !((AbstractHorse) e).isTame());
        }
        if (this.filterVillagers.isChecked()) {
            stream = stream.filter(e -> !(e instanceof EntityVillager));
        }
        if (this.filterGolems.isChecked()) {
            stream = stream.filter(e -> !(e instanceof EntityGolem));
        }
        if (this.filterInvisible.isChecked()) {
            stream = stream.filter(e -> !e.isInvisible());
        }
        this.target = stream.min(this.priority.getSelected().comparator).orElse(null);
        if (this.target == null) {
            return;
        }
        RotationUtils.faceVectorPacket(this.target.getEntityBoundingBox().getCenter());
        KillauraHack.mc.playerController.attackEntity((EntityPlayer)player, (Entity)this.target);
        player.swingArm(EnumHand.MAIN_HAND);
    }
    
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (this.target == null) {
            return;
        }
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glEnable(2884);
        GL11.glDisable(2929);
        GL11.glPushMatrix();
        GL11.glTranslated(-TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY, -TileEntityRendererDispatcher.staticPlayerZ);
        final AxisAlignedBB box = new AxisAlignedBB(BlockPos.ORIGIN);
        final float p = (this.target.getMaxHealth() - this.target.getHealth()) / this.target.getMaxHealth();
        final float red = p * 2.0f;
        final float green = 2.0f - red;
        GL11.glTranslated(this.target.posX, this.target.posY, this.target.posZ);
        GL11.glTranslated(0.0, 0.05, 0.0);
        GL11.glScaled((double)this.target.width, (double)this.target.height, (double)this.target.width);
        GL11.glTranslated(-0.5, 0.0, -0.5);
        if (p < 1.0f) {
            GL11.glTranslated(0.5, 0.5, 0.5);
            GL11.glScaled((double)p, (double)p, (double)p);
            GL11.glTranslated(-0.5, -0.5, -0.5);
        }
        GL11.glColor4f(red, green, 0.0f, 0.25f);
        GL11.glBegin(7);
        RenderUtils.drawSolidBox(box);
        GL11.glEnd();
        GL11.glColor4f(red, green, 0.0f, 0.5f);
        GL11.glBegin(1);
        RenderUtils.drawOutlinedBox(box);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    private enum Priority
    {
        DISTANCE("Distance", e -> KillauraHack.mc.player.getDistanceSq(e)),
        ANGLE("Angle", e -> RotationUtils.getAngleToLookVec(e.getEntityBoundingBox().getCenter())), 
        HEALTH("Health", e -> e.getHealth());
        
        private final String name;
        private final Comparator<EntityLivingBase> comparator;
        
        private Priority(final String name, final ToDoubleFunction<EntityLivingBase> keyExtractor) {
            this.name = name;
            this.comparator = Comparator.comparingDouble(keyExtractor);
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
}
