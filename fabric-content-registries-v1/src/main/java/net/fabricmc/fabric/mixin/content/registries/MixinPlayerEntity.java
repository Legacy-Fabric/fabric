package net.fabricmc.fabric.mixin.content.registries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.content.registry.v1.item.UseTickable;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {
	@Shadow
	public PlayerInventory inventory;

	@Shadow
	private ItemStack field_8356;

	@Shadow
	private int field_8357;

	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/PlayerInventory;getMainHandStack()Lnet/minecraft/item/ItemStack;"), method = "tick")
	public void useTick(CallbackInfo ci) {
		if (this.inventory.getMainHandStack() == this.field_8356 && this.field_8356.getItem() instanceof UseTickable  && this.field_8357 > 0) {
			((UseTickable) this.field_8356.getItem()).useTick(this.field_8356, (PlayerEntity) (Object) this, this.field_8357);
		}
	}
}
