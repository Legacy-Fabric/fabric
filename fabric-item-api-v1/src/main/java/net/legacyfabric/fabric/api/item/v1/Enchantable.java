package net.legacyfabric.fabric.api.item.v1;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface Enchantable {
	int getEnchantability(ItemStack stack);

	static int enchantabilityOf(ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			return 0;
		}

		Item item = itemStack.getItem();

		if (item instanceof Enchantable) {
			return ((Enchantable) item).getEnchantability(itemStack);
		}

		return item.getEnchantability();
	}
}
