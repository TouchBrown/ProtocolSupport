package protocolsupport.protocol.packet.middle.clientbound.login;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.codec.ArrayCodec;
import protocolsupport.protocol.codec.StringCodec;
import protocolsupport.protocol.packet.middle.ClientBoundMiddlePacket;

public abstract class MiddleEncryptionRequest extends ClientBoundMiddlePacket {

	protected MiddleEncryptionRequest(MiddlePacketInit init) {
		super(init);
	}

	protected String serverId;
	protected ByteBuf publicKey;
	protected ByteBuf verifyToken;

	@Override
	protected void decode(ByteBuf serverdata) {
		serverId = StringCodec.readVarIntUTF8String(serverdata);
		publicKey = ArrayCodec.readVarIntByteArraySlice(serverdata);
		verifyToken = ArrayCodec.readVarIntByteArraySlice(serverdata);
	}

	@Override
	protected void cleanup() {
		serverId = null;
		publicKey = null;
		verifyToken = null;
	}

}
