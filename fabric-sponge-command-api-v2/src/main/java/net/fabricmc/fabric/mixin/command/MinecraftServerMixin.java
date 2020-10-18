package net.fabricmc.fabric.mixin.command;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.MinecraftServer;

import net.fabricmc.fabric.api.command.v2.PermissibleCommandSource;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements PermissibleCommandSource {
	@Override
	public boolean hasPermission(String perm) {
		return true;
	}
}
