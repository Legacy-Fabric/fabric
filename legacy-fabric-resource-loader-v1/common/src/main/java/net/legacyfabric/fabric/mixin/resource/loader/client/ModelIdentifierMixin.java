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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.resource.ModelIdentifier;
import net.minecraft.resource.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(ModelIdentifier.class)
public class ModelIdentifierMixin {
	@Definition(id = "indexOf", method = "Ljava/lang/String;indexOf(I)I")
	@Expression("? = ?.indexOf(?)")
	@Inject(method = "splitModelIdentifier", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER))
	private static void modifyArray(String id, CallbackInfoReturnable<String[]> cir, @Local String[] strings) {
		Identifier identifier = new Identifier((id));

		if (identifier.getNamespace().equals("minecraft")) {
			return;
		}

		strings[0] = identifier.getNamespace();
		strings[1] = identifier.getPath();
	}
}
