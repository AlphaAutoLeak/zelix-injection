package zelix.otherhacks.net.wurstclient.forge.hacks;

import net.minecraft.entity.player.*;
import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import org.lwjgl.opengl.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraft.client.entity.*;
import java.util.stream.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.entity.*;
import java.util.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import net.minecraft.util.math.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;

public final class PlayerEspHack extends Hack
{
    private final EnumSetting<Style> style;
    private final CheckboxSetting filterSleeping;
    private final CheckboxSetting filterInvisible;
    private int playerBox;
    private final ArrayList<EntityPlayer> players;
    
    public PlayerEspHack() {
        super("PlayerESP", "Highlights nearby players.");
        this.style = new EnumSetting<Style>("Style", Style.values(), Style.BOXES);
        this.filterSleeping = new CheckboxSetting("Filter sleeping", "Won't show sleeping players.", false);
        this.filterInvisible = new CheckboxSetting("Filter invisible", "Won't show invisible players.", false);
        this.players = new ArrayList<EntityPlayer>();
        this.setCategory(Category.RENDER);
        this.addSetting(this.style);
        this.addSetting(this.filterSleeping);
        this.addSetting(this.filterInvisible);
    }
    
    @Override
    protected void onEnable() {
        PlayerEspHack.wurst.register(this);
        GL11.glNewList(this.playerBox = GL11.glGenLists(1), 4864);
        GL11.glBegin(1);
        final AxisAlignedBB bb = new AxisAlignedBB(-0.5, 0.0, -0.5, 0.5, 1.0, 0.5);
        RenderUtils.drawOutlinedBox(bb);
        GL11.glEnd();
        GL11.glEndList();
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        GL11.glDeleteLists(this.playerBox, 1);
        this.playerBox = 0;
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        final EntityPlayerSP player = event.getPlayer();
        this.players.clear();
        Stream<EntityPlayer> stream = (Stream<EntityPlayer>)player.world.playerEntities.parallelStream().filter(e -> !e.isDead && e.getHealth() > 0.0f).filter(e -> e != player).filter(e -> !(e instanceof EntityFakePlayer)).filter(e -> Math.abs(e.posY - WMinecraft.getPlayer().posY) <= 1000000.0);
        if (this.filterSleeping.isChecked()) {
            stream = stream.filter(e -> !e.isPlayerSleeping());
        }
        if (this.filterInvisible.isChecked()) {
            stream = stream.filter(e -> !e.isInvisible());
        }
        this.players.addAll(stream.collect(Collectors.toList()));
    }
    
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glPushMatrix();
        GL11.glTranslated(-TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY, -TileEntityRendererDispatcher.staticPlayerZ);
        final double partialTicks = event.getPartialTicks();
        if (this.style.getSelected().boxes) {
            this.renderBoxes(partialTicks);
        }
        if (this.style.getSelected().lines) {
            this.renderLines(partialTicks);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    private void renderBoxes(final double partialTicks) {
        for (final EntityPlayer e : this.players) {
            GL11.glPushMatrix();
            GL11.glTranslated(e.prevPosX + (e.posX - e.prevPosX) * partialTicks, e.prevPosY + (e.posY - e.prevPosY) * partialTicks, e.prevPosZ + (e.posZ - e.prevPosZ) * partialTicks);
            GL11.glScaled(e.width + 0.1, e.height + 0.1, e.width + 0.1);
            final float f = WMinecraft.getPlayer().getDistance((Entity)e) / 20.0f;
            GL11.glColor4f(2.0f - f, f, 0.0f, 0.5f);
            GL11.glCallList(this.playerBox);
            GL11.glPopMatrix();
        }
    }
    
    private void renderLines(final double partialTicks) {
        final Vec3d start = RotationUtils.getClientLookVec().addVector(0.0, (double)WMinecraft.getPlayer().getEyeHeight(), 0.0).addVector(TileEntityRendererDispatcher.staticPlayerX, TileEntityRendererDispatcher.staticPlayerY, TileEntityRendererDispatcher.staticPlayerZ);
        GL11.glBegin(1);
        for (final EntityPlayer e : this.players) {
            final Vec3d end = e.getEntityBoundingBox().getCenter().subtract(new Vec3d(e.posX, e.posY, e.posZ).subtract(e.prevPosX, e.prevPosY, e.prevPosZ).scale(1.0 - partialTicks));
            final float f = WMinecraft.getPlayer().getDistance((Entity)e) / 20.0f;
            GL11.glColor4f(2.0f - f, f, 0.0f, 0.5f);
            GL11.glVertex3d(WVec3d.getX(start), WVec3d.getY(start), WVec3d.getZ(start));
            GL11.glVertex3d(WVec3d.getX(end), WVec3d.getY(end), WVec3d.getZ(end));
        }
        GL11.glEnd();
    }
    
    private enum Style
    {
        BOXES("Boxes only", true, false), 
        LINES("Lines only", false, true), 
        LINES_AND_BOXES("Lines and boxes", true, true);
        
        private final String name;
        private final boolean boxes;
        private final boolean lines;
        
        private Style(final String name, final boolean boxes, final boolean lines) {
            this.name = name;
            this.boxes = boxes;
            this.lines = lines;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
}
