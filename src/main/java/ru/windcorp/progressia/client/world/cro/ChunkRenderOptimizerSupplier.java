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
 
package ru.windcorp.progressia.client.world.cro;

import com.google.common.base.Supplier;

import ru.windcorp.progressia.common.util.namespaces.Namespaced;

public abstract class ChunkRenderOptimizerSupplier extends Namespaced {

	public ChunkRenderOptimizerSupplier(String id) {
		super(id);
	}

	public abstract ChunkRenderOptimizer createOptimizer();

	public static ChunkRenderOptimizerSupplier of(
		String id,
		Supplier<ChunkRenderOptimizer> supplier
	) {
		return new ChunkRenderOptimizerSupplier(id) {
			@Override
			public ChunkRenderOptimizer createOptimizer() {
				return supplier.get();
			}
		};
	}

}
