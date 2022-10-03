package zelix.otherhacks.net.wurstclient.forge.hacks;

import zelix.otherhacks.net.wurstclient.forge.settings.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import zelix.otherhacks.net.wurstclient.forge.*;
import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import java.lang.reflect.*;
import net.minecraft.block.*;
import net.minecraft.init.*;

public final class XRayHack extends Hack
{
    public static final BlockListSetting blocks;
    
    public XRayHack() {
        super("X-Ray", "Allows you to see ores through walls.");
        this.setCategory(Category.RENDER);
        this.addSetting(XRayHack.blocks);
        try {
            BlockRendererDispatcher renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            if (renderer.getClass().getName().equals("codechicken.lib.render.block.CCBlockRendererDispatcher")) {
                final Field parentDispatcher = renderer.getClass().getDeclaredField("parentDispatcher");
                parentDispatcher.setAccessible(true);
                renderer = (BlockRendererDispatcher)parentDispatcher.get(renderer);
            }
            final Field blockModelRenderer = renderer.getClass().getDeclaredField(ForgeWurst.getForgeWurst().isObfuscated() ? "blockModelRenderer" : "blockModelRenderer");
            blockModelRenderer.setAccessible(true);
            blockModelRenderer.set(renderer, new WForgeBlockModelRenderer(XRayHack.mc.getBlockColors()));
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        }
    }
    
    @Override
    public String getRenderName() {
        return "X-Wurst";
    }
    
    @Override
    protected void onEnable() {
        XRayHack.mc.renderGlobal.loadRenderers();
    }
    
    @Override
    protected void onDisable() {
        XRayHack.mc.renderGlobal.loadRenderers();
    }
    
    static {
        blocks = new BlockListSetting("Blocks", new Block[] { Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE });
    }
}
