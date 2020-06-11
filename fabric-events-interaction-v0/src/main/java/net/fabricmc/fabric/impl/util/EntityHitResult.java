package net.fabricmc.fabric.impl.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class EntityHitResult extends HitResult {
	private final Entity entity;

	public EntityHitResult(Entity entity) {
		this(entity, entity.getPos());
	}

	public EntityHitResult(Entity entity, Vec3d pos) {
		super(entity);
		this.entity = entity;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public Type getType() {
		return Type.ENTITY;
	}
}

