package zelix.hack.hacks;

import zelix.hack.*;
import zelix.utils.ReflectionHelper;
import zelix.value.*;
import zelix.eventapi.event.*;
import net.minecraft.entity.*;
import zelix.utils.system.*;
import java.lang.reflect.*;
import net.minecraft.client.gui.*;
import java.awt.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.managers.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.client.entity.*;
import net.minecraft.world.*;

import java.util.List;
import java.util.stream.*;
import java.util.*;
import net.minecraft.item.*;
import zelix.utils.*;
import net.minecraft.init.*;
import net.minecraft.enchantment.*;
import net.minecraft.network.*;
import net.minecraft.entity.item.*;
import net.minecraft.client.settings.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;

public class KillAura extends Hack
{
    public ModeValue priority;
    public ModeValue mode;
    public BooleanValue walls;
    public BooleanValue autoDelay;
    public BooleanValue packetReach;
    public NumberValue minCPS;
    public NumberValue maxCPS;
    public NumberValue packetRange;
    public NumberValue range;
    public NumberValue FOV;
    public BooleanValue rotate;
    public ModeValue noHytBot;
    boolean send;
    public TimerUtils timer;
    public static EntityLivingBase target;
    public List<EntityLivingBase> targets;
    public static float[] facingCam;
    public float[] facing;
    Vec3d randomCenter;
    boolean phaseOne;
    boolean phaseTwo;
    boolean phaseThree;
    boolean fakephaseOne;
    boolean fakephaseTwo;
    boolean fakephaseThree;
    public static boolean blockstate;
    FontManager fontManager;
    
