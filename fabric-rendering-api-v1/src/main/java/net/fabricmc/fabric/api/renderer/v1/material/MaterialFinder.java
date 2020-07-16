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

package net.fabricmc.fabric.api.renderer.v1.material;

import com.google.common.annotations.Beta;

import net.minecraft.client.render.RenderLayer;

@Beta
public interface MaterialFinder {
	RenderMaterial find();

	MaterialFinder clear();

	MaterialFinder spriteDepth(int depth);

	MaterialFinder blendMode(int spriteIndex, RenderLayer blendMode);

	MaterialFinder disableColorIndex(int spriteIndex, boolean disable);

	MaterialFinder disableDiffuse(int spriteIndex, boolean disable);

	MaterialFinder disableAo(int spriteIndex, boolean disable);

	MaterialFinder emissive(int spriteIndex, boolean isEmissive);
}
