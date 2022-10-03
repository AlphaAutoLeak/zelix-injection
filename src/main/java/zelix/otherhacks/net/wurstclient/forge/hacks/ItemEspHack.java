package zelix.otherhacks.net.wurstclient.forge.hacks;

import net.minecraft.entity.item.*;
import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import org.lwjgl.opengl.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.client.renderer.*;
import net.minecraft.item.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import net.minecraft.util.math.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;

public final class ItemEspHack extends Hack
{
    private final CheckboxSetting names;
    private final EnumSetting<Style> style;
    private int itemBox;
    private final ArrayList<EntityItem> items;
    
    public ItemEspHack() {
        super("ItemESP", "Highlights nearby items.");
        this.names = new CheckboxSetting("Show item names", true);
        this.style = new EnumSetting<Style>("Style", Style.values(), Style.BOXES);
        this.items = new ArrayList<EntityItem>();
        this.setCategory(Category.RENDER);
        this.addSetting(this.names);
        this.addSetting(this.style);
    }
    
    @Override
    protected void onEnable() {
        ItemEspHack.wurst.register(this);
        GL11.glNewList(this.itemBox = GL11.glGenLists(1), 4864);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glColor4f(1.0f, 1.0f, 0.0f, 0.5f);
        GL11.glBegin(1);
        RenderUtils.drawOutlinedBox(new AxisAlignedBB(-0.175, 0.0, -0.175, 0.175, 0.35, 0.175));
        GL11.glEnd();
        GL11.glEndList();
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        GL11.glDeleteLists(this.itemBox, 1);
        this.itemBox = 0;
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        final World world = event.getPlayer().world;
        this.items.clear();
        for (final Entity entity : world.loadedEntityList) {
            if (entity instanceof EntityItem) {
                this.items.add((EntityItem)entity);
            }
        }
    }
    
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glPushMatrix();
        GL11.glTranslated(-TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY, -TileEntityRendererDispatcher.staticPlayerZ);
        final double partialTicks = event.getPartialTicks();
        this.renderBoxes(partialTicks);
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
        for (final EntityItem e : this.items) {
            GL11.glPushMatrix();
            GL11.glTranslated(e.prevPosX + (e.posX - e.prevPosX) * partialTicks, e.prevPosY + (e.posY - e.prevPosY) * partialTicks, e.prevPosZ + (e.posZ - e.prevPosZ) * partialTicks);
            if (this.style.getSelected().boxes) {
                GL11.glCallList(this.itemBox);
            }
            if (this.names.isChecked()) {
                final ItemStack stack = e.getItem();
                EntityRenderer.drawNameplate(WMinecraft.getFontRenderer(), stack.getCount() + "x " + stack.getDisplayName(), 0.0f, 1.0f, 0.0f, 0, ItemEspHack.mc.getRenderManager().playerViewY, ItemEspHack.mc.getRenderManager().playerViewX, ItemEspHack.mc.getRenderManager().options.thirdPersonView == 2, false);
                GL11.glDisable(2896);
            }
            GL11.glPopMatrix();
        }
    }
    
    private void renderTracers(final double partialTicks) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glColor4f(1.0f, 1.0f, 0.0f, 0.5f);
        final Vec3d start = RotationUtils.getClientLookVec().addVector(0.0, (double)WMinecraft.getPlayer().getEyeHeight(), 0.0).addVector(TileEntityRendererDispatcher.staticPlayerX, TileEntityRendererDispatcher.staticPlayerY, TileEntityRendererDispatcher.staticPlayerZ);
        GL11.glBegin(1);
        for (final EntityItem e : this.items) {
            final Vec3d end = e.getEntityBoundingBox().getCenter().subtract(new Vec3d(e.posX, e.posY, e.posZ).subtract(e.prevPosX, e.prevPosY, e.prevPosZ).scale(1.0 - partialTicks));
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
