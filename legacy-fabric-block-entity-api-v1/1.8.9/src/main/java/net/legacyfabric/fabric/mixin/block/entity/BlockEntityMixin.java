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

package net.legacyfabric.fabric.mixin.block.entity;

import java.util.Map;

import net.legacyfabric.fabric.api.block.entity.v1.BlockEntityEvents;
import net.legacyfabric.fabric.impl.block.entity.FakeBlockEntityRegistry;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImplementation;

import net.ornithemc.osl.registries.api.registry.Registry;
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
	private static void register(Class<? extends BlockEntity> type, String id) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void registerRegistry(CallbackInfo ci) {
		Registry<Class<? extends BlockEntity>> registry = new FakeBlockEntityRegistry((id, value) -> {
			register(value, id.toString());
			return value;
		});

		RegistryHelperImplementation.registerCompatId(RegistryIds.BLOCK_ENTITY_TYPES, registry);
		BlockEntityEvents.REGISTER_BLOCK_ENTITIES.invoker().accept((id, clazz) -> register(clazz, id.toString()));
	}

	/*
		Previous version of LFAPI used to transform vanilla names to id.
		We don't do that anymore, so we still check for id versions to not break old saves.
	 */
	@ModifyArg(method = "fromNbt", at = @At(value = "INVOKE", remap = false, target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
	private static Object fixOldSaves(Object oldKey) {
		Identifier asId = new Identifier(oldKey);

		if (BlockEntityUtils.ID_TO_OLD.containsKey(asId)) {
			return BlockEntityUtils.ID_TO_OLD.get(asId);
		}

		return oldKey;
	}
}
