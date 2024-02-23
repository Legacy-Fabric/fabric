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

package net.legacyfabric.fabric.mixin.networking;

import com.google.common.util.concurrent.ListenableFuture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.server.MinecraftServer;

import net.legacyfabric.fabric.impl.networking.server.MinecraftServerExtensions;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements MinecraftServerExtensions {
	@Shadow
	public abstract boolean isOnThread();

	@Shadow
	public abstract ListenableFuture<Object> submit(Runnable task);

	@Override
	public boolean isOnGameThread() {
		return this.isOnThread();
	}

	@Override
	public ListenableFuture<Object> executeTask(Runnable task) {
		return this.submit(task);
	}
}
