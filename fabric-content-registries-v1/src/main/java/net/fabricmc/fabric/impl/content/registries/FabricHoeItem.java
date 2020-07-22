package net.fabricmc.fabric.impl.content.registries;

import net.minecraft.block.*;
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
		this(material.getMaxDurability());
		this.material = material;
	}

	public FabricHoeItem(int maxDamage) {
		this.maxCount = 1;
		this.setMaxDamage(maxDamage);
		this.setItemGroup(ItemGroup.TOOLS);
	}

	public boolean use(ItemStack itemStack, PlayerEntity player, World world, BlockPos pos, Direction direction, float f, float g, float h) {
		if (!player.method_7981(pos.offset(direction), direction, itemStack)) {
			return false;
		} else {
			BlockState blockState = world.getBlockState(pos);
			Block block = blockState.getBlock();
			if (direction != Direction.DOWN && world.getBlockState(pos.up()).getBlock().getMaterial() == Material.AIR) {
				if (block == Blocks.GRASS) {
					return this.setBlockState(itemStack, player, world, pos, Blocks.FARMLAND.getDefaultState());
				}

				if (block == Blocks.DIRT) {
					switch(blockState.get(DirtBlock.VARIANT)) {
						case DIRT:
							return this.setBlockState(itemStack, player, world, pos, Blocks.FARMLAND.getDefaultState());
						case COARSE_DIRT:
							return this.setBlockState(itemStack, player, world, pos, Blocks.DIRT.getDefaultState().with(DirtBlock.VARIANT, DirtBlock.DirtType.DIRT));
					}
				}
			}

			return false;
		}
	}

	protected boolean setBlockState(ItemStack itemStack, PlayerEntity playerEntity, World world, BlockPos blockPos, BlockState blockState) {
		world.method_265((float)blockPos.getX() + 0.5F, (float)blockPos.getY() + 0.5F, (float)blockPos.getZ() + 0.5F, blockState.getBlock().sound.getStepSound(), (blockState.getBlock().sound.getVolume() + 1.0F) / 2.0F, blockState.getBlock().sound.getPitch() * 0.8F);
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
		if(this.material == null) {
			return false;
		}
		return this.material.getRepairIngredient().get().getItem() == ingredient.getItem();
	}
}
