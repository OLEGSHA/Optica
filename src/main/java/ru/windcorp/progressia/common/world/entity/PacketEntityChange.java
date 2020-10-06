package ru.windcorp.progressia.common.world.entity;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import ru.windcorp.progressia.common.comms.packets.PacketWorldChange;
import ru.windcorp.progressia.common.state.IOContext;
import ru.windcorp.progressia.common.util.DataBuffer;
import ru.windcorp.progressia.common.world.WorldData;

public class PacketEntityChange extends PacketWorldChange {
	
	private long entityId;
	private final DataBuffer buffer = new DataBuffer();

	public PacketEntityChange() {
		super("Core", "EntityChange");
	}
	
	public long getEntityId() {
		return entityId;
	}
	
	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}
	
	public DataBuffer getBuffer() {
		return buffer;
	}

	public DataInput getReader() {
		return buffer.getReader();
	}

	public DataOutput getWriter() {
		return buffer.getWriter();
	}

	@Override
	public void apply(WorldData world) {
		EntityData entity = world.getEntity(getEntityId());
		
		if (entity == null) {
			throw new RuntimeException(
					"Entity with ID " + getEntityId() + " not found"
			);
		}
		
		try {
			entity.read(getReader(), IOContext.COMMS);
		} catch (IOException e) {
			throw new RuntimeException(
					"Entity could not be read", e
			);
		}
	}

}