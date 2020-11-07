package net.fabricmc.fabric.impl.worldgen.event;

import org.spongepowered.asm.mixin.injection.callback.Cancellable;

public class OreCancellable implements Cancellable {
	private boolean cancelled = false;

	@Override
	public boolean isCancellable() {
		return true;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void cancel() {
		this.cancelled = true;
	}
}
