package net.fabricmc.fabric.mixin.gamerule;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.GameRules;

import net.fabricmc.fabric.api.gamerule.v1.GameRulesInitializedCallback;

@Mixin(GameRules.class)
public class MixinGameRules {
	@Inject(at = @At("RETURN"), method = "<init>")
	public void registerModdedGamerules(CallbackInfo ci) {
		GameRulesInitializedCallback.EVENT.invoker().onGamerulesRegistered((GameRules) (Object) this);
	}
}
