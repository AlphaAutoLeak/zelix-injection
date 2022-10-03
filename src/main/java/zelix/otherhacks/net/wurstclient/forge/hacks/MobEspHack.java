package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import org.lwjgl.opengl.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraft.world.*;
import java.util.stream.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.entity.*;
import java.util.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import net.minecraft.util.math.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;

public final class MobEspHack extends Hack
{
    private final EnumSetting<Style> style;
    private final CheckboxSetting filterInvisible;
    private int mobBox;
    private final ArrayList<Entity> mobs;
    
    public MobEspHack() {
        super("MobESP", "Highlights nearby mobs.");
        this.style = new EnumSetting<Style>("Style", Style.values(), Style.BOXES);
        this.filterInvisible = new CheckboxSetting("Filter invisible", "Won't show invisible mobs.", false);
        this.mobs = new ArrayList<Entity>();
        this.setCategory(Category.RENDER);
        this.addSetting(this.style);
        this.addSetting(this.filterInvisible);
    }
    
    @Override
    protected void onEnable() {
        MobEspHack.wurst.register(this);
        GL11.glNewList(this.mobBox = GL11.glGenLists(1), 4864);
        GL11.glBegin(1);
        final AxisAlignedBB bb = new AxisAlignedBB(-0.5, 0.0, -0.5, 0.5, 1.0, 0.5);
        RenderUtils.drawOutlinedBox(bb);
        GL11.glEnd();
        GL11.glEndList();
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        GL11.glDeleteLists(this.mobBox, 1);
        this.mobBox = 0;
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        final World world = event.getPlayer().world;
        this.mobs.clear();
        Stream<Entity> stream = world.loadedEntityList.parallelStream().filter(e -> e instanceof EntityLiving).map(e -> e).filter(e -> !e.isDead && ((EntityLiving) e).getHealth() > 0.0f);
        if (this.filterInvisible.isChecked()) {
            stream = stream.filter(e -> !e.isInvisible());
        }
        this.mobs.addAll(stream.collect(Collectors.toList()));
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
            this.renderTracers(partialTicks);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    private void renderBoxes(final double partialTicks) {
        for (final Entity e : this.mobs) {
            GL11.glPushMatrix();
            GL11.glTranslated(e.prevPosX + (e.posX - e.prevPosX) * partialTicks, e.prevPosY + (e.posY - e.prevPosY) * partialTicks, e.prevPosZ + (e.posZ - e.prevPosZ) * partialTicks);
            GL11.glScaled(e.width + 0.1, e.height + 0.1, e.width + 0.1);
            final float f = MobEspHack.mc.player.getDistance((Entity)e) / 20.0f;
            GL11.glColor4f(2.0f - f, f, 0.0f, 0.5f);
            GL11.glCallList(this.mobBox);
            GL11.glPopMatrix();
        }
    }
    
    private void renderTracers(final double partialTicks) {
        final Vec3d start = RotationUtils.getClientLookVec().addVector(0.0, (double)WMinecraft.getPlayer().getEyeHeight(), 0.0).addVector(TileEntityRendererDispatcher.staticPlayerX, TileEntityRendererDispatcher.staticPlayerY, TileEntityRendererDispatcher.staticPlayerZ);
        GL11.glBegin(1);
        for (final Entity e : this.mobs) {
            final Vec3d end = e.getEntityBoundingBox().getCenter().subtract(new Vec3d(e.posX, e.posY, e.posZ).subtract(e.prevPosX, e.prevPosY, e.prevPosZ).scale(1.0 - partialTicks));
            final float f = MobEspHack.mc.player.getDistance((Entity)e) / 20.0f;
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
