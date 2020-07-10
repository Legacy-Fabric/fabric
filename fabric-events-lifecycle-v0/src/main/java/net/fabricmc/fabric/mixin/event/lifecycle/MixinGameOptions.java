package net.fabricmc.fabric.mixin.event.lifecycle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.GameOptionsSavedCallback;
import net.minecraft.client.options.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameOptions.class)
public class MixinGameOptions {
	@Inject(at = @At(value = "INVOKE",target = "Ljava/io/PrintWriter;close()V"),method = "save")
	public void save(CallbackInfo ci){
		GameOptionsSavedCallback.EVENT.invoker().onGameOptionsSaved((GameOptions)(Object)this);
	}
}
