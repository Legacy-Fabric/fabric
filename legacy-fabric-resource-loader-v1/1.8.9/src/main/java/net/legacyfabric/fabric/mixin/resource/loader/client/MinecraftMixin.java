package net.legacyfabric.fabric.mixin.resource.loader.client;

import net.legacyfabric.fabric.impl.resource.loader.ItemModelRegistryImpl;

import net.minecraft.client.Minecraft;

import net.minecraft.client.render.entity.ItemRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Shadow
	private ItemRenderer itemRenderer;

	@Inject(method = "init", at = @At("TAIL"))
	public void addItemModels(CallbackInfo ci) {
		((ItemModelRegistryImpl.Registrar) this.itemRenderer).fabric_register();
	}
}
