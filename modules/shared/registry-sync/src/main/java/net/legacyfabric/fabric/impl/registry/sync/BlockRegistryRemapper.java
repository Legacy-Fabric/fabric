package net.legacyfabric.fabric.impl.registry.sync;

import net.legacyfabric.fabric.mixin.registry.sync.BlockAccessor;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.IdList;
import net.minecraft.util.registry.SimpleRegistry;

public class BlockRegistryRemapper extends RegistryRemapper {
	public BlockRegistryRemapper() {
		super(Block.REGISTRY);
	}

	@Override
	public void remap() {
		IdList oldList = getIdList(this.registry);
		super.remap();
		IdList newList = getIdList(this.registry);
		Map<Integer, Integer> old2new = new HashMap<>();
		for (Object value : oldList) {
			old2new.put(oldList.getId(value), newList.getId(value));
		}
		IdList<BlockState> oldStates = Block.BLOCK_STATES;
		IdList<BlockState> newStates = new IdList<>();
		for (BlockState blockState : oldStates) {
			int id = oldStates.getId(blockState);
			int blockId = id >> 4;
			int stateId = id & 0xF;
			int newBlockId = old2new.get(blockId);
			int newBlockStateId = newBlockId << 4 | stateId;
			newStates.set(blockState, newBlockStateId);
		}
		BlockAccessor.setBLOCK_STATES(newStates);
	}
}
