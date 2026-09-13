package net.legacyfabric.fabric.test.registry;

import net.minecraft.block.BlockWithBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

public class TestBlockWithEntity extends BlockWithBlockEntity {
	protected TestBlockWithEntity(Material material) {
		super(material);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(World world, int id) {
		return new TestBlockEntity();
	}

	@Override
	public boolean use(World world, BlockPos blockPos, BlockState blockState, PlayerEntity playerEntity, InteractionHand hand, @Nullable ItemStack itemStack, Direction direction, float f, float g, float h) {
		if (!world.isClient) {
			BlockEntity entity = world.getBlockEntity(blockPos);

			if (entity instanceof TestBlockEntity) {
				playerEntity.sendMessage(new LiteralText(entity + " at " + blockPos.toString()));
			}
		}

		return true;
	}
}
