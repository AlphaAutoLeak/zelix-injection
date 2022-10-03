package zelix.hack.hacks;

import net.minecraft.entity.*;
import java.util.*;
import zelix.value.*;
import net.minecraft.client.*;
import zelix.hack.*;

public class GaGaNiuShaLu extends Hack
{
    public static EntityLivingBase target;
    public List<EntityLivingBase> targets;
    public NumberValue MaxTurnSpeed;
    public NumberValue MinTurnSpeed;
    public NumberValue MaxPredictSize;
    public NumberValue MinPredictSize;
    public NumberValue MaxCPS;
    public NumberValue MinCPS;
    public NumberValue Fov;
    public NumberValue Distance;
    public NumberValue throughWallDistance;
    public NumberValue hurtTime;
    public NumberValue cooldown;
    public BooleanValue throughWall;
    public BooleanValue autoDelay;
    public BooleanValue silentRotation;
    public BooleanValue AutoBlock;
    public BooleanValue Predict;
    public BooleanValue NoSwing;
    public ModeValue PriorityMode;
    public ModeValue TargetMode;
    public Minecraft mc;
    
    public GaGaNiuShaLu() {
        super("GaGaNiuShaLu", HackCategory.COMBAT);
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void onEnable() {
    }
    
    @Override
    public void onDisable() {
    }
}
