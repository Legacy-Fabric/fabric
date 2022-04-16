package net.legacyfabric.fabric.mixin.registry.sync;

import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapper;
import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapperAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements RegistryRemapperAccess {
	@Unique
	private final RegistryRemapper itemRemapper = new RegistryRemapper(Item.REGISTRY);
	@Unique
	private final RegistryRemapper blockRemapper = new RegistryRemapper(Block.REGISTRY);

	@Override
	public RegistryRemapper getItemRemapper() {
		return this.itemRemapper;
	}

	@Override
	public RegistryRemapper getBlockRemapper() {
		return this.blockRemapper;
	}
}
