package net.legacyfabric.fabric.test.mixin.biome;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.gui.screen.CustomizeWorldScreen;
import net.minecraft.world.biome.Biome;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;
import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedFabricRegistry;

@Mixin(CustomizeWorldScreen.class)
public class CustomizeWorldScreenMixin {
	@ModifyArg(method = "initPages",
			at = @At(value = "INVOKE", ordinal = 4, target = "Lnet/minecraft/class_2307;<init>(ILjava/lang/String;ZLnet/minecraft/class_2296;FFF)V"),
			index = 5
	)
	private float allowSelectingAllBiomesInSelector(float max) {
		System.out.println(Biome.getBiomes().length);
		SyncedFabricRegistry<Biome> registry = (SyncedFabricRegistry<Biome>) (Object) RegistryHelper.getRegistry(RegistryIds.BIOMES);
		return registry.stream().mapToInt(b -> registry.fabric$getRawId(b)).max().orElse((int) max) - 1;
	}
}
