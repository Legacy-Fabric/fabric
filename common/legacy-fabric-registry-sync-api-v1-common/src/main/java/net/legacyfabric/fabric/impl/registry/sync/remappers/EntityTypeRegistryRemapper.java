package net.legacyfabric.fabric.impl.registry.sync.remappers;

import net.minecraft.entity.Entity;

import net.legacyfabric.fabric.api.registry.v1.RegistryIds;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;

public class EntityTypeRegistryRemapper extends RegistryRemapper<Class<? extends Entity>> {
	public EntityTypeRegistryRemapper() {
		super(RegistryHelperImpl.registriesGetter.getEntityTypeRegistry(), RegistryIds.ENTITY_TYPES, "EntityType", "EntityTypes");
	}
}
