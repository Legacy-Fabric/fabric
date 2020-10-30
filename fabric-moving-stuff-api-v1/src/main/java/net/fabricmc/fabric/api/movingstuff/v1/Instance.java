package net.fabricmc.fabric.api.movingstuff.v1;

import net.minecraft.nbt.CompoundTag;

public interface Instance<T> {
	T get();

	int getAmount();

	CompoundTag getTag();
}
