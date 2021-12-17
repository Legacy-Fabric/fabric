package io.github.legacyrewoven.mixin.networking;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import io.github.legacyrewoven.impl.networking.server.MinecraftServerExtensions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.class_2553;
import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements MinecraftServerExtensions {

	@Unique private final Queue<FutureTask<?>> queue = Queues.newArrayDeque();
	@Unique private	Thread serverThread;

	@Redirect(method = "method_10403", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_2553;start()V"))
	public void startServerThread(class_2553 class_2553) {
		this.serverThread = new class_2553((MinecraftServer)(Object)this, "Server thread");
		this.serverThread.start();
	}

	@Shadow	@Environment(EnvType.SERVER) public boolean isStopped() {
		return false;
	}

	@Unique	private <V> ListenableFuture<V> runCallable(Callable<V> callable) {
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
			} catch (Exception e) {
				return Futures.immediateFailedCheckedFuture(e);
			}
		}
	}


	public ListenableFuture<Object> execute(Runnable task) {
		Validate.notNull(task);
		return this.runCallable(Executors.callable(task));
	}

	public boolean isOnThread() {
		return Thread.currentThread() == this.serverThread;
	}
}
