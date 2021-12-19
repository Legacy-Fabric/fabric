package io.github.legacyrewoven.impl.networking.server;

import com.google.common.util.concurrent.ListenableFuture;

public interface MinecraftServerExtensions {
	ListenableFuture<Object> execute(Runnable task);
	boolean isOnThread();
}
