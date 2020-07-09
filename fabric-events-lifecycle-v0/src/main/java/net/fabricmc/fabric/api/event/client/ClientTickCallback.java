package net.fabricmc.fabric.api.event.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

public interface ClientTickCallback {

	Event<ClientTickCallback> EVENT = EventFactory.createArrayBacked(ClientTickCallback.class,(listeners)->{
		if(EventFactory.isProfilingEnabled()){
			return (client) -> {
				client.profiler.push("fabricClientTick");

				for (ClientTickCallback event : listeners) {
					client.profiler.push(EventFactory.getHandlerName(event));
					event.tick(client);
					client.profiler.pop();
				}

				client.profiler.pop();
			};
		} else{
			return (client) -> {
				for (ClientTickCallback event : listeners) {
					event.tick(client);
				}
			};
		}
	});

	void tick(MinecraftClient client);
}
