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

package net.legacyfabric.fabric.mixin.biome;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.ArrayMapper;
import net.ornithemc.osl.registries.api.registry.sync.DynamicArray;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.biome.Biome;

import net.legacyfabric.fabric.api.biome.BiomeExtension;
import net.legacyfabric.fabric.impl.biome.versioned.BiomeRegistryImpl;

@Mixin(Biome.class)
public class BiomeMixin implements BiomeExtension {
	@Mutable
	@Shadow
	@Final
	public static Biome[] BY_ID;

	@Inject(method = "<clinit>", at = @At("HEAD"))
	private static void lf$unlockRegistry(CallbackInfo ci) {
		BiomeRegistryImpl.unlock();
	}

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/biome/Biome;BY_ID:[Lnet/minecraft/world/biome/Biome;", ordinal = 2, opcode = Opcodes.GETSTATIC))
	private static void api$registerRegistry(CallbackInfo ci) {
		BiomeRegistryImpl.registerBiomes();

		SyncedRegistries.registerMapper(BiomeRegistryImpl.KEY, NamespacedIdentifiers.from("biome/by_id"), ArrayMapper.of(() -> BY_ID, a -> BY_ID = a));
	}

	@ModifyVariable(method = "<init>", argsOnly = true, ordinal = 0, at = @At("HEAD"))
	private static int lf$autoIdAssignment(int id) {
		if (id == REGISTRY_AUTO_ASSIGN_ID) {
			id = DynamicArray.length(BY_ID);
		}

		return id;
	}

	@Inject(method = "<init>", at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/biome/Biome;BY_ID:[Lnet/minecraft/world/biome/Biome;",
			opcode = Opcodes.GETSTATIC,
			args = "array=set"))
	private void lf$growArray(int id, CallbackInfo ci) {
		int capacity = id + 1;

		BY_ID = DynamicArray.grow(BY_ID, capacity);
	}
}
