package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import zelix.utils.system.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.entity.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.item.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;
import zelix.utils.*;
import com.mojang.authlib.*;
import java.util.*;
import net.minecraft.client.*;
import net.minecraft.client.network.*;

public class AntiBot extends Hack
{
    public static ArrayList<EntityBot> bots;
    public NumberValue level;
    public NumberValue tick;
    public BooleanValue ifInAir;
    public BooleanValue ifGround;
    public BooleanValue ifZeroHealth;
    public BooleanValue ifInvisible;
    public BooleanValue ifEntityId;
    public BooleanValue ifTabName;
    public BooleanValue ifPing;
    public BooleanValue remove;
    public BooleanValue gwen;
    public BooleanValue mineland;
    public static ModeValue mode;
    private static List invalid;
    
    public AntiBot() {
        super("AntiBot", HackCategory.COMBAT);
        this.level = new NumberValue("AILevel", 0.0, 0.0, 6.0);
        this.tick = new NumberValue("TicksExisted", 0.0, 0.0, 999.0);
        this.ifInvisible = new BooleanValue("Invisible", Boolean.valueOf(false));
        this.ifInAir = new BooleanValue("InAir", Boolean.valueOf(false));
        this.ifGround = new BooleanValue("OnGround", Boolean.valueOf(false));
        this.ifZeroHealth = new BooleanValue("ZeroHealth", Boolean.valueOf(false));
        this.ifEntityId = new BooleanValue("EntityId", Boolean.valueOf(false));
        this.ifTabName = new BooleanValue("OutTabName", Boolean.valueOf(false));
        this.ifPing = new BooleanValue("PingCheck", Boolean.valueOf(false));
        this.remove = new BooleanValue("RemoveBots", Boolean.valueOf(false));
        this.gwen = new BooleanValue("Gwen", Boolean.valueOf(false));
        this.mineland = new BooleanValue("Mineland", Boolean.valueOf(false));
        AntiBot.mode = new ModeValue("Mode", new Mode[] { new Mode("Basic", false), new Mode("Mineplex", true), new Mode("Hypixel", false) });
        this.addValue(this.level, this.tick, this.remove, this.gwen, this.ifInvisible, this.ifInAir, this.ifGround, this.ifZeroHealth, this.ifEntityId, this.ifTabName, this.ifPing, AntiBot.mode);
    }
    
    @Override
    public String getDescription() {
        return "Ignore/Remove anti cheat bots.";
    }
    
