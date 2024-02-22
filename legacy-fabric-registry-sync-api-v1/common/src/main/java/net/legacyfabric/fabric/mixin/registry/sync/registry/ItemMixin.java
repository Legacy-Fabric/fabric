/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

package net.legacyfabric.fabric.mixin.registry.sync.registry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;

import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;
import net.legacyfabric.fabric.impl.registry.sync.remappers.ItemRegistryRemapper;

@Mixin(Item.class)
public class ItemMixin {
	@Inject(method = "setup", at = @At("RETURN"))
	private static void initRegistryRemapper(CallbackInfo ci) {
		RegistryHelperImpl.registerRegistryRemapper(ItemRegistryRemapper::new);

		if (!RegistryHelperImpl.bootstrap) {
			try {
				Class.forName(Biome.class.getName());

				Class.forName(BlockEntity.class.getName());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
