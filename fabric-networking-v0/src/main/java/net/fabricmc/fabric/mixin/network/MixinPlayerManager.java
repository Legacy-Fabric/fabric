package net.fabricmc.fabric.mixin.network;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.impl.network.PacketRegistryImpl;
import net.fabricmc.fabric.impl.network.ServerSidePacketRegistryImpl;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(priority = 500, value = PlayerManager.class)
public class MixinPlayerManager {
	@Inject(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/DifficultyS2CPacket;<init>(Lnet/minecraft/world/Difficulty;Z)V"))
	public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo info) {
		Optional<Packet<?>> optionalPacket = PacketRegistryImpl.createInitialRegisterPacket(ServerSidePacketRegistry.INSTANCE);

		if (optionalPacket.isPresent()) {
			player.networkHandler.sendPacket(optionalPacket.get());
			((ServerSidePacketRegistryImpl) ServerSidePacketRegistry.INSTANCE).addNetworkHandler(player.networkHandler);
		}
	}
}
