package net.legacyfabric.fabric.mixin.biome;

import net.minecraft.Bootstrap;

import net.minecraft.world.biome.Biome;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bootstrap.class)
public class BootstrapMixin {
	@Inject(method = "initialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/Bootstrap;setupDispenserBehavior()V"))
	private static void initializeBiomeRegistry(CallbackInfo ci) {
		try {
			Class.forName(Biome.class.getName());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
