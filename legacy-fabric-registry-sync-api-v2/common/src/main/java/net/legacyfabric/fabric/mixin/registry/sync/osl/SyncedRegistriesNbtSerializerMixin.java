package net.legacyfabric.fabric.mixin.registry.sync.osl;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.legacyfabric.fabric.impl.registry.RegistryHelperImplementation;

import net.minecraft.nbt.NbtCompound;

import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingSource;
import net.ornithemc.osl.registries.impl.registry.sync.SyncedRegistriesNbtSerializer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SyncedRegistriesNbtSerializer.class)
public class SyncedRegistriesNbtSerializerMixin {
	@WrapMethod(method = "deserialize(Lnet/minecraft/nbt/NbtCompound;Lnet/ornithemc/osl/registries/impl/registry/sync/RegistryMappingSource;)V")
	private static void convertLFMappings(NbtCompound nbt, RegistryMappingSource source, Operation<Void> original) {
		if (!nbt.contains("format")) {
			NbtCompound registeries = new NbtCompound();

			for (String key : nbt.getKeys()) {
				NbtCompound content = nbt.getCompound(key);

				registeries.put(RegistryHelperImplementation.convertRegistryId(key), content);
			}

			NbtCompound main = new NbtCompound();
			main.putInt("format", 1);
			main.put("registries", registeries);
			nbt = main;
		}

		original.call(nbt, source);
	}
}
