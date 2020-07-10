package net.fabricmc.fabric.api.server;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Stream;

public final class PlayerStream {
	private PlayerStream() {
	}

	public static Stream<ServerPlayerEntity> all(MinecraftServer server) {
		if (server.getPlayerManager() != null) {
			return server.getPlayerManager().getPlayers().stream();
		} else {
			return Stream.empty();
		}
	}

	public static Stream<PlayerEntity> world(World world) {
		if (!(world instanceof ServerWorld)) {
			throw new RuntimeException("Only supported on ServerWorld!");
		}

		return ((ServerWorld) world).playerEntities.stream();
	}

	public static Stream<PlayerEntity> watching(World world, BlockPos pos) {
		if (!(world instanceof ServerWorld)) {
			throw new RuntimeException("Only supported on ServerWorld!");
		}

		Stream<PlayerEntity> playerEntityStream = ((ServerWorld) world).playerEntities.stream();
		List<PlayerEntity> playerEntityList = Lists.newArrayList();
		playerEntityStream.forEach((player)->{
			if(((player.x - pos.getX()) < 32 || (player.z - pos.getZ()) < 32) && (player.y - pos.getY()) < 32){
				playerEntityList.add(player);
			}
		});
		return playerEntityList.stream();
	}
}
