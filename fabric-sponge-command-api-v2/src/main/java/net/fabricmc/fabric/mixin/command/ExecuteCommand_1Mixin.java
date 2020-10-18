package net.fabricmc.fabric.mixin.command;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.Entity;

import net.fabricmc.fabric.api.command.v2.PermissibleCommandSource;

@Mixin(targets = "net/minecraft/server/command/ExecuteCommand$1")
public abstract class ExecuteCommand_1Mixin implements PermissibleCommandSource {
	@Shadow
	public abstract Entity getEntity();

	@Override
	public boolean hasPermission(String perm) {
		return ((PermissibleCommandSource) this.getEntity()).hasPermission(perm);
	}
}
