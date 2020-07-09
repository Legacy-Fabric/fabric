package net.fabricmc.fabric.mixin.event.lifecycle;

import net.fabricmc.fabric.api.event.world.ServerPlayerTickCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {
	@Inject(at=@At("TAIL"),method = "tick")
	public void tick(CallbackInfo ci){
		ServerPlayerTickCallback.EVENT.invoker().tick((PlayerEntity)(Object)this);
	}
}
