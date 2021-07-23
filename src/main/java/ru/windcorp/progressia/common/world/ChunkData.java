package ru.windcorp.progressia.common.world;

import ru.windcorp.progressia.common.world.block.BlockData;
import ru.windcorp.progressia.common.world.generic.ChunkGenericWO;
import ru.windcorp.progressia.common.world.tile.TileData;

public interface ChunkData extends ChunkDataRO, ChunkGenericWO<BlockData, TileData, TileDataStack, TileDataReference, ChunkData> {
	
//	@Override
//	default TileDataStack getTiles(Vec3i blockInChunk, BlockFace face) {
//		return null;
//	}

}
