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

package net.legacyfabric.fabric.mixin.registry.sync.osl;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingSource;
import net.ornithemc.osl.registries.impl.registry.sync.SyncedRegistriesNbtSerializer;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.nbt.NbtCompound;

import net.legacyfabric.fabric.impl.registry.RegistryHelperImplementation;

@Mixin(SyncedRegistriesNbtSerializer.class)
public class SyncedRegistriesNbtSerializerMixin {
	@WrapMethod(method = "deserialize(Lnet/minecraft/nbt/NbtCompound;Lnet/ornithemc/osl/registries/impl/registry/sync/RegistryMappingSource;)V")
	private static void convertLFMappings(NbtCompound nbt, RegistryMappingSource source, Operation<Void> original) {
		if (!nbt.contains("format")) {
			NbtCompound registeries = new NbtCompound();

			for (String key : nbt.getKeys()) {
				NbtCompound content = nbt.getCompound(key);

				registeries.put(RegistryHelperImplementation.convertRegistryId(key), content);
			}

			NbtCompound main = new NbtCompound();
			main.putInt("format", 1);
			main.put("registries", registeries);
			nbt = main;
		}

		original.call(nbt, source);
	}
}
