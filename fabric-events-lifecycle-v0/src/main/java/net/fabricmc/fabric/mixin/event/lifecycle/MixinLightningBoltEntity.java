package net.fabricmc.fabric.mixin.event.lifecycle;

import net.fabricmc.fabric.api.event.world.LightningStruckCallback;
import net.minecraft.entity.LightningBoltEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningBoltEntity.class)
public class MixinLightningBoltEntity {
	@Inject(at = @At("HEAD"),method = "<init>")
	public void onLightningStrike(World world, double d, double e, double f,CallbackInfo info){
		LightningStruckCallback.EVENT.invoker().onLightningStrike(world,d,e,f);
	}
}
