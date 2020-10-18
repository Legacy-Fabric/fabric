package net.fabricmc.fabric.mixin.command;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.player.PlayerEntity;

import net.fabricmc.fabric.api.command.v2.PermissibleCommandSource;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PermissibleCommandSource {
	// TODO
	@Override
	public boolean hasPermission(String perm) {
		return false;
	}
}
