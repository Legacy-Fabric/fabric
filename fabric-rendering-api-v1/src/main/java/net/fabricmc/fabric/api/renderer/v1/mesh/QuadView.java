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

package net.fabricmc.fabric.api.renderer.v1.mesh;

import org.lwjgl.util.vector.Vector3f;

import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.math.Direction;

public interface QuadView {
	void toVanilla(int spriteIndex, int[] target, int targetIndex, boolean isItem);

	void copyTo(MutableQuadView target);

	RenderMaterial material();

	int colorIndex();

	Direction lightFace();

	Direction cullFace();

	Direction nominalFace();

	Vector3f faceNormal();

	default BakedQuad toBakedQuad(int spriteIndex, Direction direction, boolean isItem) {
		int[] vertexData = new int[28];
		toVanilla(spriteIndex, vertexData, 0, isItem);
		return new BakedQuad(vertexData, colorIndex(), direction);
	}

	int tag();

	Vector3f copyPos(int vertexIndex, Vector3f target);

	float posByIndex(int vertexIndex, int coordinateIndex);

	float x(int vertexIndex);

	float y(int vertexIndex);

	float z(int vertexIndex);

	boolean hasNormal(int vertexIndex);

	Vector3f copyNormal(int vertexIndex, Vector3f target);

	float normalX(int vertexIndex);

	float normalY(int vertexIndex);

	float normalZ(int vertexIndex);

	int lightmap(int vertexIndex);

	int spriteColor(int vertexIndex, int spriteIndex);

	float spriteU(int vertexIndex, int spriteIndex);

	float spriteV(int vertexIndex, int spriteIndex);
}
