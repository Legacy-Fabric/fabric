package net.fabricmc.fabric.api.event.registries.v1;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface RegistryItemAddedCallback {

    Event<RegistryItemAddedCallback> EVENT = EventFactory.createArrayBacked(RegistryItemAddedCallback.class, (listeners) -> (id, item) -> {
        for (RegistryItemAddedCallback callback : listeners) {
            callback.itemAdded(id,item);
        }
    });

    void itemAdded(Identifier id, Item item);

}
