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

package net.legacyfabric.fabric.mixin.registry.sync.versioned;

import java.util.Objects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.registry.DefaultedIdRegistry;
import net.minecraft.util.registry.IdRegistry;

import net.legacyfabric.fabric.api.util.Identifier;

@Mixin(DefaultedIdRegistry.class)
public abstract class BiDefaultedRegistryMixin extends IdRegistry {
	@Shadow
	private Object defaultValue;

	@Shadow
	public abstract Object get(String par1);

	@Unique
	private Identifier lf$defaultKey;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void fabric$setDefaultKey(String par1, CallbackInfo ci) {
		this.lf$defaultKey = new Identifier(par1);
	}

	@Override
	public Object fabric$getValue(Identifier id) {
		String key = (String) fabric$toKeyType(id);
		Object value = get(key);

		if (value == this.defaultValue && !Objects.equals(this.lf$defaultKey.toString(), key)) return null;

		return value;
	}
}
