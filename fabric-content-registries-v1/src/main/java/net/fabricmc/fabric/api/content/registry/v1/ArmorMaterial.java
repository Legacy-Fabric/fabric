package net.fabricmc.fabric.api.content.registry.v1;

public interface ArmorMaterial extends MaterialProvider {
	int getProtection(int slot);

	int getDurability(int slot);

	String getName();
}
