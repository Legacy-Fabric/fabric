package net.fabricmc.fabric.api.content.registry.v1;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

public interface ToolMaterial {
	int getMaxDurability();

	float getMiningSpeedMultiplier();

	float getAttackMultiplier();

	int getMiningLevel();

	int getEnchantability();

	Supplier<ItemStack> getRepairIngredient();
}
