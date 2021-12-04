/*
 * Copyright (c) 2021 Legacy Rewoven
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

package net.legacyfabric.fabric.mixin.permission;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.Entity;

import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

@Mixin(targets = "net/minecraft/server/command/ExecuteCommand$1")
public abstract class ExecuteCommand_1Mixin implements PermissibleCommandSource {
	@Shadow
	public abstract Entity getEntity();

	@Override
	public boolean hasPermission(String perm) {
		return ((PermissibleCommandSource) this.getEntity()).hasPermission(perm);
	}
}
