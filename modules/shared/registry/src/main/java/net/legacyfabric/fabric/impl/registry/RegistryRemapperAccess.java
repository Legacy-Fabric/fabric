package net.legacyfabric.fabric.impl.registry;

import net.minecraft.world.level.LevelProperties;

public interface RegistryRemapperAccess {
	RegistryRemapper getItemRemapper();

	RegistryRemapper getBlockRemapper();

	default void remap() {
		this.getItemRemapper().remap();
		this.getBlockRemapper().remap();
	}

	static void remap(LevelProperties props) {
		((RegistryRemapperAccess) props).remap();
	}
}
