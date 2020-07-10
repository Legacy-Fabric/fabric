package net.fabricmc.fabric.mixin.event.lifecycle;

import net.fabricmc.fabric.api.event.server.PlayerDisconnectCallback;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {

	@Shadow
	@Final
	public ClientConnection connection;

	@Shadow
	public ServerPlayerEntity player;

	@Shadow
	@Final
	private MinecraftServer server;

	@Inject(at = @At("RETURN"),method = "disconnect")
	public void onPlayerDisconnect(String reason, CallbackInfo info){
		PlayerDisconnectCallback.EVENT.invoker().playerDisconnect(this.connection,this.player,this.server);
	}
}
