package net.legacyfabric.fabric.api.item.v1;

import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.ItemStack;

/**
 * Implementing this interface on your item makes it act as a fuel.
 */
public interface Fuel {
	int getBurnTime(ItemStack itemStack);

	static int burnTimeOf(ItemStack itemStack) {
		return FurnaceBlockEntity.method_26989(itemStack);
	}
}
