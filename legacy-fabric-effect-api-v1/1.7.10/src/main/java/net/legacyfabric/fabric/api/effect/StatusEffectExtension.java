package net.legacyfabric.fabric.api.effect;

import net.ornithemc.osl.registries.api.registry.Registry;

import net.minecraft.entity.living.effect.StatusEffect;

import net.legacyfabric.fabric.impl.effect.versioned.StatusEffectRegistryImpl;

public interface StatusEffectExtension {
	Registry<StatusEffect> STATUS_EFFECT_REGISTRY = StatusEffectRegistryImpl.REGISTRY;
	int REGISTRY_AUTO_ASSIGN_ID = -172;
}
