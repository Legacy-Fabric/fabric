package net.legacyfabric.fabric.impl.registry.sync;

import net.minecraft.nbt.CompoundTag;

public interface RegistryRemapperAccess {
	RegistryRemapper getItemRemapper();

	RegistryRemapper getBlockRemapper();

	default void remap() {
		this.getItemRemapper().remap();
		this.getBlockRemapper().remap();
	}

	default void readAndRemap(CompoundTag tag) {
		this.getItemRemapper().fromTag(tag.getCompound("Items"));
		this.getBlockRemapper().fromTag(tag.getCompound("Blocks"));
		this.remap();
	}

	default CompoundTag toRegistryTag() {
		CompoundTag tag = new CompoundTag();
		tag.put("Items", this.getItemRemapper().toTag());
		tag.put("Blocks", this.getBlockRemapper().toTag());
		return tag;
	}
}
