/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
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

package net.legacyfabric.fabric.mixin.resource.loader.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.property.Property;
import net.minecraft.client.resource.ModelIdentifier;
import net.minecraft.client.resource.model.VariantBlockModelProvider;
import net.minecraft.resource.Identifier;

@Mixin(VariantBlockModelProvider.class)
public class VariantBlockModelProviderMixin {
	@Shadow
	@Final
	private Property property;

	@Definition(id = "ModelIdentifier", type = ModelIdentifier.class)
	@Expression("new ModelIdentifier(?, ?)")
	@ModifyArg(method = "provide", at = @At("MIXINEXTRAS:EXPRESSION"), index = 0)
	private String lf$fixModelIdentifier(String s, @Local(argsOnly = true) BlockState state) {
		if (this.property != null) {
			Identifier identifier = Block.REGISTRY.getKey(state.getBlock());
			s = identifier.getNamespace() + ":" + s;
		}

		return s;
	}
}
