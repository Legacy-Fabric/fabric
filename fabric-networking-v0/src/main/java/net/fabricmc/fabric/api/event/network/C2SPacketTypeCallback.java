package net.fabricmc.fabric.api.event.network;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Collection;

public interface C2SPacketTypeCallback {
	Event<C2SPacketTypeCallback> REGISTERED = EventFactory.createArrayBacked(
			C2SPacketTypeCallback.class,
			(callbacks) -> (client, types) -> {
				for (C2SPacketTypeCallback callback : callbacks) {
					callback.accept(client, types);
				}
			}
	);

	Event<C2SPacketTypeCallback> UNREGISTERED = EventFactory.createArrayBacked(
			C2SPacketTypeCallback.class,
			(callbacks) -> (client, types) -> {
				for (C2SPacketTypeCallback callback : callbacks) {
					callback.accept(client, types);
				}
			}
	);

	void accept(PlayerEntity client, Collection<Identifier> types);
}
