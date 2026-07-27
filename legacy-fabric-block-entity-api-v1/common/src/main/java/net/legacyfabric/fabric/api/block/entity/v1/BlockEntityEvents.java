package net.legacyfabric.fabric.api.block.entity.v1;

import net.minecraft.block.entity.BlockEntity;

import net.ornithemc.osl.core.api.events.Event;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BlockEntityEvents {
	public static final Event<Consumer<BiConsumer<NamespacedIdentifier, Class<? extends BlockEntity>>>> REGISTER_BLOCK_ENTITIES = Event.consumer();
}
