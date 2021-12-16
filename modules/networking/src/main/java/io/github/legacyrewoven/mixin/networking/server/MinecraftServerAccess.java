package io.github.legacyrewoven.mixin.networking.server;

import com.google.common.util.concurrent.ListenableFuture;

public interface MinecraftServerAccess {
	boolean isOnThread();
	ListenableFuture<Object> execute(Runnable task);
}
