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

import java.util.Random;
import java.util.function.Supplier;

import com.google.common.annotations.Beta;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;

@Beta
public interface FabricBakedModel {
	boolean isVanillaAdapter();

	void emitBlockQuads(WorldView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context);

	void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context);
}
