package net.legacyfabric.fabric.mixin.effect;

import net.legacyfabric.fabric.api.registry.v2.VanillaRegistryKeys;

import net.minecraft.entity.living.effect.PotionHelper;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.IntegerMapMapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(PotionHelper.class)
public class PotionHelperMixin {
	@Shadow
	@Final
	private static Map<Integer, String> DURATION_RECIPES;

	@Shadow
	@Final
	private static Map<Integer, String> AMPLIFIER_RECIPES;

	@Shadow
	@Final
	private static Map<Integer, Integer> COLOR_CACHE;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void lf$registerRemappers(CallbackInfo ci) {
		SyncedRegistries.registerMapper(VanillaRegistryKeys.STATUS_EFFECT, NamespacedIdentifiers.from("potion/duration_recipes"), IntegerMapMapper.of(DURATION_RECIPES));
		SyncedRegistries.registerMapper(VanillaRegistryKeys.STATUS_EFFECT, NamespacedIdentifiers.from("potion/amplifier_recipes"), IntegerMapMapper.of(AMPLIFIER_RECIPES));
		SyncedRegistries.registerMapper(VanillaRegistryKeys.STATUS_EFFECT, NamespacedIdentifiers.from("potion/color_cache"), IntegerMapMapper.of(COLOR_CACHE));
	}
}
