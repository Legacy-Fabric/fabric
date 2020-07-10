package net.fabricmc.fabric.api.event.network;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.Identifier;

import java.util.Collection;

public interface S2CPacketTypeCallback {
	Event<S2CPacketTypeCallback> REGISTERED = EventFactory.createArrayBacked(
			S2CPacketTypeCallback.class,
			(callbacks) -> (types) -> {
				for (S2CPacketTypeCallback callback : callbacks) {
					callback.accept(types);
				}
			}
	);

	Event<S2CPacketTypeCallback> UNREGISTERED = EventFactory.createArrayBacked(
			S2CPacketTypeCallback.class,
			(callbacks) -> (types) -> {
				for (S2CPacketTypeCallback callback : callbacks) {
					callback.accept(types);
				}
			}
	);

	void accept(Collection<Identifier> types);
}
