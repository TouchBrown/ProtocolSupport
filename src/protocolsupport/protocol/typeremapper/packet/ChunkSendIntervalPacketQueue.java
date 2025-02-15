package protocolsupport.protocol.typeremapper.packet;

import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;

import protocolsupport.protocol.PacketDataCodecImpl.ClientBoundPacketDataProcessor;
import protocolsupport.protocol.packet.ClientBoundPacketType;
import protocolsupport.protocol.packet.PacketData;
import protocolsupport.protocol.packet.middleimpl.ClientBoundPacketData;
import protocolsupport.utils.JavaSystemProperty;

public class ChunkSendIntervalPacketQueue extends ClientBoundPacketDataProcessor {

	protected static final long chunkSendInterval = TimeUnit.MILLISECONDS.toNanos(JavaSystemProperty.getValue("chunksend18interval", 5L, Long::parseLong));

	public static boolean isEnabled() {
		return chunkSendInterval > 0;
	}

	protected static boolean shouldLock(ClientBoundPacketData packet) {
		return packet.getPacketType() == ClientBoundPacketType.PLAY_CHUNK_SINGLE;
	}

	protected boolean locked = false;
	protected final ArrayDeque<ClientBoundPacketData> queue = new ArrayDeque<>(1024);

	protected void clearQueue() {
		queue.forEach(PacketData::release);
		queue.clear();
	}

	@Override
	public void process(ClientBoundPacketData packet) {
		if (locked) {
			switch (packet.getPacketType()) {
				case PLAY_RESPAWN: {
					clearQueue();
					write(packet);
					break;
				}
				case PLAY_ENTITY_PASSENGERS: {
					queue.add(packet.clone());
					write(packet);
					break;
				}
				case PLAY_CHUNK_SINGLE:
				case PLAY_CHUNK_UNLOAD:
				case PLAY_BLOCK_CHANGE_SINGLE:
				case PLAY_BLOCK_CHANGE_MULTI:
				case PLAY_BLOCK_ACTION:
				case PLAY_BLOCK_BREAK_ANIMATION:
				case PLAY_BLOCK_TILE:
				case CLIENTBOUND_LEGACY_PLAY_UPDATE_SIGN:
				case CLIENTBOUND_LEGACY_PLAY_USE_BED: {
					queue.add(packet);
					break;
				}
				default: {
					write(packet);
					break;
				}
			}
		} else {
			write(packet);
			if (shouldLock(packet)) {
				lock();
			}
		}
	}

	protected void lock() {
		locked = true;
		getIOExecutor().schedule(
			() -> {
				locked = false;
				if (!queue.isEmpty()) {
					ClientBoundPacketData qPacket = null;
					while ((qPacket = queue.pollFirst()) != null) {
						write(qPacket);
						if (shouldLock(qPacket)) {
							lock();
						}
					}
				}
			},
			chunkSendInterval, TimeUnit.NANOSECONDS
		);
	}

	@Override
	public void release() {
		clearQueue();
	}

}
