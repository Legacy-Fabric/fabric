package io.github.legacyrewoven.mixin.networking.server;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements Runnable {

	protected final Queue<FutureTask<?>> queue = Queues.newArrayDeque();
	private Thread serverThread;

	@Shadow
	@Environment(EnvType.SERVER)
	public boolean isStopped() {
		return false;
	}

	public <V> ListenableFuture<V> method_6444(Callable<V> callable) {
		Validate.notNull(callable);
		if (!this.isOnThread() && !this.isStopped()) {
			ListenableFutureTask<V> listenableFutureTask = ListenableFutureTask.create(callable);
			synchronized(this.queue) {
				this.queue.add(listenableFutureTask);
				return listenableFutureTask;
			}
		} else {
			try {
				return Futures.immediateFuture(callable.call());
			} catch (Exception var6) {
				return Futures.immediateFailedCheckedFuture(var6);
			}
		}
	}

	public ListenableFuture<Object> execute(Runnable task) {
		Validate.notNull(task);
		return this.method_6444(Executors.callable(task));
	}

	public boolean isOnThread() {
		return Thread.currentThread() == this.serverThread;
	}

	@Shadow
	public void run() {

	}
}
