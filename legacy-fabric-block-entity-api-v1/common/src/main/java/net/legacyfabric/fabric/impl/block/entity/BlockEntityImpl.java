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

package net.legacyfabric.fabric.impl.block.entity;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

import net.ornithemc.osl.blockentities.api.BlockEntityEvents;
import net.ornithemc.osl.blockentities.api.BlockEntityTypeRegistry;
import net.ornithemc.osl.blockentities.api.blockentity.BlockEntityType;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

import net.minecraft.block.entity.BlockEntity;

public class BlockEntityImpl implements ModInitializer {
	@Override
	public void init() {
		BlockEntityEvents.REGISTER_BLOCK_ENTITY_TYPES.register(() -> {
			net.legacyfabric.fabric.api.block.entity.v1.BlockEntityEvents.REGISTER_BLOCK_ENTITIES.invoker()
					.accept((id, type) -> {
						Constructor<? extends BlockEntity> constructor;

						try {
							constructor = type.getConstructor();
						} catch (NoSuchMethodException e) {
							throw new IllegalArgumentException("Invalid class " + type + ": no constructor taking no arguments");
						}

						Supplier<? extends BlockEntity> factory = () -> {
							try {
								return constructor.newInstance();
							} catch (Throwable t) {
								throw new IllegalStateException("error creating block entity of type " + type, t);
							}
						};

						BlockEntityTypeRegistry.register(id, BlockEntityType.Builder.of(type, factory));
					});
		});
	}
}
