package net.legacyfabric.fabric.mixin.item.versioned;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.api.resource.ItemModelRegistry;

import net.minecraft.item.Item;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.items.impl.ItemRegistryImpl;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ItemRegistryImpl.class)
public class ItemRegistryImplMixin {
	@Inject(method = "register(Lnet/ornithemc/osl/core/api/util/NamespacedIdentifier;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", at = @At("RETURN"))
	private static <T extends Item> void lf$addDefaultModel(NamespacedIdentifier identifier, T item, CallbackInfoReturnable<T> cir) {
		if (identifier.namespace() == null || identifier.namespace().equals("minecraft") || item.hasCustomData()) return;

		ItemModelRegistry.registerItemModel(item, identifier);
	}

	@Inject(method = "register(Lnet/ornithemc/osl/registries/api/registry/ResourceKey;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", at = @At("RETURN"))
	private static <T extends Item> void lf$addDefaultModel(ResourceKey<Item> key, T item, CallbackInfoReturnable<T> cir) {
		NamespacedIdentifier identifier = key.identifier();
		if (identifier.namespace() == null || identifier.namespace().equals("minecraft") || item.hasCustomData()) return;

		ItemModelRegistry.registerItemModel(item, identifier);
	}

	@Inject(method = "register(ILnet/ornithemc/osl/core/api/util/NamespacedIdentifier;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", at = @At("RETURN"))
	private static <T extends Item> void lf$addDefaultModel(int id, NamespacedIdentifier key, T item, CallbackInfoReturnable<T> cir) {
		if (key.namespace() == null || key.namespace().equals("minecraft") || item.hasCustomData()) return;

		ItemModelRegistry.registerItemModel(item, key);
	}
}
