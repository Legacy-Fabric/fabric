package net.legacyfabric.fabric.test.mixin.biome;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;
import net.legacyfabric.fabric.api.registry.v2.RegistryIds;

import net.legacyfabric.fabric.api.registry.v2.registry.holder.SyncedFabricRegistry;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.CustomizedWorldProperties;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CustomizedWorldProperties.Serializer.class)
public class CustomizedWorldPropertiesSerializerMixin {
	@ModifyConstant(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/world/gen/CustomizedWorldProperties$Builder;",
			constant = @Constant(intValue = 38))
	private int fixBiomeSelector(int max) {
		SyncedFabricRegistry<Biome> registry = (SyncedFabricRegistry<Biome>) (Object) RegistryHelper.getRegistry(RegistryIds.BIOMES);
		return registry.stream().mapToInt(b -> registry.fabric$getRawId(b)).max().orElse(max) - 1;
	}
}
