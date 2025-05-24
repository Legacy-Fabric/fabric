/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
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

package net.legacyfabric.fabric.mixin.client.keybinding;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.option.KeyBinding;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {
	@Shadow
	@Final
	private static Map<String, Integer> field_15867;
	private static int lf$category_count = 7;
	@Inject(method = "<init>", at = @At("RETURN"))
	private void registerCategory(String string, int i, String category, CallbackInfo ci) {
		if (!field_15867.containsKey(category)) {
			while (field_15867.containsValue(lf$category_count)) lf$category_count++;
			field_15867.put(category, lf$category_count);
		}
	}
}
