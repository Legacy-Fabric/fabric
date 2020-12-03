/*
 * Copyright (c) 2020 Legacy Fabric
 * Copyright (c) 2016 - 2020 FabricMC
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

package net.fabricmc.fabric.api.event.world;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;

@Deprecated
public interface EntityHurtCallback {
	/**
	 * @deprecated Please use {@link ServerEntityEvents#HURT}
	 */
	@Deprecated
	Event<EntityHurtCallback> EVENT = EventFactory.createArrayBacked(EntityHurtCallback.class, (listeners) -> (entity, source, original, damage) -> {
		for (EntityHurtCallback callback : listeners) {
			callback.hurtEntity(entity, source, original, damage);
		}
	});

	void hurtEntity(LivingEntity entity, DamageSource damageSource, float originalHealth, float damage);
}
