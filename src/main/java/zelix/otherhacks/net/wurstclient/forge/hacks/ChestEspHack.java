package zelix.otherhacks.net.wurstclient.forge.hacks;

import net.minecraft.entity.*;
import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import org.lwjgl.opengl.*;
import net.minecraftforge.common.*;
import zelix.otherhacks.net.wurstclient.forge.events.*;
import net.minecraft.tileentity.*;
import net.minecraft.entity.item.*;
import net.minecraft.world.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.tileentity.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import net.minecraft.util.math.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import net.minecraft.block.*;

public final class ChestEspHack extends Hack
{
    private final EnumSetting<Style> style;
    private final ArrayList<AxisAlignedBB> basicChests;
    private final ArrayList<AxisAlignedBB> trappedChests;
    private final ArrayList<AxisAlignedBB> enderChests;
    private final ArrayList<Entity> minecarts;
    private int greenBox;
    private int orangeBox;
    private int cyanBox;
    private int normalChests;
    
    public ChestEspHack() {
        super("ChestESP", "Highlights nearby chests.\n¡ìagreen¡ìr - normal chests\n¡ì6orange¡ìr - trapped chests\n¡ìbcyan¡ìr - ender chests");
        this.style = new EnumSetting<Style>("Style", Style.values(), Style.BOXES);
        this.basicChests = new ArrayList<AxisAlignedBB>();
        this.trappedChests = new ArrayList<AxisAlignedBB>();
        this.enderChests = new ArrayList<AxisAlignedBB>();
        this.minecarts = new ArrayList<Entity>();
        this.setCategory(Category.RENDER);
        this.addSetting(this.style);
    }
    
