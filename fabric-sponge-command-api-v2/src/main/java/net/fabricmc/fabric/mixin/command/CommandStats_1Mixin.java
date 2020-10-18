package net.fabricmc.fabric.mixin.command;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.command.CommandStats;

import net.fabricmc.fabric.api.command.v2.PermissibleCommandSource;

@SuppressWarnings("ShadowTarget")
@Mixin(targets = "net/minecraft/command/CommandStats$1")
public abstract class CommandStats_1Mixin implements PermissibleCommandSource {
	@Final
	@Shadow
	CommandStats field_6958;

	@Override
	public boolean hasPermission(String perm) {
		return false;
	}
}
