package net.fabricmc.fabric.api.content.registry.v1;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

interface MaterialProvider {
	int getEnchantability();

	Supplier<ItemStack> getRepairIngredient();
}
