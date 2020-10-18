package net.fabricmc.fabric.mixin.command;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.CommandBlockExecutor;

import net.fabricmc.fabric.api.command.v2.PermissibleCommandSource;

@Mixin(CommandBlockExecutor.class)
public abstract class CommandBlockExecutorMixin implements PermissibleCommandSource {
	@Override
	public boolean hasPermission(String perm) {
		return true;
	}
}
