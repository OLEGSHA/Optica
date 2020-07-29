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
package ru.windcorp.optica.client.graphics.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.ObjectArrays;

import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import ru.windcorp.optica.client.graphics.backend.VertexBufferObject;
import ru.windcorp.optica.client.graphics.backend.VertexBufferObject.BindTarget;
import ru.windcorp.optica.client.graphics.backend.shaders.CombinedShader;
import ru.windcorp.optica.client.graphics.backend.shaders.Program;
import ru.windcorp.optica.client.graphics.backend.shaders.attributes.AttributeVertexArray;
import ru.windcorp.optica.client.graphics.backend.shaders.uniforms.Uniform1Int;
import ru.windcorp.optica.client.graphics.backend.shaders.uniforms.Uniform2Float;
import ru.windcorp.optica.client.graphics.backend.shaders.uniforms.Uniform4Matrix;
import ru.windcorp.optica.client.graphics.texture.Sprite;
import ru.windcorp.optica.client.graphics.world.WorldRenderer;

public class ShapeRenderProgram extends Program {
	
	private static ShapeRenderProgram def = null;
	
	public static void init() {
		def = new ShapeRenderProgram(
				new String[] {"WorldDefault.vertex.glsl"},
				new String[] {"WorldDefault.fragment.glsl"}
		);
	}
	
	public static ShapeRenderProgram getDefault() {
		return def;
	}
	
	private static final int DEFAULT_BYTES_PER_VERTEX =
			3 * Float.BYTES + // Position
			3 * Float.BYTES + // Color multiplier
			2 * Float.BYTES + // Texture coordinates
			3 * Float.BYTES;  // Normals
	
	private static final String SHAPE_VERTEX_SHADER_RESOURCE =
			"Shape.vertex.glsl";
	private static final String SHAPE_FRAGMENT_SHADER_RESOURCE =
			"Shape.fragment.glsl";
	
	private static final String
			FINAL_TRANSFORM_UNIFORM_NAME   = "finalTransform",
			WORLD_TRANSFORM_UNIFORM_NAME   = "worldTransform",
			POSITIONS_ATTRIBUTE_NAME       = "inputPositions",
			COLOR_MULTIPLER_ATTRIBUTE_NAME = "inputColorMultiplier",
			TEXTURE_COORDS_ATTRIBUTE_NAME  = "inputTextureCoords",
			TEXTURE_SLOT_UNIFORM_NAME      = "textureSlot",
			TEXTURE_START_UNIFORM_NAME     = "textureStart",
			TEXTURE_SIZE_UNIFORM_NAME      = "textureSize",
			NORMALS_ATTRIBUTE_NAME         = "inputNormals";
	
	private final Uniform4Matrix finalTransformUniform;
	private final Uniform4Matrix worldTransformUniform;
	private final AttributeVertexArray positionsAttribute;
	private final AttributeVertexArray colorsAttribute;
	private final AttributeVertexArray textureCoordsAttribute;
	private final Uniform1Int textureSlotUniform;
	private final Uniform2Float textureStartUniform;
	private final Uniform2Float textureSizeUniform;
	private final AttributeVertexArray normalsAttribute;

	public ShapeRenderProgram(
			String[] vertexShaderResources,
			String[] fragmentShaderResources
	) {
		super(
				new CombinedShader(
						attachVertexShader(vertexShaderResources)
				),
				new CombinedShader(
						attachFragmentShader(fragmentShaderResources)
				)
		);
		
		this.finalTransformUniform = getUniform(FINAL_TRANSFORM_UNIFORM_NAME)
				.as4Matrix();
		
		this.worldTransformUniform = getUniform(WORLD_TRANSFORM_UNIFORM_NAME)
				.as4Matrix();
		
		this.positionsAttribute =
				getAttribute(POSITIONS_ATTRIBUTE_NAME).asVertexArray();
		
		this.colorsAttribute =
				getAttribute(COLOR_MULTIPLER_ATTRIBUTE_NAME).asVertexArray();
		
		this.textureCoordsAttribute =
				getAttribute(TEXTURE_COORDS_ATTRIBUTE_NAME).asVertexArray();
		
		this.textureSlotUniform = getUniform(TEXTURE_SLOT_UNIFORM_NAME)
				.as1Int();
		
		this.textureStartUniform = getUniform(TEXTURE_START_UNIFORM_NAME)
				.as2Float();
		
		this.textureSizeUniform = getUniform(TEXTURE_SIZE_UNIFORM_NAME)
				.as2Float();
		
		this.normalsAttribute = getAttribute(NORMALS_ATTRIBUTE_NAME)
				.asVertexArray();
	}

