package net.legacyfabric.fabric.mixin.enchantment;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;

import net.minecraft.enchantment.Enchantment;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Shadow
	@Final
	public static SimpleRegistry<Identifier, Enchantment> REGISTRY;

	@Inject(method = "register", at = @At("RETURN"))
	private static void api$registerRegistry(CallbackInfo ci) {
		RegistryHelper.addRegistry(RegistryIds.ENCHANTMENTS, REGISTRY);
	}
}
