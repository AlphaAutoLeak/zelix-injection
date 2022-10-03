package zelix.otherhacks.net.wurstclient.forge.utils;

import org.lwjgl.opengl.*;
import net.minecraft.util.math.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;

public final class RenderUtils
{
    public static void drawSolidBox(final AxisAlignedBB bb) {
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
    }
    
    public static void drawOutlinedBox(final AxisAlignedBB bb) {
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
    }
    
    public static void drawNode(final AxisAlignedBB bb) {
        final double midX = (bb.minX + bb.maxX) / 2.0;
        final double midY = (bb.minY + bb.maxY) / 2.0;
        final double midZ = (bb.minZ + bb.maxZ) / 2.0;
        GL11.glVertex3d(midX, midY, bb.maxZ);
        GL11.glVertex3d(bb.minX, midY, midZ);
        GL11.glVertex3d(bb.minX, midY, midZ);
        GL11.glVertex3d(midX, midY, bb.minZ);
        GL11.glVertex3d(midX, midY, bb.minZ);
        GL11.glVertex3d(bb.maxX, midY, midZ);
        GL11.glVertex3d(bb.maxX, midY, midZ);
        GL11.glVertex3d(midX, midY, bb.maxZ);
        GL11.glVertex3d(midX, bb.maxY, midZ);
        GL11.glVertex3d(bb.maxX, midY, midZ);
        GL11.glVertex3d(midX, bb.maxY, midZ);
        GL11.glVertex3d(bb.minX, midY, midZ);
        GL11.glVertex3d(midX, bb.maxY, midZ);
        GL11.glVertex3d(midX, midY, bb.minZ);
        GL11.glVertex3d(midX, bb.maxY, midZ);
        GL11.glVertex3d(midX, midY, bb.maxZ);
        GL11.glVertex3d(midX, bb.minY, midZ);
        GL11.glVertex3d(bb.maxX, midY, midZ);
        GL11.glVertex3d(midX, bb.minY, midZ);
        GL11.glVertex3d(bb.minX, midY, midZ);
        GL11.glVertex3d(midX, bb.minY, midZ);
        GL11.glVertex3d(midX, midY, bb.minZ);
        GL11.glVertex3d(midX, bb.minY, midZ);
        GL11.glVertex3d(midX, midY, bb.maxZ);
    }
    
    public static void drawArrow(final Vec3d from, final Vec3d to) {
        final double startX = WVec3d.getX(from);
        final double startY = WVec3d.getY(from);
        final double startZ = WVec3d.getZ(from);
        final double endX = WVec3d.getX(to);
        final double endY = WVec3d.getY(to);
        final double endZ = WVec3d.getZ(to);
        GL11.glPushMatrix();
        GL11.glBegin(1);
        GL11.glVertex3d(startX, startY, startZ);
        GL11.glVertex3d(endX, endY, endZ);
        GL11.glEnd();
        GL11.glTranslated(endX, endY, endZ);
        GL11.glScaled(0.1, 0.1, 0.1);
        final double angleX = Math.atan2(endY - startY, startZ - endZ);
        GL11.glRotated(Math.toDegrees(angleX) + 90.0, 1.0, 0.0, 0.0);
        final double angleZ = Math.atan2(endX - startX, Math.sqrt(Math.pow(endY - startY, 2.0) + Math.pow(endZ - startZ, 2.0)));
        GL11.glRotated(Math.toDegrees(angleZ), 0.0, 0.0, 1.0);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0, 2.0, 1.0);
        GL11.glVertex3d(-1.0, 2.0, 0.0);
        GL11.glVertex3d(-1.0, 2.0, 0.0);
        GL11.glVertex3d(0.0, 2.0, -1.0);
        GL11.glVertex3d(0.0, 2.0, -1.0);
        GL11.glVertex3d(1.0, 2.0, 0.0);
        GL11.glVertex3d(1.0, 2.0, 0.0);
        GL11.glVertex3d(0.0, 2.0, 1.0);
        GL11.glVertex3d(1.0, 2.0, 0.0);
        GL11.glVertex3d(-1.0, 2.0, 0.0);
        GL11.glVertex3d(0.0, 2.0, 1.0);
        GL11.glVertex3d(0.0, 2.0, -1.0);
        GL11.glVertex3d(0.0, 0.0, 0.0);
        GL11.glVertex3d(1.0, 2.0, 0.0);
        GL11.glVertex3d(0.0, 0.0, 0.0);
        GL11.glVertex3d(-1.0, 2.0, 0.0);
        GL11.glVertex3d(0.0, 0.0, 0.0);
        GL11.glVertex3d(0.0, 2.0, -1.0);
        GL11.glVertex3d(0.0, 0.0, 0.0);
        GL11.glVertex3d(0.0, 2.0, 1.0);
        GL11.glEnd();
        GL11.glPopMatrix();
    }
}
