package net.fabricmc.fabric.mixin.armor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.item.ArmorItem;

@Mixin(ArmorItem.class)
public interface MixinArmorItem {
	@Accessor("protection")
	void setProtection(int protection);
}
