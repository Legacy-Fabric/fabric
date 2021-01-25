package net.legacyfabric.fabric.mixin.item;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.legacyfabric.fabric.api.item.v1.Enchantable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Redirect(method = "calculateEnchantmentPower", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/Item;getEnchantability()I"))
	private static int redirectCalculateEnchantmentPower(Item item, Random random, int num, int enchantmentPower, ItemStack stack) {
		return Enchantable.enchantabilityOf(stack);
	}

	@Redirect(method = "getEnchantments", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/Item;getEnchantability()I"))
	private static int redirectGetEnchantments(Item item, Random random, ItemStack stack, int level, boolean hasTreasure) {
		return Enchantable.enchantabilityOf(stack);
	}
}