    @Override
    public void onEnable() {
        AntiBot.bots.clear();
        AntiBot.invalid.clear();
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        AntiBot.bots.clear();
        AntiBot.invalid.clear();
        super.onDisable();
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        if (this.gwen.getValue()) {
            for (final Object entity : Utils.getEntityList()) {
                if (packet instanceof SPacketSpawnPlayer) {
                    final SPacketSpawnPlayer spawn = (SPacketSpawnPlayer)packet;
                    final double posX = spawn.getX() / 32.0;
                    final double posY = spawn.getY() / 32.0;
                    final double posZ = spawn.getZ() / 32.0;
                    final double difX = Wrapper.INSTANCE.player().posX - posX;
                    final double difY = Wrapper.INSTANCE.player().posY - posY;
                    final double difZ = Wrapper.INSTANCE.player().posZ - posZ;
                    final double dist = Math.sqrt(difX * difX + difY * difY + difZ * difZ);
                    if (dist <= 17.0 && posX != Wrapper.INSTANCE.player().posX && posY != Wrapper.INSTANCE.player().posY && posZ != Wrapper.INSTANCE.player().posZ) {
                        return false;
                    }
                    continue;
                }
            }
        }
        return true;
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (AntiBot.mode.getMode("Basic").isToggled()) {
            if ((int)(Object)this.tick.getValue() > 0.0) {
                AntiBot.bots.clear();
            }
            for (final Object object : Utils.getEntityList()) {
                if (object instanceof EntityLivingBase) {
                    final EntityLivingBase entity = (EntityLivingBase)object;
                    if (entity instanceof EntityPlayerSP || !(entity instanceof EntityPlayer) || entity instanceof EntityArmorStand || entity == Wrapper.INSTANCE.player()) {
                        continue;
                    }
                    final EntityPlayer bot = (EntityPlayer)entity;
                    if (!this.isBotBase(bot)) {
                        final int ailevel = (int)(Object)this.level.getValue();
                        final boolean isAi = ailevel > 0.0;
                        if (isAi && this.botPercentage(bot) > ailevel) {
                            this.addBot(bot);
                        }
                        else {
                            if (isAi || !this.botCondition(bot)) {
                                continue;
                            }
                            this.addBot(bot);
                        }
                    }
                    else {
                        this.addBot(bot);
                        if (!this.remove.getValue()) {
                            continue;
                        }
                        Wrapper.INSTANCE.world().removeEntity((Entity)bot);
                    }
                }
            }
        }
        else if (AntiBot.mode.getMode("Mineplex").isToggled() && Wrapper.INSTANCE.player().ticksExisted > 40) {
            for (final Object o1 : Wrapper.INSTANCE.world().loadedEntityList) {
                final Entity ent1 = (Entity)o1;
                if (ent1 instanceof EntityPlayer && !(ent1 instanceof EntityPlayerSP)) {
                    final int ticks1 = ent1.ticksExisted;
                    final double formated = Math.abs(Wrapper.INSTANCE.player().posY - ent1.posY);
                    final String name = ent1.getName();
                    final String diffX1 = ent1.getCustomNameTag();
                    if (diffX1 != "" || AntiBot.invalid.contains(ent1)) {
                        continue;
                    }
                    AntiBot.invalid.add(ent1);
                    Wrapper.INSTANCE.world().removeEntity(ent1);
                }
            }
        }
        else if (AntiBot.mode.getMode("Hypixel").isToggled()) {
            for (final Object o2 : Wrapper.INSTANCE.world().getLoadedEntityList()) {
                if (o2 instanceof EntityPlayer) {
                    final EntityPlayer ent2 = (EntityPlayer)o2;
                    if (ent2 == Wrapper.INSTANCE.player() || AntiBot.invalid.contains(ent2)) {
                        continue;
                    }
                    final String formated2 = ent2.getDisplayName().getFormattedText();
                    final String custom = ent2.getCustomNameTag();
                    final String name2 = ent2.getName();
                    if (ent2.isInvisible() && !formated2.startsWith("\ufffd\ufffdc") && formated2.endsWith("\ufffd\ufffdr") && custom.equals(name2)) {
                        final double diffX2 = Math.abs(ent2.posX - Wrapper.INSTANCE.player().posX);
                        final double diffY = Math.abs(ent2.posY - Wrapper.INSTANCE.player().posY);
                        final double diffZ = Math.abs(ent2.posZ - Wrapper.INSTANCE.player().posZ);
                        final double diffH = Math.sqrt(diffX2 * diffX2 + diffZ * diffZ);
                        if (diffY < 13.0 && diffY > 10.0 && diffH < 3.0) {
                            final List<EntityPlayer> list = getTabPlayerList();
                            if (!list.contains(ent2)) {
                                AntiBot.invalid.add(ent2);
                                Wrapper.INSTANCE.world().removeEntity((Entity)ent2);
                            }
                        }
                    }
                    if (!formated2.startsWith("\ufffd\ufffd") && formated2.endsWith("\ufffd\ufffdr")) {
                        AntiBot.invalid.add(ent2);
                    }
                    if (ent2.isInvisible() && !custom.equalsIgnoreCase("") && custom.toLowerCase().contains("\ufffd\ufffdc\ufffd\ufffdc") && name2.contains("\ufffd\ufffdc")) {
                        AntiBot.invalid.add(ent2);
                    }
                    if (!custom.equalsIgnoreCase("") && custom.toLowerCase().contains("\ufffd\ufffdc") && custom.toLowerCase().contains("\ufffd\ufffdr")) {
                        AntiBot.invalid.add(ent2);
                    }
                    if (formated2.contains("\ufffd\ufffd8[NPC]")) {
                        AntiBot.invalid.add(ent2);
                    }
                    if (!formated2.contains("\ufffd\ufffdc") && !custom.equalsIgnoreCase("")) {
                        AntiBot.invalid.add(ent2);
                        break;
                    }
                    break;
                }
            }
        }
        super.onClientTick(event);
    }
    
    void addBot(final EntityPlayer player) {
        if (!isBot((Entity)player)) {
            AntiBot.bots.add(new EntityBot(player));
        }
    }
    
