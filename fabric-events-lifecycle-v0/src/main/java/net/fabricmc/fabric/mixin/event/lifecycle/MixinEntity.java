package net.fabricmc.fabric.mixin.event.lifecycle;

import net.fabricmc.fabric.api.event.world.EntityKilledCallback;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {
	@Inject(at=@At("HEAD"),method="kill")
	public void entityKilled(CallbackInfo info){
		EntityKilledCallback.EVENT.invoker().entityKilled((Entity)(Object)this);
	}
}
