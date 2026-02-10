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

package net.legacyfabric.fabric.api.entity.event.v1;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;

/**
 * Events related to entities in combat.
 */
public final class ServerEntityCombatEvents {
	/**
	 * An event that is called after an entity is directly responsible for killing another entity.
	 *
	 * @see Entity#onKilledOther(LivingEntity)
	 */
	public static final Event<AfterKilledOtherEntity> AFTER_KILLED_OTHER_ENTITY = EventFactory.createArrayBacked(AfterKilledOtherEntity.class, callbacks -> (entity, killedEntity) -> {
		for (AfterKilledOtherEntity callback : callbacks) {
			callback.afterKilledOtherEntity(entity, killedEntity);
		}
	});

	@FunctionalInterface
	public interface AfterKilledOtherEntity {
		/**
		 * Called after an entity has killed another entity.
		 *
		 * @param entity       the entity
		 * @param killedEntity the entity which was killed by the {@code entity}
		 */
		void afterKilledOtherEntity(Entity entity, LivingEntity killedEntity);
	}

	private ServerEntityCombatEvents() {
	}
}
