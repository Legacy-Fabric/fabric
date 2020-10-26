package net.fabricmc.fabric.impl.command;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrar;
import net.fabricmc.fabric.api.command.v2.lib.sponge.CommandManager;
import net.fabricmc.fabric.api.registry.FabricCommandRegistry;
import net.fabricmc.loader.api.FabricLoader;

public class ImplInit implements ModInitializer, CommandRegistrar {
	@Override
	public void onInitialize() {
		FabricLoader.getInstance().getEntrypoints("fabric-sponge-command-api-v2:registrar", CommandRegistrar.class).forEach(registrar -> {
			registrar.register(InternalObjects.getCommandManager(), FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER);
		});
		InternalObjects.getCommandManager().getCommands().forEach(mapping -> {
			CommandWrapper wrapper = new CommandWrapper(mapping);
			FabricCommandRegistry.INSTANCE.register(wrapper);
		});
	}

	@Override
	public void register(CommandManager manager, boolean dedicated) {
		CommandRegistrar.EVENT.invoker().register(manager, dedicated);
	}
}
