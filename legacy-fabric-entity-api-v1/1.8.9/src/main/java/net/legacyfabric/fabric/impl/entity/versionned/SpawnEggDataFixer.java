package net.legacyfabric.fabric.impl.entity.versionned;

import net.minecraft.entity.Entities;

import net.ornithemc.osl.registries.api.registry.sync.IdFixer;

import java.util.Map;

public class SpawnEggDataFixer implements IdFixer {
	@Override
	public void apply() {
		for (Map.Entry<Integer, Entities.SpawnEggData> entry : Entities.SPAWN_EGG_DATA.entrySet()) {
			int newId = entry.getKey();
			Entities.SpawnEggData data = entry.getValue();

			data.id = newId;
		}
	}
}
