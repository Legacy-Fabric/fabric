package net.legacyfabric.fabric.api.entity;

import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.entity.EntityHelperImpl;

public interface EntityHelper {
	static void registerSpawnEgg(Identifier identifier, int color0, int color1) {
		EntityHelperImpl.registerSpawnEgg(identifier, color0, color1);
	}
}
