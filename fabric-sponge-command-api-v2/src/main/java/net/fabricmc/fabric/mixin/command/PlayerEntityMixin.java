package net.fabricmc.fabric.mixin.command;

import com.mojang.authlib.GameProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.player.PlayerEntity;

import net.fabricmc.fabric.api.command.v2.PermissibleCommandSource;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PermissibleCommandSource {
	@Shadow
	public abstract GameProfile getGameProfile();

	// TODO
	@Override
	public boolean hasPermission(String perm) {
		return false;
	}
}
