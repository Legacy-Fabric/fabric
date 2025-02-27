package net.legacyfabric.fabric.mixin.effect;

import net.legacyfabric.fabric.impl.effect.versioned.PotionItemRemapper;

import net.minecraft.item.PotionItem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionItem.class)
public class PotionItemMixin {
	@Inject(method = "<init>", at = @At("RETURN"))
	private void api$trackPotionItems(CallbackInfo ci) {
		PotionItemRemapper.POTION_ITEMS.add((PotionItem) (Object) this);
	}
}
