package net.legacyfabric.fabric.mixin.registry.sync;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.collection.IdList;

@Mixin(Block.class)
public interface BlockAccessor {
	@Mutable
	@Accessor
	static void setBLOCK_STATES(IdList<BlockState> BLOCK_STATES) {
		throw new UnsupportedOperationException();
	}
}
