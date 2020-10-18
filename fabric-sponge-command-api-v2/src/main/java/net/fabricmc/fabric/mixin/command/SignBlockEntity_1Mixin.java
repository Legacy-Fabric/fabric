package net.fabricmc.fabric.mixin.command;

import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.fabric.api.command.v2.PermissibleCommandSource;

@Mixin(targets = "net/minecraft/block/entity/SignBlockEntity$1")
public abstract class SignBlockEntity_1Mixin implements PermissibleCommandSource {
	@Override
	public boolean hasPermission(String perm) {
		return true;
	}
}
