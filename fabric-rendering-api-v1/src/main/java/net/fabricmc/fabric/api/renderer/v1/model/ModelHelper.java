/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.api.renderer.v1.model;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.lwjgl.util.vector.Vector3f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.util.math.Direction;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;

@Environment(EnvType.CLIENT)
public abstract class ModelHelper {
	private ModelHelper() { }

	public static final int NULL_FACE_ID = 6;

	public static int toFaceIndex(Direction face) {
		return face == null ? NULL_FACE_ID : face.getId();
	}

	public static Direction faceFromIndex(int faceIndex) {
		return FACES[faceIndex];
	}

	private static final Direction[] FACES = Arrays.copyOf(Direction.values(), 7);

	public static List<BakedQuad>[] toQuadLists(Mesh mesh, Direction dir) {
		SpriteFinder finder = SpriteFinder.get(MinecraftClient.getInstance().method_2240());

		@SuppressWarnings("unchecked")
		final ImmutableList.Builder<BakedQuad>[] builders = new ImmutableList.Builder[7];

		for (int i = 0; i < 7; i++) {
			builders[i] = ImmutableList.builder();
		}

		mesh.forEach(q -> {
			final int limit = q.material().spriteDepth();

			for (int l = 0; l < limit; l++) {
				Direction face = q.cullFace();
				builders[face == null ? 6 : face.getId()].add(q.toBakedQuad(l, dir, false));
			}
		});

		@SuppressWarnings("unchecked")
		List<BakedQuad>[] result = new List[7];

		for (int i = 0; i < 7; i++) {
			result[i] = builders[i].build();
		}

		return result;
	}

	private static Transformation makeTransform(
			float rotationX, float rotationY, float rotationZ,
			float translationX, float translationY, float translationZ,
			float scaleX, float scaleY, float scaleZ) {
		Vector3f translation = new Vector3f(translationX, translationY, translationZ);
		translation.scale(0.0625f);
		return new Transformation(
				new Vector3f(rotationX, rotationY, rotationZ),
				translation,
				new Vector3f(scaleX, scaleY, scaleZ));
	}

	public static final Transformation TRANSFORM_BLOCK_GUI = makeTransform(30, 225, 0, 0, 0, 0, 0.625f, 0.625f, 0.625f);
	public static final Transformation TRANSFORM_BLOCK_GROUND = makeTransform(0, 0, 0, 0, 3, 0, 0.25f, 0.25f, 0.25f);
	public static final Transformation TRANSFORM_BLOCK_FIXED = makeTransform(0, 0, 0, 0, 0, 0, 0.5f, 0.5f, 0.5f);
	public static final Transformation TRANSFORM_BLOCK_3RD_PERSON_RIGHT = makeTransform(75, 45, 0, 0, 2.5f, 0, 0.375f, 0.375f, 0.375f);
	public static final Transformation TRANSFORM_BLOCK_1ST_PERSON_RIGHT = makeTransform(0, 45, 0, 0, 0, 0, 0.4f, 0.4f, 0.4f);
	public static final Transformation TRANSFORM_BLOCK_1ST_PERSON_LEFT = makeTransform(0, 225, 0, 0, 0, 0, 0.4f, 0.4f, 0.4f);

	public static final ModelTransformation MODEL_TRANSFORM_BLOCK = new ModelTransformation(
			TRANSFORM_BLOCK_3RD_PERSON_RIGHT,
			TRANSFORM_BLOCK_1ST_PERSON_LEFT,
			TRANSFORM_BLOCK_1ST_PERSON_RIGHT,
			TRANSFORM_BLOCK_GUI,
			TRANSFORM_BLOCK_GROUND,
			TRANSFORM_BLOCK_FIXED);
}
