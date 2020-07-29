/*******************************************************************************
 * Optica
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
package ru.windcorp.optica.client.graphics.backend;

import glm.vec._2.d.Vec2d;

public class GraphicsInterface {
	
	private GraphicsInterface() {}
	
	public static Thread getRenderThread() {
		return GraphicsBackend.getRenderThread();
	}
	
	public static boolean isRenderThread() {
		return Thread.currentThread() == getRenderThread();
	}
	
	public static int getFramebufferWidth() {
		return GraphicsBackend.getFramebufferWidth();
	}
	
	public static int getFramebufferHeight() {
		return GraphicsBackend.getFramebufferHeight();
	}
	
	public static float getAspectRatio() {
		return ((float) getFramebufferWidth()) / getFramebufferHeight();
	}
	
	public static double getTime() {
		return GraphicsBackend.getFrameStart();
	}
	
	public static double getFrameLength() {
		return GraphicsBackend.getFrameLength();
	}
	
	public static double getFPS() {
		return 1 / GraphicsBackend.getFrameLength();
	}
	
	public static void subscribeToInputEvents(Object listener) {
		InputHandler.register(listener);
	}
	
	public static double getCursorX() {
		return InputHandler.getCursorX();
	}
	
	public static double getCursorY() {
		return InputHandler.getCursorY();
	}
	
	public static Vec2d getCursorPosition() {
		return InputHandler.getCursorPosition();
	}

}