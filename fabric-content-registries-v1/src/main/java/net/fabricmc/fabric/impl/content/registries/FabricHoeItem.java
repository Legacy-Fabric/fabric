/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.impl.content.registries;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.DirtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.content.registry.v1.ToolMaterial;

public class FabricHoeItem extends Item {
	private ToolMaterial material;

	public FabricHoeItem(ToolMaterial material) {
		this(material.getDurability());
		this.material = material;
	}

	public FabricHoeItem(int maxDamage) {
		this.maxCount = 1;
		this.setMaxDamage(maxDamage);
		this.setItemGroup(ItemGroup.TOOLS);
	}

	public boolean use(ItemStack itemStack, PlayerEntity player, World world, BlockPos pos, Direction direction, float f, float g, float h) {
		if (player.method_7981(pos.offset(direction), direction, itemStack)) {
			BlockState blockState = world.getBlockState(pos);
			Block block = blockState.getBlock();

			if (direction != Direction.DOWN && world.getBlockState(pos.up()).getBlock().getMaterial() == Material.AIR) {
				if (block == Blocks.GRASS) {
					return this.setBlockState(itemStack, player, world, pos, Blocks.FARMLAND.getDefaultState());
				}

				if (block == Blocks.DIRT) {
					switch (blockState.get(DirtBlock.VARIANT)) {
					case DIRT:
						return this.setBlockState(itemStack, player, world, pos, Blocks.FARMLAND.getDefaultState());
					case COARSE_DIRT:
						return this.setBlockState(itemStack, player, world, pos, Blocks.DIRT.getDefaultState().with(DirtBlock.VARIANT, DirtBlock.DirtType.DIRT));
					}
				}
			}
		}

		return false;
	}

	protected boolean setBlockState(ItemStack itemStack, PlayerEntity playerEntity, World world, BlockPos blockPos, BlockState blockState) {
		world.method_265((float) blockPos.getX() + 0.5F, (float) blockPos.getY() + 0.5F, (float) blockPos.getZ() + 0.5F, blockState.getBlock().sound.getStepSound(), (blockState.getBlock().sound.getVolume() + 1.0F) / 2.0F, blockState.getBlock().sound.getPitch() * 0.8F);

		if (!world.isClient) {
			world.setBlockState(blockPos, blockState);
			itemStack.method_8335(1, playerEntity);
		}

		return true;
	}

	@Environment(EnvType.CLIENT)
	public boolean isHandheld() {
		return true;
	}

	public String getMaterialAsString() {
		return String.valueOf(this.material);
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		if (this.material == null) {
			return false;
		}

		return this.material.getRepairIngredient().get().getItem() == ingredient.getItem();
	}
}
