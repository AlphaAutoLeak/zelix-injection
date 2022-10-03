package zelix.utils;

import zelix.*;
import java.security.*;
import java.io.*;
import java.awt.*;
import java.awt.datatransfer.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.potion.*;
import java.util.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.*;
import zelix.managers.*;
import net.minecraft.init.*;
import com.mojang.authlib.*;
import net.minecraft.item.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.gui.*;
import java.math.*;
import net.minecraft.util.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.client.*;
import net.minecraft.util.math.*;
import java.lang.reflect.*;
import java.util.List;

import net.minecraft.world.*;
import net.minecraft.block.*;
import zelix.utils.system.*;
import zelix.utils.hooks.visual.*;

public class Utils
{
    public static boolean lookChanged;
    public static float[] rotationsToBlock;
    private static final Random RANDOM;
    public static String LastContent;
    
    public static String encodeHexString(final byte[] data) {
        return new String(encodeHex(data));
    }
    
    public static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }
    
    public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }
    
    protected static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        int i = 0;
        int j = 0;
        while (i < l) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0xF & data[i]];
            ++i;
        }
        return out;
    }
    
    public static boolean isPlayerHoldingWeapon() {
        if (Wrapper.INSTANCE.player().getHeldItem(EnumHand.MAIN_HAND) == null) {
            return false;
        }
        final Item item = Wrapper.INSTANCE.player().getHeldItem(EnumHand.MAIN_HAND).getItem();
        return item instanceof ItemSword || item instanceof ItemAxe;
    }
    
    public static String readFile(final String fileName) {
        try {
            final File file = new File(fileName);
            final FileReader fr = new FileReader(file);
            final BufferedReader br = new BufferedReader(fr);
            final String line;
            if ((line = br.readLine()) != null) {
                return line;
            }
            br.close();
            fr.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return null;
    }
    
    public static String getCPUSerialNumber() {
        String serial;
        try {
            final Process process = Runtime.getRuntime().exec(new String[] { "wmic", "cpu", "get", "ProcessorId" });
            process.getOutputStream().close();
            final Scanner sc = new Scanner(process.getInputStream());
            serial = sc.next();
            serial = sc.next();
        }
        catch (IOException e) {
            throw new RuntimeException("\u83b7\u53d6CPU\u5e8f\u5217\u53f7\u5931\u8d25");
        }
        return serial;
    }
    
    public static String getHWID(final boolean ismd5) {
        String serial;
        try {
            final Process process = Runtime.getRuntime().exec(new String[] { "wmic", "cpu", "get", "ProcessorId" });
            process.getOutputStream().close();
            final Scanner sc = new Scanner(process.getInputStream());
            serial = sc.next();
            serial = sc.next();
        }
        catch (IOException e) {
            throw new RuntimeException("\u83b7\u53d6CPU\u5e8f\u5217\u53f7\u5931\u8d25");
        }
        return serial;
    }
    
    public static byte[] getClassByteCode(final String className) {
        final String jarname = "/" + className.replace('.', '/') + ".class";
        final InputStream is = Core.class.getResourceAsStream(jarname);
        final ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        byte[] imgdata = null;
        try {
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            imgdata = bytestream.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
            try {
                bytestream.close();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        finally {
            try {
                bytestream.close();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return imgdata;
    }
    
    public static String getSubString(final String text, final String left, final String right) {
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        }
        else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            }
            else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }
    
    public static byte[] toBytes(final String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        final byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; ++i) {
            final String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte)Integer.parseInt(subStr, 16);
        }
        return bytes;
    }
    
    public static String getMD5Str(final String str) {
        byte[] digest = null;
        try {
            final MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes("utf-8"));
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        final String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }
    
    public static String bytesToHex(final byte[] bytes) {
        final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        final char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for (final byte b : bytes) {
            if (b < 0) {
                a = 256 + b;
            }
            else {
                a = b;
            }
            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }
        return new String(buf);
    }
    
    public static boolean nullCheck() {
        return Wrapper.INSTANCE.player() == null || Wrapper.INSTANCE.world() == null;
    }
    
    public static void copy(final String content) {
        if (Utils.LastContent != content) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(content), null);
        }
        Utils.LastContent = content;
    }
    
    public static String getSysClipboardText() {
        String ret = "";
        final Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        final Transferable clipTf = sysClip.getContents(null);
        if (clipTf != null && clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                ret = (String)clipTf.getTransferData(DataFlavor.stringFlavor);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
    
    public static int random(final int min, final int max) {
        return Utils.RANDOM.nextInt(max - min) + min;
    }
    
    public static Vec3d getRandomCenter(final AxisAlignedBB bb) {
        return new Vec3d(bb.minX + (bb.maxX - bb.minX) * 0.8 * Math.random(), bb.minY + (bb.maxY - bb.minY) * Math.random() + 0.1 * Math.random(), bb.minZ + (bb.maxZ - bb.minZ) * 0.8 * Math.random());
    }
    
    public static boolean isMoving(final Entity e) {
        return e.motionX != 0.0 && e.motionZ != 0.0 && (e.motionY != 0.0 || e.motionY > 0.0);
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return BlockUtils.getBlock(pos).canCollideCheck(BlockUtils.getState(pos), false);
    }
    
    public static Vec3d getEyesPos() {
        return new Vec3d(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight(), Wrapper.INSTANCE.player().posZ);
    }
    
    public static void faceVectorPacketInstant(final Vec3d vec) {
        Utils.rotationsToBlock = getNeededRotations(vec);
    }
    
    public static List<Entity> getEntityList() {
        return (List<Entity>)Wrapper.INSTANCE.world().getLoadedEntityList();
    }
    
    public static boolean isNullOrEmptyStack(final ItemStack stack) {
        return stack == null || stack.isEmpty();
    }
    
    public static void windowClick(final int windowId, final int slotId, final int mouseButton, final ClickType type) {
        Wrapper.INSTANCE.controller().windowClick(windowId, slotId, mouseButton, type, (EntityPlayer)Wrapper.INSTANCE.player());
    }
    
    public static void swingMainHand() {
        Wrapper.INSTANCE.player().swingArm(EnumHand.MAIN_HAND);
    }
    
    public static void attack(final Entity entity) {
        Wrapper.INSTANCE.controller().attackEntity((EntityPlayer)Wrapper.INSTANCE.player(), entity);
    }
    
    public static void addEffect(final int id, final int duration, final int amplifier) {
        Wrapper.INSTANCE.player().addPotionEffect(new PotionEffect(Potion.getPotionById(id), duration, amplifier));
    }
    
    public static void removeEffect(final int id) {
        Wrapper.INSTANCE.player().removePotionEffect(Potion.getPotionById(id));
    }
    
    public static void clearEffects() {
        for (final PotionEffect effect : Wrapper.INSTANCE.player().getActivePotionEffects()) {
            Wrapper.INSTANCE.player().removePotionEffect(effect.getPotion());
        }
    }
    
    public static double[] teleportToPosition(final double[] startPosition, final double[] endPosition, final double setOffset, final double slack, final boolean extendOffset, final boolean onGround) {
        boolean wasSneaking = false;
        if (Wrapper.INSTANCE.player().isSneaking()) {
            wasSneaking = true;
        }
        double startX = startPosition[0];
        double startY = startPosition[1];
        double startZ = startPosition[2];
        final double endX = endPosition[0];
        final double endY = endPosition[1];
        final double endZ = endPosition[2];
        double distance = Math.abs(startX - startY) + Math.abs(startY - endY) + Math.abs(startZ - endZ);
        int count = 0;
        while (distance > slack) {
            distance = Math.abs(startX - endX) + Math.abs(startY - endY) + Math.abs(startZ - endZ);
            if (count > 120) {
                break;
            }
            final double offset = (extendOffset && (count & 0x1) == 0x0) ? (setOffset + 0.15) : setOffset;
            final double diffX = startX - endX;
            final double diffY = startY - endY;
            final double diffZ = startZ - endZ;
            if (diffX < 0.0) {
                if (Math.abs(diffX) > offset) {
                    startX += offset;
                }
                else {
                    startX += Math.abs(diffX);
                }
            }
            if (diffX > 0.0) {
                if (Math.abs(diffX) > offset) {
                    startX -= offset;
                }
                else {
                    startX -= Math.abs(diffX);
                }
            }
            if (diffY < 0.0) {
                if (Math.abs(diffY) > offset) {
                    startY += offset;
                }
                else {
                    startY += Math.abs(diffY);
                }
            }
            if (diffY > 0.0) {
                if (Math.abs(diffY) > offset) {
                    startY -= offset;
                }
                else {
                    startY -= Math.abs(diffY);
                }
            }
            if (diffZ < 0.0) {
                if (Math.abs(diffZ) > offset) {
                    startZ += offset;
                }
                else {
                    startZ += Math.abs(diffZ);
                }
            }
            if (diffZ > 0.0) {
                if (Math.abs(diffZ) > offset) {
                    startZ -= offset;
                }
                else {
                    startZ -= Math.abs(diffZ);
                }
            }
            if (wasSneaking) {
                Wrapper.INSTANCE.sendPacket((Packet)new CPacketEntityAction((Entity)Wrapper.INSTANCE.player(), CPacketEntityAction.Action.STOP_SNEAKING));
            }
            Wrapper.INSTANCE.mc().getConnection().getNetworkManager().sendPacket((Packet)new CPacketPlayer.Position(startX, startY, startZ, onGround));
            ++count;
        }
        if (wasSneaking) {
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketEntityAction((Entity)Wrapper.INSTANCE.player(), CPacketEntityAction.Action.START_SNEAKING));
        }
        return new double[] { startX, startY, startZ };
    }
    
    public static void selfDamage(final double posY) {
        if (!Wrapper.INSTANCE.player().onGround) {
            return;
        }
        for (int i = 0; i <= 64.0; ++i) {
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + posY, Wrapper.INSTANCE.player().posZ, false));
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ, i == 64.0));
        }
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        player.motionX *= 0.2;
        final EntityPlayerSP player2 = Wrapper.INSTANCE.player();
        player2.motionZ *= 0.2;
        swingMainHand();
    }
    
    public static String getPlayerName(final EntityPlayer player) {
        return (player.getGameProfile() != null) ? player.getGameProfile().getName() : player.getName();
    }
    
    public static boolean isPlayer(final Entity entity) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            final String entityName = getPlayerName(player);
            final String playerName = getPlayerName((EntityPlayer)Wrapper.INSTANCE.player());
            if (entityName.equals(playerName)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isMurder(final EntityLivingBase entity) {
        mysteryFind(entity, 0);
        if (!EnemyManager.murders.isEmpty() && entity instanceof EntityPlayer) {
            final EntityPlayer murder = (EntityPlayer)entity;
            for (final String name : EnemyManager.murders) {
                if (murder.getGameProfile().getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isDetect(final EntityLivingBase entity) {
        mysteryFind(entity, 1);
        if (!EnemyManager.detects.isEmpty() && entity instanceof EntityPlayer) {
            final EntityPlayer murder = (EntityPlayer)entity;
            for (final String name : EnemyManager.detects) {
                if (murder.getGameProfile().getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void mysteryFind(final EntityLivingBase entity, final int target) {
        if (target == 0) {
            if (!EnemyManager.murders.isEmpty()) {
                for (int index = 0; index < EnemyManager.murders.size(); ++index) {
                    final EntityLivingBase murder = getWorldEntityByName(EnemyManager.murders.get(index));
                    if (murder == null) {
                        EnemyManager.murders.remove(index);
                    }
                }
            }
        }
        else if (target == 1 && !EnemyManager.detects.isEmpty()) {
            for (int index = 0; index < EnemyManager.detects.size(); ++index) {
                final EntityLivingBase detect = getWorldEntityByName(EnemyManager.detects.get(index));
                if (detect == null) {
                    EnemyManager.detects.remove(index);
                }
            }
        }
        if (entity instanceof EntityPlayerSP) {
            return;
        }
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        final EntityPlayer player = (EntityPlayer)entity;
        if (player.getGameProfile() == null) {
            return;
        }
        final GameProfile profile = player.getGameProfile();
        if (profile.getName() == null) {
            return;
        }
        if (EnemyManager.murders.contains(profile.getName()) || EnemyManager.detects.contains(profile.getName())) {
            return;
        }
        if (player.inventory == null) {
            return;
        }
        for (int slot = 0; slot < 36; ++slot) {
            final ItemStack stack = player.inventory.getStackInSlot(slot);
            if (stack != null) {
                final Item item = stack.getItem();
                if (item != null) {
                    if (target == 0) {
                        if (item == Items.IRON_SWORD || item == Items.DIAMOND_SWORD || item == Items.GOLDEN_SWORD || item == Items.STONE_SWORD || item == Items.WOODEN_SWORD || item == Items.IRON_SHOVEL || item == Items.DIAMOND_SHOVEL || item == Items.GOLDEN_SHOVEL || item == Items.STONE_SHOVEL || item == Items.WOODEN_SHOVEL || item == Items.IRON_AXE || item == Items.DIAMOND_AXE || item == Items.GOLDEN_AXE || item == Items.STONE_AXE || item == Items.WOODEN_AXE || item == Items.IRON_PICKAXE || item == Items.DIAMOND_PICKAXE || item == Items.GOLDEN_PICKAXE || item == Items.STONE_PICKAXE || item == Items.WOODEN_PICKAXE || item == Items.IRON_HOE || item == Items.DIAMOND_HOE || item == Items.GOLDEN_HOE || item == Items.STONE_HOE || item == Items.WOODEN_HOE || item == Items.STICK || item == Items.BLAZE_ROD || item == Items.FISHING_ROD || item == Items.CARROT || item == Items.GOLDEN_CARROT || item == Items.BONE || item == Items.COOKIE || item == Items.FEATHER || item == Items.PUMPKIN_PIE || item == Items.COOKED_FISH || item == Items.FISH || item == Items.SHEARS || item == Items.CARROT_ON_A_STICK) {
                            final String name = player.getGameProfile().getName();
                            EnemyManager.murders.add(name);
                        }
                    }
                    else if (target == 1 && item == Items.BOW) {
                        final String name = player.getGameProfile().getName();
                        EnemyManager.detects.add(name);
                    }
                }
            }
        }
    }
    
    public static boolean checkEnemyNameColor(final EntityLivingBase entity) {
        final String name = entity.getDisplayName().getFormattedText();
        return !getEntityNameColor((EntityLivingBase)Wrapper.INSTANCE.player()).equals(getEntityNameColor(entity));
    }
    
    public static String getEntityNameColor(final EntityLivingBase entity) {
        final String name = entity.getDisplayName().getFormattedText();
        if (name.contains("¡ì")) {
            if (name.contains("¡ì1")) {
                return "¡ì1";
            }
            if (name.contains("¡ì2")) {
                return "¡ì2";
            }
            if (name.contains("¡ì3")) {
                return "¡ì3";
            }
            if (name.contains("¡ì4")) {
                return "¡ì4";
            }
            if (name.contains("¡ì5")) {
                return "¡ì5";
            }
            if (name.contains("¡ì6")) {
                return "¡ì6";
            }
            if (name.contains("¡ì7")) {
                return "¡ì7";
            }
            if (name.contains("¡ì8")) {
                return "¡ì8";
            }
            if (name.contains("¡ì9")) {
                return "¡ì9";
            }
            if (name.contains("¡ì0")) {
                return "¡ì0";
            }
            if (name.contains("¡ìe")) {
                return "¡ìe";
            }
            if (name.contains("¡ìd")) {
                return "¡ìd";
            }
            if (name.contains("¡ìa")) {
                return "¡ìa";
            }
            if (name.contains("¡ìb")) {
                return "¡ìb";
            }
            if (name.contains("¡ìc")) {
                return "¡ìc";
            }
            if (name.contains("¡ìf")) {
                return "¡ìf";
            }
        }
        return "null";
    }
    
    public static int getPlayerArmorColor(final EntityPlayer player, final ItemStack stack) {
        if (player == null || stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemArmor)) {
            return -1;
        }
        final ItemArmor itemArmor = (ItemArmor)stack.getItem();
        if (itemArmor == null || itemArmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER) {
            return -1;
        }
        return itemArmor.getColor(stack);
    }
    
    public static boolean checkEnemyColor(final EntityPlayer enemy) {
        final int colorEnemy0 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(0));
        final int colorEnemy2 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(1));
        final int colorEnemy3 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(2));
        final int colorEnemy4 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(3));
        final int colorPlayer0 = getPlayerArmorColor((EntityPlayer)Wrapper.INSTANCE.player(), Wrapper.INSTANCE.inventory().armorItemInSlot(0));
        final int colorPlayer2 = getPlayerArmorColor((EntityPlayer)Wrapper.INSTANCE.player(), Wrapper.INSTANCE.inventory().armorItemInSlot(1));
        final int colorPlayer3 = getPlayerArmorColor((EntityPlayer)Wrapper.INSTANCE.player(), Wrapper.INSTANCE.inventory().armorItemInSlot(2));
        final int colorPlayer4 = getPlayerArmorColor((EntityPlayer)Wrapper.INSTANCE.player(), Wrapper.INSTANCE.inventory().armorItemInSlot(3));
        return (colorEnemy0 != colorPlayer0 || colorPlayer0 == -1 || colorEnemy0 == 1) && (colorEnemy2 != colorPlayer2 || colorPlayer2 == -1 || colorEnemy2 == 1) && (colorEnemy3 != colorPlayer3 || colorPlayer3 == -1 || colorEnemy3 == 1) && (colorEnemy4 != colorPlayer4 || colorPlayer4 == -1 || colorEnemy4 == 1);
    }
    
    public static boolean screenCheck() {
        return !(Wrapper.INSTANCE.mc().currentScreen instanceof GuiContainer) && !(Wrapper.INSTANCE.mc().currentScreen instanceof GuiChat) && !(Wrapper.INSTANCE.mc().currentScreen instanceof GuiScreen);
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static EntityLivingBase getWorldEntityByName(final String name) {
        EntityLivingBase entity = null;
        for (final Object object : getEntityList()) {
            if (object instanceof EntityLivingBase) {
                final EntityLivingBase entityForCheck = (EntityLivingBase)object;
                if (!entityForCheck.getName().contains(name)) {
                    continue;
                }
                entity = entityForCheck;
            }
        }
        return entity;
    }
    
    public static float[] getDirectionToBlock(final int var0, final int var1, final int var2, final EnumFacing var3) {
        final EntityEgg var4 = new EntityEgg((World)Wrapper.INSTANCE.world());
        var4.posX = var0 + 0.5;
        var4.posY = var1 + 0.5;
        var4.posZ = var2 + 0.5;
        final EntityEgg entityEgg = var4;
        entityEgg.posX += var3.getDirectionVec().getX() * 0.25;
        final EntityEgg entityEgg2 = var4;
        entityEgg2.posY += var3.getDirectionVec().getY() * 0.25;
        final EntityEgg entityEgg3 = var4;
        entityEgg3.posZ += var3.getDirectionVec().getZ() * 0.25;
        return getDirectionToEntity((Entity)var4);
    }
    
    private static float[] getDirectionToEntity(final Entity var0) {
        return new float[] { getYaw(var0) + Wrapper.INSTANCE.player().rotationYaw, getPitch(var0) + Wrapper.INSTANCE.player().rotationPitch };
    }
    
    public static float getPitch(final Entity entity) {
        final double x = entity.posX - Wrapper.INSTANCE.player().posX;
        double y = entity.posY - Wrapper.INSTANCE.player().posY;
        final double z = entity.posZ - Wrapper.INSTANCE.player().posZ;
        y /= Wrapper.INSTANCE.player().getDistanceSq(entity);
        double pitch = Math.asin(y) * 57.29577951308232;
        pitch = -pitch;
        return (float)pitch;
    }
    
    public static float getYaw(final Entity entity) {
        final double x = entity.posX - Wrapper.INSTANCE.player().posX;
        final double y = entity.posY - Wrapper.INSTANCE.player().posY;
        final double z = entity.posZ - Wrapper.INSTANCE.player().posZ;
        double yaw = Math.atan2(x, z) * 57.29577951308232;
        yaw = -yaw;
        return (float)yaw;
    }
    
    public static float[] getNeededRotations(final Vec3d vec) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { Wrapper.INSTANCE.player().rotationYaw + MathHelper.wrapDegrees(yaw - Wrapper.INSTANCE.player().rotationYaw), Wrapper.INSTANCE.player().rotationPitch + MathHelper.wrapDegrees(pitch - Wrapper.INSTANCE.player().rotationPitch) };
    }
    
    public static float getDirection() {
        float var1 = Wrapper.INSTANCE.player().rotationYaw;
        if (Wrapper.INSTANCE.player().moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        if (Wrapper.INSTANCE.player().moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (Wrapper.INSTANCE.player().moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Wrapper.INSTANCE.player().moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (Wrapper.INSTANCE.player().moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        var1 *= 0.017453292f;
        return var1;
    }
    
    public static void faceVectorPacket(final Vec3d vec) {
        final float[] rotations = getNeededRotations(vec);
        final EntityPlayerSP pl = Minecraft.getMinecraft().player;
        final float preYaw = pl.rotationYaw;
        final float prePitch = pl.rotationPitch;
        pl.rotationYaw = rotations[0];
        pl.rotationPitch = rotations[1];
        try {
            final Method onUpdateWalkingPlayer = pl.getClass().getDeclaredMethod(Mapping.onUpdateWalkingPlayer, (Class<?>[])new Class[0]);
            onUpdateWalkingPlayer.setAccessible(true);
            onUpdateWalkingPlayer.invoke(pl, new Object[0]);
        }
        catch (Exception ex) {}
        pl.rotationYaw = preYaw;
        pl.rotationPitch = prePitch;
    }
    
    public static void setEntityBoundingBoxSize(final Entity entity, final float width, final float height) {
        if (entity.width == width && entity.height == height) {
            return;
        }
        entity.width = width;
        entity.height = height;
        final double d0 = width / 2.0;
        entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + entity.height, entity.posZ + d0));
    }
    
    public static boolean placeBlockScaffold(final BlockPos pos) {
        final Vec3d eyesPos = new Vec3d(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight(), Wrapper.INSTANCE.player().posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3d((Vec3i)pos).addVector(0.5, 0.5, 0.5)) < eyesPos.squareDistanceTo(new Vec3d((Vec3i)neighbor).addVector(0.5, 0.5, 0.5)) && canBeClicked(neighbor)) {
                final Vec3d hitVec = new Vec3d((Vec3i)neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
                if (eyesPos.squareDistanceTo(hitVec) <= 18.0625) {
                    faceVectorPacketInstant(hitVec);
                    swingMainHand();
                    Wrapper.INSTANCE.controller().processRightClickBlock(Wrapper.INSTANCE.player(), Wrapper.INSTANCE.world(), neighbor, side2, hitVec, EnumHand.MAIN_HAND);
                    try {
                        final Field f = Minecraft.class.getDeclaredField(Mapping.rightClickDelayTimer);
                        f.setAccessible(true);
                        f.set(Wrapper.INSTANCE.mc(), 4);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isInsideBlock(final EntityLivingBase entity) {
        for (int x = MathHelper.floor(entity.getEntityBoundingBox().minX); x < MathHelper.floor(entity.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int y = MathHelper.floor(entity.getEntityBoundingBox().minY); y < MathHelper.floor(entity.getEntityBoundingBox().maxY) + 1; ++y) {
                for (int z = MathHelper.floor(entity.getEntityBoundingBox().minZ); z < MathHelper.floor(entity.getEntityBoundingBox().maxZ) + 1; ++z) {
                    final Block block = BlockUtils.getBlock(new BlockPos(x, y, z));
                    final AxisAlignedBB boundingBox;
                    if (block != null && !(block instanceof BlockAir) && (boundingBox = block.getCollisionBoundingBox(BlockUtils.getState(new BlockPos(x, y, z)), (IBlockAccess)Wrapper.INSTANCE.world(), new BlockPos(x, y, z))) != null && entity.getEntityBoundingBox().intersects(boundingBox)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isBlockEdge(final EntityLivingBase entity) {
        return Wrapper.INSTANCE.world().getCollisionBoxes((Entity)entity, entity.getEntityBoundingBox().offset(0.0, -0.5, 0.0).expand(0.001, 0.0, 0.001)).isEmpty() && entity.onGround;
    }
    
    public static void faceEntity(final EntityLivingBase entity) {
        if (entity == null) {
            return;
        }
        final double d0 = entity.posX - Wrapper.INSTANCE.player().posX;
        final double d2 = entity.posY - Wrapper.INSTANCE.player().posY;
        final double d3 = entity.posZ - Wrapper.INSTANCE.player().posZ;
        final double d4 = Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight() - (entity.posY + entity.getEyeHeight());
        final double d5 = MathHelper.sqrt(d0 * d0 + d3 * d3);
        final float f = (float)(Math.atan2(d3, d0) * 180.0 / 3.141592653589793) - 90.0f;
        final float f2 = (float)(-(Math.atan2(d4, d5) * 180.0 / 3.141592653589793));
        Wrapper.INSTANCE.player().rotationYaw = f;
        Wrapper.INSTANCE.player().rotationPitch = f2;
    }
    
    public static void assistFaceEntity(final Entity entity, final float yaw, final float pitch) {
        if (entity == null) {
            return;
        }
        final double diffX = entity.posX - Wrapper.INSTANCE.player().posX;
        final double diffZ = entity.posZ - Wrapper.INSTANCE.player().posZ;
        double yDifference;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            yDifference = entityLivingBase.posY + entityLivingBase.getEyeHeight() - (Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight());
        }
        else {
            yDifference = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight());
        }
        final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        final float rotationYaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float rotationPitch = (float)(-(Math.atan2(yDifference, dist) * 180.0 / 3.141592653589793));
        if (yaw > 0.0f) {
            Wrapper.INSTANCE.player().rotationYaw = updateRotation(Wrapper.INSTANCE.player().rotationYaw, rotationYaw, yaw / 4.0f);
        }
        if (pitch > 0.0f) {
            Wrapper.INSTANCE.player().rotationPitch = updateRotation(Wrapper.INSTANCE.player().rotationPitch, rotationPitch, pitch / 4.0f);
        }
    }
    
    public static float updateRotation(final float angle, final float targetAngle, final float maxIncrease) {
        float var4 = MathHelper.wrapDegrees(targetAngle - angle);
        if (var4 > maxIncrease) {
            var4 = maxIncrease;
        }
        if (var4 < -maxIncrease) {
            var4 = -maxIncrease;
        }
        return angle + var4;
    }
    
    public static int getDistanceFromMouse(final EntityLivingBase entity) {
        final float[] neededRotations = getRotationsNeeded((Entity)entity);
        if (neededRotations != null) {
            final float neededYaw = Wrapper.INSTANCE.player().rotationYaw - neededRotations[0];
            final float neededPitch = Wrapper.INSTANCE.player().rotationPitch - neededRotations[1];
            final float distanceFromMouse = MathHelper.sqrt(neededYaw * neededYaw + neededPitch * neededPitch * 2.0f);
            return (int)distanceFromMouse;
        }
        return -1;
    }
    
    public static float[] getSmoothNeededRotations(final Vec3d vec, final float yaw, final float pitch) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float rotationYaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float rotationPitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { updateRotation(Wrapper.INSTANCE.player().rotationYaw, rotationYaw, yaw / 4.0f), updateRotation(Wrapper.INSTANCE.player().rotationPitch, rotationPitch, pitch / 4.0f) };
    }
    
    public static float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final double diffX = entity.posX - Wrapper.INSTANCE.mc().player.posX;
        final double diffZ = entity.posZ - Wrapper.INSTANCE.mc().player.posZ;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() - (Wrapper.INSTANCE.mc().player.posY + Wrapper.INSTANCE.mc().player.getEyeHeight());
        }
        else {
            diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (Wrapper.INSTANCE.mc().player.posY + Wrapper.INSTANCE.mc().player.getEyeHeight());
        }
        final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { Wrapper.INSTANCE.mc().player.rotationYaw + MathHelper.wrapDegrees(yaw - Wrapper.INSTANCE.mc().player.rotationYaw), Wrapper.INSTANCE.mc().player.rotationPitch + MathHelper.wrapDegrees(pitch - Wrapper.INSTANCE.mc().player.rotationPitch) };
    }
    
    public static String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final StringBuilder sb = new StringBuilder();
        final String main = System.getenv("PROCESS_IDENTIFIER") + System.getenv("COMPUTERNAME");
        final byte[] bytes = main.getBytes("UTF-8");
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] md2 = md.digest(bytes);
        int i = 0;
        for (final byte b : md2) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x300), 0, 3);
            if (i != md2.length - 1) {
                sb.append("-");
            }
            ++i;
        }
        return sb.toString();
    }
    
    public static boolean checkuser() {
        try {
            if (WebUtils.get("https://gitee.com/beimian-hurricane/codes/9j3p4subfkaiyt8egdw2586/raw?blob_name=HWID.txt").contains(getHWID())) {
                ChatUtils.message("OK!");
                return true;
            }
            ChatUtils.error("verification failed! your uuid is" + getHWID());
            return false;
        }
        catch (NoSuchAlgorithmException e) {
            ChatUtils.error("Network Error!");
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e2) {
            ChatUtils.error("Error");
            e2.printStackTrace();
        }
        catch (IOException e3) {
            ChatUtils.error("Error");
            e3.printStackTrace();
        }
        return true;
    }
    
    static {
        Utils.rotationsToBlock = null;
        RANDOM = new Random();
    }
    
    public static class Player
    {
        public static Minecraft mc;
        
        public static void hotkeyToSlot(final int slot) {
            if (!isPlayerInGame()) {
                return;
            }
            Player.mc.player.inventory.currentItem = slot;
        }
        
        public static void sendMessageToSelf(final String txt) {
            ChatUtils.message(txt);
        }
        
        public static boolean isPlayerInGame() {
            return Player.mc.player != null && Player.mc.world != null;
        }
        
        public static boolean isMoving() {
            return Player.mc.player.moveForward != 0.0f || Player.mc.player.moveStrafing != 0.0f;
        }
        
        public static void aim(final Entity en, final float ps, final boolean pc) {
            if (en != null) {
                final float[] t = getTargetRotations(en);
                if (t != null) {
                    final float y = t[0];
                    final float p = t[1] + 4.0f + ps;
                    if (pc) {
                        Player.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Rotation(y, p, Player.mc.player.onGround));
                    }
                    else {
                        Player.mc.player.rotationYaw = y;
                        Player.mc.player.rotationPitch = p;
                    }
                }
            }
        }
        
        public static double fovFromEntity(final Entity en) {
            return ((Player.mc.player.rotationYaw - fovToEntity(en)) % 360.0 + 540.0) % 360.0 - 180.0;
        }
        
        public static float fovToEntity(final Entity ent) {
            final double x = ent.posX - Player.mc.player.posX;
            final double z = ent.posZ - Player.mc.player.posZ;
            final double yaw = Math.atan2(x, z) * 57.2957795;
            return (float)(yaw * -1.0);
        }
        
        public static boolean fov(final Entity entity, float fov) {
            fov *= 0.5;
            final double v = ((Player.mc.player.rotationYaw - fovToEntity(entity)) % 360.0 + 540.0) % 360.0 - 180.0;
            return (v > 0.0 && v < fov) || (-fov < v && v < 0.0);
        }
        
        public static float[] getTargetRotations(final Entity q) {
            if (q == null) {
                return null;
            }
            final double diffX = q.posX - Player.mc.player.posX;
            double diffY;
            if (q instanceof EntityLivingBase) {
                final EntityLivingBase en = (EntityLivingBase)q;
                diffY = en.posY + en.getEyeHeight() * 0.9 - (Player.mc.player.posY + Player.mc.player.getEyeHeight());
            }
            else {
                diffY = (q.getEntityBoundingBox().minY + q.getEntityBoundingBox().maxY) / 2.0 - (Player.mc.player.posY + Player.mc.player.getEyeHeight());
            }
            final double diffZ = q.posZ - Player.mc.player.posZ;
            final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
            final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
            final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
            return new float[] { Player.mc.player.rotationYaw + wrapAngleTo180_float(yaw - Player.mc.player.rotationYaw), Player.mc.player.rotationPitch + wrapAngleTo180_float(pitch - Player.mc.player.rotationPitch) };
        }
        
        public static float wrapAngleTo180_float(float value) {
            value %= 360.0f;
            if (value >= 180.0f) {
                value -= 360.0f;
            }
            if (value < -180.0f) {
                value += 360.0f;
            }
            return value;
        }
        
        public static void fixMovementSpeed(final double s, final boolean m) {
            if (!m || isMoving()) {
                Player.mc.player.motionX = -Math.sin(correctRotations()) * s;
                Player.mc.player.motionZ = Math.cos(correctRotations()) * s;
            }
        }
        
        public static void bop(final double s) {
            double forward = Player.mc.player.movementInput.moveForward;
            double strafe = Player.mc.player.movementInput.moveStrafe;
            float yaw = Player.mc.player.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                Player.mc.player.motionX = 0.0;
                Player.mc.player.motionZ = 0.0;
            }
            else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += ((forward > 0.0) ? -45 : 45);
                    }
                    else if (strafe < 0.0) {
                        yaw += ((forward > 0.0) ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    }
                    else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }
                final double rad = Math.toRadians(yaw + 90.0f);
                final double sin = Math.sin(rad);
                final double cos = Math.cos(rad);
                Player.mc.player.motionX = forward * s * cos + strafe * s * sin;
                Player.mc.player.motionZ = forward * s * sin - strafe * s * cos;
            }
        }
        
        public static float correctRotations() {
            float yw = Player.mc.player.rotationYaw;
            if (Player.mc.player.moveForward < 0.0f) {
                yw += 180.0f;
            }
            float f;
            if (Player.mc.player.moveForward < 0.0f) {
                f = -0.5f;
            }
            else if (Player.mc.player.moveForward > 0.0f) {
                f = 0.5f;
            }
            else {
                f = 1.0f;
            }
            if (Player.mc.player.moveStrafing > 0.0f) {
                yw -= 90.0f * f;
            }
            if (Player.mc.player.moveStrafing < 0.0f) {
                yw += 90.0f * f;
            }
            yw *= 0.017453292f;
            return yw;
        }
        
        public static double pythagorasMovement() {
            return Math.sqrt(Player.mc.player.motionX * Player.mc.player.motionX + Player.mc.player.motionZ * Player.mc.player.motionZ);
        }
        
        static {
            Player.mc = Minecraft.getMinecraft();
        }
    }
}
