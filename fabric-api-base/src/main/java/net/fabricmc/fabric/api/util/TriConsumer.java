package net.fabricmc.fabric.api.util;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<K, L, R> {
	void accept(K key, L left, R right);

	default TriConsumer<K, L, R> andThen(TriConsumer<? super K, ? super L, ? super R> after) {
		Objects.requireNonNull(after);

		return (k, t, v) -> {
			this.accept(k, t, v);
			after.accept(k, t, v);
		};
	}
}
