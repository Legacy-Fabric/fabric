package net.fabricmc.fabric.mixin.command;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.Entity;

import net.fabricmc.fabric.api.command.v2.PermissibleCommandSource;

@Mixin(Entity.class)
public abstract class EntityMixin implements PermissibleCommandSource {
	@Override
	public boolean hasPermission(String perm) {
		return false;
	}
}
