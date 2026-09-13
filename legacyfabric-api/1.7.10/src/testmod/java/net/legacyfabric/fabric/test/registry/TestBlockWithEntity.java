package net.legacyfabric.fabric.test.registry;

import net.minecraft.block.BlockWithBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.text.LiteralText;
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
	public boolean use(World world, int x, int y, int z, PlayerEntity player, int i, float f, float g, float h) {
		if (!world.isMultiplayer) {
			BlockEntity entity = world.getBlockEntity(x, y, z);

			if (entity instanceof TestBlockEntity) {
				player.sendMessage(new LiteralText(entity + " at " + x + "," + y + "," + z));
			}
		}

		return true;
	}
}
