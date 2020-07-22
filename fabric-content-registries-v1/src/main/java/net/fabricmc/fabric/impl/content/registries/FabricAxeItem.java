package net.fabricmc.fabric.impl.content.registries;

import java.util.Map;
import java.util.function.BiConsumer;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;

import net.fabricmc.fabric.api.content.registry.v1.FabricToolItem;
import net.fabricmc.fabric.api.content.registry.v1.ToolMaterial;

public class FabricAxeItem extends FabricToolItem {
	public static final Map<Block, Integer> BLOCKS_BY_MINING_LEVEL = Maps.newHashMap();
	public static final BiConsumer<Block, Integer> MAP_ADDER = BLOCKS_BY_MINING_LEVEL::putIfAbsent;
	public static final BiConsumer<Block, Integer> MAP_REPLACER = BLOCKS_BY_MINING_LEVEL::replace;

	public FabricAxeItem(float attackDamage, ToolMaterial material) {
		super(attackDamage, material);
	}

	@Override
	public boolean isEffectiveOn(Block block) {
		if(BLOCKS_BY_MINING_LEVEL.containsKey(block)){
			return this.material.getMiningLevel() >= BLOCKS_BY_MINING_LEVEL.get(block);
		}
		return false;
	}
}
