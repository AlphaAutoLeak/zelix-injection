package zelix.hack.hacks;

import zelix.hack.*;
import zelix.value.*;
import zelix.utils.system.*;
import net.minecraft.network.play.client.*;

public class PacketFilter extends Hack
{
    public ModeValue mode;
    public BooleanValue cCPacketPlayer;
    public BooleanValue cCPacketCloseWindow;
    public BooleanValue cCPacketRotation;
    public BooleanValue cCPacketPosition;
    public BooleanValue cCPacketPositionRotation;
    public BooleanValue cCPacketClientStatus;
    public BooleanValue cCPacketInput;
    public BooleanValue cCPacketPlayerAbilities;
    public BooleanValue cCPacketPlayerDigging;
    public BooleanValue cCPacketUseEntity;
    public BooleanValue cCPacketVehicleMove;
    public BooleanValue cCPacketEntityAction;
    public BooleanValue cCPacketClickWindow;
    
    public PacketFilter() {
        super("PacketFilter", HackCategory.ANOTHER);
        this.mode = new ModeValue("Mode", new Mode[] { new Mode("Output", true), new Mode("Input", false), new Mode("AllSides", false) });
        this.cCPacketPlayer = new BooleanValue("Player", Boolean.valueOf(false));
        this.cCPacketEntityAction = new BooleanValue("EntityAction", Boolean.valueOf(false));
        this.cCPacketCloseWindow = new BooleanValue("CloseWindow", Boolean.valueOf(false));
        this.cCPacketRotation = new BooleanValue("Rotation", Boolean.valueOf(false));
        this.cCPacketPosition = new BooleanValue("Position", Boolean.valueOf(false));
        this.cCPacketPositionRotation = new BooleanValue("PositionRotation", Boolean.valueOf(false));
        this.cCPacketClientStatus = new BooleanValue("ClientStatus", Boolean.valueOf(false));
        this.cCPacketInput = new BooleanValue("Input", Boolean.valueOf(false));
        this.cCPacketPlayerAbilities = new BooleanValue("PlayerAbilities", Boolean.valueOf(false));
        this.cCPacketPlayerDigging = new BooleanValue("PlayerDigging", Boolean.valueOf(false));
        this.cCPacketUseEntity = new BooleanValue("UseEntity", Boolean.valueOf(false));
        this.cCPacketVehicleMove = new BooleanValue("VehicleMove", Boolean.valueOf(false));
        this.cCPacketEntityAction = new BooleanValue("EntityAction", Boolean.valueOf(false));
        this.cCPacketClickWindow = new BooleanValue("ClickWindow", Boolean.valueOf(false));
        this.addValue(this.mode, this.cCPacketPlayer, this.cCPacketEntityAction, this.cCPacketCloseWindow, this.cCPacketRotation, this.cCPacketPosition, this.cCPacketPositionRotation, this.cCPacketClientStatus, this.cCPacketInput, this.cCPacketPlayerAbilities, this.cCPacketPlayerDigging, this.cCPacketUseEntity, this.cCPacketVehicleMove, this.cCPacketEntityAction, this.cCPacketClickWindow);
    }
    
    @Override
    public String getDescription() {
        return "Packet filter.";
    }
    
    @Override
    public boolean onPacket(final Object packet, final Connection.Side side) {
        return ((!this.mode.getMode("Output").isToggled() || side != Connection.Side.OUT) && (!this.mode.getMode("Input").isToggled() || side != Connection.Side.IN) && !this.mode.getMode("AllSides").isToggled()) || this.checkPacket(packet);
    }
    
    public boolean checkPacket(final Object packet) {
        return (!this.cCPacketPlayer.getValue() || !(packet instanceof CPacketPlayer)) && (!this.cCPacketEntityAction.getValue() || !(packet instanceof CPacketEntityAction)) && (!this.cCPacketCloseWindow.getValue() || !(packet instanceof CPacketCloseWindow)) && (!this.cCPacketRotation.getValue() || !(packet instanceof CPacketPlayer.Rotation)) && (!this.cCPacketPosition.getValue() || !(packet instanceof CPacketPlayer.Position)) && (!this.cCPacketPositionRotation.getValue() || !(packet instanceof CPacketPlayer.PositionRotation)) && (!this.cCPacketClientStatus.getValue() || !(packet instanceof CPacketClientStatus)) && (!this.cCPacketInput.getValue() || !(packet instanceof CPacketInput)) && (!this.cCPacketPlayerAbilities.getValue() || !(packet instanceof CPacketPlayerAbilities)) && (!this.cCPacketPlayerDigging.getValue() || !(packet instanceof CPacketPlayerDigging)) && (!this.cCPacketUseEntity.getValue() || !(packet instanceof CPacketUseEntity)) && (!this.cCPacketVehicleMove.getValue() || !(packet instanceof CPacketVehicleMove)) && (!this.cCPacketEntityAction.getValue() || !(packet instanceof CPacketEntityAction)) && (!this.cCPacketClickWindow.getValue() || !(packet instanceof CPacketClickWindow));
    }
}
