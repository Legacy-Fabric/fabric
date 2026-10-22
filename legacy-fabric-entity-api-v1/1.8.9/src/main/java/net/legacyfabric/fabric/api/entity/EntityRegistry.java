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

package net.legacyfabric.fabric.api.entity;

import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.function.Function;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.entities.api.EntityTypeRegistry;
import net.ornithemc.osl.entities.api.entity.EntityType;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * @deprecated Use {@link EntityTypeRegistry}
 */
@Deprecated
public final class EntityRegistry {
	public static int getId(Class<? extends Entity> type) {
		return EntityTypeRegistry.getId(toEntityType(type));
	}

	public static NamespacedIdentifier getIdentifier(Class<? extends Entity> type) {
		return EntityTypeRegistry.getIdentifier(toEntityType(type));
	}

	public static Class<? extends Entity> getEntityType(int id) {
		return EntityTypeRegistry.getEntityType(id).getType();
	}

	public static Class<? extends Entity> getEntityType(NamespacedIdentifier identifier) {
		return EntityTypeRegistry.getEntityType(identifier).getType();
	}

	public static Set<NamespacedIdentifier> identifierSet() {
		return EntityTypeRegistry.identifierSet();
	}

	public static <T extends Entity> Class<T> register(NamespacedIdentifier identifier, Class<T> type) {
		return (Class<T>) EntityTypeRegistry.register(identifier, builder(type)).getType();
	}

	private static <T extends Entity> EntityType<T> toEntityType(Class<T> type) {
		EntityType<T> entityType = null;

		for (EntityType<?> eType : EntityTypeRegistry.REGISTRY) {
			if (eType.getType() == type) {
				entityType = (EntityType<T>) eType;
				break;
			}
		}

		return entityType;
	}

	private static <T extends Entity> EntityType.Builder<T> builder(Class<T> type) {
		Constructor<? extends T> constructor = null;

		try {
			constructor = type.getConstructor(World.class);
		} catch (NoSuchMethodException e) {
			// empty
		}

		Constructor<? extends T> method = constructor;
		boolean instantiable = (constructor != null);

		Function<? super World, ? extends T> factory = world -> {
			if (instantiable) {
				try {
					return method.newInstance(world);
				} catch (Throwable t) {
					throw new IllegalStateException("error creating entity of type " + type, t);
				}
			} else {
				throw new UnsupportedOperationException();
			}
		};

		return EntityType.Builder.of(type, factory);
	}
}
