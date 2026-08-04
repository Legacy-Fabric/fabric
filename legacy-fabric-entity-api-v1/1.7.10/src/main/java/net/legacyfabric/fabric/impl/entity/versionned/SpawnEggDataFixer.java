package net.legacyfabric.fabric.impl.entity.versionned;

import java.util.Map;

import net.minecraft.entity.Entities__SpawnEggData;

import net.ornithemc.osl.registries.api.registry.sync.IdFixer;

import net.minecraft.entity.Entities;

public class SpawnEggDataFixer implements IdFixer {
	@Override
	public void apply() {
		for (Map.Entry<Integer, Entities__SpawnEggData> entry : Entities.SPAWN_EGG_DATA.entrySet()) {
			int newId = entry.getKey();
			Entities__SpawnEggData data = entry.getValue();

			data.id = newId;
		}
	}
}
