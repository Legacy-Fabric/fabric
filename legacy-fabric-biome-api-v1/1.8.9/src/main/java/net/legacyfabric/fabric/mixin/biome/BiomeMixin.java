package net.legacyfabric.fabric.mixin.biome;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;

import net.legacyfabric.fabric.api.registry.v2.registry.holder.Registry;

import net.legacyfabric.fabric.impl.biome.EarlyInitializer;
import net.legacyfabric.fabric.impl.registry.wrapper.SyncedArrayRegistryWrapper;

import net.minecraft.world.biome.Biome;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(Biome.class)
public class BiomeMixin {
	@Mutable
	@Shadow
	@Final
	private static Biome[] BIOMES;
	@Unique
	private static Registry<Biome> REGISTRY;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void api$registerRegistry(CallbackInfo ci) {
		REGISTRY = new SyncedArrayRegistryWrapper<>(
				RegistryIds.BIOMES,
				BIOMES, EarlyInitializer.getVanillaIds(),
				universal -> universal,
				id -> id,
				ids -> {
					Biome[] array = new Biome[ids.fabric$size() + 1];
					Arrays.fill(array, null);

					for (Biome enchantment : ids) {
						int id = ids.fabric$getId(enchantment);

						if (id >= array.length - 1) {
							Biome[] newArray = new Biome[id + 2];
							Arrays.fill(newArray, null);
							System.arraycopy(array, 0, newArray, 0, array.length);
							array = newArray;
						}

						array[id] = enchantment;
					}

					BIOMES = array;
				}
		);

		RegistryHelper.addRegistry(RegistryIds.BIOMES, REGISTRY);
	}
}