	private static String[] attachVertexShader(String[] others) {
		return ObjectArrays.concat(SHAPE_VERTEX_SHADER_RESOURCE, others);
	}
	
	private static String[] attachFragmentShader(String[] others) {
		return ObjectArrays.concat(SHAPE_FRAGMENT_SHADER_RESOURCE, others);
	}
	
	public void render(
			WorldRenderer renderer,
			Shape shape
	) {
		use();
		configure(renderer);
		
		bindVertices(shape.getVerticesVbo());
		bindIndices(shape.getIndicesVbo());
		
		try {
			positionsAttribute.enable();
			colorsAttribute.enable();
			textureCoordsAttribute.enable();
			normalsAttribute.enable();
			
			for (Face face : shape.getFaces()) {
				renderFace(face);
			}
			
		} finally {
			positionsAttribute.disable();
			colorsAttribute.disable();
			textureCoordsAttribute.disable();
			normalsAttribute.disable();
		}
	}
	
	protected void configure(WorldRenderer renderer) {
		finalTransformUniform.set(renderer.getFinalTransform());
		worldTransformUniform.set(renderer.getWorldTransform());
	}

	protected int bindVertices(VertexBufferObject vertices) {
		int vertexStride = getBytesPerVertex();
		int offset = 0;
		
		positionsAttribute.set(
				3, GL11.GL_FLOAT, false, vertexStride, vertices,
				offset
		);
		offset += 3 * Float.BYTES;
		
		colorsAttribute.set(
				3, GL11.GL_FLOAT, false, vertexStride, vertices,
				offset
		);
		offset += 3 * Float.BYTES;
		
		textureCoordsAttribute.set(
				2, GL11.GL_FLOAT, false, vertexStride, vertices,
				offset
		);
		offset += 2 * Float.BYTES;
		
		normalsAttribute.set(
				3, GL11.GL_FLOAT, false, vertexStride, vertices,
				offset
		);
		offset += 3 * Float.BYTES;
		
		return offset;
	}
	
	protected void bindIndices(VertexBufferObject indices) {
		indices.bind(BindTarget.ELEMENT_ARRAY);
	}

	protected void renderFace(Face face) {
		Sprite sprite = face.getTexture().getSprite();
		
		sprite.getPrimitive().bind(0);
		textureSlotUniform.set(0);
		
		textureStartUniform.set(sprite.getStart());
		textureSizeUniform.set(sprite.getSize());
		
		GL11.glDrawElements(
				GL11.GL_TRIANGLES,
				face.getIndexCount(),
				GL11.GL_UNSIGNED_SHORT,
				face.getByteOffsetOfIndices()
		);
	}
	
	public int getBytesPerVertex() {
		return DEFAULT_BYTES_PER_VERTEX;
	}
	
	public static class VertexBuilder {
		
		private static class Vertex {
			final Vec3 position;
			final Vec3 colorMultiplier;
			final Vec2 textureCoords;
			
			Vertex(Vec3 position, Vec3 colorMultiplier, Vec2 textureCoords) {
				this.position = position;
				this.colorMultiplier = colorMultiplier;
				this.textureCoords = textureCoords;
			}
		}
		
		private final List<Vertex> vertices = new ArrayList<>();
		
		public VertexBuilder addVertex(
				float x, float y, float z,
				float r, float g, float b,
				float tx, float ty
		) {
			vertices.add(new Vertex(
					new Vec3(x, y, z),
					new Vec3(r, g, b),
					new Vec2(tx, ty)
			));
			
			return this;
		}
		
		public VertexBuilder addVertex(
				Vec3 position,
				Vec3 colorMultiplier,
				Vec2 textureCoords
		) {
			vertices.add(new Vertex(
					new Vec3(position),
					new Vec3(colorMultiplier),
					new Vec2(textureCoords)
			));
			
			return this;
		}
		
		public ByteBuffer assemble() {
			ByteBuffer result = BufferUtils.createByteBuffer(
					DEFAULT_BYTES_PER_VERTEX * vertices.size()
			);
			
			for (Vertex v : vertices) {
				result
					.putFloat(v.position.x)
					.putFloat(v.position.y)
					.putFloat(v.position.z)
					.putFloat(v.colorMultiplier.x)
					.putFloat(v.colorMultiplier.y)
					.putFloat(v.colorMultiplier.z)
					.putFloat(v.textureCoords.x)
					.putFloat(v.textureCoords.y)
					.putFloat(Float.NaN)
					.putFloat(Float.NaN)
					.putFloat(Float.NaN);
			}
			
			result.flip();
			
			return result;
		}
		
	}

}