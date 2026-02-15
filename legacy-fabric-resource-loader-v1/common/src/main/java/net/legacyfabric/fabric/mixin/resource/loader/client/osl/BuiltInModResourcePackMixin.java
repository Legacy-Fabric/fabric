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

package net.legacyfabric.fabric.mixin.resource.loader.client.osl;

import net.ornithemc.osl.resource.loader.api.ModResourcePack;
import net.ornithemc.osl.resource.loader.impl.BuiltInModResourcePack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

@Mixin(BuiltInModResourcePack.class)
public abstract class BuiltInModResourcePackMixin implements ModResourcePack, net.legacyfabric.fabric.api.resource.ModResourcePack {
	@Shadow
	@Final
	private ModContainer mod;

	@Override
	public ModMetadata getFabricModMetadata() {
		return this.getModMetadata();
	}

	@Override
	public ModContainer getOwner() {
		return this.mod;
	}
}
