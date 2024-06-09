package net.legacyfabric.fabric.impl.entity.versionned;

import net.legacyfabric.fabric.api.util.Identifier;

import net.minecraft.entity.EntityType;

public class EntityHelperImpl {
	public static void registerSpawnEgg(Identifier identifier, int color0, int color1) {
		net.minecraft.util.Identifier mcId = new net.minecraft.util.Identifier(identifier.toString());
		EntityType.SPAWN_EGGS.put(mcId, new EntityType.SpawnEggData(mcId, color0, color1));
	}
}
