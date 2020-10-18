package net.fabricmc.fabric.mixin.command;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.player.PlayerEntity;

import net.fabricmc.fabric.api.command.v2.PermissibleCommandSource;

@Mixin(targets = "net/minecraft/block/entity/SignBlockEntity$2")
public abstract class SignBlockEntity_2Mixin implements PermissibleCommandSource {
	@SuppressWarnings("ShadowTarget")
	@Final
	@Shadow
	PlayerEntity field_1466;

	@Override
	public boolean hasPermission(String perm) {
		return ((PermissibleCommandSource) this.field_1466).hasPermission(perm);
	}
}
