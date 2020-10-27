package net.fabricmc.fabric.mixin.command;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntityMixin {
	@Override
	public boolean hasPermission(String perm) {
		return MinecraftServer.getServer().getPlayerManager().isOperator(this.getGameProfile()) || super.hasPermission(perm);
	}
}
