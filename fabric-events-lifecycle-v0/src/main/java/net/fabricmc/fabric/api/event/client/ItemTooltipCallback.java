package net.fabricmc.fabric.api.event.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public interface ItemTooltipCallback {

	Event<ItemTooltipCallback> EVENT = EventFactory.createArrayBacked(ItemTooltipCallback.class, (listeners) -> (stack,player, lines) -> {
		for (ItemTooltipCallback callback : listeners) {
			callback.getTooltip(stack, player,lines);
		}
	});

	void getTooltip(ItemStack stack, PlayerEntity player, List<String> lines);

}
