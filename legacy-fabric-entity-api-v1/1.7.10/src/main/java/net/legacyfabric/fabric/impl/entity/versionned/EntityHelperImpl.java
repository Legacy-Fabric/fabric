package net.legacyfabric.fabric.impl.entity.versionned;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.util.Identifier;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.class_868;

public class EntityHelperImpl {
	public static void registerSpawnEgg(Identifier identifier, int color0, int color1) {
		int mcId = RegistryHelper.getRawId(RegistryIds.ENTITY_TYPES,
				RegistryHelper.getValue(RegistryIds.ENTITY_TYPES, identifier));
		EntityType.field_3267.put(mcId, new class_868(mcId, color0, color1));
	}
}
