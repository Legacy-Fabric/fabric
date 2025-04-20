package net.legacyfabric.fabric.mixin.biome;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;

import net.legacyfabric.fabric.api.registry.v2.registry.holder.Registry;

import net.legacyfabric.fabric.impl.biome.EarlyInitializer;
import net.legacyfabric.fabric.impl.registry.wrapper.SyncedArrayRegistryWrapper;

import net.minecraft.world.biome.Biome;

import net.minecraft.world.biome.TaigaBiome;
import net.minecraft.world.biome.class_1742;

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
	@Shadow
	@Final
	public static Biome MEGA_TAIGA_HILLS;
	@Shadow
	@Final
	public static Biome MEGA_TAIGA;
	@Unique
	private static Registry<Biome> REGISTRY;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void api$registerRegistry(CallbackInfo ci) {
		BIOMES[MEGA_TAIGA_HILLS.id + 128] = new TaigaBiome(MEGA_TAIGA_HILLS.id + 128, 2)
				.seedModifier(5858897, true)
				.setName("Mega Spruce Taiga")
				.method_3820(5159473)
				.setTempratureAndDownfall(0.25F, 0.8F)
				.method_6422(new class_1742(MEGA_TAIGA.depth, MEGA_TAIGA.variationModifier));

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
