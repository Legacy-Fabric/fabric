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

package net.legacyfabric.fabric.test.registry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.resource.loader.api.resource.ResourcePath;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;

import net.minecraft.entity.mob.monster.CreeperEntity;
import net.minecraft.world.World;

public class TestCreeperEntity extends CreeperEntity {
	private static final NamespacedIdentifier TEXTURE = NamespacedIdentifiers.from("legacy-fabric-api", "textures/entity/creeper/creeper.png");

	public TestCreeperEntity(World world) {
		super(world);
		this.texture = ResourcePath.nameOf(ResourceType.CLIENT_ASSETS, TEXTURE);
	}

	@Override
	public void tick() {
		//		if (this.isAlive()) {
		//			if (this.hasStatusEffect(EFFECT)) {
		//				this.setIgnited();
		//			}
		//		}

		super.tick();
	}
}
