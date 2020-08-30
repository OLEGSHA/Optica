/*******************************************************************************
 * Progressia
 * Copyright (C) 2020  Wind Corporation
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
 *******************************************************************************/
package ru.windcorp.progressia.common.block;

import java.util.HashMap;
import java.util.Map;

public class TileDataRegistry {
	
	private static final Map<String, TileData> REGISTRY = new HashMap<>();
	
	static {
		register(new TileData("Test", "Grass"));
		register(new TileData("Test", "Stones"));
		register(new TileData("Test", "YellowFlowers"));
		register(new TileData("Test", "Sand"));
	}
	
	public static TileData get(String name) {
		return REGISTRY.get(name);
	}
	
	public static void register(TileData tileData) {
		REGISTRY.put(tileData.getId(), tileData);
	}

}