package net.legacyfabric.fabric.mixin.enchantment;

import net.minecraft.enchantment.Enchantment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Enchantment.class)
public interface EnchantmentAccessor {
	@Accessor
	void setId(int id);
}
