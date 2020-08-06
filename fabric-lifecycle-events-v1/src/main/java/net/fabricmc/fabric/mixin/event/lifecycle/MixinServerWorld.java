package net.fabricmc.fabric.mixin.event.lifecycle;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.world.ServerWorld;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

@Mixin(ServerWorld.class)
public class MixinServerWorld {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;tick()V", shift = At.Shift.AFTER), method = "tick")
	public void startWorldTick(CallbackInfo ci) {
		ServerTickEvents.START_WORLD_TICK.invoker().onStartTick((ServerWorld) (Object) this);
	}

	@Inject(at = @At("TAIL"), method = "tick")
	public void endWorldTick(CallbackInfo ci) {
		ServerTickEvents.END_WORLD_TICK.invoker().onEndTick((ServerWorld) (Object) this);
	}
}
