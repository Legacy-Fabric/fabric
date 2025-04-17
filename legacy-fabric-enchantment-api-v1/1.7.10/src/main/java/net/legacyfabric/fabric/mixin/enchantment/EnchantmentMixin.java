package net.legacyfabric.fabric.mixin.enchantment;

import com.google.common.collect.Lists;

import net.legacyfabric.fabric.api.registry.v2.RegistryHelper;

import net.legacyfabric.fabric.api.registry.v2.RegistryIds;

import net.legacyfabric.fabric.api.registry.v2.registry.holder.Registry;

import net.legacyfabric.fabric.impl.enchantment.versioned.EarlyInitializer;

import net.legacyfabric.fabric.impl.registry.wrapper.SyncedArrayRegistryWrapper;

import net.minecraft.enchantment.Enchantment;

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

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Mutable
	@Shadow
	@Final
	public static Enchantment[] field_5457;
	@Mutable
	@Shadow
	@Final
	public static Enchantment[] ALL_ENCHANTMENTS;

	@Unique
	private static Registry<Enchantment> ENCHANTMENT_REGISTRY;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void api$registerRegistry(CallbackInfo ci) {
		ENCHANTMENT_REGISTRY = new SyncedArrayRegistryWrapper<>(
				RegistryIds.ENCHANTMENTS,
				ALL_ENCHANTMENTS, EarlyInitializer.getVanillaIds(),
				universal -> universal,
				id -> id,
				ids -> {
					Enchantment[] array = new Enchantment[ids.fabric$size() + 1];
					Arrays.fill(array, null);

					for (Enchantment enchantment : ids) {
						int id = ids.fabric$getId(enchantment);

						if (id >= array.length - 1) {
							Enchantment[] newArray = new Enchantment[array.length + 2];
							Arrays.fill(newArray, null);
							System.arraycopy(array, 0, newArray, 0, array.length);
							array = newArray;
						}

						array[id] = enchantment;
					}

					ALL_ENCHANTMENTS = array;

					List<Enchantment> list = Lists.<Enchantment>newArrayList();

					for (Enchantment enchantment : ALL_ENCHANTMENTS) {
						if (enchantment != null) {
							list.add(enchantment);
						}
					}

					field_5457 = list.toArray(new Enchantment[list.size()]);
				}
		);

		RegistryHelper.addRegistry(RegistryIds.ENCHANTMENTS, ENCHANTMENT_REGISTRY);
	}
}
