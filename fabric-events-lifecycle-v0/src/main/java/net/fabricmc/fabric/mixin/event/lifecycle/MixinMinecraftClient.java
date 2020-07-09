package net.fabricmc.fabric.mixin.event.lifecycle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
	@Inject(at = @At("RETURN"), method = "tick")
	public void tick(CallbackInfo info) {
		ClientTickCallback.EVENT.invoker().tick((MinecraftClient) (Object) this);
	}
}
