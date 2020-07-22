package net.fabricmc.fabric.impl.content.registries;

import java.util.Map;
import java.util.function.BiConsumer;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.content.registry.v1.FabricToolItem;
import net.fabricmc.fabric.api.content.registry.v1.ToolMaterial;

public class FabricPickaxeItem extends FabricToolItem {
	private static boolean initialized = false;
	public static final Map<Block, Integer> BLOCKS_BY_MINING_LEVEL = Maps.newHashMap();
	public static final BiConsumer<Block, Integer> MAP_ADDER = BLOCKS_BY_MINING_LEVEL::putIfAbsent;
	public static final BiConsumer<Block, Integer> MAP_REPLACER = BLOCKS_BY_MINING_LEVEL::replace;

	public FabricPickaxeItem(float attackDamage, ToolMaterial material) {
		super(attackDamage, material);

		if(!initialized) {
			initialized = true;
			MAP_REPLACER.accept(Blocks.OBSIDIAN, 3);
			MAP_REPLACER.accept(Blocks.DIAMOND_BLOCK, 2);
			MAP_REPLACER.accept(Blocks.DIAMOND_ORE, 2);
			MAP_REPLACER.accept(Blocks.EMERALD_BLOCK, 2);
			MAP_REPLACER.accept(Blocks.EMERALD_ORE, 2);
			MAP_REPLACER.accept(Blocks.GOLD_BLOCK, 2);
			MAP_REPLACER.accept(Blocks.GOLD_ORE, 2);
			MAP_REPLACER.accept(Blocks.LIT_REDSTONE_ORE, 2);
			MAP_REPLACER.accept(Blocks.REDSTONE_ORE, 2);
			MAP_REPLACER.accept(Blocks.IRON_BLOCK, 1);
			MAP_REPLACER.accept(Blocks.IRON_ORE, 1);
			MAP_ADDER.accept(Blocks.ICE, 0);
			MAP_ADDER.accept(Blocks.ACTIVATOR_RAIL, 0);
			MAP_ADDER.accept(Blocks.DETECTOR_RAIL, 0);
			MAP_ADDER.accept(Blocks.RAIL, 0);
			MAP_ADDER.accept(Blocks.POWERED_RAIL, 0);
			MAP_ADDER.accept(Blocks.PACKED_ICE, 0);
		}
	}

	@Override
	public boolean isEffectiveOn(Block block) {
		if(BLOCKS_BY_MINING_LEVEL.containsKey(block)) {
			return this.material.getMiningLevel() >= BLOCKS_BY_MINING_LEVEL.get(block);
		}
		return false;
	}

	@Override
	public float getMiningSpeedForBlock(ItemStack stack, Block block) {
		return BLOCKS_BY_MINING_LEVEL.containsKey(block) ? this.miningSpeed : 1.0F;
	}
}
