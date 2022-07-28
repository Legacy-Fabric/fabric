package net.legacyfabric.fabric.mixin.registry.sync;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;

import net.legacyfabric.fabric.api.registry.v1.RegistryIds;
import net.legacyfabric.fabric.impl.registry.RegistryHelperImpl;
import net.legacyfabric.fabric.impl.registry.sync.remappers.RegistryRemapper;
import net.legacyfabric.fabric.impl.registry.util.OldRemappedRegistry;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyArg(method = "getTranslationKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/CommonI18n;translate(Ljava/lang/String;)Ljava/lang/String;"))
	private String remapTranslationKey(String key) {
		String newKey = key;

		RegistryRemapper<Class<? extends Entity>> remapper = RegistryHelperImpl.getRegistryRemapper(RegistryIds.ENTITY_TYPES);
		OldRemappedRegistry<String, Class<? extends Entity>> registry = (OldRemappedRegistry<String, Class<? extends Entity>>) remapper.getRegistry();

		if (key.contains("minecraft:")) {
			String entityId = key.replace("entity.", "").replace(".name", "");

			newKey = "entity." + registry.getOldKey(entityId) + ".name";
		} else if (key.contains(":")) {
			newKey = key.replace(":", ".");
		}

		return newKey;
	}
}
