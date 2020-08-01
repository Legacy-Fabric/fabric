package net.fabricmc.fabric.api.armor.v1;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

public interface ArmorMaterial {
	void getEnchantability();

	Supplier<ItemStack> getRepairIngredient();

	int getDurabilityMultiplier();

	int[] getProtectionValues();

	String getName();
}
