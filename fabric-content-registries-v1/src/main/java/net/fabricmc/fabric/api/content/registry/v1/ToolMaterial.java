package net.fabricmc.fabric.api.content.registry.v1;

public interface ToolMaterial extends MaterialProvider {
	int getDurability();

	float getMiningSpeedMultiplier();

	float getAttackMultiplier();

	int getMiningLevel();
}
