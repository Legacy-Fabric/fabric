package net.legacyfabric.fabric.api.effect;

import net.legacyfabric.fabric.impl.effect.versioned.StatusEffectRegistryImpl;

import net.minecraft.entity.living.effect.StatusEffect;

import net.ornithemc.osl.registries.api.registry.Registry;

public interface StatusEffectExtension {
	Registry<StatusEffect> STATUS_EFFECT_REGISTRY = StatusEffectRegistryImpl.REGISTRY;
	int REGISTRY_AUTO_ASSIGN_ID = -172;
}
