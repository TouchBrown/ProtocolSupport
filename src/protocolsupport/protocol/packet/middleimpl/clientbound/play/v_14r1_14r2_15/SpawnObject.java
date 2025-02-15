package protocolsupport.protocol.packet.middleimpl.clientbound.play.v_14r1_14r2_15;

import protocolsupport.protocol.codec.UUIDCodec;
import protocolsupport.protocol.codec.VarNumberCodec;
import protocolsupport.protocol.packet.ClientBoundPacketType;
import protocolsupport.protocol.packet.middleimpl.ClientBoundPacketData;
import protocolsupport.protocol.packet.middleimpl.clientbound.play.v_4_5_6_7_8_9r1_9r2_10_11_12r1_12r2_13_14r1_14r2_15.AbstractThunderboltSpawnObject;
import protocolsupport.protocol.typeremapper.entity.FlatteningNetworkEntityIdRegistry;
import protocolsupport.protocol.typeremapper.utils.MappingTable.ArrayBasedIntMappingTable;

public class SpawnObject extends AbstractThunderboltSpawnObject {

	protected final ArrayBasedIntMappingTable flatteningEntityIdTable = FlatteningNetworkEntityIdRegistry.INSTANCE.getTable(version);

	public SpawnObject(MiddlePacketInit init) {
		super(init);
	}

	@Override
	protected void writeSpawnObject() {
		ClientBoundPacketData serializer = ClientBoundPacketData.create(ClientBoundPacketType.PLAY_SPAWN_OBJECT);
		VarNumberCodec.writeVarInt(serializer, entity.getId());
		UUIDCodec.writeUUID2L(serializer, entity.getUUID());
		serializer.writeByte(flatteningEntityIdTable.get(fType.getNetworkTypeId()));
		serializer.writeDouble(x);
		serializer.writeDouble(y);
		serializer.writeDouble(z);
		serializer.writeByte(pitch);
		serializer.writeByte(yaw);
		serializer.writeInt(fObjectdata);
		serializer.writeShort(velX);
		serializer.writeShort(velY);
		serializer.writeShort(velZ);
		codec.writeClientbound(serializer);
	}

	@Override
	protected void writeSpawnThunderbolt() {
		ClientBoundPacketData spawnglobal = ClientBoundPacketData.create(ClientBoundPacketType.CLIENTBOUND_LEGACY_PLAY_SPAWN_GLOBAL);
		VarNumberCodec.writeVarInt(spawnglobal, entity.getId());
		spawnglobal.writeByte(1);
		spawnglobal.writeDouble(x);
		spawnglobal.writeDouble(y);
		spawnglobal.writeDouble(z);
		codec.writeClientbound(spawnglobal);
	}

}
