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
package ru.windcorp.progressia.client.graphics.gui;

import ru.windcorp.progressia.client.graphics.Colors;
import ru.windcorp.progressia.client.graphics.flat.RenderTarget;

public class Panel extends Group {

	public Panel(String name, Layout layout) {
		super(name, layout);
	}
	
	@Override
	protected void assembleSelf(RenderTarget target) {
		target.fill(getX(), getY(), getWidth(), getHeight(), Colors.LIGHT_GRAY);
		target.fill(getX() + 2, getY() + 2, getWidth() - 4, getHeight() - 4, Colors.WHITE);
	}

}
