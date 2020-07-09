package net.fabricmc.fabric.mixin.event.lifecycle;

import net.fabricmc.fabric.api.event.world.EntityHurtCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageTracker.class)
public class MixinDamageTracker {
	@Shadow
	@Final
	private LivingEntity field_7239;

	@Inject(at=@At("HEAD"),method="onDamage")
	public void onDamage(DamageSource damageSource, float originalHealth, float damage, CallbackInfo info){
		EntityHurtCallback.EVENT.invoker().chunksSaved(this.field_7239,damageSource,originalHealth,damage);
	}
}
