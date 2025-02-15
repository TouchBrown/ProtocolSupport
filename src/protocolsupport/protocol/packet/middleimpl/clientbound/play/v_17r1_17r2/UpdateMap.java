package protocolsupport.protocol.packet.middleimpl.clientbound.play.v_17r1_17r2;

import protocolsupport.protocol.codec.ArrayCodec;
import protocolsupport.protocol.codec.StringCodec;
import protocolsupport.protocol.codec.VarNumberCodec;
import protocolsupport.protocol.packet.ClientBoundPacketType;
import protocolsupport.protocol.packet.middle.clientbound.play.MiddleUpdateMap;
import protocolsupport.protocol.packet.middleimpl.ClientBoundPacketData;
import protocolsupport.protocol.typeremapper.mapcolor.MapColorMappingRegistry;
import protocolsupport.protocol.typeremapper.utils.MappingTable.ArrayBasedIntMappingTable;

public class UpdateMap extends MiddleUpdateMap {

	public UpdateMap(MiddlePacketInit init) {
		super(init);
	}

	@Override
	public void write() {
		ClientBoundPacketData updatemapPacket = ClientBoundPacketData.create(ClientBoundPacketType.PLAY_UPDATE_MAP);
		VarNumberCodec.writeVarInt(updatemapPacket, id);
		updatemapPacket.writeByte(scale);
		updatemapPacket.writeBoolean(locked);
		updatemapPacket.writeBoolean(showIcons);
		if (showIcons) {
			ArrayCodec.writeVarIntTArray(updatemapPacket, icons, (to, icon) -> {
				VarNumberCodec.writeVarInt(to, icon.type);
				to.writeByte(icon.x);
				to.writeByte(icon.z);
				to.writeByte(icon.direction);
				to.writeBoolean(icon.displayName != null);
				if (icon.displayName != null) {
					StringCodec.writeVarIntUTF8String(to, icon.displayName);
				}
			});
		}
		updatemapPacket.writeByte(columns);
		if (columns > 0) {
			ArrayBasedIntMappingTable colorRemapper = MapColorMappingRegistry.INSTANCE.getTable(version);
			for (int i = 0; i < colors.length; i++) {
				colors[i] = (byte) colorRemapper.get(colors[i] & 0xFF);
			}
			updatemapPacket.writeByte(rows);
			updatemapPacket.writeByte(xstart);
			updatemapPacket.writeByte(zstart);
			ArrayCodec.writeVarIntByteArray(updatemapPacket, colors);
		}
		codec.writeClientbound(updatemapPacket);
	}

}
