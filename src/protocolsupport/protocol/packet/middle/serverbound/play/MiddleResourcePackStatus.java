package protocolsupport.protocol.packet.middle.serverbound.play;

import protocolsupport.protocol.codec.VarNumberCodec;
import protocolsupport.protocol.packet.ServerBoundPacketType;
import protocolsupport.protocol.packet.middle.ServerBoundMiddlePacket;
import protocolsupport.protocol.packet.middleimpl.ServerBoundPacketData;

public abstract class MiddleResourcePackStatus extends ServerBoundMiddlePacket {

	protected MiddleResourcePackStatus(MiddlePacketInit init) {
		super(init);
	}

	protected int result;

	@Override
	protected void write() {
		ServerBoundPacketData resourcepackstatus = ServerBoundPacketData.create(ServerBoundPacketType.PLAY_RESOURCE_PACK_STATUS);
		VarNumberCodec.writeVarInt(resourcepackstatus, result);
		codec.writeServerbound(resourcepackstatus);
	}

}
