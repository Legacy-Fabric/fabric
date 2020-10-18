package net.fabricmc.fabric.mixin.command;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.command.Console;

import net.fabricmc.fabric.api.command.v2.PermissibleCommandSource;

@Mixin(Console.class)
public abstract class ConsoleMixin implements PermissibleCommandSource {
	@Override
	public boolean hasPermission(String perm) {
		return true;
	}
}
