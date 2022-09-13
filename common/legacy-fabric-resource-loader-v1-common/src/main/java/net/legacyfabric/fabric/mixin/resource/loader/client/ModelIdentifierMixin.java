/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(ModelIdentifier.class)
public class ModelIdentifierMixin {
	@Inject(method = "split", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/String;indexOf(I)I"), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void modifyArray(String id, CallbackInfoReturnable<String[]> cir, String[] strings) {
		Identifier identifier = new Identifier((id));
		if (identifier.getNamespace().equals("minecraft")) {
			return;
		}
		strings[0] = identifier.getNamespace();
		strings[1] = identifier.getPath();
	}
}
