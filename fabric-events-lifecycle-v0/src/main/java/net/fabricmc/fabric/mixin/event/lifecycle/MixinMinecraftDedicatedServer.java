package net.fabricmc.fabric.mixin.event.lifecycle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.server.DedicatedServerSetupCallback;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.SERVER)
@Mixin(MinecraftDedicatedServer.class)
public class MixinMinecraftDedicatedServer {
	@Inject(at=@At("TAIL"),method="setupServer")
	public void setupServer(CallbackInfoReturnable<Boolean> info){
		DedicatedServerSetupCallback.EVENT.invoker().onServerSetup((MinecraftDedicatedServer) (Object)this);
	}
}
