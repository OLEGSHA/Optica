package ru.windcorp.progressia.common.world;

import static ru.windcorp.progressia.common.world.ChunkData.BLOCKS_PER_CHUNK;

import glm.vec._3.i.Vec3i;

/**
 * Contains static methods to convert world block coordinates, chunk block
 * coordinates and chunk coordinates.
 * <p>
 * Three types of coordinates are used in Progressia:
 * <ul>
 * 
 * <li><em>World coordinates</em>, in code referred to as {@code blockInWorld} -
 * coordinates relative to world origin. Every block in the world has unique
 * world coordinates.</li>
 * 
 * <li><em>Chunk coordinates</em>, in code referred to as {@code blockInChunk} -
 * coordinates relative some chunk's origin. Every block in the chunk has unique
 * chunk coordinates, but blocks in different chunks may have identical chunk
 * coordinates. These coordinates are only useful in combination with a chunk
 * reference. Chunk coordinates are always <tt>[0; {@link #BLOCKS_PER_CHUNK})
 * </tt>.</li>
 * 
 * <li><em>Coordinates of chunk</em>, in code referred to as {@code chunk} -
 * chunk coordinates relative to world origin. Every chunk in the world has
 * unique coordinates of chunk.</li>
 * 
 * </ul> 
 */
public class Coordinates {
	
	public static final int BITS_IN_CHUNK_COORDS = 4;
	public static final int CHUNK_SIZE = 1 << BITS_IN_CHUNK_COORDS;
	public static final int CHUNK_COORDS_MASK = CHUNK_SIZE - 1;
	
	/**
	 * Computes the coordinate of the chunk that the block specified by the
	 * provided world coordinate belongs to.
	 * 
	 * @param blockInWorld world coordinate of the block
	 * @return the corresponding coordinate of the chunk containing the block
	 * 
	 * @see #convertInWorldToChunk(Vec3i, Vec3i)
	 */
	public static int convertInWorldToChunk(int blockInWorld) {
		return blockInWorld >> BITS_IN_CHUNK_COORDS;
	}
	
	/**
	 * Computes the coordinates of the chunk that the block specified by the
	 * provided world coordinates belongs to.
	 * 
	 * @param blockInWorld world coordinates of the block
	 * @param output a {@link Vec3i} to store the result in
	 * @return {@code output}
	 * 
	 * @see #convertInWorldToChunk(int)
	 */
	public static Vec3i convertInWorldToChunk(
			Vec3i blockInWorld,
			Vec3i output
	) {
		output.x = convertInWorldToChunk(blockInWorld.x);
		output.y = convertInWorldToChunk(blockInWorld.y);
		output.z = convertInWorldToChunk(blockInWorld.z);
		
		return output;
	}
	
	/**
	 * Computes the chunk coordinate that the block specified by the
	 * provided world coordinate has in its chunk.
	 * 
	 * @param blockInWorld world coordinate of the block
	 * @return the corresponding chunk coordinate of the block
	 * 
	 * @see #convertInWorldToInChunk(Vec3i, Vec3i)
	 */
	public static int convertInWorldToInChunk(int blockInWorld) {
		return blockInWorld & CHUNK_COORDS_MASK;
	}
	
	/**
	 * Computes the chunk coordinates that the block specified by the
	 * provided world coordinates has in its chunk.
	 * 
	 * @param blockInWorld world coordinates of the block
	 * @param output a {@link Vec3i} to store the result in
	 * @return {@code output}
	 * 
	 * @see #convertInWorldToInChunk(int)
	 */
	public static Vec3i convertInWorldToInChunk(
			Vec3i blockInWorld,
			Vec3i output
	) {
		output.x = convertInWorldToInChunk(blockInWorld.x);
		output.y = convertInWorldToInChunk(blockInWorld.y);
		output.z = convertInWorldToInChunk(blockInWorld.z);
		
		return output;
	}
	
	/**
	 * Computes the world coordinate of the block specified by its chunk
	 * coordinate and the coordinate of its chunk.
	 * 
	 * @param chunk coordinate of chunk
	 * @param blockInChunk chunk coordinate of block
	 * @return corresponding world coordinate
	 * 
	 * @see #getInWorld(int, int)
	 */
	public static int getInWorld(int chunk, int blockInChunk) {
		return blockInChunk | (chunk << BITS_IN_CHUNK_COORDS);
	}
	
	/**
	 * Computes the world coordinates of the block specified by its chunk
	 * coordinates and the coordinates of its chunk.
	 * 
	 * @param chunk coordinate of chunk
	 * @param blockInChunk chunk coordinate of block
	 * @param output a {@link Vec3i} to store the result in
	 * @return {@code output}
	 * 
	 * @see #getInWorld(int)
	 */
	public static Vec3i getInWorld(
			Vec3i chunk, Vec3i blockInChunk,
			Vec3i output
	) {
		output.x = getInWorld(chunk.x, blockInChunk.x);
		output.y = getInWorld(chunk.y, blockInChunk.y);
		output.z = getInWorld(chunk.z, blockInChunk.z);
		
		return output;
	}

}
