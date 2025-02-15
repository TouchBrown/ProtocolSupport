package protocolsupport.protocol.packet.middleimpl.clientbound.play.v_10_11_12r1_12r2_13_14r1_14r2_15_16r1_16r2_17r1_17r2;

import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.codec.MiscDataCodec;
import protocolsupport.protocol.codec.StringCodec;
import protocolsupport.protocol.packet.ClientBoundPacketType;
import protocolsupport.protocol.packet.middle.clientbound.play.MiddleWorldCustomSound;
import protocolsupport.protocol.packet.middleimpl.ClientBoundPacketData;
import protocolsupport.protocol.typeremapper.basic.SoundRemapper;
import protocolsupport.protocol.types.SoundCategory;

public class WorldCustomSound extends MiddleWorldCustomSound {

	public WorldCustomSound(MiddlePacketInit init) {
		super(init);
	}

	@Override
	protected void write() {
		codec.writeClientbound(create(version, x, y, z, id, category, volume, pitch));
	}

	public static ClientBoundPacketData create(
		ProtocolVersion version,
		double x, double y, double z,
		String sound, SoundCategory category, float volume, float pitch
	) {
		return create(version, (int) (x * 8), (int) (y * 8), (int) (z * 8), sound, category, volume, pitch);
	}

	public static ClientBoundPacketData create(
		ProtocolVersion version,
		int x, int y, int z,
		String sound, SoundCategory category, float volume, float pitch
	) {
		ClientBoundPacketData worldcustomsound = ClientBoundPacketData.create(ClientBoundPacketType.PLAY_WORLD_CUSTOM_SOUND);
		StringCodec.writeVarIntUTF8String(worldcustomsound, SoundRemapper.getSoundName(version, sound));
		MiscDataCodec.writeVarIntEnum(worldcustomsound, category);
		worldcustomsound.writeInt(x);
		worldcustomsound.writeInt(y);
		worldcustomsound.writeInt(z);
		worldcustomsound.writeFloat(volume);
		worldcustomsound.writeFloat(pitch);
		return worldcustomsound;
	}

}
