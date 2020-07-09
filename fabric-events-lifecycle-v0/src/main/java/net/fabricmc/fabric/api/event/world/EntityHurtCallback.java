package net.fabricmc.fabric.api.event.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public interface EntityHurtCallback {
	Event<EntityHurtCallback> EVENT = EventFactory.createArrayBacked(EntityHurtCallback.class, (listeners) -> (entity,source,original,damage) -> {
		for (EntityHurtCallback callback : listeners) {
			callback.chunksSaved(entity,source,original,damage);
		}
	});

	void chunksSaved(LivingEntity entity, DamageSource damageSource, float originalHealth, float damage);
}
