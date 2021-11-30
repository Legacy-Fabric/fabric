package net.legacyfabric.fabric.impl.base.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.realms.RealmsScreenProxy;
import net.minecraft.realms.RealmsBridge;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RealmsBridge.class)
public class RealmsBridgeMixin {

	@Shadow
	@Final
	private static Logger LOGGER;

	@Inject(method = "switchToRealms", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V"), cancellable = true)
	private void fabric_suppressRealms(Screen screen, CallbackInfo ci) {
		LOGGER.info("Suppressing Realms error.");
		ci.cancel();
	}

	@Inject(method = "getNotificationScreen", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V"), cancellable = true)
	private void fabric_suppressRealms2(Screen screen, CallbackInfoReturnable<RealmsScreenProxy> cir) {
		LOGGER.info("Suppressing Realms error.");
		cir.setReturnValue(null);
	}
}