    public KillAura() {
        super("KillAura", HackCategory.COMBAT);
        this.send = false;
        this.facing = null;
        this.randomCenter = null;
        this.phaseOne = false;
        this.phaseTwo = false;
        this.phaseThree = false;
        this.fakephaseThree = false;
        this.fontManager = new FontManager();
        this.priority = new ModeValue("Priority", new Mode[] { new Mode("Closest", true), new Mode("Health", false) });
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("Simple", true), new Mode("AAC", false), new Mode("Legit", false), new Mode("Multi", false), new Mode("Fake", false) });
        this.walls = new BooleanValue("ThroughWalls", Boolean.valueOf(false));
        this.autoDelay = new BooleanValue("AutoDelay", Boolean.valueOf(false));
        this.rotate = new BooleanValue("Rotate", Boolean.valueOf(false));
        this.packetReach = new BooleanValue("PacketReach", Boolean.valueOf(false));
        this.packetRange = new NumberValue("PacketRange", 10.0, 1.0, 100.0);
        this.minCPS = new NumberValue("MinCPS", 4.0, 1.0, 30.0);
        this.maxCPS = new NumberValue("MaxCPS", 8.0, 1.0, 30.0);
        this.range = new NumberValue("Range", 3.4, 1.0, 7.0);
        this.FOV = new NumberValue("FOV", 180.0, 1.0, 180.0);
        this.noHytBot = new ModeValue("HytNpcIgnore", new Mode[] { new Mode("None", true), new Mode("4v4", false) });
        this.addValue(this.mode, this.priority, this.walls, this.autoDelay, this.packetReach, this.minCPS, this.maxCPS, this.packetRange, this.range, this.FOV, this.rotate, this.noHytBot);
        this.timer = new TimerUtils();
    }
    
    @Override
    public String getDescription() {
        return "Attacks the entities around you.";
    }
    
    @Override
    public void onEnable() {
        KillAura.facingCam = null;
        if (this.mode.getMode("AAC").isToggled() || this.mode.getMode("Fake").isToggled()) {
            this.facing = null;
            this.randomCenter = null;
            this.phaseOne = false;
            this.phaseTwo = false;
            this.phaseThree = false;
            this.fakephaseOne = false;
            this.fakephaseTwo = false;
            this.fakephaseThree = false;
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        KillAura.facingCam = null;
        KillAura.target = null;
        AutoShield.block(false);
        KillAura.blockstate = false;
        super.onDisable();
    }
    
    @Override
    public void onPlayerEventPre(final EventPlayerPre event) {
        Utils.nullCheck();
        if (this.mode.getMode("Fake").isToggled()) {
            this.rotate.setValue(false);
            this.killAuraUpdate();
            this.FakekillAuraAttack(KillAura.target, event);
        }
        if (this.rotate.getValue() && KillAura.target != null) {
            event.setPitch(Utils.getRotationsNeeded((Entity)KillAura.target)[1]);
            event.setYaw(Utils.getRotationsNeeded((Entity)KillAura.target)[0]);
            Wrapper.INSTANCE.player().setRotationYawHead(event.getYaw());
            Wrapper.INSTANCE.player().renderYawOffset = event.getYaw();
        }
        super.onPlayerEventPre(event);
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (side == Connection.Side.OUT && packet instanceof CPacketPlayer) {
            final Field field = ReflectionHelper.findField((Class)CPacketPlayer.class, new String[] { "rotating", "rotating" });
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (KillAura.target != null) {
                    final Field field2 = ReflectionHelper.findField((Class)CPacketPlayer.class, new String[] { "pitch", "pitch" });
                    final Field field3 = ReflectionHelper.findField((Class)CPacketPlayer.class, new String[] { "yaw", "yaw" });
                    if (!field2.isAccessible()) {
                        field2.setAccessible(true);
                    }
                    if (!field3.isAccessible()) {
                        field3.setAccessible(true);
                    }
                }
                if (field.getBoolean(packet)) {
                    RotationUtils.serverRotation = new RotationUtils.Rotation(((CPacketPlayer)packet).getYaw(0.0f), ((CPacketPlayer)packet).getPitch(0.0f));
                }
            }
            catch (Exception ex) {}
        }
        return super.onPacket(packet, side);
    }
    
    @Override
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Text event) {
        final ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
        if (KillAura.target != null) {
            float health = KillAura.target.getHealth();
            if (health > 20.0f) {
                health = 20.0f;
            }
            final int red = (int)Math.abs(health * 5.0f * 0.01f * 0.0f + (1.0f - health * 5.0f * 0.01f) * 255.0f);
            final int green = (int)Math.abs(health * 5.0f * 0.01f * 255.0f + (1.0f - health * 5.0f * 0.01f) * 0.0f);
            final Color customColor = new Color(red, green, 0).brighter();
            Wrapper.INSTANCE.fontRenderer().drawString("¡ì4\u2764¡ìf", sr.getScaledWidth() / 2 - Wrapper.INSTANCE.fontRenderer().getStringWidth("¡ì4\u2764¡ìf"), sr.getScaledHeight() / 2 - 15, customColor.getRGB());
            this.fontManager.getFont("SFM 7").drawString("" + (int)KillAura.target.getHealth(), sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - 15, customColor.getRGB());
            Wrapper.INSTANCE.fontRenderer().drawString(KillAura.target.getName(), sr.getScaledWidth() / 2 - Wrapper.INSTANCE.fontRenderer().getStringWidth("¡ì4\u2764¡ìf"), sr.getScaledHeight() / 2 - 24, -1);
        }
        super.onRenderGameOverlay(event);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void onCameraSetup(final EntityViewRenderEvent.CameraSetup event) {
        if (!this.mode.getMode("AAC").isToggled() || event.getEntity() != Wrapper.INSTANCE.player() || KillAura.target == null) {
            return;
        }
        final float yaw = Utils.getRotationsNeeded((Entity)KillAura.target)[0] - 180.0f;
        final float pitch = Utils.getRotationsNeeded((Entity)KillAura.target)[1] + 10.0f;
        KillAura.facingCam = new float[] { Utils.getRotationsNeeded((Entity)KillAura.target)[0], pitch };
        event.setYaw(yaw);
        event.setPitch(pitch);
        super.onCameraSetup(event);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.mode.getMode("AAC").isToggled()) {
            this.phaseOne();
            this.phaseTwo();
            this.phaseFour();
        }
        else if (this.mode.getMode("Simple").isToggled()) {
            this.killAuraUpdate();
            this.killAuraAttack(KillAura.target);
        }
        else if (this.mode.getMode("Multi").isToggled()) {
            this.MultiUpdate();
        }
        super.onClientTick(event);
    }
    
    void MultiUpdate() {
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        final World world = (World)Wrapper.INSTANCE.world();
        final double rangeSq = Math.pow(this.range.getValue(), 2.0);
        Stream<Entity> stream = world.loadedEntityList.parallelStream().filter(e -> e instanceof EntityLivingBase).map(e -> e).filter(e -> !e.isDead && ((EntityLivingBase) e).getHealth() > 0.0f).filter(e -> EntityUtils.getDistanceSq((Entity)player, e) <= rangeSq).filter(e -> e != player);
        final Hack h = HackManager.getHack("Targets");
        if (!h.isToggledValue("Players")) {
            stream = stream.filter(e -> !(e instanceof EntityPlayer));
        }
        if (!h.isToggledValue("Mobs")) {
            stream = stream.filter(e -> !(e instanceof IMob) && !(e instanceof EntityAnimal));
        }
        final ArrayList<Entity> entities = stream.collect(Collectors.toCollection(() -> new ArrayList()));
        if (entities.isEmpty()) {
            return;
        }
        final int CPS = Utils.random((int)(Object)this.minCPS.getValue(), (int)(Object)this.maxCPS.getValue());
        final int r1 = Utils.random(1, 50);
        final int r2 = Utils.random(1, 60);
        final int r3 = Utils.random(1, 70);
        if (this.timer.hasReached((1000 + (r1 - r2 + r3)) / CPS)) {
            for (final Entity entity : entities) {
                Wrapper.INSTANCE.controller().attackEntity((EntityPlayer)player, entity);
            }
            player.swingArm(EnumHand.MAIN_HAND);
            this.timer.reset();
        }
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (this.mode.getMode("AAC").isToggled()) {
            this.phaseThree(event);
        }
        super.onPlayerTick(event);
    }
    
    void phaseOne() {
        if (KillAura.target != null) {
            this.randomCenter = Utils.getRandomCenter(KillAura.target.getEntityBoundingBox());
            this.facing = Utils.getSmoothNeededRotations(this.randomCenter, 100.0f, 100.0f);
        }
        this.killAuraUpdate();
        if (KillAura.target != null) {
            this.phaseOne = true;
        }
    }
    
    void fakephaseOne(final EventPlayerPre evnt) {
        if (KillAura.target != null) {
            this.randomCenter = Utils.getRandomCenter(KillAura.target.getEntityBoundingBox());
            this.facing = Utils.getSmoothNeededRotations(this.randomCenter, 100.0f, 100.0f);
        }
        this.killAuraUpdate();
        if (KillAura.target != null) {
            this.fakephaseOne = true;
        }
    }
    
    void phaseTwo() {
        if (KillAura.target == null || this.randomCenter == null || !this.phaseOne) {
            return;
        }
        if (this.facing[0] == Utils.getNeededRotations(this.randomCenter)[0]) {
            this.phaseOne = false;
            this.phaseTwo = true;
        }
    }
    
    void fakephaseTwo(final EventPlayerPre event) {
        if (KillAura.target == null || this.randomCenter == null || !this.fakephaseOne) {
            return;
        }
        if (this.facing[0] == Utils.getNeededRotations(this.randomCenter)[0]) {
            this.fakephaseOne = false;
            this.fakephaseTwo = true;
        }
    }
    
    void phaseThree(final TickEvent.PlayerTickEvent event) {
        if (KillAura.target == null || this.facing == null || event.player != Wrapper.INSTANCE.player()) {
            return;
        }
        if (KillAura.target.hurtTime <= KillAura.target.maxHurtTime) {
            event.player.rotationYaw = this.facing[0];
            event.player.rotationPitch = this.facing[1];
            Wrapper.INSTANCE.player().rotationYawHead = this.facing[0];
        }
        if (!this.phaseTwo) {
            return;
        }
        event.player.rotationYaw = this.facing[0];
        event.player.rotationPitch = this.facing[1];
        Wrapper.INSTANCE.player().rotationYawHead = this.facing[0];
        this.phaseTwo = false;
        this.phaseThree = true;
    }
    
    void fakephaseThree(final EventPlayerPre event) {
        if (KillAura.target == null || this.facing == null) {
            return;
        }
        if (KillAura.target.hurtTime <= KillAura.target.maxHurtTime) {
            event.setYaw(this.facing[0]);
            event.setPitch(this.facing[1]);
            Wrapper.INSTANCE.player().rotationYawHead = this.facing[0];
            Wrapper.INSTANCE.player().setRotationYawHead(event.getYaw());
            Wrapper.INSTANCE.player().renderYawOffset = event.getYaw();
        }
        if (!this.fakephaseTwo) {
            return;
        }
        event.setYaw(this.facing[0]);
        event.setPitch(this.facing[1]);
        Wrapper.INSTANCE.player().setRotationYawHead(event.getYaw());
        Wrapper.INSTANCE.player().renderYawOffset = event.getYaw();
        Wrapper.INSTANCE.player().rotationYawHead = this.facing[0];
        this.fakephaseTwo = false;
        this.fakephaseThree = true;
    }
    
    void phaseFour() {
        if (KillAura.target == null || this.randomCenter == null || !this.phaseThree || this.facing[0] != Utils.getNeededRotations(this.randomCenter)[0]) {
            KillAura.facingCam = null;
            return;
        }
        final Entity rayCastEntity = RayCastUtils.rayCast((float)(Object)this.range.getValue() + 1.0f, this.facing[0], this.facing[1]);
        this.killAuraAttack((rayCastEntity == null) ? KillAura.target : ((EntityLivingBase)rayCastEntity));
    }
    
    void fakephaseFour(final EventPlayerPre event) {
        if (KillAura.target == null || this.randomCenter == null || !this.fakephaseThree) {
            KillAura.facingCam = null;
            return;
        }
        event.setPitch(Utils.getRotationsNeeded((Entity)KillAura.target)[1]);
        event.setYaw(Utils.getRotationsNeeded((Entity)KillAura.target)[0]);
        this.killAuraAttack(KillAura.target);
    }
    
    void killAuraUpdate() {
        try {
            for (final Object object : Wrapper.INSTANCE.world().loadedEntityList) {
                if (!(object instanceof EntityLivingBase)) {
                    continue;
                }
                final EntityLivingBase entity = (EntityLivingBase)object;
                if (!this.check(entity)) {
                    continue;
                }
                KillAura.target = entity;
            }
        }
        catch (ConcurrentModificationException ex) {}
    }
    
    public void FakekillAuraAttack(final EntityLivingBase entity, final EventPlayerPre event) {
        if (entity == null) {
            AutoShield.block(false);
            KillAura.blockstate = false;
            return;
        }
        event.setPitch(Utils.getRotationsNeeded((Entity)KillAura.target)[1]);
        event.setYaw(Utils.getRotationsNeeded((Entity)KillAura.target)[0]);
        Wrapper.INSTANCE.player().setRotationYawHead(event.getYaw());
        Wrapper.INSTANCE.player().renderYawOffset = event.getYaw();
        if (this.autoDelay.getValue()) {
            if (Wrapper.INSTANCE.player().getCooledAttackStrength(0.0f) == 1.0f) {
                this.processAttack(entity);
                KillAura.target = null;
            }
        }
        else {
            final int CPS = Utils.random((int)(Object)this.minCPS.getValue(), (int)(Object)this.maxCPS.getValue());
            final int r1 = Utils.random(1, 50);
            final int r2 = Utils.random(1, 60);
            final int r3 = Utils.random(1, 70);
            if (this.timer.isDelay((1000 + (r1 - r2 + r3)) / CPS)) {
                this.processAttack(entity);
                this.timer.setLastMS();
                KillAura.facingCam = null;
                KillAura.target = null;
                this.phaseThree = false;
            }
        }
    }
    
    public void killAuraAttack(final EntityLivingBase entity) {
        if (entity == null) {
            AutoShield.block(false);
            KillAura.blockstate = false;
            return;
        }
        if (this.autoDelay.getValue()) {
            if (Wrapper.INSTANCE.player().getCooledAttackStrength(0.0f) == 1.0f) {
                this.processAttack(entity);
                KillAura.target = null;
            }
        }
        else {
            final int CPS = Utils.random((int)(Object)this.minCPS.getValue(), (int)(Object)this.maxCPS.getValue());
            final int r1 = Utils.random(1, 50);
            final int r2 = Utils.random(1, 60);
            final int r3 = Utils.random(1, 70);
            if (this.timer.isDelay((1000 + (r1 - r2 + r3)) / CPS)) {
                this.processAttack(entity);
                this.timer.setLastMS();
                KillAura.facingCam = null;
                this.phaseThree = false;
                this.fakephaseThree = false;
                KillAura.target = null;
            }
        }
    }
    
    public void BMultiAttack(final EntityLivingBase entity) {
        if (entity == null) {
            AutoShield.block(false);
            KillAura.blockstate = false;
            return;
        }
        if (this.autoDelay.getValue()) {
            if (Wrapper.INSTANCE.player().getCooledAttackStrength(0.0f) == 1.0f) {
                this.processAttack(entity);
                KillAura.target = null;
            }
        }
        else {
            final int CPS = Utils.random((int)(Object)this.minCPS.getValue(), (int)(Object)this.maxCPS.getValue());
            final int r1 = Utils.random(1, 50);
            final int r2 = Utils.random(1, 60);
            final int r3 = Utils.random(1, 70);
            if (this.timer.isDelay((1000 + (r1 - r2 + r3)) / CPS)) {
                this.processAttack(entity);
                this.timer.setLastMS();
                KillAura.facingCam = null;
                KillAura.target = null;
                this.phaseThree = false;
            }
        }
    }
    
    private boolean hasSword() {
        return Wrapper.INSTANCE.player().inventory.getCurrentItem() != null && Wrapper.INSTANCE.player().inventory.getCurrentItem().getItem() instanceof ItemSword;
    }
    
    public void processAttack(final EntityLivingBase entity) {
        AutoShield.block(false);
        if (this.hasSword()) {
            this.unBlock();
        }
        if (!this.isInAttackRange(entity) || !ValidUtils.isInAttackFOV(entity, (int)(Object)this.FOV.getValue())) {
            return;
        }
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        if (this.noHytBot.getMode("4v4").isToggled() && entity instanceof EntityPlayer && (((EntityPlayer)entity).inventory.armorItemInSlot(2).isEmpty() || player.inventory.getStackInSlot(0).getItem() == Items.BED)) {
            return;
        }
        final float sharpLevel = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), entity.getCreatureAttribute());
        if (this.packetReach.getValue()) {
            final double posX = entity.posX - 3.5 * Math.cos(Math.toRadians(Utils.getYaw((Entity)entity) + 90.0f));
            final double posZ = entity.posZ - 3.5 * Math.sin(Math.toRadians(Utils.getYaw((Entity)entity) + 90.0f));
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.PositionRotation(posX, entity.posY, posZ, Utils.getYaw((Entity)entity), Utils.getPitch((Entity)entity), player.onGround));
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketUseEntity((Entity)entity));
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(player.posX, player.posY, player.posZ, player.onGround));
        }
        else if (this.autoDelay.getValue() || this.mode.getMode("Simple").isToggled()) {
            Utils.attack((Entity)entity);
        }
        else {
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketUseEntity((Entity)entity));
        }
        Utils.swingMainHand();
        if (sharpLevel > 0.0f) {
            player.onEnchantmentCritical((Entity)entity);
        }
        AutoShield.block(true);
    }
    
    public void MultiprocessAttack(final EntityLivingBase entity) {
        AutoShield.block(false);
        KillAura.blockstate = false;
        if (!this.isInAttackRange(entity) || !ValidUtils.isInAttackFOV(entity, (int)(Object)this.FOV.getValue())) {
            return;
        }
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        final float sharpLevel = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), entity.getCreatureAttribute());
        if (this.packetReach.getValue()) {
            final double posX = entity.posX - 3.5 * Math.cos(Math.toRadians(Utils.getYaw((Entity)entity) + 90.0f));
            final double posZ = entity.posZ - 3.5 * Math.sin(Math.toRadians(Utils.getYaw((Entity)entity) + 90.0f));
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.PositionRotation(posX, entity.posY, posZ, Utils.getYaw((Entity)entity), Utils.getPitch((Entity)entity), player.onGround));
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketUseEntity((Entity)entity));
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(player.posX, player.posY, player.posZ, player.onGround));
        }
        else if (this.autoDelay.getValue() || this.mode.getMode("Multi").isToggled()) {
            Utils.attack((Entity)entity);
        }
        else {
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketUseEntity((Entity)entity));
        }
        Utils.swingMainHand();
        if (sharpLevel > 0.0f) {
            player.onEnchantmentCritical((Entity)entity);
        }
    }
    
    boolean isPriority(final EntityLivingBase entity) {
        return (this.priority.getMode("Closest").isToggled() && ValidUtils.isClosest(entity, KillAura.target)) || (this.priority.getMode("Health").isToggled() && ValidUtils.isLowHealth(entity, KillAura.target));
    }
    
    boolean isInAttackRange(final EntityLivingBase entity) {
        return this.packetReach.getValue() ? (entity.getDistanceSq((Entity)Wrapper.INSTANCE.player()) <= (float)(Object)this.packetRange.getValue()) : (entity.getDistanceSq((Entity)Wrapper.INSTANCE.player()) <= (float)(Object)this.range.getValue());
    }
    
    public boolean check(final EntityLivingBase entity) {
        return !(entity instanceof EntityArmorStand) && !ValidUtils.isValidEntity(entity) && ValidUtils.isNoScreen() && entity != Wrapper.INSTANCE.player() && !entity.isDead && entity.deathTime <= 0 && !ValidUtils.isBot(entity) && ValidUtils.isFriendEnemy(entity) && ValidUtils.isInvisible(entity) && ValidUtils.isInAttackFOV(entity, (int)(Object)this.FOV.getValue()) && this.isInAttackRange(entity) && ValidUtils.isTeam(entity) && ValidUtils.pingCheck(entity) && (this.walls.getValue() || Wrapper.INSTANCE.player().canEntityBeSeen((Entity)entity)) && this.isPriority(entity);
    }
    
    public void doBlock() {
        KeyBinding.setKeyBindState(Wrapper.INSTANCE.mc().gameSettings.keyBindUseItem.getKeyCode(), true);
    }
    
    public static boolean getBlockState() {
        return KillAura.blockstate;
    }
    
    public static int randomNumber(final int max, final int min) {
        return Math.round(min + (float)Math.random() * (max - min));
    }
    
    private void unBlock() {
        KillAura.blockstate = false;
        Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
    }
    
    public static EntityLivingBase getTarget() {
        return KillAura.target;
    }
    
    static {
        KillAura.facingCam = null;
    }
}
