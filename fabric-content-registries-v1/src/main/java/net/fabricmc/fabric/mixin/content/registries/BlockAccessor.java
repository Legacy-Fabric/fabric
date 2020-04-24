package net.fabricmc.fabric.mixin.content.registries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.Block;
import net.minecraft.util.IdList;

@SuppressWarnings("rawtypes")
@Mixin(Block.class)
public interface BlockAccessor {
	@Accessor("BLOCK_STATES")
	static void setBlockStates(IdList list) {
		throw new AssertionError();
	}
}
