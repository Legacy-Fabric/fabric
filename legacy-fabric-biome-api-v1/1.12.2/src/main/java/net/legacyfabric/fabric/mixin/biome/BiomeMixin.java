package net.legacyfabric.fabric.mixin.biome;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Biome.class)
public class BiomeMixin {
	@Shadow
	@Final
	public static SimpleRegistry<Identifier, Biome> REGISTRY;

	@Inject(method = "register()V", at = @At("RETURN"))
	private static void api$registerRegistry(CallbackInfo ci) {
		RegistryHelper.addRegistry(RegistryIds.BIOMES, REGISTRY);
	}
}