    @Override
    protected void onEnable() {
        ChestEspHack.wurst.register(this);
        final AxisAlignedBB bb = new AxisAlignedBB(BlockPos.ORIGIN);
        GL11.glNewList(this.greenBox = GL11.glGenLists(1), 4864);
        GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.25f);
        GL11.glBegin(7);
        RenderUtils.drawSolidBox(bb);
        GL11.glEnd();
        GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.5f);
        GL11.glBegin(1);
        RenderUtils.drawOutlinedBox(bb);
        GL11.glEnd();
        GL11.glEndList();
        GL11.glNewList(this.orangeBox = GL11.glGenLists(1), 4864);
        GL11.glColor4f(1.0f, 0.5f, 0.0f, 0.25f);
        GL11.glBegin(7);
        RenderUtils.drawSolidBox(bb);
        GL11.glEnd();
        GL11.glColor4f(1.0f, 0.5f, 0.0f, 0.5f);
        GL11.glBegin(1);
        RenderUtils.drawOutlinedBox(bb);
        GL11.glEnd();
        GL11.glEndList();
        GL11.glNewList(this.cyanBox = GL11.glGenLists(1), 4864);
        GL11.glColor4f(0.0f, 1.0f, 1.0f, 0.25f);
        GL11.glBegin(7);
        RenderUtils.drawSolidBox(bb);
        GL11.glEnd();
        GL11.glColor4f(0.0f, 1.0f, 1.0f, 0.5f);
        GL11.glBegin(1);
        RenderUtils.drawOutlinedBox(bb);
        GL11.glEnd();
        GL11.glEndList();
        this.normalChests = GL11.glGenLists(1);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        GL11.glDeleteLists(this.greenBox, 1);
        this.greenBox = 0;
        GL11.glDeleteLists(this.orangeBox, 1);
        this.orangeBox = 0;
        GL11.glDeleteLists(this.cyanBox, 1);
        this.cyanBox = 0;
        GL11.glDeleteLists(this.normalChests, 1);
        this.normalChests = 0;
    }
    
    @SubscribeEvent
    public void onUpdate(final WUpdateEvent event) {
        final World world = event.getPlayer().world;
        this.basicChests.clear();
        this.trappedChests.clear();
        this.enderChests.clear();
        for (final TileEntity tileEntity : world.loadedTileEntityList) {
            if (tileEntity instanceof TileEntityChest) {
                final TileEntityChest chest = (TileEntityChest)tileEntity;
                if (chest.adjacentChestXPos != null) {
                    continue;
                }
                if (chest.adjacentChestZPos != null) {
                    continue;
                }
                final BlockPos pos = chest.getPos();
                AxisAlignedBB bb = BlockUtils.getBoundingBox(pos);
                if (chest.adjacentChestXNeg != null) {
                    final BlockPos pos2 = chest.adjacentChestXNeg.getPos();
                    final AxisAlignedBB bb2 = BlockUtils.getBoundingBox(pos2);
                    bb = bb.union(bb2);
                }
                else if (chest.adjacentChestZNeg != null) {
                    final BlockPos pos2 = chest.adjacentChestZNeg.getPos();
                    final AxisAlignedBB bb2 = BlockUtils.getBoundingBox(pos2);
                    bb = bb.union(bb2);
                }
                switch (chest.getChestType()) {
                    case BASIC: {
                        this.basicChests.add(bb);
                        continue;
                    }
                    case TRAP: {
                        this.trappedChests.add(bb);
                        continue;
                    }
                }
            }
            else {
                if (!(tileEntity instanceof TileEntityEnderChest)) {
                    continue;
                }
                final BlockPos pos3 = ((TileEntityEnderChest)tileEntity).getPos();
                final AxisAlignedBB bb3 = BlockUtils.getBoundingBox(pos3);
                this.enderChests.add(bb3);
            }
        }
        GL11.glNewList(this.normalChests, 4864);
        this.renderBoxes(this.basicChests, this.greenBox);
        this.renderBoxes(this.trappedChests, this.orangeBox);
        this.renderBoxes(this.enderChests, this.cyanBox);
        GL11.glEndList();
        this.minecarts.clear();
        for (final Entity entity : world.loadedEntityList) {
            if (entity instanceof EntityMinecartChest) {
                this.minecarts.add(entity);
            }
        }
    }
    
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glEnable(2884);
        GL11.glDisable(2929);
        GL11.glPushMatrix();
        GL11.glTranslated(-TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY, -TileEntityRendererDispatcher.staticPlayerZ);
        final double partialTicks = event.getPartialTicks();
        final ArrayList<AxisAlignedBB> minecartBoxes = new ArrayList<AxisAlignedBB>(this.minecarts.size());

        final ArrayList<AxisAlignedBB> list = new ArrayList<>();
        double n = partialTicks;

        this.minecarts.forEach(e -> {
            double offsetX = -(e.posX - e.lastTickPosX) + (e.posX - e.lastTickPosX) * n;
            double offsetY = -(e.posY - e.lastTickPosY) + (e.posY - e.lastTickPosY) * n;
            double offsetZ = -(e.posZ - e.lastTickPosZ) + (e.posZ - e.lastTickPosZ) * n;
            list.add(e.getRenderBoundingBox().offset(offsetX, offsetY, offsetZ));
            return;
        });
        if (this.style.getSelected().boxes) {
            GL11.glCallList(this.normalChests);
            this.renderBoxes(minecartBoxes, this.greenBox);
        }
        if (this.style.getSelected().lines) {
            final Vec3d start = RotationUtils.getClientLookVec().addVector(0.0, (double)WMinecraft.getPlayer().getEyeHeight(), 0.0).addVector(TileEntityRendererDispatcher.staticPlayerX, TileEntityRendererDispatcher.staticPlayerY, TileEntityRendererDispatcher.staticPlayerZ);
            GL11.glBegin(1);
            GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.5f);
            this.renderLines(start, this.basicChests);
            this.renderLines(start, minecartBoxes);
            GL11.glColor4f(1.0f, 0.5f, 0.0f, 0.5f);
            this.renderLines(start, this.trappedChests);
            GL11.glColor4f(0.0f, 1.0f, 1.0f, 0.5f);
            this.renderLines(start, this.enderChests);
            GL11.glEnd();
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    private void renderBoxes(final ArrayList<AxisAlignedBB> boxes, final int displayList) {
        for (final AxisAlignedBB bb : boxes) {
            GL11.glPushMatrix();
            GL11.glTranslated(bb.minX, bb.minY, bb.minZ);
            GL11.glScaled(bb.maxX - bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ);
            GL11.glCallList(displayList);
            GL11.glPopMatrix();
        }
    }
    
    private void renderLines(final Vec3d start, final ArrayList<AxisAlignedBB> boxes) {
        for (final AxisAlignedBB bb : boxes) {
            final Vec3d end = bb.getCenter();
            GL11.glVertex3d(WVec3d.getX(start), WVec3d.getY(start), WVec3d.getZ(start));
            GL11.glVertex3d(WVec3d.getX(end), WVec3d.getY(end), WVec3d.getZ(end));
        }
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
