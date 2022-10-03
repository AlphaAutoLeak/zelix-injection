package zelix.utils;

import java.util.*;
import net.minecraft.entity.player.*;

public class EntityBot
{
    private String name;
    private int id;
    private UUID uuid;
    private boolean invisible;
    private boolean ground;
    
    public EntityBot(final EntityPlayer player) {
        this.name = String.valueOf(player.getGameProfile().getName());
        this.id = player.getEntityId();
        this.uuid = player.getGameProfile().getId();
        this.invisible = player.isInvisible();
        this.ground = player.onGround;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public boolean isInvisible() {
        return this.invisible;
    }
    
    public boolean isGround() {
        return this.ground;
    }
}
