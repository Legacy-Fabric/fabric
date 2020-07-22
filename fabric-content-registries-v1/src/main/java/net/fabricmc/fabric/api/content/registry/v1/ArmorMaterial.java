package net.fabricmc.fabric.api.content.registry.v1;

import net.minecraft.item.Item;

public interface ArmorMaterial {
	int getDurabilty(int slot);

	int getDurability(int slot);

	int getEnchantability();

	Item getRepairIngredient();

	String getName();
}