    public static boolean isBot(final Entity player) {
        if (AntiBot.mode.getMode("Basic").isToggled()) {
            for (final EntityBot bot : AntiBot.bots) {
                if (bot.getName().equals(player.getName())) {
                    return player.isInvisible() == bot.isInvisible() || player.isInvisible();
                }
                final EntityPlayer X = (EntityPlayer)player;
                if (bot.getId() == player.getEntityId() || bot.getUuid().equals(X.getGameProfile().getId())) {
                    return true;
                }
            }
        }
        else if (AntiBot.mode.getMode("Mineplex").isToggled()) {
            for (final Entity ent : Wrapper.INSTANCE.world().loadedEntityList) {
                if (AntiBot.invalid.contains(ent)) {
                    return true;
                }
            }
        }
        else if (AntiBot.mode.getMode("Hypixel").isToggled()) {
            for (final Entity ent : Wrapper.INSTANCE.world().loadedEntityList) {
                if (AntiBot.invalid.contains(ent)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isHypixelNPC(final Entity entity) {
        final String formattedName = entity.getDisplayName().getFormattedText();
        final String customName = entity.getCustomNameTag();
        return (!formattedName.startsWith("¡ì") && formattedName.endsWith("¡ìr")) || formattedName.contains("[NPC]");
    }
    
    boolean botCondition(final EntityPlayer bot) {
        final int percentage = 0;
        if ((int)(Object)this.tick.getValue() > 0.0 && bot.ticksExisted < (int)(Object)this.tick.getValue()) {
            return true;
        }
        if (this.ifInAir.getValue() && bot.isInvisible() && bot.motionY == 0.0 && bot.posY > Wrapper.INSTANCE.player().posY + 1.0 && BlockUtils.isBlockMaterial(new BlockPos((Entity)bot).down(), Blocks.AIR)) {
            return true;
        }
        if (this.ifGround.getValue() && bot.motionY == 0.0 && !bot.collidedVertically && bot.onGround && bot.posY % 1.0 != 0.0 && bot.posY % 0.5 != 0.0) {
            return true;
        }
        if (this.ifZeroHealth.getValue() && bot.getHealth() <= 0.0f) {
            return true;
        }
        if (this.ifInvisible.getValue() && bot.isInvisible()) {
            return true;
        }
        if (this.ifEntityId.getValue() && bot.getEntityId() >= 1000000000) {
            return true;
        }
        if (this.ifTabName.getValue()) {
            boolean isTabName = false;
            for (final NetworkPlayerInfo npi : Wrapper.INSTANCE.mc().getConnection().getPlayerInfoMap()) {
                if (npi.getGameProfile() != null && npi.getGameProfile().getName().contains(bot.getName())) {
                    isTabName = true;
                }
            }
            if (!isTabName) {
                return true;
            }
        }
        return false;
    }
    
    int botPercentage(final EntityPlayer bot) {
        int percentage = 0;
        if ((int)(Object)this.tick.getValue() > 0.0 && bot.ticksExisted < (int)(Object)this.tick.getValue()) {
            ++percentage;
        }
        if (this.ifInAir.getValue() && bot.isInvisible() && bot.posY > Wrapper.INSTANCE.player().posY + 1.0 && BlockUtils.isBlockMaterial(new BlockPos((Entity)bot).down(), Blocks.AIR)) {
            ++percentage;
        }
        if (this.ifGround.getValue() && bot.motionY == 0.0 && !bot.collidedVertically && bot.onGround && bot.posY % 1.0 != 0.0 && bot.posY % 0.5 != 0.0) {
            ++percentage;
        }
        if (this.ifZeroHealth.getValue() && bot.getHealth() <= 0.0f) {
            ++percentage;
        }
        if (this.ifInvisible.getValue() && bot.isInvisible()) {
            ++percentage;
        }
        if (this.ifEntityId.getValue() && bot.getEntityId() >= 1000000000) {
            ++percentage;
        }
        if (this.ifTabName.getValue()) {
            boolean isTabName = false;
            for (final NetworkPlayerInfo npi : Wrapper.INSTANCE.mc().getConnection().getPlayerInfoMap()) {
                if (npi.getGameProfile() != null && npi.getGameProfile().getName().contains(bot.getName())) {
                    isTabName = true;
                }
            }
            if (!isTabName) {
                ++percentage;
            }
        }
        return percentage;
    }
    
    boolean isBotBase(final EntityPlayer bot) {
        if (isBot((Entity)bot)) {
            return true;
        }
        if (bot.getGameProfile() == null) {
            return true;
        }
        final GameProfile botProfile = bot.getGameProfile();
        if (bot.getUniqueID() == null) {
            return true;
        }
        final UUID botUUID = bot.getUniqueID();
        if (botProfile.getName() == null) {
            return true;
        }
        final String botName = botProfile.getName();
        return botName.contains("Body #") || botName.contains("NPC") || botName.equalsIgnoreCase(Utils.getEntityNameColor((EntityLivingBase)bot));
    }
    
    public List getInvalid() {
        return AntiBot.invalid;
    }
    
    public static List<EntityPlayer> getTabPlayerList() {
        final NetHandlerPlayClient nhpc = Minecraft.getMinecraft().player.connection;
        final List<EntityPlayer> list = new ArrayList<EntityPlayer>();
        return list;
    }
    
    static {
        AntiBot.bots = new ArrayList<EntityBot>();
        AntiBot.invalid = new ArrayList();
    }
}
