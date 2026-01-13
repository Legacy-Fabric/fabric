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

package net.legacyfabric.fabric.mixin.networking;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.class_739;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.impl.networking.server.MinecraftServerExtensions;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements MinecraftServerExtensions {
	@Unique
	private final Queue<FutureTask<?>> queue = Queues.newArrayDeque();
	@Unique
	private Thread serverThread;

	@Redirect(method = "startServerThread", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/class_739;start()V"))
	public void startServerThread(class_739 class_739) {
		this.serverThread = new class_739((MinecraftServer) (Object) this, "Server thread");
		this.serverThread.start();
	}

	@Override
	public boolean isOnGameThread() {
		return Thread.currentThread() == this.serverThread;
	}

	@Shadow
	@Environment(EnvType.SERVER)
	public boolean isStopped() {
		return false;
	}

	@Unique
	private <V> ListenableFuture<V> runCallable(Callable<V> callable) {
		Validate.notNull(callable);

		if (!this.isOnGameThread() && !this.isStopped()) {
			ListenableFutureTask<V> listenableFutureTask = ListenableFutureTask.create(callable);
			synchronized (this.queue) {
				this.queue.add(listenableFutureTask);
				return listenableFutureTask;
			}
		} else {
			try {
				return Futures.immediateFuture(callable.call());
			} catch (Exception e) {
				return Futures.immediateFailedFuture(e);
			}
		}
	}

	@Override
	public ListenableFuture<Object> executeTask(Runnable task) {
		Validate.notNull(task);
		return this.runCallable(Executors.callable(task));
	}
}
