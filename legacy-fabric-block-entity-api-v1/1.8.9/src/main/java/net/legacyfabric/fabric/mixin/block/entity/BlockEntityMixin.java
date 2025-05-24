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

package net.legacyfabric.fabric.mixin.block.entity;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.BlockEntity;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.block.entity.BlockEntityUtils;
import net.legacyfabric.fabric.impl.registry.wrapper.MapFabricRegistryWrapper;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {
	@Shadow
	@Final
	private static Map<String, Class<? extends BlockEntity>> stringClassMap;
	@Shadow
	@Final
	private static Map<Class<? extends BlockEntity>, String> classStringMap;
	@Unique
	private static FabricRegistry<Class<? extends BlockEntity>> BLOCK_ENTITY_TYPE_REGISTRY;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void registerRegistry(CallbackInfo ci) {
		BLOCK_ENTITY_TYPE_REGISTRY = new MapFabricRegistryWrapper<>(
				RegistryIds.BLOCK_ENTITY_TYPES, stringClassMap, classStringMap,
				identifier -> BlockEntityUtils.ID_TO_OLD.getOrDefault(identifier, identifier.toString()),
				string -> BlockEntityUtils.OLD_TO_ID.getOrDefault(string, new Identifier(string))
		);

		RegistryHelper.addRegistry(RegistryIds.BLOCK_ENTITY_TYPES, BLOCK_ENTITY_TYPE_REGISTRY);
	}

	/*
		Previous version of LFAPI used to transform vanilla names to id.
		We don't do that anymore, so we still check for id versions to not break old saves.
	 */
	@ModifyArg(method = "createFromNbt", at = @At(value = "INVOKE", remap = false, target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
	private static Object fixOldSaves(Object oldKey) {
		Identifier asId = new Identifier(oldKey);

		if (BlockEntityUtils.ID_TO_OLD.containsKey(asId)) {
			return BlockEntityUtils.ID_TO_OLD.get(asId);
		}

		return oldKey;
	}
}
