package net.legacyfabric.fabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.profiler.Profiler;

public class CommandRegisterCallback {

	/**
	 * Called when it is time to register Common sided commands.
	 *
	 * <p>When this event is called, all Vanilla commands have already been registered
	 */
	public static final Event<CommonCommandRegister> COMMON = EventFactory.createArrayBacked(CommonCommandRegister.class, callbacks -> (dispatcher, profiler) -> {
		if (EventFactory.isProfilingEnabled()) {
			profiler.push("fabricCommonCommandLoad");

			for (CommonCommandRegister callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onCommandRegistration(dispatcher, profiler);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (CommonCommandRegister callback : callbacks) {
				callback.onCommandRegistration(dispatcher, profiler);
			}
		}
	});

	/**
	 * Called when it is time to register Common sided commands.
	 *
	 * <p>When this event is called, all Vanilla commands have already been registered
	 */
	public static final Event<ClientCommandRegister> CLIENT = EventFactory.createArrayBacked(ClientCommandRegister.class, callbacks -> (dispatcher, client, profiler) -> {
		if (EventFactory.isProfilingEnabled()) {
			profiler.push("fabricCommonCommandLoad");

			for (ClientCommandRegister callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onCommandRegistration(dispatcher, client, profiler);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (ClientCommandRegister callback : callbacks) {
				callback.onCommandRegistration(dispatcher, client, profiler);
			}
		}
	});

	/**
	 * Called when it is time to register Common sided commands.
	 *
	 * <p>When this event is called, all Vanilla commands have already been registered
	 */
	public static final Event<ServerCommandRegister> SERVER = EventFactory.createArrayBacked(ServerCommandRegister.class, callbacks -> (dispatcher, server, profiler) -> {
		if (EventFactory.isProfilingEnabled()) {
			profiler.push("fabricCommonCommandLoad");

			for (ServerCommandRegister callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onCommandRegistration(dispatcher, server, profiler);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (ServerCommandRegister callback : callbacks) {
				callback.onCommandRegistration(dispatcher, server, profiler);
			}
		}
	});

	@FunctionalInterface
	public interface CommonCommandRegister {
		void onCommandRegistration(CommandDispatcher<CommandSource> dispatcher, Profiler profiler);
	}

	@FunctionalInterface
	public interface ClientCommandRegister {
		void onCommandRegistration(CommandDispatcher<CommandSource> dispatcher, MinecraftClient client, Profiler profiler);
	}

	@FunctionalInterface
	public interface ServerCommandRegister {
		void onCommandRegistration(CommandDispatcher<CommandSource> dispatcher, MinecraftServer server, Profiler profiler);
	}
}
