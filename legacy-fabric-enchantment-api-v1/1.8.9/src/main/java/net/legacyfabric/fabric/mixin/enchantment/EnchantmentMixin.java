package net.legacyfabric.fabric.mixin.enchantment;

import com.google.common.collect.BiMap;

import com.google.common.collect.HashBiMap;

import com.google.common.collect.Lists;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;

import net.legacyfabric.fabric.api.registry.v2.registry.holder.FabricRegistry;

import net.legacyfabric.fabric.impl.registry.wrapper.SyncedArrayMapFabricRegistryWrapper;

import net.minecraft.enchantment.Enchantment;

import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Mutable
	@Shadow
	@Final
	private static Map<Identifier, Enchantment> ENCHANTMENT_MAP;
	@Mutable
	@Shadow
	@Final
	private static Enchantment[] ENCHANTMENTS;
	@Mutable
	@Shadow
	@Final
	public static Enchantment[] ALL_ENCHANTMENTS;
	@Unique
	private static FabricRegistry<Enchantment> ENCHANTMENT_REGISTRY;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void api$registerRegistry(CallbackInfo ci) {
		BiMap<Identifier, Enchantment> map = HashBiMap.create(ENCHANTMENT_MAP);
		ENCHANTMENT_MAP = map;

		ENCHANTMENT_REGISTRY = new SyncedArrayMapFabricRegistryWrapper<>(
				RegistryIds.ENCHANTMENTS,
				ENCHANTMENTS, map, false,
				universal -> new Identifier(universal.toString()),
				net.legacyfabric.fabric.api.util.Identifier::new,
				ids -> {
					Enchantment[] array = new Enchantment[ids.fabric$size() + 1];
					Arrays.fill(array, null);

					for (Enchantment enchantment : ids) {
						int id = ids.fabric$getId(enchantment);

						if (id >= array.length - 1) {
							Enchantment[] newArray = new Enchantment[id + 2];
							Arrays.fill(newArray, null);
							System.arraycopy(array, 0, newArray, 0, array.length);
							array = newArray;
						}

						array[id] = enchantment;
					}

					ENCHANTMENTS = array;

					List<Enchantment> list = Lists.<Enchantment>newArrayList();

					for (Enchantment enchantment : ENCHANTMENTS) {
						if (enchantment != null) {
							list.add(enchantment);
						}
					}

					ALL_ENCHANTMENTS = list.toArray(new Enchantment[list.size()]);
				}
		);

		RegistryHelper.addRegistry(RegistryIds.ENCHANTMENTS, ENCHANTMENT_REGISTRY);
	}
}
