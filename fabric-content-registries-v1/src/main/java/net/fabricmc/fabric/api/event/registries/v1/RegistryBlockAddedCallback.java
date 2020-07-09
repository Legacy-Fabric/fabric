package net.fabricmc.fabric.api.event.registries.v1;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface RegistryBlockAddedCallback {

    Event<RegistryBlockAddedCallback> EVENT = EventFactory.createArrayBacked(RegistryBlockAddedCallback.class, (listeners) -> (id, block) -> {
        for (RegistryBlockAddedCallback callback : listeners) {
            callback.blockAdded(id,block);
        }
    });

    void blockAdded(Identifier id, Block block);

}
