package net.legacyfabric.fabric.api.registry.v2;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.effect.StatusEffect;
import net.minecraft.item.Item;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;

public class VanillaRegistryKeys {
	public static final ResourceKey<Registry<Block>> BLOCK = net.ornithemc.osl.registries.api.registry.RegistryKeys.BLOCK;
	public static final ResourceKey<Registry<Item>> ITEM = net.ornithemc.osl.registries.api.registry.RegistryKeys.ITEM;
	public static final ResourceKey<Registry<StatusEffect>> STATUS_EFFECT = net.ornithemc.osl.registries.api.registry.RegistryKeys.from("status_effect");
	public static final ResourceKey<Registry<Enchantment>> ENCHANTMENT = net.ornithemc.osl.registries.api.registry.RegistryKeys.from("enchantment");
	public static final ResourceKey<Registry<Biome>> BIOME = net.ornithemc.osl.registries.api.registry.RegistryKeys.from("biome");
	public static final ResourceKey<Registry<Class<? extends Entity>>> ENTITY_TYPE = net.ornithemc.osl.registries.api.registry.RegistryKeys.from("entity_type");
	// Potion
}
