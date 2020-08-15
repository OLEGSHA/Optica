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
package ru.windcorp.progressia.client.graphics.texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.Arrays;

import ru.windcorp.progressia.client.graphics.backend.OpenGLObjectTracker;
import ru.windcorp.progressia.client.graphics.backend.OpenGLObjectTracker.OpenGLDeletable;

public class TexturePrimitive implements OpenGLDeletable {
	
	private static final int NOT_LOADED = -1;
	
	private static int[] currentlyBound = new int[32];
	static {
		Arrays.fill(currentlyBound, NOT_LOADED);
	}
	
	private int handle = NOT_LOADED;
	private TextureData pixels;

	public TexturePrimitive(TextureData pixels) {
		this.pixels = pixels;
	}
	
	public TextureData getData() {
		return pixels;
	}
	
	public int getBufferWidth() {
		return pixels.getBufferWidth();
	}

	public int getBufferHeight() {
		return pixels.getBufferHeight();
	}

	public int getWidth() {
		return pixels.getContentWidth();
	}

	public int getHeight() {
		return pixels.getContentHeight();
	}

	public boolean isLoaded() {
		return handle != NOT_LOADED;
	}
	
	public void bind(int slot) {
		if (!isLoaded()) {
			load();
		}
		
		if (currentlyBound[slot] == handle) {
			return;
		}
		
		int code = GL_TEXTURE0 + slot;
		
		glActiveTexture(code);
		glBindTexture(GL_TEXTURE_2D, handle);
		
		currentlyBound[slot] = handle;
	}

	protected void load() {
		if (isLoaded()) return;
		
		handle = pixels.load();
		OpenGLObjectTracker.register(this);
		
		if (handle < 0) {
			throw new RuntimeException("oops");
		}
	}

	@Override
	public void delete() {
		if (isLoaded())
			glDeleteTextures(handle);
	}

}