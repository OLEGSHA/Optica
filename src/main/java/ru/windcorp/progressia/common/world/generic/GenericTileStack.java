/*
 * Progressia
 * Copyright (C)  2020-2021  Wind Corporation and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
 
package ru.windcorp.progressia.common.world.generic;

import java.util.AbstractList;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Consumer;

import glm.vec._3.i.Vec3i;
import ru.windcorp.progressia.common.world.Coordinates;
import ru.windcorp.progressia.common.world.rels.RelFace;

// @formatter:off
public abstract class GenericTileStack<
	B  extends GenericBlock,
	T  extends GenericTile,
	TS extends GenericTileStack     <B, T, TS, TR, C>,
	TR extends GenericTileReference <B, T, TS, TR, C>,
	C  extends GenericChunk         <B, T, TS, TR, C>
> extends AbstractList<T> implements RandomAccess {
// @formatter:on

	public static interface TSConsumer<T> {
		void accept(int layer, T tile);
	}

	public static final int TILES_PER_FACE = 8;

	public abstract Vec3i getBlockInChunk(Vec3i output);

	public abstract C getChunk();

	public abstract RelFace getFace();

	public abstract TR getReference(int index);

	public abstract int getIndexByTag(int tag);

	public abstract int getTagByIndex(int index);

	public Vec3i getBlockInWorld(Vec3i output) {
		// This is safe
		return Coordinates.getInWorld(getChunk().getPosition(), getBlockInChunk(output), output);
	}

	public boolean isFull() {
		return size() >= TILES_PER_FACE;
	}

	public T getClosest() {
		return get(0);
	}

	public T getFarthest() {
		return get(size() - 1);
	}

	public void forEach(TSConsumer<T> action) {
		Objects.requireNonNull(action, "action");
		for (int i = 0; i < size(); ++i) {
			action.accept(i, get(i));
		}
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		Objects.requireNonNull(action, "action");
		for (int i = 0; i < size(); ++i) {
			action.accept(get(i));
		}
	}

	public T findClosest(String id) {
		Objects.requireNonNull(id, "id");

		for (int i = 0; i < size(); ++i) {
			T tile = get(i);
			if (tile.getId().equals(id)) {
				return tile;
			}
		}

		return null;
	}

	public T findFarthest(String id) {
		Objects.requireNonNull(id, "id");

		for (int i = 0; i < size(); ++i) {
			T tile = get(i);
			if (tile.getId().equals(id)) {
				return tile;
			}
		}

		return null;
	}

	public boolean contains(String id) {
		return findClosest(id) != null;
	}

	public B getHost() {
		return getChunk().getBlock(getBlockInChunk(null));
	}

}
