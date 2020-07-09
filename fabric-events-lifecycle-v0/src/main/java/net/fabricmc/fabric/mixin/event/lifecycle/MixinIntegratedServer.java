package net.fabricmc.fabric.mixin.event.lifecycle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.LanServerPublishedCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.level.LevelInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(IntegratedServer.class)
public class MixinIntegratedServer  {
	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	@Final
	private LevelInfo levelInfo;

	@Inject(method="method_6437",at=@At("HEAD"))
	public void publishToLan(LevelInfo.GameMode gameMode, boolean cheats, CallbackInfoReturnable<String> info){
		LanServerPublishedCallback.EVENT.invoker().onServerPublished(this.client,gameMode,cheats,this.levelInfo);
	}
}
